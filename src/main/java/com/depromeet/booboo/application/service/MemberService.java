package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.MemberResponse;

public interface MemberService {
    MemberResponse getMember(Long memberId);
}
