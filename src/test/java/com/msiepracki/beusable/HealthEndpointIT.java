package com.msiepracki.beusable;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthEndpointIT extends BaseIT {

    @Test
    void should_RunActuatorAndHealthEndpointWorks() throws Exception {
        // when
        mockMvc.perform(get("/actuator/health"))
                // then
                .andExpect(status().isOk());
    }
}
