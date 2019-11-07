package com.depromeet.booboo.domain.expenditure;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenditureValue {
    private Long amountOfMoney;
    private String title;
    private String description;
    private String category;
    private String paymentMethod;
    private LocalDate expendedAt;
}
