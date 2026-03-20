package com.healthcore.patientservice.domain.model.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PatientIdTest {

    @Test
    void shouldCreatePatientIdFromUUID() {
        UUID uuid = UUID.randomUUID();
        PatientId id = PatientId.of(uuid);

        assertNotNull(id);
        assertEquals(uuid, id.value());
    }

    @Test
    void shouldGenerateNewIdSuccessfully() {
        PatientId id1 = PatientId.newId();
        PatientId id2 = PatientId.newId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2); // very high probability they are unique
    }

    @Test
    void shouldThrowWhenUUIDIsNull() {
        assertThrows(NullPointerException.class, () -> PatientId.of(null));
    }

    @Test
    void equalityAndHashCodeShouldWork() {
        UUID uuid = UUID.randomUUID();
        PatientId id1 = PatientId.of(uuid);
        PatientId id2 = PatientId.of(uuid);
        PatientId id3 = PatientId.newId();

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());

        assertNotEquals(id1, id3);
    }
}