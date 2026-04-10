package com.healthcore.patientservice.application.command.model;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePatientInsuranceCommand(
        UUID id,
        UUID patientId,
        String providerName,
        String policyNumber,
        String planType,
        LocalDate coverageStart,
        LocalDate coverageEnd,
        boolean main
) {}