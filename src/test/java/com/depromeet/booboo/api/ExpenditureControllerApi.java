package com.depromeet.booboo.api;

import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ExpenditureControllerApi {
    private static final TypeReference<ApiResponse<ExpenditureResponse>> EXPENDITURE_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<ExpenditureResponse>>() {
    };

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ExpenditureControllerApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public TestApiResult<ExpenditureResponse> createExpenditure(String accessToken, ExpenditureRequest expenditureRequest) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/expenditures")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(expenditureRequest)))
                .andReturn();
        return new TestApiResult<>(
                mvcResult,
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsByteArray(),
                        EXPENDITURE_RESPONSE_TYPE_REFERENCE
                )
        );
    }
}
