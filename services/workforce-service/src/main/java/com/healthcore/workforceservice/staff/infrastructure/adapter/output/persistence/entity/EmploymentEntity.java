package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.healthcorecommon.domain.BaseEntity;
import com.healthcore.workforceservice.staff.domain.model.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employment", indexes = {
        @Index(name = "idx_employment_staff_id", columnList = "staff_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmploymentEntity extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "staff_id", nullable = false)
    private UUID staffId;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType type;

    @Column(nullable = false)
    private LocalDate dateHired;

    @Column(name = "department_id")
    private UUID departmentId;
}