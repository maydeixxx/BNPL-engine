package com.BNPLEngine.BNPLEngine.models.domains;

import com.BNPLEngine.BNPLEngine.models.domains.valueObjects.*;
import com.BNPLEngine.BNPLEngine.models.enums.PlanStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InstallmentPlanDomain {
    private final PlanId id;
    private final OrderId orderId;
    private final CustomerId customerId;

    private CardToken cardToken;
    private Money totalAmount;
    private Money remainingDebt;
    private Money lateFee;

    private PlanStatus planStatus;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private InstallmentPlanDomain(Money totalAmount, PlanId id, OrderId orderId, CustomerId customerId, CardToken cardToken, Money remainingDebt, Money lateFee, PlanStatus planStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (createdAt == null || updatedAt == null) throw new IllegalArgumentException("Missing time stamps");

        this.totalAmount = totalAmount;
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.cardToken = cardToken;
        this.remainingDebt = remainingDebt;
        this.lateFee = lateFee;
        this.planStatus = planStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //TODO Business logic
}
