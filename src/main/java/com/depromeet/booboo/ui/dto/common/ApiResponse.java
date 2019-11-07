package com.depromeet.booboo.ui.dto.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private static final String EMPTY_MESSAGE = "";
    private static final String DEFAULT_ERROR_MESSAGE = "서버 에러가 발생했습니다";
    private static final long UNUSED_TOTAL_COUNT = -1L;
    private static final Object UNUSED_DATA = null;

    private String message;
    private Long totalCount;
    private T data;

    private ApiResponse(String message, Long totalCount, T data) {
        this.message = message;
        this.totalCount = totalCount;
        this.data = data;
    }

    private ApiResponse(String message, Integer totalCount, T data) {
        this.message = message;
        this.totalCount = Long.valueOf(totalCount);
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

    public static <T> ApiResponse<List<T>> successFrom(Page<T> dataPage) {
        Assert.notNull(dataPage, "'data' must not be null");
        return new ApiResponse<>(
                EMPTY_MESSAGE,
                dataPage.getTotalElements(),
                dataPage.stream().collect(Collectors.toList())
        );
    }

    public static ApiResponse<Object> failure() {
        return new ApiResponse<>(DEFAULT_ERROR_MESSAGE, UNUSED_TOTAL_COUNT, UNUSED_DATA);
    }

    public static ApiResponse<Object> failureFrom(String message) {
        return new ApiResponse<>(message, UNUSED_TOTAL_COUNT, UNUSED_DATA);
    }
}
