package com.depromeet.booboo.ui.dto.common;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;

@Data
public class ApiResponse<T> {
    private static final String EMPTY_MESSAGE = "";
    private static final String DEFAULT_ERROR_MESSAGE = "서버 에러가 발생했습니다";
    private static final int UNUSED_TOTAL_COUNT = -1;
    private static final Object UNUSED_DATA = null;

    private final String message;
    private final Integer totalCount;
    private final T data;

    private ApiResponse(String message, Integer totalCount, T data) {
        this.message = message;
        this.totalCount = totalCount;
        this.data = data;
    }

    public static ApiResponse<Object> success() {
        return new ApiResponse<>(EMPTY_MESSAGE, UNUSED_TOTAL_COUNT, UNUSED_DATA);
    }

    public static <T> ApiResponse<T> successFrom(T data) {
        return new ApiResponse<>(EMPTY_MESSAGE, UNUSED_TOTAL_COUNT, data);
    }

    public static <T> ApiResponse<List<T>> successFrom(List<T> dataList) {
        Assert.notNull(dataList, "'dataList' must not be null");
        return new ApiResponse<>(EMPTY_MESSAGE, dataList.size(), dataList);
    }

    public static ApiResponse<Object> failure() {
        return new ApiResponse<>(DEFAULT_ERROR_MESSAGE, UNUSED_TOTAL_COUNT, UNUSED_DATA);
    }

    public static ApiResponse<Object> failureFrom(String message) {
        return new ApiResponse<>(message, UNUSED_TOTAL_COUNT, UNUSED_DATA);
    }
}
