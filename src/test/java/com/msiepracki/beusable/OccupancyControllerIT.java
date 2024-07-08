package com.msiepracki.beusable;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OccupancyControllerIT extends BaseIT {

    private static final String OCCUPANCY_PATH = "/occupancy";

    @Test
    void should_FailGuestMoneyValidation_When_IsZero() throws Exception {
        // given
        var request = Map.of(
                "premiumRooms", 3,
                "economyRooms", 3,
                "potentialGuests", List.of(0)
        );

        // when
        mockMvc.perform(post(OCCUPANCY_PATH)
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest()); // TODO: Add @ExceptionHandler to unify validation errors and check exact message
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
                )
        );
    }
}
