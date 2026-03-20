package com.healthcore.patientservice.application.util;

import java.time.LocalDate;
import java.time.Period;

public final class AgeCalculator {

    private AgeCalculator() {}

    public static int calculate(LocalDate dateOfBirth) {
        return dateOfBirth == null ? 0 :
                Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}