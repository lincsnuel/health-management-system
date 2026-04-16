package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department_projection")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentProjection {

    @Id
    private String departmentId;

    private String tenantId;
    private String name;
    private boolean active;
}