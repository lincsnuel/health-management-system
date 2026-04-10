package com.healthcore.patientservice.application.command.model;

import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.enums.IdentityType;

import java.time.LocalDate;
import java.util.List;

public record RegisterPatientCommand(

        /* ================== SYSTEM CONTEXT ================== */
        String hospitalPatientNumber,

        /* ================== CORE IDENTITY ================== */
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Gender gender,
        String email,
        String contactNumber,

        /* ================== NATIONAL ID (OPTIONAL) ================== */
        IdentityType identityType,
        String nationalIdNumber,

        /* ================== ADDRESS ================== */
        RegisterAddressCommand address,

        /* ================== RESPONSIBLE PARTIES ================== */
        List<RegisterResponsiblePartyCommand> responsibleParties,

        /* ================== INSURANCE (OPTIONAL) ================== */
        RegisterInsurancePolicyCommand insurancePolicy

) {}