package com.healthcore.patientservice.application.service;

import com.healthcore.patientservice.application.command.model.UpdatePatientStatusCommand;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.loader.PatientLoader;
import com.healthcore.patientservice.application.usecase.UpdatePatientStatusUseCase;
import com.healthcore.patientservice.domain.model.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePatientStatusService
        implements UpdatePatientStatusUseCase {

    private final PatientCommandRepository patientRepository;
    private final PatientLoader loader;

    @Override
    public void updateStatus(UpdatePatientStatusCommand command) {

        Patient patient = loader.load(command.patientId(), command.tenantId());

        patient.updateStatus(command.status());

        patientRepository.save(patient);
    }
}