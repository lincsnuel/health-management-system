package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.patient.Address;
import com.healthcore.patientservice.domain.model.vo.AddressId;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.AddressEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AddressEntityMapper {

    /* ================= DOMAIN -> ENTITY ================= */

    public AddressEntity toEntity(Address address) {
        if (address == null) {
            return null;
        }

        return AddressEntity.builder()
                .addressId(address.getAddressId().value())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .primaryAddress(address.isPrimary())
                .build();
        // patient intentionally not set here
    }

    /* ================= ENTITY -> DOMAIN ================= */

    public Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }

        return Address.reconstruct(
                AddressId.of(entity.getAddressId()),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.isPrimaryAddress()
        );
    }

    /* ================= COLLECTION MAPPERS ================= */

    public List<Address> toDomainList(Set<AddressEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public Set<AddressEntity> toEntitySet(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return Collections.emptySet();
        }

        return addresses.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}