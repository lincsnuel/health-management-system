package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.exception.InvalidInsurancePolicyException;

import java.util.*;

class InsurancePortfolio {

    private final List<InsurancePolicy> policies = new ArrayList<>();

    void add(InsurancePolicy policy) {

        Objects.requireNonNull(policy);

        if (!policy.isActive()) {
            throw new InvalidInsurancePolicyException("Cannot add inactive insurance");
        }

        if (policy.isMain()) {
            policies.stream()
                    .filter(InsurancePolicy::isMain)
                    .forEach(InsurancePolicy::unmarkAsMain);
        }

        policies.add(policy);
    }

    void remove(InsurancePolicy policy) {

        if (!policies.remove(policy)) return;

        if (policy.isMain() && !policies.isEmpty()) {
            policies.getFirst().markAsMain();
        }
    }

    Optional<InsurancePolicy> getMainPolicy() {
        return policies.stream()
                .filter(InsurancePolicy::isMain)
                .findFirst();
    }

    List<InsurancePolicy> all() {
        return Collections.unmodifiableList(policies);
    }

    void deactivateAll() {
        policies.forEach(InsurancePolicy::deactivate);
    }

    void activateAll() {
        policies.forEach(InsurancePolicy::activate);
    }
}
