package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "patient_address",
        indexes = {
                @Index(name = "idx_patient_address_patient", columnList = "patient_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Id
    @Column(name = "address_id", nullable = false, updatable = false)
    private UUID addressId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(nullable = false, length = 150)
    private String street;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 80)
    private String state;

    @Column(length = 80)
    private String country;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryAddress;

    public void assignToPatient(PatientEntity patient) {
        this.patient = patient;
    }
}