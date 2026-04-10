package com.healthcore.patientservice.application.command.service;

import com.healthcore.patientservice.application.command.model.UpdateResponsiblePartyCommand;
import com.healthcore.patientservice.domain.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.common.loader.PatientLoader;
import com.healthcore.patientservice.application.command.usecase.UpdateResponsiblePartyUseCase;
import com.healthcore.patientservice.domain.model.patient.Patient;
import com.healthcore.patientservice.domain.model.patient.Address;
import com.healthcore.patientservice.domain.model.vo.AddressId;
import com.healthcore.patientservice.domain.model.vo.PersonName;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import com.healthcore.patientservice.domain.model.patient.ResponsibleParty;
import com.healthcore.patientservice.domain.model.vo.ResponsiblePartyId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateResponsiblePartyService implements UpdateResponsiblePartyUseCase {

    private final PatientCommandRepository patientRepository;
    private final PatientLoader loader;

    @Override
    public void updateResponsibleParty(UpdateResponsiblePartyCommand command) {

        Patient patient = loader.load(command.patientId());

        ResponsibleParty responsibleParty = ResponsibleParty.reconstruct(
                ResponsiblePartyId.of(command.responsiblePartyId()),
                new PersonName(command.firstName(), command.lastName()),
                new PhoneNumber(command.phoneNumber()),
                command.relationship(),
                command.type(),
                Address.reconstruct(
                        AddressId.of(command.addressId()),
                        command.street(),
                        command.city(),
                        command.state(),
                        command.country(),
                        command.primary()
                )
                );

        patient.updateResponsibleParty(responsibleParty);

        patientRepository.save(patient);

    }
}
