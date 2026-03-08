package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import com.healthcore.patientservice.domain.model.vo.Address;
import com.healthcore.patientservice.domain.model.vo.PersonName;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import com.healthcore.patientservice.domain.model.vo.ResponsibleParty;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.ResponsiblePartyAddressEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.ResponsiblePartyEntity;

public class ResponsiblePartyMapper {

    /* ================== ENTITY -> DOMAIN ================== */
    public static ResponsibleParty toDomain(ResponsiblePartyEntity entity) {

        if (entity == null) return null;

        return ResponsibleParty.of(
                new PersonName(entity.getFirstName(), entity.getLastName()),
                new PhoneNumber(entity.getContactNumber()),
                entity.getRelationship(),
                entity.getType(),
                mapAddressEntityToDomain(entity.getAddress())
        );
    }

    /* ================== DOMAIN -> ENTITY ================== */
    public static ResponsiblePartyEntity toEntity(ResponsibleParty domain) {

        if (domain == null) return null;

        ResponsiblePartyEntity entity =
                ResponsiblePartyEntity.builder()
                        .firstName(domain.firstName())
                        .lastName(domain.lastName())
                        .contactNumber(domain.phoneNumber())
                        .relationship(domain.relationship())
                        .type(domain.type() != null
                                ? domain.type()
                                : ResponsiblePartyType.NEXT_OF_KIN)
                        .build();

        ResponsiblePartyAddressEntity addressEntity = mapAddressToEntity(domain.address());

        if (addressEntity != null) {
            entity.addAddress(addressEntity);
        }

        return entity;
    }

    /* ================== PRIVATE HELPERS ================== */

    private static Address mapAddressEntityToDomain(ResponsiblePartyAddressEntity entity) {

        if (entity == null) return null;

        return new Address(
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                true
        );
    }

    private static ResponsiblePartyAddressEntity mapAddressToEntity(Address address) {

        if (address == null) return null;

        return ResponsiblePartyAddressEntity.builder()
                .street(address.street())
                .city(address.city())
                .state(address.state())
                .country(address.country())
                .build();
    }
}