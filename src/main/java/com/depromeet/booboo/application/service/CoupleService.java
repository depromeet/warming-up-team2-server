package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.ConnectCoupleRequest;
import com.depromeet.booboo.ui.dto.CoupleResponse;

public interface CoupleService {
    CoupleResponse connectCouple(Long memberId, ConnectCoupleRequest connectCoupleRequest);
}