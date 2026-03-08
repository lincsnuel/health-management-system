package com.healthcore.patientservice.domain.model;

import com.healthcore.patientservice.domain.exception.InvalidInsurancePolicyException;
import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
import lombok.Getter;

import java.time.LocalDate;

public class InsurancePolicy {
    @Getter
    private final InsurancePolicyId policyId; // nullable before persistence
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

    public InsurancePolicy(
            String providerName,
            String policyNumber,
            String planType,
            LocalDate coverageStart,
            LocalDate coverageEnd,
            boolean main,
            boolean active
    ) {
        this.policyId = InsurancePolicyId.newId();
        this.providerName = providerName.trim();
        this.policyNumber = policyNumber.trim().toUpperCase();
        this.planType = planType.trim();
        this.coverageStart = coverageStart;
        this.coverageEnd = coverageEnd;
        this.main = main;
        this.active = active;

        validateDates();
    }

    private void validateDates() {
        if (coverageEnd.isBefore(coverageStart)) {
            throw new InvalidInsurancePolicyException("Coverage end date cannot be before start date");
        }
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return active &&
                !today.isBefore(coverageStart) &&
                !today.isAfter(coverageEnd);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(coverageEnd);
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