package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.LoginService;
import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/members/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);
        return ApiResponse.successFrom(loginResponse);
    }
}
