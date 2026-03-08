package com.healthcore.patientservice.application.service;

import com.healthcore.patientservice.application.usecase.DeletePatientUseCase;
import com.healthcore.patientservice.application.exception.NotFoundException;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.PatientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePatientService implements DeletePatientUseCase {
    private final PatientEntityRepository patientEntityRepository;
//    private final PatientEntityMapper jpaMapper;

    @Override
    public void deletePatientById(UUID patientId) {
        if (patientEntityRepository.existsById(patientId)) {
            patientEntityRepository.deleteById(patientId);
        } else throw new NotFoundException("Patient does not exist with id " + patientId);
    }
}
