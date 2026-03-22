package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response;

import java.util.UUID;

public record BranchResponse(
        UUID id,
        String name,
        boolean main,
        boolean active
) {}
