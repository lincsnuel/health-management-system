package com.healthcore.patientservice.domain.model.vo;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InsurancePolicyIdTest {

    @Test
    void shouldCreateValidId() {
        UUID uuid = UUID.randomUUID();
        InsurancePolicyId id = new InsurancePolicyId(uuid);

        assertEquals(uuid, id.value());
    }

    @Test
    void factoryMethodShouldWork() {
        UUID uuid = UUID.randomUUID();
        InsurancePolicyId id = InsurancePolicyId.of(uuid);

        assertEquals(uuid, id.value());
    }

    @Test
    void newIdShouldGenerateNonNullUniqueValue() {
        InsurancePolicyId id1 = InsurancePolicyId.newId();
        InsurancePolicyId id2 = InsurancePolicyId.newId();

        assertNotNull(id1.value());
        assertNotNull(id2.value());

        assertNotEquals(id1, id2); // extremely safe assumption
    }

    @Test
    void shouldThrowWhenNull() {
        assertThrows(NullPointerException.class, () ->
                new InsurancePolicyId(null)
        );
    }

    @Test
    void equalityAndHashCodeShouldWork() {
        UUID uuid = UUID.randomUUID();

        InsurancePolicyId id1 = new InsurancePolicyId(uuid);
        InsurancePolicyId id2 = new InsurancePolicyId(uuid);
        InsurancePolicyId id3 = new InsurancePolicyId(UUID.randomUUID());

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());

        assertNotEquals(id1, id3);
    }
}