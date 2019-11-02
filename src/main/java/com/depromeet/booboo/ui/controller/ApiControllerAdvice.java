package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.adapter.kakao.KakaoApiFailedException;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(KakaoApiFailedException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse<Object> handleRuntimeException(RuntimeException ex) {
        log.error("External service is unavailable", ex);
        return ApiResponse.failureFrom(ex.getMessage());
    }

    @ExceptionHandler({
            RuntimeException.class,
            Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleException(Exception ex) {
        log.error("Internal server error occurred", ex);
        return ApiResponse.failureFrom(ex.getMessage());
    }
}
