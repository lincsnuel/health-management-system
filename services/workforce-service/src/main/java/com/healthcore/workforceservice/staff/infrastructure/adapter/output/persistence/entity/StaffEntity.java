package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.tenant.persistence.BaseTenantEntity;
import com.healthcore.workforceservice.staff.domain.model.enums.Gender;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffStatus;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffEntity extends BaseTenantEntity {

    @Id
    @Column(name = "staff_id", nullable = false, updatable = false)
    private UUID staffId;

    // ======================
    // VALUE OBJECTS
    // ======================
    @Embedded
    private FullNameEmbeddable fullName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    // ======================
    // DOMAIN ENUMS
    // ======================
    @Enumerated(EnumType.STRING)
    private StaffType staffType;

    @Enumerated(EnumType.STRING)
    private StaffStatus status;

    // ======================
    // REFERENCES (STORED AS IDs ONLY)
    // ======================
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "employment_id")
    private UUID employmentId;

    @Column(name = "professional_profile_id")
    private UUID professionalProfileId;

    @Column(name = "credentialing_id")
    private UUID credentialingId;

    // ======================
    // RELATIONSHIPS
    // ======================

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "role_name")
    private Set<String> roles = new HashSet<>();
}