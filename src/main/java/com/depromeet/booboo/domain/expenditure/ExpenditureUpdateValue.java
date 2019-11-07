package com.depromeet.booboo.domain.expenditure;

import lombok.Data;

@Data
public class ExpenditureUpdateValue {
    private Long amountOfMoney;
    private String title;
    private String description;
    private String category;
    private String paymentMethod;
}
