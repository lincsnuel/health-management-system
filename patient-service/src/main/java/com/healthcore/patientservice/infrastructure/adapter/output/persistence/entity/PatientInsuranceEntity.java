package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient_insurance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = "patient")
public class PatientInsuranceEntity {

    @Id
    @Column(name = "insurance_id", nullable = false, updatable = false)
    private UUID insuranceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(nullable = false, length = 100)
    private String providerName;

    @Column(nullable = false, length = 100)
    private String policyNumber;

    @Column(nullable = false, length = 100)
    private String planType;

    @Column(nullable = false)
    private LocalDate coverageStart;

    @Column(nullable = false)
    private LocalDate coverageEnd;

    @Column(nullable = false)
    private boolean main;

    @Column(nullable = false)
    private boolean active;

    public void assignToPatient(PatientEntity patient) {
        this.patient = patient;
    }
}