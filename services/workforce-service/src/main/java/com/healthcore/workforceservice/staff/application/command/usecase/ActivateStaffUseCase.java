package com.healthcore.workforceservice.staff.application.command.usecase;

import java.util.UUID;

public interface ActivateStaffUseCase {
    void activate(UUID staffId, UUID tenantId);
}