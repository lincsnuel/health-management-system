package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalLicense;
import com.healthcore.workforceservice.staff.domain.model.vo.CredentialingId;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.CredentialingEntity;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.ProfessionalLicenseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CredentialingMapper {

    public Credentialing toDomain(CredentialingEntity entity) {
        Credentialing credentialing = new Credentialing(
                new CredentialingId(entity.getId()),
                new StaffId(entity.getStaffId())
        );

        entity.getLicenses().forEach(l ->
                credentialing.loadLicense(
                        ProfessionalLicense.reconstruct(
                                l.getId(),
                                l.getLicenseNumber(),
                                l.getIssuingBody(),
                                l.getExpiryDate(),
                                l.getStatus()
                        )
                )
        );

        return credentialing;
    }

    public CredentialingEntity toEntity(Credentialing domain) {
        // 1. Build the parent entity
        CredentialingEntity entity = CredentialingEntity.builder()
                .id(domain.getId().value())
                .staffId(domain.getStaffId().value())
                .build();

        // 2. Map the children
        List<ProfessionalLicenseEntity> licenseEntities = domain.getLicenses().stream()
                .map(this::toLicenseEntity)
                .collect(Collectors.toList());

        // 3. Use the helper method to sync bidirectional relationship
        entity.replaceLicenses(licenseEntities);

        return entity;
    }

    private ProfessionalLicenseEntity toLicenseEntity(ProfessionalLicense license) {
        return ProfessionalLicenseEntity.builder()
                .id(license.getId())
                .licenseNumber(license.getLicenseNumber())
                .issuingBody(license.getIssuingBody())
                .expiryDate(license.getExpiryDate())
                .status(license.getStatus())
                .build();
    }
}