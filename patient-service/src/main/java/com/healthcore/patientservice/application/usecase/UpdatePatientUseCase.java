package com.healthcore.patientservice.application.usecase;

import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request.UpdatePatientRequest;

public interface UpdatePatientUseCase {
    void updatePatient(Long patientId, UpdatePatientRequest patientRequest);
}
