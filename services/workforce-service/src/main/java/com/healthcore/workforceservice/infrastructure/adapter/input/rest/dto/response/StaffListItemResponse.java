package com.healthcore.workforceservice.infrastructure.adapter.input.rest.dto.response;

public record StaffListItemResponse(
        String staffId,
        String fullName,
        String email,
        String departmentName,
        String status
) {}