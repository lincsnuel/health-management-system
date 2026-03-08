package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InsurancePolicyRequest(

        @NotBlank(message = "Insurance provider name is required")
        @Size(max = 100)
        String providerName,

        @NotBlank(message = "Policy number is required")
        @Size(max = 50)
        String policyNumber,

        @Size(max = 50)
        String planType,

        @PastOrPresent(message = "Coverage start cannot be in the future")
        LocalDate coverageStart,

        LocalDate coverageEnd,

        Boolean main

) {

    public boolean isMain() {
        return main != null && main;
    }

    @AssertTrue(message = "Coverage end must be after coverage start")
    public boolean isCoveragePeriodValid() {
        if (coverageStart == null || coverageEnd == null) {
            return true;
        }
        return coverageEnd.isAfter(coverageStart);
    }
}