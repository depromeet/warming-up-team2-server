package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.ExpenditureService;
import com.depromeet.booboo.ui.dto.ExpenditureQueryRequest;
import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

@Api(value = "지출내역", description = "인증이 필요한 요청입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExpenditureController {
    private final ExpenditureService expenditureService;

    @ApiOperation("지출 내역을 조회합니다")
    @GetMapping("/expenditures")
    public ApiResponse<List<ExpenditureResponse>> getExpenditures(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                                  @RequestHeader(name = "Authorization") String authorization,
                                                                  @ApiIgnore @RequestAttribute Long memberId,
                                                                  @RequestParam(required = false) String category,
                                                                  @PageableDefault(size = 20) Pageable pageable) {
        ExpenditureQueryRequest expenditureQueryRequest = new ExpenditureQueryRequest();
        expenditureQueryRequest.setCategory(category);

        Page<ExpenditureResponse> expenditureResponses = expenditureService.getExpenditures(memberId, expenditureQueryRequest, pageable);
        return ApiResponse.successFrom(expenditureResponses);
    }

    @ApiOperation("지출 내역을 생성합니다")
    @PostMapping("/expenditures")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExpenditureResponse> createExpenditure(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                              @RequestHeader(name = "Authorization") String authorization,
                                                              @ApiIgnore @RequestAttribute Long memberId,
                                                              @RequestBody ExpenditureRequest expenditureRequest) {
        ExpenditureResponse expenditureResponse = expenditureService.createExpenditure(memberId, expenditureRequest);
        return ApiResponse.successFrom(expenditureResponse);
    }

    @ApiOperation("지출 내역에 이미지를 추가합니다 (multipart/form-data)")
    @PostMapping("/expenditures/{expenditureId}/upload-image")
    public ApiResponse<ExpenditureResponse> uploadImage(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                        @RequestHeader(name = "Authorization") String authorization,
                                                        @ApiIgnore @RequestAttribute Long memberId,
                                                        @PathVariable Long expenditureId,
                                                        @RequestParam("file") MultipartFile multipartFile) throws IOException {
        ExpenditureResponse expenditureResponse = expenditureService.updateImage(
                memberId,
                expenditureId,
                MediaType.valueOf(multipartFile.getContentType()),
                multipartFile.getInputStream()
        );
        return ApiResponse.successFrom(expenditureResponse);
    }

    @ApiOperation("지출 내역을 수정합니다")
    @PutMapping("/expenditures/{expenditureId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ExpenditureResponse> updateExpenditure(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                              @RequestHeader(name = "Authorization") String authorization,
                                                              @ApiIgnore @RequestAttribute Long memberId,
                                                              @PathVariable Long expenditureId,
                                                              @RequestBody ExpenditureRequest expenditureRequest) {
        ExpenditureResponse expenditureResponse = expenditureService.updateExpenditure(memberId, expenditureId, expenditureRequest);
        return ApiResponse.successFrom(expenditureResponse);
    }

}
