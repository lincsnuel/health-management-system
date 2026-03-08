package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidPhoneNumberException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    // ======= Valid Numbers =======
    @Test
    void shouldNormalizeLocalNumber() {
        PhoneNumber number = PhoneNumber.of("08012345678");
        assertEquals("+2348012345678", number.value());
    }

    @Test
    void shouldAcceptAlreadyNormalizedNumber() {
        PhoneNumber number = PhoneNumber.of("+2349012345678");
        assertEquals("+2349012345678", number.value());
    }

    @Test
    void shouldRemoveNonNumericCharactersAndNormalize() {
        PhoneNumber number = PhoneNumber.of("080-1234-5678");
        assertEquals("+2348012345678", number.value());
    }

    @Test
    void shouldRemoveSpacesAndParentheses() {
        PhoneNumber number = PhoneNumber.of("080 1234 (5678)");
        assertEquals("+2348012345678", number.value());
    }

    // ======= Null or Blank =======
    @Test
    void shouldThrowOnNullOrBlank() {
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(null));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of(""));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("   "));
    }

    // ======= Non-Nigerian Numbers =======
    @Test
    void shouldThrowOnNonNigerianNumber() {
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("+12025550123"));
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("1234567890"));
    }

    // ======= Invalid Nigerian Numbers =======
    @Test
    void shouldThrowOnInvalidLengthOrPrefix() {
        // Too short
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("0701234"));
        // Wrong second digit
        assertThrows(InvalidPhoneNumberException.class, () -> PhoneNumber.of("06012345678"));
    }
}