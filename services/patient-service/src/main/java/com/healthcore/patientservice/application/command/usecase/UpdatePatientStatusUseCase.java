package com.healthcore.patientservice.application.command.usecase;

import com.healthcore.patientservice.application.command.model.UpdatePatientStatusCommand;

public interface UpdatePatientStatusUseCase {
    void updateStatus(UpdatePatientStatusCommand command);
}
