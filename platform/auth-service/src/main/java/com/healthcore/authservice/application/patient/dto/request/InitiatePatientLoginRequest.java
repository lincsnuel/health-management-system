package com.healthcore.authservice.application.patient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record InitiatePatientLoginRequest(

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String phoneNumber,

        @Email(message = "Email must be valid")
        @Size(max = 100)
        String email

) {}