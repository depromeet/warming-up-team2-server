package com.depromeet.booboo.ui.dto;

import lombok.Data;

@Data
public class MemberResponse {
    private Long id;
    private String name;
    private String profileImageUrl;
    private String status;
    private String connectionCode;
    private String spouseName;
}
