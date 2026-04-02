package com.healthcore.patientservice.application.command.usecase;

import java.util.UUID;

public interface DeletePatientUseCase {

    void deletePatientById(UUID patientId);
}
