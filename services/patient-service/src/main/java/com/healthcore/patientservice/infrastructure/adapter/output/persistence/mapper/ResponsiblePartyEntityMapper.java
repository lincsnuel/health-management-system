package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import com.healthcore.patientservice.domain.model.patient.Address;
import com.healthcore.patientservice.domain.model.patient.ResponsibleParty;
import com.healthcore.patientservice.domain.model.vo.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ResponsiblePartyEntityMapper {

    /* ================= DOMAIN -> ENTITY ================= */
    public ResponsiblePartyEntity toEntity(ResponsibleParty domain) {
        if (domain == null) return null;

        return ResponsiblePartyEntity.builder()
                .id(domain.getResponsiblePartyId().value())
                .firstName(domain.getName().firstName())
                .lastName(domain.getName().lastName())
                .contactNumber(domain.getPhoneNumber().value())
                .relationship(domain.getRelationship())
                .type(domain.getType())
                .address(map(domain.getAddress()))
                .build();

        // back-reference to patient is set manually after building
    }

    /* ================= ENTITY -> DOMAIN ================= */
    public ResponsibleParty toDomain(ResponsiblePartyEntity entity) {
        if (entity == null) return null;

        ResponsiblePartyId id = ResponsiblePartyId.of(entity.getId());
        PersonName name = PersonName.of(entity.getFirstName(), entity.getLastName());
        PhoneNumber phoneNumber = PhoneNumber.of(entity.getContactNumber());
        Address address = map(entity.getAddress());
        ResponsiblePartyType type = entity.getType();
        String relationship = entity.getRelationship();

        return ResponsibleParty.reconstruct(id, name, phoneNumber, relationship, type, address);
    }

    /* ================= ADDRESS MAPPING ================= */
    public ResponsiblePartyAddressEntity map(Address address) {
        if (address == null) return null;
        return ResponsiblePartyAddressEntity.builder()
                .id(address.getAddressId().value())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .build();
    }

    public Address map(ResponsiblePartyAddressEntity entity) {
        if (entity == null) return null;
        return Address.reconstruct(
                AddressId.of(entity.getId()),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                true // always primary
        );
    }

    /* ================= COLLECTION MAPPERS ================= */
    public List<ResponsibleParty> toDomainList(Set<ResponsiblePartyEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public Set<ResponsiblePartyEntity> toEntitySet(List<ResponsibleParty> parties) {
        if (parties == null) return Set.of();
        return parties.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    /* ================= BACK REFERENCE ================= */
    public void linkPatient(ResponsiblePartyEntity entity, PatientEntity patient) {
        if (entity != null && patient != null) {
            entity.assignPatient(patient);
        }
    }
}