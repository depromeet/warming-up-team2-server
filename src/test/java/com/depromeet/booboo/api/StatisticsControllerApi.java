package com.depromeet.booboo.api;

import com.depromeet.booboo.ui.dto.MonthlyTotalExpenditureResponse;
import com.depromeet.booboo.ui.dto.MostExpendedCategoryResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class StatisticsControllerApi {
    private static final TypeReference<ApiResponse<MonthlyTotalExpenditureResponse>> MONTHLY_TOTAL_EXPENDITURE_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<MonthlyTotalExpenditureResponse>>() {
    };
    private static final TypeReference<ApiResponse<MostExpendedCategoryResponse>> MOST_EXPENDED_CATEGORY_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<MostExpendedCategoryResponse>>() {
    };

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public StatisticsControllerApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public TestApiResult<MonthlyTotalExpenditureResponse> getExpendituresMonthly(String accessToken) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/expenditures?format=graph&type=monthly")
                .header("Authorization", "Bearer " + accessToken))
                .andReturn();
        return new TestApiResult<>(
                mvcResult,
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), MONTHLY_TOTAL_EXPENDITURE_RESPONSE_TYPE_REFERENCE)
        );
    }

    public TestApiResult<MostExpendedCategoryResponse> getCategoriesMostExpended(String accessToken) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/categories?format=graph")
                .header("Authorization", "Bearer " + accessToken))
                .andReturn();
        return new TestApiResult<>(
                mvcResult,
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), MOST_EXPENDED_CATEGORY_RESPONSE_TYPE_REFERENCE)
        );
    }
}
