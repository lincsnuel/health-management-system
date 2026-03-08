package com.healthcore.patientservice.application.usecase;

import java.util.UUID;

public interface DeletePatientUseCase {

    void deletePatientById(UUID patientId);
}
