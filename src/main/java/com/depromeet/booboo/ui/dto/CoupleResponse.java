package com.depromeet.booboo.ui.dto;

import lombok.Data;

@Data
public class CoupleResponse {
    private Long coupleId;
    private MemberResponse me;
    private MemberResponse spouse;
}
