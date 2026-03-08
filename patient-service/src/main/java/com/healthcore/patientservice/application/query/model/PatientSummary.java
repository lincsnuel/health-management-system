package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.enums.PatientStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record PatientSummary(
        UUID patientId,
        String hospitalPatientNumber,
        String fullName,
        Gender gender,
        LocalDate dateOfBirth,
        String contactNumber,
        PatientStatus status
) {

    /**
     * Calculates age from date of birth dynamically.
     */
    public int age() {
        if (dateOfBirth == null) {
            return 0; // or throw an exception if DOB is mandatory
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}