package com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper;

import com.healthcore.patientservice.application.command.model.*;

import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.patient.InsurancePolicy;
import com.healthcore.patientservice.domain.model.patient.Address;
import com.healthcore.patientservice.domain.model.vo.PersonName;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import com.healthcore.patientservice.domain.model.patient.ResponsibleParty;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request.*;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PatientCommandRestMapper {

    /* =========================================================
       REQUEST → COMMAND
       ========================================================= */

    public RegisterPatientCommand toRegisterPatientCommand(
            RegisterPatientRequest request,
            String tenantId
    ) {

        if (request == null) {
            return null;
        }

        return new RegisterPatientCommand(
                tenantId,
                request.hospitalPatientNumber(),
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                Gender.valueOf(request.gender().toUpperCase()),
                request.email(),
                request.contactNumber(),
                request.identityType(),
                request.nationalIdNumber(),
                toAddressCommand(request.address()),
                toResponsiblePartyCommands(request.responsibleParties()),
                toInsuranceCommand(request.insurancePolicy())
        );
    }

    /* =========================================================
       RESULT → RESPONSE
       ========================================================= */

    public RegisterPatientResponse toRegisterPatientResponse(
            RegisterPatientResult result
    ) {

        if (result == null) {
            return null;
        }

        return new RegisterPatientResponse(
                result.patientId(),
                result.tenantId(),
                result.hospitalPatientNumber(),
                result.fullName(),
                result.status().toString()
        );
    }

    /* =========================================================
       ADDRESS
       ========================================================= */

    public List<RegisterAddressCommand> toAddressCommands(List<AddressRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .filter(Objects::nonNull)
                .map(this::toAddressCommand)
                .toList();
    }

    private RegisterAddressCommand toAddressCommand(AddressRequest request) {

        return new RegisterAddressCommand(
                request.street(),
                request.city(),
                request.state(),
                request.country(),
                true
        );
    }

    /* =========================================================
       INSURANCE
       ========================================================= */

    public List<RegisterInsurancePolicyCommand> toInsuranceCommands(
            List<InsurancePolicyRequest> requests
    ) {

        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .filter(Objects::nonNull)
                .map(this::toInsuranceCommand)
                .toList();
    }

    private RegisterInsurancePolicyCommand toInsuranceCommand(
            InsurancePolicyRequest request
    ) {
        if (request == null) {
            return null;
        }

        return new RegisterInsurancePolicyCommand(
                request.providerName(),
                request.policyNumber(),
                request.planType(),
                request.coverageStart(),
                request.coverageEnd(),
                request.isMain()
        );
    }

    /* =========================================================
       RESPONSIBLE PARTY
       ========================================================= */

    public List<RegisterResponsiblePartyCommand> toResponsiblePartyCommands(
            List<ResponsiblePartyRequest> requests
    ) {

        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .filter(Objects::nonNull)
                .map(this::toResponsiblePartyCommand)
                .toList();
    }

    public RegisterResponsiblePartyCommand toResponsiblePartyCommand(
            ResponsiblePartyRequest request
    ) {

        return new RegisterResponsiblePartyCommand(
                request.firstName(),
                request.lastName(),
                request.contactNumber(),
                request.relationship(),
                request.type(),
                toAddressCommand(request.address())
        );
    }

    // ---------------- Responsible Party ----------------
    public ResponsibleParty toResponsibleParty(RegisterResponsiblePartyCommand cmd) {
        return ResponsibleParty.create(
                PersonName.of(cmd.firstName(), cmd.lastName()),
                PhoneNumber.of(cmd.contactNumber()),
                cmd.relationship(),
                cmd.type(),
                cmd.address() != null
                        ? toAddress(cmd.address())
                        : null
        );
    }

    // ---------------- Address ----------------
    public Address toAddress(RegisterAddressCommand cmd) {
        return Address.create(
                cmd.street(),
                cmd.city(),
                cmd.state(),
                cmd.country(),
                cmd.primary()
        );
    }

    // ---------------- Insurance ----------------
    public InsurancePolicy toInsurancePolicy(RegisterInsurancePolicyCommand cmd) {
        return InsurancePolicy.create(
                cmd.providerName(),
                cmd.policyNumber(),
                cmd.planType(),
                cmd.coverageStart(),
                cmd.coverageEnd(),
                cmd.main()
        );
    }

    // ---------------- Optional list converters ----------------
    public List<ResponsibleParty> toResponsiblePartyList(List<RegisterResponsiblePartyCommand> list) {
        return list.stream()
                .map(this::toResponsibleParty)
                .toList();
    }

    public List<Address> toAddressList(List<RegisterAddressCommand> list) {
        return list.stream()
                .map(this::toAddress)
                .toList();
    }

    public List<InsurancePolicy> toInsurancePolicyList(List<RegisterInsurancePolicyCommand> list) {
        return list.stream()
                .map(this::toInsurancePolicy)
                .toList();
    }
}