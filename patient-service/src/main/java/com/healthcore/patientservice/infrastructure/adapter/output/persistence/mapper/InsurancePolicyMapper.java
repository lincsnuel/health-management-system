package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.InsurancePolicy;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientInsuranceEntity;

public class InsurancePolicyMapper {

    public static InsurancePolicy toDomain(PatientInsuranceEntity entity) {
        if (entity == null) return null;

        return new InsurancePolicy(
                entity.getProviderName(),
                entity.getPolicyNumber(),
                entity.getPlanType(),
                entity.getCoverageStart(),
                entity.getCoverageEnd(),
                entity.isMain(),
                entity.isActive()
        );
    }

    public static PatientInsuranceEntity toEntity(InsurancePolicy domain) {
        if (domain == null) return null;

        return PatientInsuranceEntity.builder()
                .insuranceId(domain.getPolicyId() != null ? domain.getPolicyId().value() : null)
                .providerName(domain.getProviderName())
                .policyNumber(domain.getPolicyNumber())
                .planType(domain.getPlanType())
                .coverageStart(domain.getCoverageStart())
                .coverageEnd(domain.getCoverageEnd())
                .main(domain.isMain())
                .active(domain.isActive())
                .build();
    }
}