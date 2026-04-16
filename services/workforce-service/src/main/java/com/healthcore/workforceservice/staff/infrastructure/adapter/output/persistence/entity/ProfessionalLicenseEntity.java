package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.workforceservice.staff.domain.model.enums.LicenseStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "professional_licenses", indexes = {
        @Index(name = "idx_license_credentialing_id", columnList = "credentialing_id"),
        @Index(name = "idx_license_number", columnList = "licenseNumber")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfessionalLicenseEntity extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String issuingBody;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus status;

    // FIXED OWNERSHIP
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credentialing_id", nullable = false)
    private CredentialingEntity credentialing;
}