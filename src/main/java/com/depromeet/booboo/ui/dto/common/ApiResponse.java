package com.depromeet.booboo.ui.dto.common;

import org.springframework.util.Assert;

import java.util.List;

public class ApiResponse<T> {
    private static final String EMPTY_MESSAGE = "";
    private static final int TOTAL_COUNT_UNUSED_VALUE = -1;

    private final String message;
    private final Integer totalCount;
    private final T data;

    private ApiResponse(String message, Integer totalCount, T data) {
        this.message = message;
        this.totalCount = totalCount;
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(EMPTY_MESSAGE, TOTAL_COUNT_UNUSED_VALUE, null);
    }

    public static <T> ApiResponse<T> successFrom(T data) {
        return new ApiResponse<>(EMPTY_MESSAGE, TOTAL_COUNT_UNUSED_VALUE, data);
    }

    public static <T> ApiResponse<List<T>> successFrom(List<T> dataList) {
        Assert.notNull(dataList, "'dataList' must not be null");
        return new ApiResponse<>(EMPTY_MESSAGE, dataList.size(), dataList);
    }
}
