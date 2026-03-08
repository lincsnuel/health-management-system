package com.healthcore.patientservice.domain.model.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantIdTest {

    @Test
    void shouldTrimAndUppercase() {
        TenantId tenantId = TenantId.of("  abc123  ");
        assertEquals("ABC123", tenantId.value());
    }

    @Test
    void shouldKeepAlreadyUppercase() {
        TenantId tenantId = TenantId.of("XYZ789");
        assertEquals("XYZ789", tenantId.value());
    }

    @Test
    void factoryMethodShouldWork() {
        TenantId tenantId = TenantId.of("tenant001");
        assertEquals("TENANT001", tenantId.value());
    }

    @Test
    void shouldHandleEmptyString() {
        TenantId tenantId = TenantId.of("   ");
        assertEquals("", tenantId.value());
    }

    @Test
    void equalityCheck() {
        TenantId id1 = TenantId.of("tenantX");
        TenantId id2 = TenantId.of("TENANTX");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void immutabilityCheck() {
        TenantId tenantId = TenantId.of("immutable123");
        assertEquals("IMMUTABLE123", tenantId.value());
    }
}