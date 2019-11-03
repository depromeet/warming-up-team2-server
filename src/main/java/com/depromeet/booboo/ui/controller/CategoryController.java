package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.CategoryService;
import com.depromeet.booboo.ui.dto.CategoryRequest;
import com.depromeet.booboo.ui.dto.CategoryResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "카테고리", description = "인증이 필요한 요청입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @ApiOperation("카테고리를 생성합니다. name 이 중복되지 않아야 합니다")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> createCategory(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                        @RequestHeader(name = "Authorization") String authorization,
                                                        @ApiIgnore @RequestAttribute Long memberId,
                                                        @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(memberId, categoryRequest);
        return ApiResponse.successFrom(categoryResponse);
    }

    @ApiOperation("카테고리를 조회합니다. 나와 배우자의 카테고리만 접근할 수 있습니다")
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getCategories(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                             @RequestHeader(name = "Authorization") String authorization,
                                                             @ApiIgnore @RequestAttribute Long memberId,
                                                             @PageableDefault(size = 20) Pageable pageable) {
        Page<CategoryResponse> categoryResponsePage = categoryService.getCategories(memberId, pageable);
        return ApiResponse.successFrom(categoryResponsePage);
    }
}
