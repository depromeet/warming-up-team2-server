package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}
