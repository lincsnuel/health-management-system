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
    // ENTITY → DOMAIN
    // =========================
    public Staff toDomain(StaffEntity entity) {

        Staff staff = Staff.reconstruct(
                new StaffId(entity.getStaffId()),
                toFullName(entity),
                new EmailAddress(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber()),
                entity.getGender(),
                entity.getDateOfBirth(),
                entity.getStaffType(),
                entity.getStatus(),
                toDepartmentId(entity),
                toRoles(entity)
        );

        // Attach aggregate references safely
        if (entity.getEmploymentId() != null) {
            staff.attachEmployment(
                    new EmploymentId(entity.getEmploymentId())
            );
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

        return staff;
    }

    // =========================
    // DOMAIN → ENTITY
    // =========================
    public StaffEntity toEntity(Staff domain) {

        return StaffEntity.builder()
                .staffId(domain.getStaffId().value())
                .fullName(toEmbeddable(domain.getFullName()))
                .email(domain.getEmail().value())
                .phoneNumber(domain.getPhoneNumber() != null ? domain.getPhoneNumber().value() : null)
                .gender(domain.getGender())
                .dateOfBirth(domain.getDateOfBirth())
                .staffType(domain.getStaffType())
                .status(domain.getStatus())
                .departmentId(domain.getCurrentDepartmentId() != null
                        ? domain.getCurrentDepartmentId().value()
                        : null)
                .employmentId(domain.getEmploymentId().value())
                .professionalProfileId(domain.getProfessionalProfileId().value())
                .credentialingId(domain.getCredentialingId().value())
                .roles(fromDomainRoles(domain.getRoles()))
                .build();
    }

    // =========================
    // HELPERS
    // =========================

    private FullName toFullName(StaffEntity entity) {
        return new FullName(
                entity.getFullName().getFirstName(),
                entity.getFullName().getMiddleName(),
                entity.getFullName().getLastName()
        );
    }

    private FullNameEmbeddable toEmbeddable(FullName fullName) {
        return new FullNameEmbeddable(
                fullName.firstName(),
                fullName.middleName(),
                fullName.lastName()
        );
    }

    private DepartmentId toDepartmentId(StaffEntity entity) {
        return entity.getDepartmentId() != null
                ? new DepartmentId(entity.getDepartmentId())
                : null;
    }

    private Set<Role> toRoles(StaffEntity entity) {
        return entity.getRoles().stream()
                .map(Role::new)
                .collect(Collectors.toSet());
    }

    private Set<String> fromDomainRoles(Set<Role> roles) {
        return roles.stream()
                .map(Record::toString)
                .collect(Collectors.toSet());
    }
}