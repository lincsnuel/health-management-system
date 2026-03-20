package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidNationalIdentityException;
import com.healthcore.patientservice.domain.model.enums.IdentityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NationalIdentityTest {

    // ======= Null Type =======
    @Test
    void shouldThrowIfTypeIsNull() {
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(null, "12345678901")
        );
    }

    // ======= Normalization =======
    @Test
    void shouldTrimAndUppercaseNumber() {
        NationalIdentity id = NationalIdentity.of(IdentityType.INTERNATIONAL_PASSPORT, "  ab123cd  ");
        assertEquals("AB123CD", id.number());
    }

    // ======= NIN / BVN =======
    @Test
    void shouldAcceptValidNin() {
        NationalIdentity id = NationalIdentity.of(IdentityType.NIN, "12345678901");
        assertEquals("12345678901", id.number());
    }

    @Test
    void shouldRejectInvalidNin() {
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.NIN, "12345")
        );
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.BVN, "abcdefghijk")
        );
    }

    // ======= International Passport =======
    @Test
    void shouldAcceptValidPassport() {
        NationalIdentity id = NationalIdentity.of(IdentityType.INTERNATIONAL_PASSPORT, "A12345");
        assertEquals("A12345", id.number());
    }

    @Test
    void shouldRejectInvalidPassport() {
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.INTERNATIONAL_PASSPORT, "123") // too short
        );
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.INTERNATIONAL_PASSPORT, "1234567890123456") // too long
        );
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.INTERNATIONAL_PASSPORT, "ab$123") // invalid char
        );
    }

    // ======= Driver's License =======
    @Test
    void shouldAcceptValidDriversLicense() {
        NationalIdentity id = NationalIdentity.of(IdentityType.DRIVERS_LICENSE, "ABCDE");
        assertEquals("ABCDE", id.number());
    }

    @Test
    void shouldRejectInvalidDriversLicense() {
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.DRIVERS_LICENSE, "123")
        );
    }

    // ======= Voter Card =======
    @Test
    void shouldAcceptValidVoterCard() {
        NationalIdentity id = NationalIdentity.of(IdentityType.VOTER_CARD, "12345");
        assertEquals("12345", id.number());
    }

    @Test
    void shouldRejectInvalidVoterCard() {
        assertThrows(InvalidNationalIdentityException.class, () ->
                NationalIdentity.of(IdentityType.VOTER_CARD, "12")
        );
    }
}