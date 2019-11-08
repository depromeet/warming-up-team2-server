package com.depromeet.booboo.ui.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class MonthlyExpenditureResponse {
    private Long total;
    private LocalDate fromAt;
    private LocalDate toAt;
    private Map<String, DailyExpenditureResponse> dailyExpenditures;
}
