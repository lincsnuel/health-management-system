package com.healthcore.workforceservice.staff.application.command.model;

import com.healthcore.workforceservice.staff.domain.model.enums.EmploymentType;
import com.healthcore.workforceservice.staff.domain.model.enums.Gender;
import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;

import java.time.LocalDate;
import java.util.List;

public record RegisterStaffCommand(

        // =========================
        // ORGANIZATION CONTEXT
        // =========================
        String departmentId,

        // =========================
        // PERSONAL IDENTITY
        // =========================
        String firstName,
        String middleName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth,

        // =========================
        // STAFF CLASSIFICATION
        // =========================
        StaffType staffType,

        // =========================
        // EMPLOYMENT (Employment Aggregate)
        // =========================
        String employeeId,
        EmploymentType employmentType,
        LocalDate dateOfHire,

        // =========================
        // PROFESSIONAL PROFILE (ProfessionalProfile Aggregate)
        // =========================
        String specialization,
        String academicTitle,
        boolean isConsultant,

        // =========================
        // ACCESS CONTROL
        // =========================
        List<String> roles

) {}