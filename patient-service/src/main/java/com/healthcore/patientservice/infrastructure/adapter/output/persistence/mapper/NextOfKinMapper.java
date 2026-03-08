package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.vo.NextOfKin;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.NextOfKinEntity;

public class NextOfKinMapper {

    public static NextOfKin toDomain(NextOfKinEntity entity) {
        if (entity == null) return null;

        PhoneNumber phoneNumber = new PhoneNumber(entity.getContactNumber());

        return NextOfKin.of(
                entity.getFullName(),
                entity.getRelationship(),
                phoneNumber,
                entity.getAddress() // just the string now
        );
    }

    public static NextOfKinEntity toEntity(NextOfKin domain) {
        if (domain == null) return null;

        return NextOfKinEntity.builder()
                .fullName(domain.fullName())
                .relationship(domain.relationship())
                .contactNumber(domain.contactNumber().value())
                .address(domain.address()) // string only
                .build();
    }
}