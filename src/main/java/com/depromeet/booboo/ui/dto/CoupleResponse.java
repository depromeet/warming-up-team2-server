package com.depromeet.booboo.ui.dto;

import lombok.Data;

@Data
public class CoupleResponse {
    private Long id;
    private MemberResponse me;
    private MemberResponse spouse;
}
