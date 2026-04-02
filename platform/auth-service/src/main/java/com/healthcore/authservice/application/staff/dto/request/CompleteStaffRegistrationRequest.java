package com.healthcore.authservice.application.staff.dto.request;

import lombok.Data;

@Data
public class CompleteStaffRegistrationRequest {
    private String token;      // activation token
    private String password;
}