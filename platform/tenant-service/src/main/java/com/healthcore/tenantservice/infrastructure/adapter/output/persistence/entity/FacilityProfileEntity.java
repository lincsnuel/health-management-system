package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.tenantservice.domain.model.enums.FacilityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FacilityProfileEntity {

    @Column(name = "facility_id", nullable = false)
    private UUID id;

    @Column(name = "facility_name", nullable = false)
    private String facilityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "facility_type", nullable = false)
    private FacilityType facilityType;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "established_date")
    private LocalDate establishedDate;

    @Embedded
    private AddressEntity address;

    @Embedded
    private ContactInfoEntity contactInfo;

    /* ================= HELPERS ================= */
    public void updateName(String name) {
        this.facilityName = name;
    }

    public void updateAddress(AddressEntity newAddress) {
        this.address = newAddress;
    }

    public void updateContactInfo(ContactInfoEntity newContact) {
        this.contactInfo = newContact;
    }

    public void updateRegistration(String registrationNumber, String taxId) {
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }
}