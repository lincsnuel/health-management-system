package com.healthcore.patientservice.application.service;

import com.healthcore.patientservice.application.command.model.RegisterPatientCommand;
import com.healthcore.patientservice.application.command.model.RegisterPatientResult;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.command.validator.CommandValidator;
import com.healthcore.patientservice.application.usecase.RegisterPatientUseCase;
import com.healthcore.patientservice.domain.model.InsurancePolicy;
import com.healthcore.patientservice.domain.model.Patient;
import com.healthcore.patientservice.domain.model.vo.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterPatientService implements RegisterPatientUseCase {

    private final PatientCommandRepository patientRepository;
    private final List<CommandValidator<RegisterPatientCommand>> validators;

    @Override
    public RegisterPatientResult registerPatient(RegisterPatientCommand command) {

        validators.forEach(v -> v.validate(command));

        // Next of Kin and/or Guarantor mapping
        var domainResponsibleParties = command.responsibleParties()
                .stream()
                .map(cmd -> ResponsibleParty.of(
                        PersonName.of(cmd.firstName(), cmd.lastName()),
                        PhoneNumber.of(cmd.contactNumber()),
                        cmd.relationship(),
                        cmd.type(),
                        Address.of(
                                cmd.address().street(),
                                cmd.address().city(),
                                cmd.address().state(),
                                cmd.address().country(),
                                true
                        )
                ))
                .toList();

        // Address mapping
        var commandAddr = command.address();
        var domainAddr = Address.of(
                commandAddr.street(),
                commandAddr.city(),
                commandAddr.state(),
                commandAddr.country(),
                true
        );

        // Create patient aggregate
        Patient patient = Patient.register(
                TenantId.of(command.tenantId()),
                HospitalPatientNumber.of(command.hospitalPatientNumber()),
                PersonName.of(command.firstName(), command.lastName()),
                DateOfBirth.of(command.dateOfBirth()),
                command.gender(),
                PhoneNumber.of(command.contactNumber()),
                domainResponsibleParties
        );

        // Optional email
        if (command.email() != null) {
            patient.updateEmail(EmailAddress.of(command.email()));
        }

        //Optional Identity
        if (command.identityType() != null && command.nationalIdNumber() != null) {
            patient.assignNationalIdentity(
                    NationalIdentity.of(command.identityType(), command.nationalIdNumber())
            );
        }

        // Add primary address
        patient.addAddress(domainAddr);

        // Optional insurance
        if (command.insurancePolicy() != null) {

            var insuranceCmd = command.insurancePolicy();

            var insurance = new InsurancePolicy(
                    insuranceCmd.providerName(),
                    insuranceCmd.policyNumber(),
                    insuranceCmd.planType(),
                    insuranceCmd.coverageStart(),
                    insuranceCmd.coverageEnd(),
                    insuranceCmd.main(),
                    true
            );

            patient.addInsurance(insurance);
        }

        patientRepository.save(patient);

        return new RegisterPatientResult(
                patient.getId().value(),
                patient.getTenantId().value(),
                patient.getHospitalPatientNumber().value(),
                patient.getName().getFullName(),
                patient.getStatus()
        );
    }
}