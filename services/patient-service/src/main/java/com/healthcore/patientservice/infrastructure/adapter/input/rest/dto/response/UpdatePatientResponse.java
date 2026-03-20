package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import com.healthcore.patientservice.domain.model.enums.PatientStatus;
import java.util.UUID;

/**
 * Response returned after a successful patient update.
 * Includes only key information relevant to the client.
 */
public record UpdatePatientResponse(

        UUID patientId,

        String fullName,

        String email,

        String contactNumber,

        PatientStatus status

) {}