package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.vo.EmploymentId;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.EmploymentEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmploymentMapper {

    public Employment toDomain(EmploymentEntity entity) {
        Employment employment = Employment.create(
                new EmploymentId(entity.getId()),
                new StaffId(entity.getStaffId()),
                entity.getEmployeeId(),
                entity.getType(),
                entity.getDateHired()
        );

        if (entity.getDepartmentId() != null) {
            employment.transferDepartment(
                    new DepartmentId(entity.getDepartmentId().toString())
            );
        }

        return employment;
    }

    public EmploymentEntity toEntity(Employment domain) {
        return EmploymentEntity.builder()
                .id(domain.getId().value())
                .staffId(domain.getStaffId().value())
                .employeeId(domain.getEmployeeId())
                .type(domain.getType())
                .dateHired(domain.getDateHired())
                .departmentId(domain.getDepartmentId() != null ?
                        UUID.fromString(domain.getDepartmentId().value()) : null)
                .build();
    }
}