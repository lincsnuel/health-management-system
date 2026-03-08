package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidPersonNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonNameTest {

    @Test
    void shouldCreateValidPersonName() {
        PersonName name = PersonName.of("john", "doe");
        assertEquals("John", name.firstName());
        assertEquals("Doe", name.lastName());
        assertEquals("John Doe", name.getFullName());
        assertEquals("J.D.", name.getInitials());
    }

    @Test
    void shouldTrimAndNormalizeNames() {
        PersonName name = PersonName.of("  alice ", "  smith ");
        assertEquals("Alice", name.firstName());
        assertEquals("Smith", name.lastName());
    }

    @Test
    void shouldHandleSingleCharacterNames() {
        PersonName name = PersonName.of("j", "d");
        assertEquals("J", name.firstName());
        assertEquals("D", name.lastName());
        assertEquals("J D", name.getFullName());
        assertEquals("J.D.", name.getInitials());
    }

    @Test
    void shouldThrowForInvalidFirstName() {
        assertThrows(InvalidPersonNameException.class, () -> PersonName.of("John123", "Doe"));
        assertThrows(InvalidPersonNameException.class, () -> PersonName.of("John!", "Doe"));
    }

    @Test
    void shouldThrowForInvalidLastName() {
        assertThrows(InvalidPersonNameException.class, () -> PersonName.of("John", "Doe123"));
        assertThrows(InvalidPersonNameException.class, () -> PersonName.of("John", "Doe!"));
    }

    @Test
    void factoryMethodShouldWork() {
        PersonName name = PersonName.of("Bob", "Brown");
        assertEquals("Bob Brown", name.getFullName());
    }
}