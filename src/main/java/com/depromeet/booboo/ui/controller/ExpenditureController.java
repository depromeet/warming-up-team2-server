package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.ExpenditureService;
import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "지출내역", description = "인증이 필요한 요청입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenditureController {
    private final ExpenditureService expenditureService;

    @ApiOperation("지출 내역을 생성합니다")
    @PostMapping("/expenditures")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExpenditureResponse> createExpenditures(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                               @RequestHeader(name = "Authorization") String authorization,
                                                               @ApiIgnore @RequestAttribute Long memberId,
                                                               @RequestBody ExpenditureRequest expenditureRequest) {
        ExpenditureResponse expenditureResponse = expenditureService.createExpenditure(memberId, expenditureRequest);
        return ApiResponse.successFrom(expenditureResponse);
    }
}
