package com.healthcore.patientservice.application.service;

import com.healthcore.patientservice.application.command.model.UpdatePatientAddressCommand;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.loader.PatientLoader;
import com.healthcore.patientservice.application.usecase.UpdatePatientAddressUseCase;
import com.healthcore.patientservice.domain.model.patient.Patient;
import com.healthcore.patientservice.domain.model.patient.Address;
import com.healthcore.patientservice.domain.model.vo.AddressId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePatientAddressService
        implements UpdatePatientAddressUseCase {

    private final PatientCommandRepository patientRepository;
    private final PatientLoader loader;

    @Override
    public void updateAddress(UpdatePatientAddressCommand command) {

        Patient patient = loader.load(command.patientId(), command.tenantId());

        Address address = Address.reconstruct(
                AddressId.of(command.addressId()),
                command.street(),
                command.city(),
                command.state(),
                command.country(),
                command.primary()
        );

        patient.updateAddress(address);

        patientRepository.save(patient);
    }
}