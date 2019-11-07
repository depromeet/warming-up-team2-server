package com.depromeet.booboo.domain.expenditure;

import java.util.stream.Stream;

public enum PaymentMethodType {
    CARD,
    CASH;

    public static PaymentMethodType from(String name) {
        return Stream.of(PaymentMethodType.values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(CARD);
    }
}
