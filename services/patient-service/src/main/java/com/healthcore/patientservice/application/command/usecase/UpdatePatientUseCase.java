package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.UpdatePatientCommand;

import java.util.UUID;

public interface UpdatePatientUseCase {
    void updatePatient(UUID patientId, UpdatePatientCommand command);
}
