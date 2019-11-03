package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.adapter.kakao.KakaoApiFailedException;
import com.depromeet.booboo.application.adapter.storage.StorageException;
import com.depromeet.booboo.domain.category.CategoryDuplicatedException;
import com.depromeet.booboo.domain.couple.CoupleConnectionFailedException;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler({
            CoupleConnectionFailedException.class,
            CategoryDuplicatedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleBadRequestException(RuntimeException ex) {
        log.info("Bad request exception occurred", ex);
        return ApiResponse.failureFrom(ex.getMessage());
    }

    @ExceptionHandler({
            KakaoApiFailedException.class,
            StorageException.class
    })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResponse<Object> handleServiceUnavailableException(RuntimeException ex) {
        log.error("External service is unavailable", ex);
        return ApiResponse.failureFrom(ex.getMessage());
    }

    @ExceptionHandler({
            RuntimeException.class,
            Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleInternalServerErrorException(Exception ex) {
        log.error("Internal server error occurred", ex);
        return ApiResponse.failureFrom(ex.getMessage());
    }
}
