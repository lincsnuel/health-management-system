package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.vo.Address;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.AddressEntity;

public class AddressMapper {

    public static Address toDomain(AddressEntity entity) {
        if (entity == null) return null;

        return Address.of(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.isPrimaryAddress()
        );
    }

    public static AddressEntity toEntity(Address domain) {
        if (domain == null) return null;

        return AddressEntity.builder()
                .street(domain.street())
                .city(domain.city())
                .state(domain.state())
                .country(domain.country())
                .primaryAddress(domain.isPrimary())
                .build();
    }
}
