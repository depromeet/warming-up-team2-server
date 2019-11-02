package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.service.CoupleService;
import com.depromeet.booboo.application.service.MemberService;
import com.depromeet.booboo.ui.dto.ConnectCoupleRequest;
import com.depromeet.booboo.ui.dto.CoupleResponse;
import com.depromeet.booboo.ui.dto.MemberResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "멤버", description = "인증이 필요한 요청입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final CoupleService coupleService;

    @ApiOperation("자기 자신의 정보를 조회합니다.")
    @GetMapping("/members/me")
    public ApiResponse<MemberResponse> getMe(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                             @RequestHeader(name = "Authorization") String authorization,
                                             @ApiIgnore @RequestAttribute Long memberId) {
        MemberResponse memberResponse = memberService.getMember(memberId);
        return ApiResponse.successFrom(memberResponse);
    }

    @ApiOperation("연동 코드를 통해 커플로 연결합니다")
    @PostMapping("/members/me/connect")
    public ApiResponse<CoupleResponse> connectCouple(@ApiParam(name = "Authorization", value = "Bearer {accessToken}", required = true)
                                                     @RequestHeader(name = "Authorization") String authorization,
                                                     @ApiIgnore @RequestAttribute Long memberId,
                                                     @RequestBody ConnectCoupleRequest connectCoupleRequest) {
        CoupleResponse coupleResponse = coupleService.connectCouple(memberId, connectCoupleRequest);
        return ApiResponse.successFrom(coupleResponse);
    }
}
