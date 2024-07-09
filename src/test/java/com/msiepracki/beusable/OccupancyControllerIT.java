package com.msiepracki.beusable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.msiepracki.beusable.config.validation.ValidationError;
import com.msiepracki.beusable.config.validation.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OccupancyControllerIT extends BaseIT {

    private static final String OCCUPANCY_PATH = "/occupancy";

    @Test
    void should_FailGuestMoneyValidation() throws Exception {
        // given
        var request = Map.of(
                "premiumRooms", 3,
                "economyRooms", -3,
                "potentialGuests", List.of(0, 1, -2)
        );

        // when
        MvcResult mvcResult = mockMvc.perform(post(OCCUPANCY_PATH)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse validationErrorResponse = deserializeResponse(mvcResult, new TypeReference<>() {
        });

        assertThat(validationErrorResponse.getErrors())
                .extracting(
                        ValidationError::getField,
                        ValidationError::getMessage
                )
                .containsExactlyInAnyOrder(
                        tuple("economyRooms", "must be greater than or equal to 0"),
                        tuple("potentialGuests[0]", "must be greater than 0"),
                        tuple("potentialGuests[2]", "must be greater than 0")
                );
    }

    @ParameterizedTest
    @MethodSource("providedScenarios")
    void should_PassProvidedScenarios(
            Map<String, Object> request,
            Map<String, Object> expectedResponse
    ) throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(post(OCCUPANCY_PATH)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = deserializeResponse(mvcResult, new TypeReference<>() {
        });

        // then
        assertThat(response).containsExactlyInAnyOrderEntriesOf(expectedResponse);
    }

    private static Stream<Arguments> providedScenarios() {
        return Stream.of(
                // Test 1 - provided
                Arguments.of(
                        Map.of(
                                "premiumRooms", 3,
                                "economyRooms", 3,
                                "potentialGuests", List.of(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209)
                        ),
                        Map.of(
                                "usagePremium", 3,
                                "revenuePremium", 738,
                                "usageEconomy", 3,
                                "revenueEconomy", 167.99
                        )
                ),
                // Test 2 - provided
                Arguments.of(
                        Map.of(
                                "premiumRooms", 7,
                                "economyRooms", 5,
                                "potentialGuests", List.of(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209)
                        ),
                        Map.of(
                                "usagePremium", 6,
                                "revenuePremium", 1054,
                                "usageEconomy", 4,
                                "revenueEconomy", 189.99
                        )
                ),
                // Test 3 - provided
                Arguments.of(
                        Map.of(
                                "premiumRooms", 2,
                                "economyRooms", 7,
                                "potentialGuests", List.of(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209)
                        ),
                        Map.of(
                                "usagePremium", 2,
                                "revenuePremium", 583,
                                "usageEconomy", 4,
                                "revenueEconomy", 189.99
                        )
                ),
                // Test 4 - All economy are booked and one gets upgrade to premium
                Arguments.of(
                        Map.of(
                                "premiumRooms", 2,
                                "economyRooms", 2,
                                "potentialGuests", List.of(23, 45, 99, 102)
                        ),
                        Map.of(
                                "usagePremium", 2,
                                "revenuePremium", 201,
                                "usageEconomy", 2,
                                "revenueEconomy", 68
                        )
                ),
                // Test 5 - All economy are booked and one gets upgrade to premium, but less paying economy is rejected
                Arguments.of(
                        Map.of(
                                "premiumRooms", 2,
                                "economyRooms", 2,
                                "potentialGuests", List.of(11, 23, 45, 99, 101)
                        ),
                        Map.of(
                                "usagePremium", 2,
                                "revenuePremium", 200,
                                "usageEconomy", 2,
                                "revenueEconomy", 68
                        )
                ),
                // Test 6 - case 2 modified by amount of free eco rooms
                Arguments.of(
                        Map.of(
                                "premiumRooms", 7,
                                "economyRooms", 3,
                                "potentialGuests", List.of(23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209)
                        ),
                        Map.of(
                                "usagePremium", 7,
                                "revenuePremium", 1153.99,
                                "usageEconomy", 3,
                                "revenueEconomy", 90
                        )
                ),
                // Test 7 - case 6, but added more guests than rooms
                Arguments.of(
                        Map.of(
                                "premiumRooms", 2,
                                "economyRooms", 2,
                                "potentialGuests", List.of(10, 20, 30, 40, 50, 60, 100, 110, 120, 130, 140, 150)
                        ),
                        Map.of(
                                "usagePremium", 2,
                                "revenuePremium", 290,
                                "usageEconomy", 2,
                                "revenueEconomy", 110
                        )
                )
        );
    }
}
