package com.depromeet.booboo.ui.dto;

import lombok.Data;

@Data
public class ExpenditureRequest {
    private Long amountOfMoney;
    private String title;
    private String description;
    private Long categoryId;
}
