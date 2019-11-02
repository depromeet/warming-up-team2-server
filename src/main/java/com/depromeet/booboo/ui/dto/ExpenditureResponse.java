package com.depromeet.booboo.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenditureResponse {
    private Long id;
    private MemberResponse member;
    private Long amountOfMoney;
    private String title;
    private String description;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
}
