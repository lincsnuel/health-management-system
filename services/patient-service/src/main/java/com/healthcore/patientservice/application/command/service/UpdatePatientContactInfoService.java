package com.healthcore.patientservice.application.command.service;

import com.healthcore.patientservice.application.command.model.UpdatePatientContactInfoCommand;
import com.healthcore.patientservice.domain.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.common.loader.PatientLoader;
import com.healthcore.patientservice.application.command.usecase.UpdatePatientContactInfoUseCase;
import com.healthcore.patientservice.domain.model.patient.Patient;
import com.healthcore.patientservice.domain.model.vo.EmailAddress;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePatientContactInfoService
        implements UpdatePatientContactInfoUseCase {

    private final PatientCommandRepository patientRepository;
    private final PatientLoader loader;

    @Override
    public void updateContactInfo(UpdatePatientContactInfoCommand command) {

        Patient patient = loader.load(command.patientId(), command.tenantId());

        PhoneNumber phone = PhoneNumber.of(command.phoneNumber());
        EmailAddress email = EmailAddress.of(command.email());

        patient.updateContactInfo(phone, email);

        patientRepository.save(patient);
    }
}