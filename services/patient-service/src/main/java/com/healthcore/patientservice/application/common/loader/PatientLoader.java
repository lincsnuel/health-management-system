package com.healthcore.patientservice.application.common.loader;

import com.healthcore.patientservice.domain.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.exception.PatientNotFoundException;
import com.healthcore.patientservice.domain.model.patient.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientLoader {

    private final PatientCommandRepository patientRepository;

    public Patient load(UUID patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id: "
                        + patientId + " not found"));
    }
}