package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.assembler.MemberAssembler;
import com.depromeet.booboo.application.exception.ConnectionCodeException;
import com.depromeet.booboo.application.exception.ResourceNotFoundException;
import com.depromeet.booboo.application.service.MemberService;
import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.category.CategoryRepository;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private static final int RETRY_COUNT = 5;

    private final MemberRepository memberRepository;
    private final MemberAssembler memberAssembler;
    private final CategoryRepository categoryRepository;

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

    @Override
    @Transactional
    public Member createKakaoMember(String name, String profileImage, String providerUserId) {
        Assert.notNull(name, "'name' must not be null");
        Assert.notNull(providerUserId, "'providerUserId' must not be null");

        Member member = Member.fromKakao(
                name,
                profileImage,
                providerUserId,
                this.generateConnectionCode()
        );
        memberRepository.saveAndFlush(member);
        Category.createDefaultCategories(member, categoryRepository);
        return member;
    }

    private String generateConnectionCode() {
        for (int i = 0; i < RETRY_COUNT; i++) {
            String code = UUID.randomUUID().toString().substring(0, 6);
            if (!memberRepository.existsByConnectionCode(code)) {
                return code;
            }
        }
        throw new ConnectionCodeException("Failed to create connectionCode");
    }
}
