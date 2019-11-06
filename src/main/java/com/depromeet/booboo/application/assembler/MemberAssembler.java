package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.ui.dto.MemberResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberAssembler {
    public MemberResponse toMemberResponse(Member member) {
        if (member == null) {
            return null;
        }
        MemberResponse response = new MemberResponse();
        response.setId(member.getMemberId());
        response.setName(member.getName());
        response.setStatus(member.getStatus().name());
        response.setProfileImageUrl(member.getProfileImg());
        response.setConnectionCode(member.getConnectionCode());
        return response;
    }
}
