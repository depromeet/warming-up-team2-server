package com.depromeet.booboo.ui.interceptor;

import com.depromeet.booboo.application.security.JwtFactory;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String MEMBER_ID = "memberId";

    private final JwtFactory jwtFactory;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Long memberId = jwtFactory.decodeToken(request.getHeader(AUTHORIZATION_HEADER)).orElse(null);
        if (memberId == null) {
            this.writeFailureResponse(response);
            return false;
        }
        request.setAttribute(MEMBER_ID, memberId);
        return true;
    }

    private void writeFailureResponse(HttpServletResponse response) throws IOException {
        ApiResponse<Object> unauthorizedResponse = ApiResponse.failureFrom("로그인이 필요합니다");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(objectMapper.writeValueAsBytes(unauthorizedResponse));
        }
    }
}
