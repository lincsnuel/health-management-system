package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.InvalidFacilityException;
import com.healthcore.tenantservice.domain.model.enums.FacilityType;
import com.healthcore.tenantservice.domain.model.vo.Address;
import com.healthcore.tenantservice.domain.model.vo.ContactInfo;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
public class FacilityProfile {

    private final UUID id;

    private final String facilityName;

    private final FacilityType facilityType;

    private final String registrationNumber;

    private final String taxId;

    private final LocalDate establishedDate;

    private final Address address;

    private final ContactInfo contactInfo;


    private FacilityProfile(
            UUID id,
            String facilityName,
            FacilityType facilityType,
            String registrationNumber,
            String taxId,
            LocalDate establishedDate,
            Address address,
            ContactInfo contactInfo
    ) {
        this.id = Objects.requireNonNull(id);
        this.facilityName = normalizeName(facilityName);
        this.facilityType = Objects.requireNonNull(facilityType);
        this.registrationNumber = normalizeOptional(registrationNumber);
        this.taxId = normalizeOptional(taxId);
        this.establishedDate = validateDate(establishedDate);
        this.address = Objects.requireNonNull(address);
        this.contactInfo = Objects.requireNonNull(contactInfo);
    }


    /* ================= FACTORY ================= */

    public static FacilityProfile create(
            String facilityName,
            FacilityType facilityType,
            String registrationNumber,
            String taxId,
            LocalDate establishedDate,
            Address address,
            ContactInfo contactInfo
    ) {
        return new FacilityProfile(
                UUID.randomUUID(),
                facilityName,
                facilityType,
                registrationNumber,
                taxId,
                establishedDate,
                address,
                contactInfo
        );
    }


    public static FacilityProfile reconstruct(
            UUID id,
            String facilityName,
            FacilityType facilityType,
            String registrationNumber,
            String taxId,
            LocalDate establishedDate,
            Address address,
            ContactInfo contactInfo
    ) {
        return new FacilityProfile(
                id,
                facilityName,
                facilityType,
                registrationNumber,
                taxId,
                establishedDate,
                address,
                contactInfo
        );
    }


    /* ================= STATE TRANSITIONS ================= */

    public FacilityProfile updateName(String newName) {
        return new FacilityProfile(
                this.id,
                newName,
                this.facilityType,
                this.registrationNumber,
                this.taxId,
                this.establishedDate,
                this.address,
                this.contactInfo
        );
    }

    public FacilityProfile updateContactInfo(ContactInfo newContact) {
        return new FacilityProfile(
                this.id,
                this.facilityName,
                this.facilityType,
                this.registrationNumber,
                this.taxId,
                this.establishedDate,
                this.address,
                newContact
        );
    }

    public FacilityProfile updateAddress(Address newAddress) {
        return new FacilityProfile(
                this.id,
                this.facilityName,
                this.facilityType,
                this.registrationNumber,
                this.taxId,
                this.establishedDate,
                newAddress,
                this.contactInfo
        );
    }

    public FacilityProfile updateRegistration(
            String registrationNumber,
            String taxId
    ) {
        return new FacilityProfile(
                this.id,
                this.facilityName,
                this.facilityType,
                registrationNumber,
                taxId,
                this.establishedDate,
                this.address,
                this.contactInfo
        );
    }


    /* ================= BUSINESS RULES ================= */

    public boolean isLegallyIdentifiable() {
        return registrationNumber != null && !registrationNumber.isBlank();
    }


    /* ================= VALIDATION ================= */

    private String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidFacilityException("Facility name cannot be blank");
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        return value == null ? null : value.trim();
    }

    private LocalDate validateDate(LocalDate date) {
        if (date == null) return null;

        if (date.isAfter(LocalDate.now())) {
            throw new InvalidFacilityException(
                    "Established date cannot be in the future"
            );
        }

        return date;
    }
}