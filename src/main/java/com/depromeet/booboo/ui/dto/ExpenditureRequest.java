package com.depromeet.booboo.ui.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenditureRequest {
    private Long amountOfMoney;
    private String title;
    private String description;
    private String category;
    private String paymentMethod;
    private LocalDate expendedAt;
}
