package com.BNPLEngine.BNPLEngine.models.domains;

import com.BNPLEngine.BNPLEngine.models.domains.valueObjects.*;
import com.BNPLEngine.BNPLEngine.models.enums.PlanStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class InstallmentPlanDomain {
    private final PlanId id;
    private final OrderId orderId;
    private final CustomerId customerId;

    private CardToken cardToken;
    private final Money totalAmount;
    private Money remainingDebt;
    private Money lateFee;

    private PlanStatus planStatus;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private InstallmentPlanDomain(PlanId id, OrderId orderId, CustomerId customerId, CardToken cardToken, Money totalAmount, Money remainingDebt, Money lateFee, PlanStatus planStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public static InstallmentPlanDomain createInstallmentPlan(UUID rawOrderId, UUID rawCustomerId, String rawCardToken, BigDecimal rawTotalAmount, BigDecimal rawRemainingDebt) {
        PlanId planId = PlanId.generatePlanId();
        OrderId orderId = new OrderId(rawOrderId);
        CustomerId customerId = new CustomerId(rawCustomerId);

        CardToken cardToken = new CardToken(rawCardToken);
        Money totalAmount = new Money(rawTotalAmount);
        Money remainingDebt = new Money(rawRemainingDebt);
        Money lateFee = Money.zeroOf();

        LocalDateTime now = LocalDateTime.now();

        return new InstallmentPlanDomain(
                planId,
                orderId,
                customerId,
                cardToken,
                totalAmount,
                remainingDebt,
                lateFee,
                PlanStatus.DRAFT,
                now,
                now
        );
    }

    public static InstallmentPlanDomain restoreFromExisting(UUID rawPlanId, UUID rawOrderId, UUID rawCustomerId,
                                                            String rawCardToken, BigDecimal rawTotalAmount, BigDecimal rawRemainingDebt,
                                                            BigDecimal rawLateFee, PlanStatus planStatus, LocalDateTime createdAt,
                                                            LocalDateTime updatedAt) {
        PlanId planId = new PlanId(rawPlanId);
        OrderId orderId = new OrderId(rawOrderId);
        CustomerId customerId = new CustomerId(rawCustomerId);

        CardToken cardToken = new CardToken(rawCardToken);
        Money totalAmount = new Money(rawTotalAmount);
        Money remainingDebt = new Money(rawRemainingDebt);
        Money lateFee = new Money(rawLateFee);

        return new InstallmentPlanDomain(
                planId,
                orderId,
                customerId,
                cardToken,
                totalAmount,
                remainingDebt,
                lateFee,
                planStatus,
                createdAt,
                updatedAt
        );
    }

    public void reduceDebt(BigDecimal value) {
        this.remainingDebt = this.remainingDebt.substruct(value);
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseDebt(BigDecimal value) {
        this.remainingDebt = this.remainingDebt.add(value);
        this.updatedAt = LocalDateTime.now();
    }

    public void changeCardToken(String newToken) {
        this.cardToken = new CardToken(newToken);
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseLateFee(BigDecimal value) {
        this.lateFee = this.lateFee.add(value);
        this.updatedAt = LocalDateTime.now();
    }

    public void reduceLateFee(BigDecimal value) {
        this.lateFee = this.lateFee.substruct(value);
        this.updatedAt = LocalDateTime.now();
    }

    public void activatePlan() {
        if (!this.planStatus.equals(PlanStatus.DRAFT)) throw new IllegalStateException("Cant activate plan from status - [%s]".formatted(this.planStatus));

        this.planStatus = PlanStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelPlan() {
        if (!this.planStatus.equals(PlanStatus.DRAFT)) throw new IllegalStateException("Cant cancel plan from status - [%s]".formatted(this.planStatus));

        this.planStatus = PlanStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void delinquentPlan() {
        if (!this.planStatus.equals(PlanStatus.ACTIVE)) throw new IllegalStateException("Cant make plan delinquent from status - [%s]".formatted(this.planStatus));

        this.planStatus = PlanStatus.DELINQUENT;
        this.updatedAt = LocalDateTime.now();
    }

    public void makePlanFullyPaid() {
        if (this.planStatus != PlanStatus.ACTIVE
                && this.planStatus != PlanStatus.DELINQUENT
                && this.planStatus != PlanStatus.PARTIALLY_REFUNDED) {
            throw new IllegalStateException("Cant make plan fully paid from status: %s".formatted(this.planStatus));
        }

        if (!this.remainingDebt.isZero()) throw new IllegalStateException("Cant close plan with remaining debt: %s".formatted(this.remainingDebt));

        this.planStatus = PlanStatus.PAID_IN_FULL;
        this.updatedAt = LocalDateTime.now();
    }

    public void partiallyRefundPlan() {
        if (!this.planStatus.equals(PlanStatus.ACTIVE)) throw new IllegalStateException("Cant refund debt from plan status - [%s]".formatted(this.planStatus));

        this.planStatus = PlanStatus.PARTIALLY_REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void fullyRefund() {
        if (!this.planStatus.equals(PlanStatus.ACTIVE)) throw new IllegalStateException("Cant refund debt from plan status - [%s]".formatted(this.planStatus));

        this.planStatus = PlanStatus.FULLY_REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }
}
