package com.depromeet.booboo.api;

import com.depromeet.booboo.ui.dto.MemberResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MemberControllerApi {
    private static final TypeReference<ApiResponse<MemberResponse>> MEMBER_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<MemberResponse>>() {
    };

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public MemberControllerApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public TestApiResult<MemberResponse> getMe(String accessToken) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/members/me")
                .header("Authorization", "Bearer " + accessToken))
                .andReturn();
        return new TestApiResult<>(
                mvcResult,
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MEMBER_RESPONSE_TYPE_REFERENCE)
        );
    }
}
