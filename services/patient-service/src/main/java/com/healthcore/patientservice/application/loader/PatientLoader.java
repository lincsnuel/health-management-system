package com.healthcore.patientservice.application.loader;

import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.exception.PatientNotFoundException;
import com.healthcore.patientservice.domain.model.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientLoader {

    private final PatientCommandRepository patientRepository;

    public Patient load(UUID patientId, String tenantId) {

        return patientRepository.findById(patientId, tenantId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id: "
                        + patientId + " not found"));
    }
}