package com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper;

import com.healthcore.patientservice.application.command.model.*;

import com.healthcore.patientservice.domain.model.enums.Gender;
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

    private List<AddressCommand> toAddressCommands(List<AddressRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        return requests.stream()
                .filter(Objects::nonNull)
                .map(this::toAddressCommand)
                .toList();
    }

    private AddressCommand toAddressCommand(AddressRequest request) {

        return new AddressCommand(
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

    private List<InsurancePolicyCommand> toInsuranceCommands(
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

    private InsurancePolicyCommand toInsuranceCommand(
            InsurancePolicyRequest request
    ) {

        return new InsurancePolicyCommand(
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

    private List<ResponsiblePartyCommand> toResponsiblePartyCommands(
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

    private ResponsiblePartyCommand toResponsiblePartyCommand(
            ResponsiblePartyRequest request
    ) {

        return new ResponsiblePartyCommand(
                request.firstName(),
                request.lastName(),
                request.contactNumber(),
                request.relationship(),
                request.type(),
                toAddressCommand(request.address())
        );
    }
}