package com.healthcore.patientservice.application.query.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Query model representing a patient's insurance policy.
 * Can be used directly as REST response to reduce redundancy.
 */
public record InsuranceDetail(
        UUID insuranceId,
        String providerName,
        String policyNumber,
        String planType,
        LocalDate coverageStart,
        LocalDate coverageEnd,
        boolean main,
        boolean active
) {}