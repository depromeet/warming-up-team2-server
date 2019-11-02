package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.assembler.MemberAssembler;
import com.depromeet.booboo.application.exception.ResourceNotFoundException;
import com.depromeet.booboo.application.service.MemberService;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberAssembler memberAssembler;

    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMember(Long memberId) {
        Assert.notNull(memberId, "'memberId' must not be null");

        return memberRepository.findById(memberId)
                .map(memberAssembler::toMemberResponse)
                .orElseThrow(() -> {
                    log.info("member not found. memberId:{}", memberId);
                    return new ResourceNotFoundException("member not found");
                });
    }
}
