package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credentialing", indexes = {
        @Index(name = "idx_credentialing_staff_id", columnList = "staff_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CredentialingEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "staff_id", nullable = false, unique = true)
    private UUID staffId;

    @OneToMany(
            mappedBy = "credentialing",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ProfessionalLicenseEntity> licenses = new ArrayList<>();
}