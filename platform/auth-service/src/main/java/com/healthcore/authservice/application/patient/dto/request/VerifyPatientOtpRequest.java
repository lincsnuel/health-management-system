package com.healthcore.authservice.application.patient.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record VerifyPatientOtpRequest(

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String phoneNumber,

        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be 6 digits")
        String otp

) {}