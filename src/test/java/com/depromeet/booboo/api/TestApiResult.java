package com.depromeet.booboo.api;

import com.depromeet.booboo.ui.dto.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

public class TestApiResult<T> {
    private final MvcResult mvcResult;
    private final ApiResponse<T> apiResponse;

    public TestApiResult(MvcResult mvcResult, ApiResponse<T> apiResponse) {
        this.mvcResult = mvcResult;
        this.apiResponse = apiResponse;
    }

    public ApiResponse<T> getApiResponse() {
        return apiResponse;
    }

    public boolean is2xxSuccessful() {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus()).is2xxSuccessful();
    }

    public boolean is4xxClientError() {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus()).is4xxClientError();
    }

    public boolean is5xxServerError() {
        return HttpStatus.valueOf(mvcResult.getResponse().getStatus()).is5xxServerError();
    }
}
