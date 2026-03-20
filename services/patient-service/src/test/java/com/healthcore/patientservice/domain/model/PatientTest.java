//package com.healthcore.patientservice.domain.model;
//
//import com.healthcore.patientservice.domain.exception.*;
//import com.healthcore.patientservice.domain.model.enums.BloodGroup;
//import com.healthcore.patientservice.domain.model.enums.Genotype;
//import com.healthcore.patientservice.domain.model.enums.Gender;
//import com.healthcore.patientservice.domain.model.vo.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static com.healthcore.patientservice.domain.model.enums.IdentityType.NIN;
//import static org.junit.jupiter.api.Assertions.*;
//
//class PatientTest {
//
//    private TenantId tenantId;
//    private HospitalPatientNumber hospitalPatientNumber;
//    private PersonName name;
//    private DateOfBirth adultDob;
//    private DateOfBirth minorDob;
//    private PhoneNumber phone;
//    private NextOfKin kin;
//
//    @BeforeEach
//    void setup() {
//        tenantId = TenantId.of("ESUTTH");
//        hospitalPatientNumber = HospitalPatientNumber.of("ESUTTH-PAT-26-001");
//        name = PersonName.of("John", "Doe");
//        adultDob = DateOfBirth.of(LocalDate.now().minusYears(30));
//        minorDob = DateOfBirth.of(LocalDate.now().minusYears(10));
//        phone = PhoneNumber.of("08031234567");
//        kin = NextOfKin.of(
//                "Jane Doe",
//                "Mother",
//                PhoneNumber.of("08039876543"),
//                "Street 1 City State Nigeria"
//        );
//    }
//
//    /* ================== REGISTRATION ================== */
//
//    @Test
//    void shouldGenerateIdOnRegistration() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        assertNotNull(patient.getId());
//        assertNotNull(patient.getId().value());
//        assertTrue(patient.isActive());
//    }
//
//    @Test
//    void generatedIdsShouldBeUnique() {
//        Patient p1 = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        Patient p2 = Patient.register(
//                tenantId,
//                HospitalPatientNumber.of("ESUTTH-PAT-26-002"),
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        assertNotEquals(p1.getId(), p2.getId());
//    }
//
//    @Test
//    void shouldRegisterAdultPatientWithoutNextOfKin() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        assertNull(patient.getNextOfKin());
//    }
//
//    @Test
//    void shouldRegisterMinorPatientWithNextOfKin() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, minorDob, Gender.Male, phone, kin
//        );
//
//        assertNotNull(patient.getNextOfKin());
//        assertEquals("Mother", patient.getNextOfKin().relationship());
//    }
//
//    @Test
//    void shouldThrowIfMinorWithoutNextOfKin() {
//        assertThrows(
//                MinorRequiresResponsiblePartyException.class,
//                () -> Patient.register(
//                        tenantId, hospitalPatientNumber,
//                        name, minorDob, Gender.Male, phone, null
//                )
//        );
//    }
//
//    /* ================== AGE & MINOR ================== */
//
//    @Test
//    void shouldCalculateAgeCorrectly() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        assertEquals(30, patient.calculateAge());
//        assertFalse(patient.isMinor());
//    }
//
//    @Test
//    void shouldDetectMinorCorrectly() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, minorDob, Gender.Male, phone, kin
//        );
//
//        assertTrue(patient.isMinor());
//    }
//
//    /* ================== EMAIL & PHONE ================== */
//
//    @Test
//    void shouldUpdateEmail() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        EmailAddress email = EmailAddress.of("john@example.com");
//        patient.updateEmail(email);
//
//        assertEquals(email, patient.getEmail());
//    }
//
//    @Test
//    void shouldUpdatePhoneNumber() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        PhoneNumber newPhone = PhoneNumber.of("08039876543");
//        patient.updatePhoneNumber(newPhone);
//
//        assertEquals(newPhone, patient.getPhoneNumber());
//    }
//
//    /* ================== ADDRESS ================== */
//
//    @Test
//    void shouldAddAddressAndSwitchPrimary() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        Address addr1 = Address.of("Street 1", "City1", "State1", "Nigeria", true);
//        Address addr2 = Address.of("Street 2", "City2", "State2", "Nigeria", true);
//
//        patient.addAddress(addr1);
//        patient.addAddress(addr2);
//
//        assertFalse(patient.getAddresses().getFirst().isPrimary());
//        assertTrue(patient.getAddresses().get(1).isPrimary());
//    }
//
//    @Test
//    void shouldNotAddMoreThanThreeAddresses() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        patient.addAddress(Address.of("A1", "C1", "S1", "Nigeria", true));
//        patient.addAddress(Address.of("A2", "C2", "S2", "Nigeria", false));
//        patient.addAddress(Address.of("A3", "C3", "S3", "Nigeria", false));
//
//        assertThrows(
//                AddressLimitExceededException.class,
//                () -> patient.addAddress(Address.of("A4", "C4", "S4", "Nigeria", false))
//        );
//    }
//
//    /* ================== NATIONAL ID ================== */
//
//    @Test
//    void shouldAssignNationalIdentityOnce() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        NationalIdentity nid = NationalIdentity.of(NIN, "12345678901");
//
//        patient.assignNationalIdentity(nid);
//        assertEquals(nid, patient.getNationalIdentity());
//
//        assertThrows(
//                NationalIdentityAlreadyAssignedException.class,
//                () -> patient.assignNationalIdentity(nid)
//        );
//    }
//
//    /* ================== NEXT OF KIN ================== */
//
////    @Test
////    void ensureNextOfKinIfMinorShouldThrowIfMissing() {
////        Patient patient = Patient.register(
////                tenantId, hospitalPatientNumber,
////                name, minorDob, Gender.Male, phone, kin
////        );
////
////        patient.removeNextOfKin(); // should throw
////        assertThrows(
////                MinorRequiresNextOfKinException.class,
////                patient::ensureNextOfKinIfMinor
////        );
////    }
//
//    /* ================== LIFECYCLE ================== */
//
//    @Test
//    void shouldThrowOnOperationsWhenInactive() {
//        Patient patient = Patient.register(
//                tenantId, hospitalPatientNumber,
//                name, adultDob, Gender.Male, phone, null
//        );
//
//        patient.deactivate();
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> patient.updateEmail(EmailAddress.of("a@b.com")));
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> patient.updatePhoneNumber(phone));
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> patient.updateClinicalProfile(BloodGroup.A_POSITIVE, Genotype.AA));
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> patient.addAddress(Address.of("S", "C", "ST", "N", true)));
//    }
//}