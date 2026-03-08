//package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;
//
//import com.healthcore.patientservice.domain.model.vo.NextOfKin;
//import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
//import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.NextOfKinEntity;
//
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.*;
//
//class NextOfKinMapperTest {
//
//    /* ================== ENTITY -> DOMAIN ================== */
//
//    @Test
//    void should_map_entity_to_domain_with_address() {
//        // Arrange
//        NextOfKinEntity entity = NextOfKinEntity.builder()
//                .fullName("Jane Doe")
//                .relationship("Mother")
//                .contactNumber("08012345678")
//                .address("Street 5")
//                .build();
//
//        // Act
//        NextOfKin domain = NextOfKinMapper.toDomain(entity);
//
//        // Assert
//        assertThat(domain).isNotNull();
//        assertThat(domain.fullName()).isEqualTo("Jane Doe");
//        assertThat(domain.relationship()).isEqualTo("Mother");
//        assertThat(domain.contactNumber().value()).isEqualTo("+2348012345678");
//        assertThat(domain.address()).isEqualTo("Street 5");
//    }
//
//    @Test
//    void should_map_entity_to_domain_without_address() {
//        // Arrange
//        NextOfKinEntity entity = NextOfKinEntity.builder()
//                .fullName("John Smith")
//                .relationship("Father")
//                .contactNumber("08099999999")
//                .address(null)
//                .build();
//
//        // Act
//        NextOfKin domain = NextOfKinMapper.toDomain(entity);
//
//        // Assert
//        assertThat(domain).isNotNull();
//        assertThat(domain.fullName()).isEqualTo("John Smith");
//        assertThat(domain.relationship()).isEqualTo("Father");
//        assertThat(domain.contactNumber().value()).isEqualTo("+2348099999999");
//        assertThat(domain.address()).isNull();
//    }
//
//    @Test
//    void should_return_null_when_entity_is_null() {
//        assertThat(NextOfKinMapper.toDomain(null)).isNull();
//    }
//
//    /* ================== DOMAIN -> ENTITY ================== */
//
//    @Test
//    void should_map_domain_to_entity_with_address() {
//        // Arrange
//        NextOfKin domain = NextOfKin.of(
//                "Mary Johnson",
//                "Sister",
//                new PhoneNumber("08088888888"),
//                "Street 9"
//        );
//
//        // Act
//        NextOfKinEntity entity = NextOfKinMapper.toEntity(domain);
//
//        // Assert
//        assertThat(entity).isNotNull();
//        assertThat(entity.getFullName()).isEqualTo("Mary Johnson");
//        assertThat(entity.getRelationship()).isEqualTo("Sister");
//        assertThat(entity.getContactNumber()).isEqualTo("+2348088888888");
//        assertThat(entity.getAddress()).isEqualTo("Street 9");
//    }
//
//    @Test
//    void should_map_domain_to_entity_without_address() {
//        // Arrange
//        NextOfKin domain = NextOfKin.of(
//                "Paul Okoye",
//                "Brother",
//                new PhoneNumber("08077777777"),
//                null
//        );
//
//        // Act
//        NextOfKinEntity entity = NextOfKinMapper.toEntity(domain);
//
//        // Assert
//        assertThat(entity).isNotNull();
//        assertThat(entity.getFullName()).isEqualTo("Paul Okoye");
//        assertThat(entity.getRelationship()).isEqualTo("Brother");
//        assertThat(entity.getContactNumber()).isEqualTo("+2348077777777");
//        assertThat(entity.getAddress()).isNull();
//    }
//
//    @Test
//    void should_return_null_when_domain_is_null() {
//        assertThat(NextOfKinMapper.toEntity(null)).isNull();
//    }
//}