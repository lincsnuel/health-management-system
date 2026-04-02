package com.healthcore.authservice.application.staff.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterStaffRequest {

    private String departmentId;

    private String firstName;
    private String middleName;
    private String lastName;

    private String email;
    private String phoneNumber;

    private String gender;
    private LocalDate dateOfBirth;

    private String staffType;

    private String identityType;
    private String identityNumber;

    private List<String> roles; // e.g. ["DOCTOR", "ADMIN"]
}