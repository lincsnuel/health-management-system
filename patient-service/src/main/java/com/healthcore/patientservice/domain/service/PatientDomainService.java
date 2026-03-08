package com.healthcore.patientservice.domain.service;

import com.healthcore.patientservice.domain.model.Patient;
import com.healthcore.patientservice.domain.model.InsurancePolicy;
import com.healthcore.patientservice.domain.model.vo.Address;
import com.healthcore.patientservice.domain.exception.*;

import java.util.List;
import java.util.Optional;

/**
 * Domain service for complex operations related to Patient that
 * don't belong solely to the Patient aggregate itself.
 */
public class PatientDomainService {

    /* ================== INSURANCE OPERATIONS ================== */

    /**
     * Sets the provided insurance policy as the main policy for a patient.
     * Ensures only one policy is marked as main at a time.
     */
    public void setMainInsurance(Patient patient, InsurancePolicy policy) {
        patient.ensureActive();
        if (!patient.getInsurancePolicies().contains(policy)) {
            throw new InvalidInsurancePolicyException("Policy does not belong to patient");
        }

        // Unset any existing main policy
        patient.getInsurancePolicies()
                .forEach(InsurancePolicy::unmarkAsMain);

        // Set the selected policy as main
        policy.markAsMain();
    }

    /**
     * Returns the main insurance policy, if any.
     */
    public Optional<InsurancePolicy> getMainInsurance(Patient patient) {
        return patient.getInsurancePolicies()
                .stream()
                .filter(InsurancePolicy::isMain)
                .findFirst();
    }

    /* ================== ADDRESS OPERATIONS ================== */

    public void setPrimaryAddress(Patient patient, Address address) {
        patient.setPrimaryAddress(address);
    }

    /* ================== BUSINESS RULE CHECKS ================== */

    /**
     * Checks if a patient is eligible for a certain clinical service
     * based on age, insurance, or other domain rules.
     */
    public boolean isEligibleForService(Patient patient, int minAge, int maxAge) {
        patient.ensureActive();
        int age = patient.calculateAge();
        return age >= minAge && age <= maxAge && patient.hasActiveInsurance();
    }

//    /**
//     * Validates that a patient has a next of kin if minor.
//     * Delegates to aggregate for enforcement.
//     */
//    public void ensureMinorHasNextOfKin(Patient patient) {
//        patient.ensureNextOfKinIfMinor();
//    }

    /* ================== ADDITIONAL DOMAIN HELPERS ================== */

    /**
     * Returns all active insurance policies of a patient.
     */
    public List<InsurancePolicy> getActiveInsurances(Patient patient) {
        return patient.getInsurancePolicies()
                .stream()
                .filter(InsurancePolicy::isActive)
                .toList();
    }

    /**
     * Adds a new insurance policy to the patient.
     * Delegates validation to aggregate.
     */
    public void addInsurance(Patient patient, InsurancePolicy policy) {
        patient.addInsurance(policy);
    }

    /**
     * Adds an address to the patient.
     * Delegates validation to aggregate.
     */
    public void addAddress(Patient patient, Address address) {
        patient.addAddress(address);
    }

}