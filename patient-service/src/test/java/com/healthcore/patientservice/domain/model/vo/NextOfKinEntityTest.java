package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidNextOfKinException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextOfKinTest {

    private PhoneNumber samplePhone() {
        return PhoneNumber.of("+2348012345678");
    }

    // ======= Valid Construction =======
    @Test
    void shouldCreateValidNextOfKin() {
        NextOfKin nok = NextOfKin.of("John Doe", "father", samplePhone(), "123 Street");

        assertEquals("Father", nok.relationship()); // normalized
        assertEquals("John Doe", nok.fullName());
        assertEquals(samplePhone(), nok.contactNumber());
        assertEquals("123 Street", nok.address());
    }

    // ======= Null or Invalid Fields =======
    @Test
    void shouldThrowIfFullNameIsNull() {
        assertThrows(InvalidNextOfKinException.class, () ->
                NextOfKin.of(null, "mother", samplePhone(), "123 Street")
        );
    }

    @Test
    void shouldThrowIfRelationshipIsNullOrBlank() {
        assertThrows(InvalidNextOfKinException.class, () ->
                NextOfKin.of("Jane Doe", null, samplePhone(), "123 Street")
        );

        assertThrows(InvalidNextOfKinException.class, () ->
                NextOfKin.of("Jane Doe", "   ", samplePhone(), "123 Street")
        );
    }

    @Test
    void shouldThrowIfPhoneNumberIsNull() {
        assertThrows(InvalidNextOfKinException.class, () ->
                NextOfKin.of("Jane Doe", "mother", null, "123 Street")
        );
    }

    // ======= Relationship Normalization =======
    @Test
    void shouldNormalizeRelationship() {
        NextOfKin nok = NextOfKin.of("Jane Doe", "broTHER", samplePhone(), "123 Street");
        assertEquals("Brother", nok.relationship());

        NextOfKin nok2 = NextOfKin.of("Jane Doe", "  sIsTer  ", samplePhone(), "123 Street");
        assertEquals("Sister", nok2.relationship());
    }
}