package com.healthcore.patientservice.application.query.service;

import com.healthcore.patientservice.application.query.model.EligiblePatientProjection;
import com.healthcore.patientservice.application.query.repository.PatientQueryRepository;
import com.healthcore.patientservice.application.query.usecase.FindEligiblePatientsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindEligiblePatientsService
        implements FindEligiblePatientsUseCase {

    private final PatientQueryRepository patientQueryRepository;

    @Override
    public List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge) {

        return patientQueryRepository.findEligiblePatients(minAge, maxAge);
    }
}