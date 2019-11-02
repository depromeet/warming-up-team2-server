package com.depromeet.booboo.application.adapter.kakao;

public class KakaoApiFailedException extends RuntimeException {
    public KakaoApiFailedException(String message) {
        super(message);
    }
}
