package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.util.Map;

public interface ExpenditureService {
    Page<ExpenditureResponse> getExpenditures(Long memberId, ExpenditureQueryRequest expenditureQueryRequest, Pageable pageable);

    ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest);

    ExpenditureResponse updateExpenditure(Long memberId, Long expenditureId, ExpenditureRequest expenditureRequest);

    ExpenditureResponse updateImage(Long memberId, Long expenditureId, MediaType mediaType, InputStream inputStream);

    MonthlyTotalExpenditureResponse getTotalExpendituresMonthly(Long memberId);

    Map<String, MonthlyExpenditureResponse> getTotalExpendituresDaily(Long memberId);
}
