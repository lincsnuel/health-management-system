package com.healthcore.patientservice.domain.model.vo;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HospitalPatientNumberTest {

    @Test
    void shouldNormalizeValue() {
        var number = HospitalPatientNumber.of("  abc123  ");
        assertEquals("ABC123", number.value());
    }

    @Test
    void shouldAllowNull() {
        var number = HospitalPatientNumber.of(null);
        assertNull(number.value());
    }

    @Test
    void shouldNormalizeBlankToEmptyString() {
        var number = HospitalPatientNumber.of("   ");
        assertEquals("", number.value());
    }
}