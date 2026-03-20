package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailAddressTest {

    @Test
    void shouldThrowExceptionWhenNull() {
        assertThrows(InvalidEmailException.class, () ->
                EmailAddress.of(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenBlank() {
        assertThrows(InvalidEmailException.class, () ->
                EmailAddress.of("   ")
        );
    }

    @Test
    void shouldNormalizeEmail() {
        EmailAddress email = EmailAddress.of("  John.DOE@Example.COM  ");

        assertEquals("john.doe@example.com", email.value());
    }

    @Test
    void shouldRejectInvalidFormatWithoutAtSymbol() {
        assertThrows(InvalidEmailException.class, () ->
                EmailAddress.of("invalid-email")
        );
    }

    @Test
    void shouldCreateValidEmail() {
        EmailAddress email = EmailAddress.of("user@example.com");

        assertEquals("user@example.com", email.value());
    }

    @Test
    void factoryMethodShouldMatchConstructor() {
        EmailAddress e1 = new EmailAddress("test@mail.com");
        EmailAddress e2 = EmailAddress.of("test@mail.com");

        assertEquals(e1, e2);
    }

    @Test
    void shouldMaskEmailCorrectlyWhenLongerThanOneCharacterBeforeAt() {
        EmailAddress email = EmailAddress.of("john@example.com");

        assertEquals("j***@example.com", email.masked());
    }

    @Test
    void shouldMaskEmailCorrectlyWhenSingleCharacterBeforeAt() {
        EmailAddress email = EmailAddress.of("a@example.com");

        assertEquals("***@example.com", email.masked());
    }

    @Test
    void shouldHandleVeryShortEmailGracefully() {
        EmailAddress email = EmailAddress.of("x@a.com");

        assertEquals("***@a.com", email.masked());
    }

    @Test
    void shouldAcceptMultiLevelDomain() {
        EmailAddress email = EmailAddress.of("user@mail.example.com");

        assertEquals("user@mail.example.com", email.value());
    }
}