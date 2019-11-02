package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.InputStream;

public interface ExpenditureService {
    Page<ExpenditureResponse> getExpenditures(Long memberId, Pageable pageable);

    ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest);

    ExpenditureResponse updateExpenditure(Long memberId, Long expenditureId, ExpenditureRequest expenditureRequest);

    ExpenditureResponse updateImage(Long memberId, Long expenditureId, MediaType mediaType, InputStream inputStream);
}
