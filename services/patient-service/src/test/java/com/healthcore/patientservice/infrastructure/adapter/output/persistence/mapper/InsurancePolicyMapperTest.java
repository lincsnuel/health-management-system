//package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;
//
//import com.healthcore.patientservice.domain.model.patient.InsurancePolicy;
//import com.healthcore.patientservice.domain.model.vo.InsurancePolicyId;
//import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientInsuranceEntity;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//
//class InsurancePolicyMapperTest {
//
//    /* ================== ENTITY -> DOMAIN ================== */
//
//    @Test
//    void should_map_entity_to_domain_with_all_fields() {
//        LocalDate today = LocalDate.now();
//        // Arrange
//        PatientInsuranceEntity entity = PatientInsuranceEntity.builder()
//                .insuranceId(UUID.randomUUID())
//                .providerName("AXA Mansard")
//                .policyNumber("POL-12345")
//                .planType("Premium")
//                .coverageStart(today.minusDays(1))
//                .coverageEnd(today.plusDays(1))
//                .main(true)
//                .staus(true)
//                .build();
//
//        // Act
//        InsurancePolicy domain = InsurancePolicyMapper.toDomain(entity);
//
//        // Assert
//        assertThat(domain).isNotNull();
//        assertThat(domain.getPolicyId()).isNotNull();
//        assertThat(domain.getPolicyId().value()).isNotNull(); // now UUID
//        assertThat(domain.getProviderName()).isEqualTo("AXA Mansard");
//        assertThat(domain.getPolicyNumber()).isEqualTo("POL-12345");
//        assertThat(domain.getPlanType()).isEqualTo("Premium");
//        assertThat(domain.getCoverageStart()).isEqualTo(today.minusDays(1));
//        assertThat(domain.getCoverageEnd()).isEqualTo(today.plusDays(1));
//        assertThat(domain.isMain()).isTrue();
//        assertThat(domain.isActive()).isTrue();
//    }
//
//    @Test
//    void should_map_entity_to_domain_with_null_id() {
//        // Arrange
//        PatientInsuranceEntity entity = PatientInsuranceEntity.builder()
//                .insuranceId(null)
//                .providerName("Hygeia")
//                .policyNumber("POL-999")
//                .planType("Basic")
//                .coverageStart(LocalDate.of(2023, 1, 1))
//                .coverageEnd(LocalDate.of(2024, 1, 1))
//                .main(false)
//                .staus(false)
//                .build();
//
//        // Act
//        InsurancePolicy domain = InsurancePolicyMapper.toDomain(entity);
//
//        // Assert
//        assertThat(domain).isNotNull();
//        assertThat(domain.getPolicyId()).isNull();
//        assertThat(domain.isMain()).isFalse();
//        assertThat(domain.isActive()).isFalse();
//    }
//
//    @Test
//    void should_return_null_when_entity_is_null() {
//        assertThat(InsurancePolicyMapper.toDomain(null)).isNull();
//    }
//
//    /* ================== DOMAIN -> ENTITY ================== */
//
//    @Test
//    void should_map_domain_to_entity_with_all_fields() {
//        // Arrange
//        InsurancePolicy domain = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "Leadway Health",
//                "POL-2020",
//                "Gold",
//                LocalDate.of(2022, 5, 1),
//                LocalDate.of(2023, 5, 1),
//                true,
//                false
//        );
//
//        // Act
//        PatientInsuranceEntity entity = InsurancePolicyMapper.toEntity(domain);
//
//        // Assert
//        assertThat(entity).isNotNull();
//        assertThat(entity.getInsuranceId()).isNotNull(); // UUID-based value
//        assertThat(entity.getProviderName()).isEqualTo("Leadway Health");
//        assertThat(entity.getPolicyNumber()).isEqualTo("POL-2020");
//        assertThat(entity.getPlanType()).isEqualTo("Gold");
//        assertThat(entity.getCoverageStart()).isEqualTo(LocalDate.of(2022, 5, 1));
//        assertThat(entity.getCoverageEnd()).isEqualTo(LocalDate.of(2023, 5, 1));
//        assertThat(entity.isMain()).isTrue();
//        assertThat(entity.isActive()).isFalse();
//    }
//
//    @Test
//    void should_map_domain_to_entity_with_null_id() {
//        // Arrange
//        InsurancePolicy domain = new InsurancePolicy(
//                null,
//                "Reliance HMO",
//                "POL-777",
//                "Silver",
//                LocalDate.of(2023, 6, 1),
//                LocalDate.of(2024, 6, 1),
//                false,
//                true
//        );
//
//        // Act
//        PatientInsuranceEntity entity = InsurancePolicyMapper.toEntity(domain);
//
//        // Assert
//        assertThat(entity.getInsuranceId()).isNull();
//    }
//
//    @Test
//    void should_return_null_when_domain_is_null() {
//        assertThat(InsurancePolicyMapper.toEntity(null)).isNull();
//    }
//}