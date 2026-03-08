package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidDateOfBirthException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateOfBirthTest {

    @Test
    void shouldCalculateCorrectAge() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = today.minusYears(25);

        DateOfBirth dob = DateOfBirth.of(birthDate);

        assertEquals(25, dob.calculateAge());
    }

    @Test
    void shouldNotAllowFutureDate() {
        LocalDate future = LocalDate.now().plusDays(1);

        assertThrows(InvalidDateOfBirthException.class, () ->
                DateOfBirth.of(future)
        );
    }

    @Test
    void shouldIdentifyMinorCorrectly() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = today.minusYears(10);

        DateOfBirth dob = DateOfBirth.of(birthDate);

        assertTrue(dob.isMinor());
        assertFalse(dob.isAdult());
    }

    @Test
    void shouldIdentifyAdultCorrectly() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = today.minusYears(18);

        DateOfBirth dob = DateOfBirth.of(birthDate);

        assertTrue(dob.isAdult());
        assertFalse(dob.isMinor());
    }

    @Test
    void shouldCheckOlderThanCorrectly() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = today.minusYears(30);

        DateOfBirth dob = DateOfBirth.of(birthDate);

        assertTrue(dob.isOlderThan(20));
        assertFalse(dob.isOlderThan(40));
    }

    @Test
    void shouldCheckYoungerThanCorrectly() {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = today.minusYears(15);

        DateOfBirth dob = DateOfBirth.of(birthDate);

        assertTrue(dob.isYoungerThan(18));
        assertFalse(dob.isYoungerThan(10));
    }

    @Test
    void plusYearsShouldReturnNewInstance() {
        LocalDate today = LocalDate.now();
        DateOfBirth dob = DateOfBirth.of(today.minusYears(20));

        DateOfBirth updated = dob.plusYears(2);

        assertNotEquals(dob.value(), updated.value());
        assertEquals(dob.value().plusYears(2), updated.value());
    }

    @Test
    void minusYearsShouldReturnNewInstance() {
        LocalDate today = LocalDate.now();
        DateOfBirth dob = DateOfBirth.of(today.minusYears(20));

        DateOfBirth updated = dob.minusYears(3);

        assertEquals(dob.value().minusYears(3), updated.value());
    }

    @Test
    void shouldBeAdultExactlyAt18() {
        LocalDate today = LocalDate.now();
        DateOfBirth dob = DateOfBirth.of(today.minusYears(18));

        assertTrue(dob.isAdult());
        assertFalse(dob.isMinor());
    }

    @Test
    void shouldStillBeMinorOneDayBefore18() {
        LocalDate today = LocalDate.now();
        DateOfBirth dob = DateOfBirth.of(today.minusYears(18).plusDays(1));

        assertTrue(dob.isMinor());
    }

    @Test
    void shouldHandleLeapYearBirthCorrectly() {
        LocalDate leapDob = LocalDate.of(2004, 2, 29);
        DateOfBirth dob = DateOfBirth.of(leapDob);

        assertDoesNotThrow(dob::calculateAge);
    }
}