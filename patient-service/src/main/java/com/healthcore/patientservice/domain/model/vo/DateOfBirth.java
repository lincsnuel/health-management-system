package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidDateOfBirthException;

import java.time.LocalDate;
import java.time.Period;

public record DateOfBirth(LocalDate value) {

    public DateOfBirth {
        if (value.isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirthException("Date of birth cannot be in the future");
        }
    }

    /** Calculate age in years from this DOB to today */
    public int calculateAge() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    /** Check if the patient is a minor (age < 18) */
    public boolean isMinor() {
        return calculateAge() < 18;
    }

    /** Check if the patient is adult (age >= 18) */
    public boolean isAdult() {
        return !isMinor();
    }

    /** Check if age is older than given years */
    public boolean isOlderThan(int years) {
        return calculateAge() > years;
    }

    /** Check if age is younger than given years */
    public boolean isYoungerThan(int years) {
        return calculateAge() < years;
    }

    /** Return a new DOB shifted forward by n years */
    public DateOfBirth plusYears(int years) {
        return new DateOfBirth(value.plusYears(years));
    }

    /** Return a new DOB shifted backward by n years */
    public DateOfBirth minusYears(int years) {
        return new DateOfBirth(value.minusYears(years));
    }

    public static DateOfBirth of(LocalDate date) {
        return new DateOfBirth(date);
    }
}