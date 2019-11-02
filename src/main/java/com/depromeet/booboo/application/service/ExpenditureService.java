package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import org.springframework.http.MediaType;

import java.io.InputStream;

public interface ExpenditureService {
    ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest);

    ExpenditureResponse updateImage(Long memberId, Long expenditureId, MediaType mediaType, InputStream inputStream);
}
