package com.depromeet.booboo.ui.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MostExpendedCategoryResponse {
    private Map<String, Long> categoryMap;
}
