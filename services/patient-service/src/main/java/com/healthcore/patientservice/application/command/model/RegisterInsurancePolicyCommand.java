package com.healthcore.patientservice.application.command.model;

import java.time.LocalDate;

public record RegisterInsurancePolicyCommand(

        String providerName,
        String policyNumber,
        String planType,
        LocalDate coverageStart,
        LocalDate coverageEnd,
        boolean main
) {}