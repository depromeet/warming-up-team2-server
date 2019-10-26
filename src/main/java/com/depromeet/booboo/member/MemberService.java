package com.depromeet.booboo.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Long createMember(Member member) {
        repository.save(member);
        return member.getMemberId();
    }
}
