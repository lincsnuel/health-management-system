package com.healthcore.patientservice.application.command.service;

import com.healthcore.patientservice.application.exception.PatientNotFoundException;
import com.healthcore.patientservice.application.command.usecase.DeletePatientUseCase;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.command.PatientJpaCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePatientService implements DeletePatientUseCase {
    private final PatientJpaCommandRepository patientJpaCommandRepository;
//    private final PatientEntityMapper jpaMapper;

    @Override
    public void deletePatientById(UUID patientId) {
        if (patientJpaCommandRepository.existsById(patientId)) {
            patientJpaCommandRepository.deleteById(patientId);
        } else throw new PatientNotFoundException("Patient does not exist with id " + patientId);
    }
}
