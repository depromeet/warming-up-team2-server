package com.depromeet.booboo.member;

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
    public Member getMember(@PathVariable Long memberId) {
        return Member.builder()
                .memberId(memberId)
                .name("mock api")
                .snsType(Member.SnsType.KAKAO)
                .createDate(LocalDateTime.now())
                .build();
    }
}
