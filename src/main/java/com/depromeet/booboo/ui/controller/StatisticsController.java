package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.CategoryService;
import com.depromeet.booboo.application.service.ExpenditureService;
import com.depromeet.booboo.ui.dto.MonthlyExpenditureResponse;
import com.depromeet.booboo.ui.dto.MonthlyTotalExpenditureResponse;
import com.depromeet.booboo.ui.dto.MostExpendedCategoryResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatisticsController {
    private final ExpenditureService expenditureService;
    private final CategoryService categoryService;

    @GetMapping(value = "/expenditures", params = {"format=graph", "type=monthly"})
    public ApiResponse<MonthlyTotalExpenditureResponse> getExpendituresMonthly(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                                               @RequestHeader(name = "Authorization") String authorization,
                                                                               @ApiIgnore @RequestAttribute Long memberId) {
        MonthlyTotalExpenditureResponse response = expenditureService.getTotalExpendituresMonthly(memberId);
        return ApiResponse.successFrom(response);
    }

    @GetMapping(value = "/expenditures", params = {"format=graph", "type=daily"})
    public ApiResponse<Map<String, MonthlyExpenditureResponse>> getExpendituresDaily(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                                                     @RequestHeader(name = "Authorization") String authorization,
                                                                                     @ApiIgnore @RequestAttribute Long memberId) {
        Map<String, MonthlyExpenditureResponse> responseMap = expenditureService.getTotalExpendituresDaily(memberId);
        return ApiResponse.successFrom(responseMap);
    }

    @GetMapping(value = "/categories", params = "format=graph")
    public ApiResponse<MostExpendedCategoryResponse> getCategoriesMostExpended(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                                               @RequestHeader(name = "Authorization") String authorization,
                                                                               @ApiIgnore @RequestAttribute Long memberId) {
        MostExpendedCategoryResponse response = categoryService.getCategoriesOrderByTotalExpenditure(memberId);
        return ApiResponse.successFrom(response);
    }
}
