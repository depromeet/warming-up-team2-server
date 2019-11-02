package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;

public interface ExpenditureService {
    ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest);
}
