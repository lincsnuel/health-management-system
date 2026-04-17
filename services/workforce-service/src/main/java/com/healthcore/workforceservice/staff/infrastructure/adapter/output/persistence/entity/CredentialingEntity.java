package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credentialing", indexes = {
        @Index(name = "idx_credentialing_staff_id", columnList = "staff_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CredentialingEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "staff_id", nullable = false, unique = true)
    private UUID staffId;

    @Builder.Default
    @OneToMany(
            mappedBy = "credentialing",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<ProfessionalLicenseEntity> licenses = new ArrayList<>();

    public void replaceLicenses(List<ProfessionalLicenseEntity> newLicenses) {
        // 1. Clear the list (JPA marks these for deletion)
        this.licenses.clear();

        // 2. Add the new ones
        if (newLicenses != null) {
            newLicenses.forEach(license -> {
                this.licenses.add(license);
                license.setCredentialing(this); // Manual sync without a helper method
            });
        }
    }
}