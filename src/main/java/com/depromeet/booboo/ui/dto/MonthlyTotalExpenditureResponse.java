package com.depromeet.booboo.ui.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MonthlyTotalExpenditureResponse {
    private Map<String, Long> monthlyTotalExpenditures;
}
