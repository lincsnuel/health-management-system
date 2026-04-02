package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.workforceservice.domain.model.enums.Gender;
import com.healthcore.workforceservice.domain.model.enums.StaffStatus;
import com.healthcore.workforceservice.domain.model.enums.StaffType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "staff", indexes = {
        @Index(name = "idx_staff_tenant_id", columnList = "tenantId"),
        @Index(name = "idx_staff_email", columnList = "email")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class StaffEntity extends BaseEntity {

    @Id
    @Column(name = "staff_id", nullable = false, updatable = false)
    private UUID staffId;

    @Column(nullable = false, updatable = false)
    private String tenantId;

    // --- Embedded Value Objects ---
    @Embedded
    private FullNameEmbeddable fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;

    @Embedded
    private NationalIdentityEmbeddable nationalIdentity;

    // --- Professional Context ---
    @Enumerated(EnumType.STRING)
    private StaffType staffType;

    @Enumerated(EnumType.STRING)
    private StaffStatus status;

    private String departmentId;

    private StaffRankEmbeddable staffRank;

    @Embedded
    private EmploymentDetailsEmbeddable employmentDetails;

    @Embedded
    private ProfessionalDetailsEmbeddable professionalDetails;

    // --- Entities & Collections ---

    /**
     * ProfessionalLicenses as a separate table linked to this Staff record.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private List<ProfessionalLicenseEntity> licenses = new ArrayList<>();

    /**
     * Roles are stored in a simple collection table.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "role_name")
    private Set<String> roles = new HashSet<>();
}