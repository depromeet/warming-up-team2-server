package com.depromeet.booboo.application.service;

import com.depromeet.booboo.application.assembler.CoupleAssembler;
import com.depromeet.booboo.domain.couple.Couple;
import com.depromeet.booboo.domain.couple.CoupleConnectionFailedException;
import com.depromeet.booboo.domain.couple.CoupleRepository;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.ConnectCoupleRequest;
import com.depromeet.booboo.ui.dto.CoupleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoupleServiceImpl implements CoupleService {
    private final MemberRepository memberRepository;
    private final CoupleRepository coupleRepository;
    private final CoupleAssembler coupleAssembler;

    @Override
    @Transactional
    public CoupleResponse connectCouple(Long memberId, ConnectCoupleRequest connectCoupleRequest) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.notNull(connectCoupleRequest, "'connectCoupleRequest' must not be null");

        String connectionCode = connectCoupleRequest.getConnectionCode();
        if (StringUtils.isEmpty(connectionCode)) {
            log.info("'connectionCode' must not be null or empty. connectionCode:{}", connectionCode);
            throw new CoupleConnectionFailedException("'connectionCode' must not be null or empty");
        }

        Member me = memberRepository.findById(memberId)
                .orElseThrow(() -> new CoupleConnectionFailedException("Source member not found. memberId:" + memberId));
        Member spouse = memberRepository.findByConnectionCode(connectionCode)
                .orElseThrow(() -> new CoupleConnectionFailedException("Target member not found. connectionCode:" + connectionCode));

        Couple couple = me.connect(spouse, coupleRepository);
        return coupleAssembler.toCoupleResponse(couple, me, spouse);
    }
}
