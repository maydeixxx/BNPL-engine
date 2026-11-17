package com.BNPLEngine.BNPLEngine.models.domains.valueObjects;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.Objects;
import java.util.UUID;

public record PlanId(UUID id) {
    public PlanId {
        Objects.requireNonNull(id, "Plan id cant be null");
    }

    public static PlanId generatePlanId() {
        return new PlanId(UuidCreator.getTimeOrderedEpoch());
    }
}
