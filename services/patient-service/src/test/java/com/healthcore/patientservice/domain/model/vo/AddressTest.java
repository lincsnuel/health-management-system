//package com.healthcore.patientservice.domain.model.vo;
//
//import com.healthcore.patientservice.domain.exception.InvalidAddressException;
//import com.healthcore.patientservice.domain.model.patient.Address;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class AddressTest {
//
//    @Test
//    void shouldNormalizeFields() {
//        Address address = Address.of(
//                "  12 Allen Avenue  ",
//                "  Ikeja ",
//                "  Lagos  ",
//                "  Nigeria  ",
//                true
//        );
//
//        assertEquals("12 Allen Avenue", address.street());
//        assertEquals("Ikeja", address.city());
//        assertEquals("Lagos", address.state());
//        assertEquals("Nigeria", address.country());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenStreetIsNull() {
//        assertThrows(InvalidAddressException.class, () ->
//                Address.of(null, "City", "State", "Country", false)
//        );
//    }
//
//    @Test
//    void shouldThrowExceptionWhenStreetIsBlank() {
//        assertThrows(InvalidAddressException.class, () ->
//                Address.of("   ", "City", "State", "Country", false)
//        );
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCityIsBlank() {
//        assertThrows(InvalidAddressException.class, () ->
//                Address.of("Street", "  ", "State", "Country", false)
//        );
//    }
//
//    @Test
//    void shouldThrowExceptionWhenStateIsNull() {
//        assertThrows(InvalidAddressException.class, () ->
//                Address.of("Street", "City", null, "Country", false)
//        );
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCountryIsBlank() {
//        assertThrows(InvalidAddressException.class, () ->
//                Address.of("Street", "City", "State", "  ", false)
//        );
//    }
//
//    @Test
//    void shouldReturnPrimaryFlagCorrectly() {
//        Address address = Address.of(
//                "Street",
//                "City",
//                "State",
//                "Country",
//                true
//        );
//
//        assertTrue(address.isPrimary());
//    }
//
//    @Test
//    void shouldUnsetPrimaryWithoutMutatingOriginal() {
//        Address address = Address.of(
//                "Street",
//                "City",
//                "State",
//                "Country",
//                true
//        );
//
//        Address updated = address.unsetPrimary();
//
//        assertFalse(updated.isPrimary());
//        assertTrue(address.isPrimary()); // immutability preserved
//    }
//
//    @Test
//    void shouldSetPrimaryWithoutMutatingOriginal() {
//        Address address = Address.of(
//                "Street",
//                "City",
//                "State",
//                "Country",
//                false
//        );
//
//        Address updated = address.setPrimary();
//
//        assertTrue(updated.isPrimary());
//        assertFalse(address.isPrimary()); // immutability preserved
//    }
//
//    @Test
//    void shouldBeEqualWhenAllFieldsMatch() {
//        Address a1 = Address.of("Street", "City", "State", "Country", true);
//        Address a2 = Address.of("Street", "City", "State", "Country", true);
//
//        assertEquals(a1, a2);
//        assertEquals(a1.hashCode(), a2.hashCode());
//    }
//
//    @Test
//    void shouldNotBeEqualWhenPrimaryDiffers() {
//        Address a1 = Address.of("Street", "City", "State", "Country", true);
//        Address a2 = Address.of("Street", "City", "State", "Country", false);
//
//        assertNotEquals(a1, a2);
//    }
//}