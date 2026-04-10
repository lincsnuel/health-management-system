package com.healthcore.patientservice.application.command.service;

import com.healthcore.patientservice.application.command.model.CheckPatientEligibilityCommand;
import com.healthcore.patientservice.application.common.loader.PatientLoader;
import com.healthcore.patientservice.application.command.usecase.CheckPatientEligibilityUseCase;
import com.healthcore.patientservice.domain.model.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckPatientEligibilityService
        implements CheckPatientEligibilityUseCase {

    private final PatientLoader loader;

    @Override
    public boolean checkEligibility(CheckPatientEligibilityCommand command) {

        Patient patient = loader.load(command.patientId());

        return patient.isEligibleForService(
                command.minAge(),
                command.maxAge()
        );

    }
}