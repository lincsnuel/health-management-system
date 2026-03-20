package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterPatientResponse(

        UUID patientId,
        String tenantId,
        String hospitalPatientNumber,
        String fullName,
        String status

) {}