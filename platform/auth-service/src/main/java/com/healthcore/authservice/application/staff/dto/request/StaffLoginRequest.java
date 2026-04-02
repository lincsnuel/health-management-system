package com.healthcore.authservice.application.staff.dto.request;

import lombok.Data;

@Data
public class StaffLoginRequest {
    private String email;
    private String password;
}