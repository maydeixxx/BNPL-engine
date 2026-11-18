package com.BNPLEngine.BNPLEngine.models.domains.valueObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {
    public Money {
        Objects.requireNonNull(amount, "Amount cant be null");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Amount cant be less or equals to zero");

        amount = amount.setScale(2, RoundingMode.HALF_DOWN);
    }

    public Money add(BigDecimal value) {
        Objects.requireNonNull(value, "Value cant be null");
        if (value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Value cant be less than zero");

        return new Money(amount.add(value));
    }

    public Money substruct(BigDecimal value) {
        Objects.requireNonNull(value, "Value cant be null");
        if (value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Value cant be less than zero");
        if (amount.compareTo(value) < 0) throw new IllegalArgumentException("Value cant be greater than actual amount");

        return new Money(amount.subtract(value));
    }

    public static Money zeroOf() {
        return new Money(BigDecimal.ZERO);
    }

    public Boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
}
