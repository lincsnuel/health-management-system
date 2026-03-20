package com.healthcore.patientservice.application.service;

import com.healthcore.patientservice.application.command.model.UpdatePatientInsuranceCommand;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.loader.PatientLoader;
import com.healthcore.patientservice.application.usecase.UpdatePatientInsuranceUseCase;
import com.healthcore.patientservice.domain.model.patient.InsurancePolicy;
import com.healthcore.patientservice.domain.model.patient.Patient;
import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePatientInsuranceService
        implements UpdatePatientInsuranceUseCase {

    private final PatientCommandRepository patientRepository;
    private final PatientLoader loader;

    @Override
    public void updateInsurance(UpdatePatientInsuranceCommand command) {

        Patient patient = loader.load(command.patientId(), command.tenantId());

        InsurancePolicy policy = InsurancePolicy.reconstruct(
                InsurancePolicyId.of(command.id()),
                command.providerName(),
                command.policyNumber(),
                command.planType(),
                command.coverageStart(),
                command.coverageEnd(),
                command.main(),
                true
        );

        patient.updateInsurance(policy);

        patientRepository.save(patient);
    }
}