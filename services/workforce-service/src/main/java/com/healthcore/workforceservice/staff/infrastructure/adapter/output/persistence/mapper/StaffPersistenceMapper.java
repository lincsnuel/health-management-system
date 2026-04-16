package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.model.vo.*;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.FullNameEmbeddable;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.StaffEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StaffPersistenceMapper {

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public static StaffEntity toEntity(Staff staff) {
        if (staff == null) return null;

        return StaffEntity.builder()
                .staffId(staff.getStaffId().value())

                .fullName(FullNameEmbeddable.fromDomain(staff.getFullName()))
                .email(staff.getEmail().value())
                .phoneNumber(
                        staff.getPhoneNumber() != null
                                ? staff.getPhoneNumber().value()
                                : null
                )

                .gender(staff.getGender())
                .dateOfBirth(staff.getDateOfBirth())
                .staffType(staff.getStaffType())
                .status(staff.getStatus())

                .departmentId(
                        staff.getCurrentDepartmentId() != null
                                ? staff.getCurrentDepartmentId().value()
                                : null
                )

                // aggregate references (IDs only)
                .employmentId(
                        staff.getEmploymentId() != null
                                ? staff.getEmploymentId().value()
                                : null
                )
                .professionalProfileId(
                        staff.getProfessionalProfileId() != null
                                ? staff.getProfessionalProfileId().value()
                                : null
                )
                .credentialingId(
                        staff.getCredentialingId() != null
                                ? staff.getCredentialingId().value()
                                : null
                )

                .roles(
                        staff.getRoles().stream()
                                .map(Role::value)
                                .collect(Collectors.toSet())
                )

                .build();
    }

    // =========================
    // ENTITY → DOMAIN
    // =========================
    public static Staff toDomain(StaffEntity entity) {
        if (entity == null) return null;

        Staff staff = Staff.reconstruct(
                new StaffId(entity.getStaffId()),
                entity.getFullName().toDomain(),
                new EmailAddress(entity.getEmail()),
                entity.getPhoneNumber() != null
                        ? new PhoneNumber(entity.getPhoneNumber())
                        : null,
                entity.getGender(),
                entity.getDateOfBirth(),
                entity.getStaffType(),
                entity.getStatus(),
                entity.getDepartmentId() != null
                        ? new DepartmentId(entity.getDepartmentId())
                        : null,
                mapRoles(entity)
        );

        // attach references safely (NO setters, NO domain logic)
        attachReferences(staff, entity);

        return staff;
    }

    // =========================
    // PRIVATE HELPERS
    // =========================

    private static Set<Role> mapRoles(StaffEntity entity) {
        if (entity.getRoles() == null || entity.getRoles().isEmpty()) {
            return Collections.emptySet();
        }

        return entity.getRoles()
                .stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    private static void attachReferences(Staff staff, StaffEntity entity) {

        if (entity.getEmploymentId() != null) {
            staff.attachEmployment(new EmploymentId(entity.getEmploymentId()));
        }

        if (entity.getProfessionalProfileId() != null) {
            staff.attachProfessionalProfile(
                    new ProfessionalProfileId(entity.getProfessionalProfileId())
            );
        }

        if (entity.getCredentialingId() != null) {
            staff.attachCredentialing(
                    new CredentialingId(entity.getCredentialingId())
            );
        }
    }
}