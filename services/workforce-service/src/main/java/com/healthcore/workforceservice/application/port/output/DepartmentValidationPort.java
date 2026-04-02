package com.healthcore.workforceservice.application.port.output;

public interface DepartmentValidationPort {
    boolean exists(String tenantId, String departmentId);
}