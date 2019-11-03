package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.application.adapter.kakao.KakaoUserResponse;
import com.depromeet.booboo.application.exception.ConnectionCodeException;
import com.depromeet.booboo.application.security.JwtFactory;
import com.depromeet.booboo.application.service.LoginService;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.domain.member.ProviderType;
import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private static final int RETRY_COUNT = 5;

    private final KakaoAdapter kakaoAdapter;
    private final MemberRepository memberRepository;
    private final JwtFactory jwtFactory;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Assert.notNull(loginRequest, "'loginRequest' must not be null");

        String kakaoAccessToken = loginRequest.getAccessToken();
        KakaoUserResponse kakaoUserResponse = kakaoAdapter.getUserInfo(kakaoAccessToken);

        final String providerUserId = kakaoUserResponse.getId().toString();

        Member member = memberRepository.findByProviderTypeAndProviderUserId(ProviderType.KAKAO, providerUserId)
                .orElseGet(() -> {
                    Member createdMember = Member.fromKakao(
                            kakaoUserResponse.getUserName(),
                            kakaoUserResponse.getProfileImage(),
                            providerUserId,
                            this.generateConnectionCode()
                    );
                    return memberRepository.save(createdMember);
                });

        String boobooAccessToken = jwtFactory.generateToken(member);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(boobooAccessToken);
        return loginResponse;
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
