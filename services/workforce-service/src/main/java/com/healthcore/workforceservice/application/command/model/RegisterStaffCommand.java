package com.healthcore.workforceservice.application.command.model;

import com.healthcore.workforceservice.domain.model.enums.Gender;
import com.healthcore.workforceservice.domain.model.enums.IdentityType;
import com.healthcore.workforceservice.domain.model.enums.StaffType;

import java.time.LocalDate;
import java.util.List;

public record RegisterStaffCommand(
        String tenantId,
        String departmentId,
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth,
        StaffType staffType,
        IdentityType identityType,
        String identityNumber,
        List<String> roles
) {}