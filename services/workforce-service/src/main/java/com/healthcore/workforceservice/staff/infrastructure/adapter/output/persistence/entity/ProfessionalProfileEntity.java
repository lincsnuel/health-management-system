package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "professional_profile", indexes = {
        @Index(name = "idx_profile_staff_id", columnList = "staff_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfessionalProfileEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "staff_id", nullable = false, unique = true)
    private UUID staffId;

    @Column(nullable = false)
    private String primarySpecialization;

    @Column(nullable = false)
    private String academicTitle;

    @Column(nullable = false)
    private boolean consultant;
}