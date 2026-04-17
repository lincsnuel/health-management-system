package com.healthcore.authservice.application.staff.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterStaffRequest {

    // =========================
    // ORGANIZATION CONTEXT
    // =========================
    private String departmentId;

    // =========================
    // PERSONAL IDENTITY
    // =========================
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String gender; // Maps to Gender enum
    private LocalDate dateOfBirth;

    // =========================
    // STAFF CLASSIFICATION
    // =========================
    private String staffType; // Maps to StaffType enum

    // =========================
    // EMPLOYMENT
    // =========================
    private String employeeId;
    private String employmentType; // Maps to EmploymentType enum
    private LocalDate dateOfHire;

    // =========================
    // PROFESSIONAL PROFILE
    // =========================
    private String specialization;
    private String academicTitle;
    private boolean isConsultant;

    // =========================
    // ACCESS CONTROL
    // =========================
    private List<String> roles;
}