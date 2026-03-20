package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.application.util.AgeCalculator;
import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.enums.PatientStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Lightweight patient info for listing purposes
 */
public record PatientListItem(
        UUID patientId,
        String hospitalPatientNumber,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dateOfBirth,
        String contactNumber,
        PatientStatus status
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