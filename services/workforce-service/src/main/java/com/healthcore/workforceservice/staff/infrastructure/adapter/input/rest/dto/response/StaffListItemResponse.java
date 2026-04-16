package com.healthcore.workforceservice.staff.infrastructure.adapter.input.rest.dto.response;

public record StaffListItemResponse(
        String staffId,
        String fullName,
        String email,
        String departmentName,
        String status
) {}