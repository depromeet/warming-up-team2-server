package com.depromeet.booboo.api;

import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LoginControllerApi {
    private static final TypeReference<ApiResponse<LoginResponse>> LOGIN_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<LoginResponse>>() {
    };

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public LoginControllerApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public TestApiResult<LoginResponse> login(LoginRequest loginRequest) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andReturn();
        return new TestApiResult<>(
                mvcResult,
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LOGIN_RESPONSE_TYPE_REFERENCE)
        );
    }
}
