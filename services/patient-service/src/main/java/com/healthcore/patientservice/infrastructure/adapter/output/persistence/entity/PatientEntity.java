package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.tenant.persistence.BaseTenantEntity;
import com.healthcore.patientservice.domain.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(
        name = "patient",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_patient_number_per_tenant",
                        columnNames = {"tenant_id", "hospital_patient_number"}
                ),
                @UniqueConstraint(
                        name = "uk_patient_email_per_tenant",
                        columnNames = {"tenant_id", "email"}
                )
        },
        indexes = {
                @Index(name = "idx_tenant_list_page", columnList = "tenant_id, status, created_at"),
                @Index(name = "idx_tenant_contact_number", columnList = "tenant_id, contact_number"),
                @Index(name = "idx_patient_names", columnList = "firstName, lastName")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"addresses", "insurancePolicies", "responsibleParties", "documents"})
public class PatientEntity extends BaseTenantEntity {

    @Id
    @Column(name = "patient_id", nullable = false, updatable = false)
    private UUID patientId;

    @Column(nullable = false, length = 30)
    private String hospitalPatientNumber;

    /* ================== BASIC IDENTITY ================== */
    @Column(nullable = false, length = 40)
    private String firstName;

    @Column(nullable = false, length = 40)
    private String lastName;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private MaritalStatus maritalStatus;

    @Column(length = 50)
    private String nationality;

    @Column(length = 100)
    private String occupation;

    /* ================== CONTACT ================== */
    @Column(nullable = false, length = 15)
    private String contactNumber;

    /* ================== MEDICAL DESCRIPTORS ================== */
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Genotype genotype;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DisabilityStatus disabilityStatus;

    /* ================== SOCIOCULTURAL ================== */
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Religion religion;

    /* ================== IDENTITY DOCUMENT ================== */
    @Enumerated(EnumType.STRING)
    @Column(name = "national_id_type", length = 30)
    private IdentityType identityType;

    @Column(name = "national_id_number", length = 50)
    private String nationalIdNumber;

    /* ================== ADDRESSES ================== */
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<AddressEntity> addresses = new HashSet<>();

    /* ================== RESPONSIBLE PARTIES ================== */
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ResponsiblePartyEntity> responsibleParties = new HashSet<>();

    /* ================== INSURANCE ================== */
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<PatientInsuranceEntity> insurancePolicies = new HashSet<>();

    /* ================== DOCUMENTS ================== */
    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<PatientDocumentEntity> documents = new HashSet<>();

    /* ================== STATUS ================== */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PatientStatus status;
}