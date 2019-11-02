package com.depromeet.booboo.application;

import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
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
