package com.depromeet.booboo.ui.dto;

import lombok.Data;

import java.util.List;

@Data
public class DailyExpenditureResponse {
    private Long total;
    private String dayOfWeek;   // 한글로
    private List<ExpenditureResponse> expenditures;
}
