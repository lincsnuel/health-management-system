package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.application.util.AgeCalculator;
import com.healthcore.patientservice.domain.model.enums.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Full patient details for query/read operations
 */
public record PatientDetails(
        UUID patientId,
        String hospitalPatientNumber,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Gender gender,
        String contactNumber,
        String email,
        BloodGroup bloodGroup,
        Genotype genotype,
        Religion religion,
        IdentityType identityType,
        String nationalIdNumber,
        List<ResponsiblePartyDetail> responsibleParties,
        List<AddressDetail> addresses,
        List<InsuranceDetail> insurancePolicies,
        PatientStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    /**
     * Derived full name from first and last name
     */
    public String fullName() {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }

    /**
     * Derived age from date of birth
     */
    public int age() {
        return AgeCalculator.calculate(dateOfBirth);
    }
}