package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.domain.model.staff.ProfessionalLicense;
import com.healthcore.workforceservice.domain.model.staff.Staff;
import com.healthcore.workforceservice.domain.model.vo.*;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Staff Domain Aggregate and Staff JPA Entity.
 * <p>
 * IMPORTANT:
 * - No domain behavior methods must be called during reconstruction.
 * - No domain events must be triggered here.
 */
@Component
public class StaffPersistenceMapper {

    /**
     * Maps Domain Aggregate → JPA Entity
     */
    public StaffEntity toEntity(Staff staff) {
        if (staff == null) return null;

        return StaffEntity.builder()
                .staffId(staff.getStaffId().value())
                .tenantId(staff.getTenantId())
                .fullName(FullNameEmbeddable.fromDomain(staff.getFullName()))
                .email(staff.getEmail().value())
                .phoneNumber(staff.getPhoneNumber() != null ? staff.getPhoneNumber().value() : null)
                .gender(staff.getGender())
                .dateOfBirth(staff.getDateOfBirth())
                .nationalIdentity(NationalIdentityEmbeddable.fromDomain(staff.getNationalIdentity()))
                .staffType(staff.getStaffType())
                .status(staff.getStatus())
                .departmentId(staff.getDepartmentId() != null ? staff.getDepartmentId().value() : null)
                .staffRank(staff.getStaffRank()
                        .map(StaffRankEmbeddable::fromDomain)
                        .orElse(null))
                .employmentDetails(
                        staff.getEmploymentDetails() != null
                                ? EmploymentDetailsEmbeddable.fromDomain(staff.getEmploymentDetails())
                                : null
                )
                .professionalDetails(
                        staff.getProfessionalDetails() != null
                                ? ProfessionalDetailsEmbeddable.fromDomain(staff.getProfessionalDetails())
                                : null
                )
                .licenses(
                        staff.getLicenses().stream()
                                .map(this::toLicenseEntity)
                                .collect(Collectors.toList())
                )
                .roles(
                        staff.getRoles().stream()
                                .map(Role::value)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    /**
     * Maps JPA Entity → Domain Aggregate
     * <p>
     * Uses reconstruct() ONLY.
     * No behavioral methods should be invoked.
     */
    public Staff toDomain(StaffEntity entity) {
        if (entity == null) return null;

        return Staff.reconstruct(
                new StaffId(entity.getStaffId()),
                entity.getTenantId(),
                entity.getFullName().toDomain(),
                new EmailAddress(entity.getEmail()),
                entity.getPhoneNumber() != null ? new PhoneNumber(entity.getPhoneNumber()) : null,
                entity.getGender(),
                entity.getDateOfBirth(),
                entity.getStaffType(),
                entity.getNationalIdentity().toDomain(),
                entity.getStatus(),
                entity.getStaffRank() != null ? entity.getStaffRank().toDomain() : null,
                entity.getDepartmentId() != null ? new DepartmentId(entity.getDepartmentId()) : null,
                entity.getEmploymentDetails() != null ? entity.getEmploymentDetails().toDomain() : null,
                entity.getProfessionalDetails() != null ? entity.getProfessionalDetails().toDomain() : null,
                mapLicenses(entity),
                mapRoles(entity)
        );
    }

    // ======================
    // PRIVATE HELPERS
    // ======================

    private List<ProfessionalLicense> mapLicenses(StaffEntity entity) {
        if (entity.getLicenses() == null || entity.getLicenses().isEmpty()) {
            return Collections.emptyList();
        }

        return entity.getLicenses()
                .stream()
                .map(this::toLicenseDomain)
                .collect(Collectors.toList());
    }

    private Set<Role> mapRoles(StaffEntity entity) {
        if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
            return Collections.emptySet();
        }

        return entity.getRoles()
                .stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    private ProfessionalLicenseEntity toLicenseEntity(ProfessionalLicense license) {
        ProfessionalLicenseEntity entity = new ProfessionalLicenseEntity();
        entity.setId(license.getId());
        entity.setLicenseNumber(license.getLicenseNumber());
        entity.setIssuingBody(license.getIssuingBody());
        entity.setExpiryDate(license.getExpiryDate());
        entity.setStatus(license.getStatus());
        return entity;
    }

    private ProfessionalLicense toLicenseDomain(ProfessionalLicenseEntity entity) {
        return ProfessionalLicense.reconstruct(
                entity.getId(),
                entity.getLicenseNumber(),
                entity.getIssuingBody(),
                entity.getExpiryDate(),
                entity.getStatus()
        );
    }
}