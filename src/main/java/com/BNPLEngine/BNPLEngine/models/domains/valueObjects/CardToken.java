package com.BNPLEngine.BNPLEngine.models.domains.valueObjects;

import java.util.Objects;

public record CardToken(String token) {
    public CardToken {
        Objects.requireNonNull(token, "Card token cant be null");
        if (token.isBlank()) throw new IllegalArgumentException("Card token cant be blank");
        token = token.trim();
    }
}
