package com.depromeet.booboo.ui.controller;

import com.depromeet.booboo.application.MemberService;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}")
    public ApiResponse<Member> getMember(@PathVariable Long memberId) {
        Member member = Member.builder()
                .memberId(memberId)
                .name("mock api")
                .snsType(Member.SnsType.KAKAO)
                .createDate(LocalDateTime.now())
                .build();
        return ApiResponse.successFrom(member);
    }
}
