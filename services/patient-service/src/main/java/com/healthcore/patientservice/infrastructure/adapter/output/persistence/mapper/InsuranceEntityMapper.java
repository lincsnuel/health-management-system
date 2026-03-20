package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.patient.InsurancePolicy;
import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientInsuranceEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class InsuranceEntityMapper {

    /* ================= DOMAIN -> ENTITY ================= */

    public PatientInsuranceEntity toEntity(InsurancePolicy policy) {
        if (policy == null) return null;

        return PatientInsuranceEntity.builder()
                .insuranceId(policy.getPolicyId().value())
                .providerName(policy.getProviderName())
                .policyNumber(policy.getPolicyNumber())
                .planType(policy.getPlanType())
                .coverageStart(policy.getCoverageStart())
                .coverageEnd(policy.getCoverageEnd())
                .main(policy.isMain())
                .active(policy.isActive())
                .build();
    }

    public Set<PatientInsuranceEntity> toEntitySet(List<InsurancePolicy> policies) {
        if (policies == null) return Set.of();

        return policies.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    /* ================= ENTITY -> DOMAIN ================= */

    public InsurancePolicy toDomain(PatientInsuranceEntity entity) {
        if (entity == null) return null;

        return InsurancePolicy.reconstruct(
                InsurancePolicyId.of(entity.getInsuranceId()),
                entity.getProviderName(),
                entity.getPolicyNumber(),
                entity.getPlanType(),
                entity.getCoverageStart(),
                entity.getCoverageEnd(),
                entity.isMain(),
                entity.isActive()
        );
    }

    public List<InsurancePolicy> toDomainList(Set<PatientInsuranceEntity> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /* ================= ID CONVERSIONS ================= */

    public UUID map(InsurancePolicyId id) {
        return id != null ? id.value() : null;
    }

    public InsurancePolicyId map(UUID id) {
        return id != null ? new InsurancePolicyId(id) : null;
    }
}