//package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;
//
//import com.healthcore.patientservice.domain.model.*;
//import com.healthcore.patientservice.domain.model.enums.*;
//import com.healthcore.patientservice.domain.model.vo.*;
//import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.*;
//
//class PatientEntityMapperTest {
//
//    /* ================== ENTITY -> DOMAIN ================== */
//
//    @Test
//    void should_map_entity_to_domain_with_all_fields() {
//        UUID patientUuid = UUID.randomUUID();
//
//        PatientEntity entity = PatientEntity.builder()
//                .patientId(patientUuid)
//                .tenantId("HOSP-000001")
//                .hospitalPatientNumber("PAT-001")
//                .firstName("Emmanuel")
//                .lastName("Achigbo")
//                .dateOfBirth(LocalDate.of(1998, 5, 20))
//                .gender(Gender.MALE)
//                .contactNumber("08012345678")
//                .email("emmanuel@test.com")
//                .bloodGroup(BloodGroup.B_POSITIVE)
//                .genotype(Genotype.AS)
//                .identityType(IdentityType.NIN)
//                .nationalIdNumber("12345678901")
//                .active(true)
//                .build();
//
//        AddressEntity addressEntity = AddressEntity.builder()
//                .addressId(UUID.randomUUID())
//                .street("Street 1")
//                .city("Lagos")
//                .state("Lagos")
//                .country("Nigeria")
//                .primaryAddress(true)
//                .build();
//
//        entity.addAddress(addressEntity);
//
//        Patient domain = PatientEntityMapper.toDomain(entity);
//
//        assertThat(domain).isNotNull();
//        assertThat(domain.getId().value()).isEqualTo(patientUuid);
//        assertThat(domain.getTenantId().value()).isEqualTo("HOSP-000001");
//        assertThat(domain.getHospitalPatientNumber().value()).isEqualTo("PAT-001");
//        assertThat(domain.getName().firstName()).isEqualTo("Emmanuel");
//        assertThat(domain.getName().lastName()).isEqualTo("Achigbo");
//        assertThat(domain.getGender()).isEqualTo(Gender.MALE);
//        assertThat(domain.getEmail().value()).isEqualTo("emmanuel@test.com");
//        assertThat(domain.getBloodGroup()).isEqualTo(BloodGroup.B_POSITIVE);
//        assertThat(domain.getGenotype()).isEqualTo(Genotype.AS);
//        assertThat(domain.getNationalIdentity()).isNotNull();
//        assertThat(domain.getAddresses()).hasSize(1);
//        assertThat(domain.isActive()).isTrue();
//    }
//
//    @Test
//    void should_deactivate_domain_if_entity_is_inactive() {
//        PatientEntity entity = PatientEntity.builder()
//                .patientId(UUID.randomUUID())
//                .tenantId("HOSP-000002")
//                .hospitalPatientNumber("PT-002")
//                .firstName("John")
//                .lastName("Doe")
//                .dateOfBirth(LocalDate.of(1990, 1, 1))
//                .gender(Gender.MALE)
//                .contactNumber("08000000000")
//                .active(false)
//                .build();
//
//        Patient domain = PatientEntityMapper.toDomain(entity);
//
//        assertThat(domain.isActive()).isFalse();
//    }
//
//    @Test
//    void should_return_null_when_entity_is_null() {
//        assertThat(PatientEntityMapper.toDomain(null)).isNull();
//    }
//
//    /* ================== DOMAIN -> ENTITY ================== */
//
//    @Test
//    void should_map_domain_to_entity_with_relationships() {
//        UUID patientUuid = UUID.randomUUID();
//
//        Patient domain = Patient.reconstitute(
//                PatientId.of(patientUuid),
//                new TenantId("HOSP-000003"),
//                new HospitalPatientNumber("HSP-010"),
//                new PersonName("Ada", "Okafor"),
//                new DateOfBirth(LocalDate.of(1995, 7, 15)),
//                Gender.FEMALE,
//                new PhoneNumber("08099999999"),
//                true
//        );
//
//        domain.updateEmail(new EmailAddress("ada@test.com"));
//        domain.updateClinicalProfile(BloodGroup.A_POSITIVE, Genotype.AS);
//
//        Address address = Address.of(
//                "Street 2",
//                "Abuja",
//                "FCT",
//                "Nigeria",
//                true
//        );
//
//        domain.addAddress(address);
//
//        PatientEntity entity = PatientEntityMapper.toEntity(domain);
//
//        assertThat(entity).isNotNull();
//        assertThat(entity.getPatientId()).isEqualTo(patientUuid);
//        assertThat(entity.getTenantId()).isEqualTo("HOSP-000003");
//        assertThat(entity.getFirstName()).isEqualTo("Ada");
//        assertThat(entity.getLastName()).isEqualTo("Okafor");
//        assertThat(entity.getEmail()).isEqualTo("ada@test.com");
//        assertThat(entity.getBloodGroup()).isEqualTo(BloodGroup.A_POSITIVE);
//        assertThat(entity.getGenotype()).isEqualTo(Genotype.AS);
//        assertThat(entity.getAddresses()).hasSize(1);
//
//        AddressEntity mappedAddress = entity.getAddresses()
//                .iterator()
//                .next();
//        assertThat(mappedAddress.getPatient()).isEqualTo(entity); // relationship integrity
//    }
//
//    @Test
//    void should_return_null_when_domain_is_null() {
//        assertThat(PatientEntityMapper.toEntity(null)).isNull();
//    }
//}