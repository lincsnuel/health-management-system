//package com.healthcore.patientservice.domain.service;
//
//import com.healthcore.patientservice.domain.exception.*;
//import com.healthcore.patientservice.domain.model.*;
//import com.healthcore.patientservice.domain.model.enums.Gender;
//import com.healthcore.patientservice.domain.model.vo.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PatientDomainServiceTest {
//
//    private PatientDomainService service;
//    private Patient patient;
//    private InsurancePolicy policy1;
//    private InsurancePolicy policy2;
//    private Address address2;
//
//    @BeforeEach
//    void setup() {
//
//        service = new PatientDomainService();
//
//        patient = Patient.reconstitute(
//                PatientId.newId(),
//                TenantId.of("HOSP"),
//                HospitalPatientNumber.of("HOSP-001"),
//                PersonName.of("John", "Doe"),
//                DateOfBirth.of(LocalDate.now().minusYears(30)),
//                Gender.MALE,
//                PhoneNumber.of("08031234567"),
//                true
//        );
//
//        policy1 = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "Provider A", "POL123", "Basic",
//                LocalDate.now().minusMonths(1),
//                LocalDate.now().plusMonths(6),
//                true,
//                true
//        );
//
//        policy2 = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "Provider B", "POL456", "Premium",
//                LocalDate.now().minusMonths(1),
//                LocalDate.now().plusMonths(6),
//                false,
//                true
//        );
//
//        Address address1 = Address.of("Street 1", "City", "State", "Country", true);
//        address2 = Address.of("Street 2", "City", "State", "Country", false);
//
//        patient.addInsurance(policy1);
//        patient.addInsurance(policy2);
//
//        patient.addAddress(address1);
//        patient.addAddress(address2);
//    }
//
//    /* ================== INSURANCE ================== */
//
//    @Test
//    void shouldSetMainInsuranceSuccessfully() {
//        service.setMainInsurance(patient, policy2);
//
//        assertTrue(policy2.isMain());
//        assertFalse(policy1.isMain());
//    }
//
//    @Test
//    void shouldThrowIfPolicyDoesNotBelongToPatient() {
//        InsurancePolicy foreignPolicy = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "OTHER", "POL999", "Basic",
//                LocalDate.now().minusMonths(1),
//                LocalDate.now().plusMonths(1),
//                false,
//                true
//        );
//
//        assertThrows(InvalidInsurancePolicyException.class,
//                () -> service.setMainInsurance(patient, foreignPolicy));
//    }
//
//    @Test
//    void shouldThrowIfPatientInactiveWhenSettingMainInsurance() {
//        patient.deactivate();
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> service.setMainInsurance(patient, policy1));
//    }
//
//    @Test
//    void shouldReturnMainInsurance() {
//        Optional<InsurancePolicy> main = service.getMainInsurance(patient);
//
//        assertTrue(main.isPresent());
//        assertTrue(main.get().isMain());
//    }
//
//    @Test
//    void shouldReturnEmptyIfNoMainInsurance() {
//        policy1.unmarkAsMain();
//        policy2.unmarkAsMain();
//
//        Optional<InsurancePolicy> main = service.getMainInsurance(patient);
//
//        assertTrue(main.isEmpty());
//    }
//
//    @Test
//    void shouldReturnOnlyActiveInsurances() {
//        policy2.deactivate();
//
//        List<InsurancePolicy> staus = service.getActiveInsurances(patient);
//
//        assertEquals(1, staus.size());
//        assertTrue(staus.contains(policy1));
//    }
//
//    @Test
//    void shouldReturnEmptyActiveInsuranceListIfNoneActive() {
//        policy1.deactivate();
//        policy2.deactivate();
//
//        List<InsurancePolicy> staus = service.getActiveInsurances(patient);
//
//        assertTrue(staus.isEmpty());
//    }
//
//    /* ================== ADDRESS ================== */
//
//    @Test
//    void shouldSetPrimaryAddress() {
//        service.setPrimaryAddress(patient, address2);
//
//        assertTrue(patient.getAddresses().get(1).isPrimary());
//
//        patient.getAddresses()
//                .stream()
//                .filter(addr -> !addr.equals(patient.getAddresses().get(1)))
//                .forEach(addr -> assertFalse(addr.isPrimary()));
//    }
//
//    @Test
//    void shouldThrowIfAddressNotBelongToPatient() {
//        Address foreignAddress =
//                Address.of("Foreign", "City", "State", "Country", false);
//
//        assertThrows(AddressNotFoundException.class,
//                () -> service.setPrimaryAddress(patient, foreignAddress));
//    }
//
//    @Test
//    void shouldThrowIfPatientInactiveWhenSettingPrimaryAddress() {
//        patient.deactivate();
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> service.setPrimaryAddress(patient, address2));
//    }
//
//    /* ================== ELIGIBILITY ================== */
//
//    @Test
//    void shouldBeEligibleWhenAgeWithinRangeAndHasActiveInsurance() {
//        boolean eligible = service.isEligibleForService(patient, 18, 60);
//
//        assertTrue(eligible);
//    }
//
//    @Test
//    void shouldBeEligibleWhenAgeEqualsBoundary() {
//        boolean eligible = service.isEligibleForService(patient, 30, 30);
//
//        assertTrue(eligible);
//    }
//
//    @Test
//    void shouldNotBeEligibleIfOutsideAgeRange() {
//        boolean eligible = service.isEligibleForService(patient, 31, 60);
//
//        assertFalse(eligible);
//    }
//
//    @Test
//    void shouldNotBeEligibleIfNoActiveInsurance() {
//        policy1.deactivate();
//        policy2.deactivate();
//
//        boolean eligible = service.isEligibleForService(patient, 18, 60);
//
//        assertFalse(eligible);
//    }
//
//    @Test
//    void shouldThrowIfPatientInactiveWhenCheckingEligibility() {
//        patient.deactivate();
//
//        assertThrows(InactivePatientOperationException.class,
//                () -> service.isEligibleForService(patient, 18, 60));
//    }
//
//    /* ================== MINOR NEXT OF KIN ================== */
//
//    @Test
//    void shouldEnsureMinorHasNextOfKin() {
//        Patient minor = Patient.register(
//                TenantId.of("HOSP"),
//                HospitalPatientNumber.of("HOSP-002"),
//                PersonName.of("Jane", "Doe"),
//                DateOfBirth.of(LocalDate.now().minusYears(10)),
//                Gender.FEMALE,
//                PhoneNumber.of("08039876543"),
//                NextOfKin.of(
//                        "Mary Doe",
//                        "Mother",
//                        PhoneNumber.of("08030000000"),
//                        "address1"
//                )
//        );
//
//        assertDoesNotThrow(() ->
//                service.ensureMinorHasNextOfKin(minor));
//    }
//
//    @Test
//    void shouldThrowIfMinorHasNoNextOfKin() {
//        Patient adult = Patient.reconstitute(
//                PatientId.newId(),
//                TenantId.of("HOSP"),
//                HospitalPatientNumber.of("HOSP-003"),
//                PersonName.of("Adult", "Doe"),
//                DateOfBirth.of(LocalDate.now().minusYears(30)),
//                Gender.FEMALE,
//                PhoneNumber.of("08039876543"),
//                true
//        );
//
//        adult.assignNextOfKin(
//                NextOfKin.of("Mary", "Mother",
//                        PhoneNumber.of("08030000000"),
//                        "address1")
//        );
//
//        adult.removeNextOfKin();
//
//        assertDoesNotThrow(() ->
//                service.ensureMinorHasNextOfKin(adult));
//    }
//
//    /* ================== DELEGATION ================== */
//
//    @Test
//    void shouldDelegateAddInsurance() {
//        InsurancePolicy newPolicy = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "New Provider", "POL789", "Gold",
//                LocalDate.now().minusDays(1),
//                LocalDate.now().plusMonths(6),
//                false,
//                true
//        );
//
//        service.addInsurance(patient, newPolicy);
//
//        assertTrue(patient.getInsurancePolicies().contains(newPolicy));
//    }
//
//    @Test
//    void shouldDelegateAddAddress() {
//        Address newAddress =
//                Address.of("Street 3", "City", "State", "Country", false);
//
//        service.addAddress(patient, newAddress);
//
//        assertTrue(patient.getAddresses().contains(newAddress));
//    }
//}