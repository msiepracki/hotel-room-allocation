package com.msiepracki.beusable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
abstract class BaseIT {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T deserializeResponse(
            MvcResult mvcResult,
            TypeReference<T> typeReference
    ) throws Exception {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), typeReference);
    }

}
