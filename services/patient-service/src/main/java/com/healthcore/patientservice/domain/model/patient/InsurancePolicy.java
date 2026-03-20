package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.exception.InvalidInsurancePolicyException;
import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

public class InsurancePolicy {

    @Getter
    private final InsurancePolicyId policyId;

    @Getter
    private final String providerName;

    @Getter
    private final String policyNumber;

    @Getter
    private final String planType;

    @Getter
    private final LocalDate coverageStart;

    @Getter
    private final LocalDate coverageEnd;

    @Getter
    private boolean main;

    private boolean active;

    private InsurancePolicy(
            InsurancePolicyId policyId,
            String providerName,
            String policyNumber,
            String planType,
            LocalDate coverageStart,
            LocalDate coverageEnd,
            boolean main,
            boolean active
    ) {
        this.policyId = Objects.requireNonNull(policyId);
        this.providerName = Objects.requireNonNull(providerName).trim();
        this.policyNumber = Objects.requireNonNull(policyNumber).trim().toUpperCase();
        this.planType = Objects.requireNonNull(planType).trim();
        this.coverageStart = Objects.requireNonNull(coverageStart);
        this.coverageEnd = Objects.requireNonNull(coverageEnd);
        this.main = main;
        this.active = active;

        validateDates();
    }

    /**
     * Factory for NEW InsurancePolicy (generates ID)
     */
    public static InsurancePolicy create(
            String providerName,
            String policyNumber,
            String planType,
            LocalDate coverageStart,
            LocalDate coverageEnd,
            boolean main
    ) {
        return new InsurancePolicy(
                InsurancePolicyId.newId(),
                providerName,
                policyNumber,
                planType,
                coverageStart,
                coverageEnd,
                main,
                true
        );
    }

    /**
     * Reconstruct EXISTING InsurancePolicy from persistence
     */
    public static InsurancePolicy reconstruct(
            InsurancePolicyId policyId,
            String providerName,
            String policyNumber,
            String planType,
            LocalDate coverageStart,
            LocalDate coverageEnd,
            boolean main,
            boolean active
    ) {
        return new InsurancePolicy(
                policyId,
                providerName,
                policyNumber,
                planType,
                coverageStart,
                coverageEnd,
                main,
                active
        );
    }

    private void validateDates() {
        if (coverageEnd.isBefore(coverageStart)) {
            throw new InvalidInsurancePolicyException(
                    "Coverage end date cannot be before start date"
            );
        }
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return active &&
                !today.isBefore(coverageStart) &&
                !today.isAfter(coverageEnd);
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void markAsMain() {
        this.main = true;
    }

    public void unmarkAsMain() {
        this.main = false;
    }
}