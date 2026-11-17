package com.BNPLEngine.BNPLEngine.models.domains.valueObjects;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID id) {
    public CustomerId {
        Objects.requireNonNull(id, "Customer id cant be null");
    }
}
