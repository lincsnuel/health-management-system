package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response;

import java.util.UUID;

public record DepartmentResponse(
        UUID id,
        String name,
        boolean active
) {}
