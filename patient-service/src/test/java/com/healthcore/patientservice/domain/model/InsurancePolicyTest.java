//package com.healthcore.patientservice.domain.model;
//
//import com.healthcore.patientservice.domain.exception.InvalidInsurancePolicyException;
//import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class InsurancePolicyTest {
//
//    private InsurancePolicy createPolicy(
//            LocalDate start, LocalDate end, boolean active, boolean main
//    ) {
//        return new InsurancePolicy(
//                null,
//                "Provider 1 ",
//                "pol123 ",
//                "Basic ",
//                start,
//                end,
//                main,
//                active
//        );
//    }
//
//    // ======= Constructor Validation =======
//    @Test
//    void shouldThrowIfCoverageEndBeforeStart() {
//        LocalDate start = LocalDate.now();
//        LocalDate end = start.minusDays(1);
//
//        assertThrows(InvalidInsurancePolicyException.class, () ->
//                createPolicy(start, end, true, false)
//        );
//    }
//
//    // ======= Normalization =======
//    @Test
//    void shouldTrimAndUppercaseFields() {
//        LocalDate today = LocalDate.now();
//        InsurancePolicy policy = createPolicy(today, today.plusDays(10), true, false);
//
//        assertEquals("Provider 1", policy.getProviderName());
//        assertEquals("POL123", policy.getPolicyNumber());
//        assertEquals("Basic", policy.getPlanType());
//    }
//
//    // ======= Active/Inactive Behavior =======
//    @Test
//    void shouldBeActiveWhenWithinCoverageAndActive() {
//        LocalDate start = LocalDate.now().minusDays(1);
//        LocalDate end = LocalDate.now().plusDays(1);
//
//        InsurancePolicy policy = createPolicy(start, end, true, false);
//        assertTrue(policy.isActive());
//    }
//
//    @Test
//    void shouldNotBeActiveIfInactiveFlag() {
//        LocalDate start = LocalDate.now().minusDays(1);
//        LocalDate end = LocalDate.now().plusDays(1);
//
//        InsurancePolicy policy = createPolicy(start, end, false, false);
//        assertFalse(policy.isActive());
//    }
//
//    @Test
//    void shouldNotBeActiveIfBeforeCoverage() {
//        LocalDate start = LocalDate.now().plusDays(1);
//        LocalDate end = LocalDate.now().plusDays(5);
//
//        InsurancePolicy policy = createPolicy(start, end, true, false);
//        assertFalse(policy.isActive());
//    }
//
//    @Test
//    void shouldNotBeActiveIfAfterCoverage() {
//        LocalDate start = LocalDate.now().minusDays(5);
//        LocalDate end = LocalDate.now().minusDays(1);
//
//        InsurancePolicy policy = createPolicy(start, end, true, false);
//        assertFalse(policy.isActive());
//    }
//
//    // ======= Expired Behavior =======
//    @Test
//    void shouldBeExpiredIfAfterCoverageEnd() {
//        LocalDate start = LocalDate.now().minusDays(10);
//        LocalDate end = LocalDate.now().minusDays(1);
//
//        InsurancePolicy policy = createPolicy(start, end, true, false);
//        assertTrue(policy.isExpired());
//    }
//
//    @Test
//    void shouldNotBeExpiredIfBeforeOrOnCoverageEnd() {
//        LocalDate start = LocalDate.now().minusDays(5);
//        LocalDate end = LocalDate.now().plusDays(1);
//
//        InsurancePolicy policy = createPolicy(start, end, true, false);
//        assertFalse(policy.isExpired());
//    }
//
//    // ======= Main flag behavior =======
//    @Test
//    void shouldMarkAsMain() {
//        InsurancePolicy policy = createPolicy(LocalDate.now(), LocalDate.now().plusDays(1), true, false);
//        policy.markAsMain();
//        assertTrue(policy.isMain());
//    }
//
//    @Test
//    void shouldUnmarkAsMain() {
//        InsurancePolicy policy = createPolicy(LocalDate.now(), LocalDate.now().plusDays(1), true, true);
//        policy.unmarkAsMain();
//        assertFalse(policy.isMain());
//    }
//
//    // ======= Deactivate =======
//    @Test
//    void shouldDeactivatePolicy() {
//        InsurancePolicy policy = createPolicy(LocalDate.now(), LocalDate.now().plusDays(1), true, false);
//        policy.deactivate();
//        assertFalse(policy.isActive());
//    }
//}