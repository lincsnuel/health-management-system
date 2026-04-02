package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.workforceservice.domain.model.enums.LicenseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "professional_licenses", indexes = {
        @Index(name = "idx_license_staff_id", columnList = "staff_id"),
        @Index(name = "idx_license_number", columnList = "licenseNumber")
})
@Getter
@Setter
public class ProfessionalLicenseEntity extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private String issuingBody;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus status;

    /**
     * Optional back-reference (not strictly needed since StaffEntity owns the relationship).
     * Keep it ONLY if you need bidirectional navigation.
     */
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "staff_id", insertable = false, updatable = false)
    // private StaffEntity staff;
}