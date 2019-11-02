package com.depromeet.booboo.domain.member;

public enum MemberStatus {
    SOLO,
    COUPLE;

    public static MemberStatus initial() {
        return SOLO;
    }
}