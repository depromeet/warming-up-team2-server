package com.depromeet.booboo.domain.member;

import com.depromeet.booboo.domain.couple.CoupleConnectionFailedException;

public enum MemberStatus {
    SOLO,
    COUPLE;

    public static MemberStatus initial() {
        return SOLO;
    }

    public MemberStatus connect() {
        if (this != SOLO) {
            throw new CoupleConnectionFailedException("'status' must be SOLO");
        }
        return COUPLE;
    }
}