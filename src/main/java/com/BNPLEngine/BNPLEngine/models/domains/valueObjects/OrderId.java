package com.BNPLEngine.BNPLEngine.models.domains.valueObjects;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID id) {
    public OrderId {
        Objects.requireNonNull(id, "Order id cant be null");
    }
}
