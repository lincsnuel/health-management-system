package com.healthcore.patientservice.application.command.model;

import com.healthcore.patientservice.domain.model.enums.PatientStatus;

import java.util.UUID;

public record RegisterPatientResult(

        UUID patientId,
        String hospitalPatientNumber,
        String fullName,
        PatientStatus status

) {}