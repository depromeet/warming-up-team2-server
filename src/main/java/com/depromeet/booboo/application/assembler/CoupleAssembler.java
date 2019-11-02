package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.couple.Couple;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.ui.dto.CoupleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoupleAssembler {
    private final MemberAssembler memberAssembler;

    public CoupleResponse toCoupleResponse(Couple couple, Member me, Member spouse) {
        if (couple == null) {
            return null;
        }
        CoupleResponse response = new CoupleResponse();
        response.setId(couple.getCoupleId());
        response.setMe(memberAssembler.toMemberResponse(me));
        response.setSpouse(memberAssembler.toMemberResponse(spouse));
        return response;
    }
}
