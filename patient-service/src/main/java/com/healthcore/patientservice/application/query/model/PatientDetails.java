package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.domain.model.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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
        List<InsuranceDetail> insurances,
        PatientStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
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
        return dateOfBirth != null ? Period.between(dateOfBirth, LocalDate.now()).getYears() : 0;
    }
}