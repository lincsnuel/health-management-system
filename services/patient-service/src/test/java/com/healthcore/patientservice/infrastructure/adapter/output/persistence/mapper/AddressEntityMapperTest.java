//package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;
//
//import com.healthcore.patientservice.domain.model.patient.Address;
//import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.AddressEntity;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.*;
//
//class AddressEntityMapperTest {
//
//    /* ================== ENTITY -> DOMAIN ================== */
//
//    @Test
//    void should_map_entity_to_domain() {
//        // Arrange
//        AddressEntity entity = AddressEntity.builder()
//                .street("12 Allen Avenue")
//                .city("Ikeja")
//                .state("Lagos")
//                .country("Nigeria")
//                .primaryAddress(true)
//                .build();
//
//        // Act
//        Address domain = AddressEntityMapper.toDomain(entity);
//
//        // Assert
//        assertThat(domain).isNotNull();
//        assertThat(domain.street()).isEqualTo("12 Allen Avenue");
//        assertThat(domain.city()).isEqualTo("Ikeja");
//        assertThat(domain.state()).isEqualTo("Lagos");
//        assertThat(domain.country()).isEqualTo("Nigeria");
//        assertThat(domain.isPrimary()).isTrue();
//    }
//
//    @Test
//    void should_return_null_when_entity_is_null() {
//        assertThat(AddressEntityMapper.toDomain(null)).isNull();
//    }
//
//    /* ================== DOMAIN -> ENTITY ================== */
//
//    @Test
//    void should_map_domain_to_entity() {
//        // Arrange
//        Address domain = Address.of(
//                "23 Admiralty Way",
//                "Lekki",
//                "Lagos",
//                "Nigeria",
//                false
//        );
//
//        // Act
//        AddressEntity entity = AddressEntityMapper.toEntity(domain);
//
//        // Assert
//        assertThat(entity).isNotNull();
//        assertThat(entity.getStreet()).isEqualTo("23 Admiralty Way");
//        assertThat(entity.getCity()).isEqualTo("Lekki");
//        assertThat(entity.getState()).isEqualTo("Lagos");
//        assertThat(entity.getCountry()).isEqualTo("Nigeria");
//        assertThat(entity.isPrimaryAddress()).isFalse();
//    }
//
//    @Test
//    void should_return_null_when_domain_is_null() {
//        assertThat(AddressEntityMapper.toEntity(null)).isNull();
//    }
//}