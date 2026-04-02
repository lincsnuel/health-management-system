package com.healthcore.authservice.application.patient.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record  ResponsiblePartyRequest(

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100)
        String lastName,

        @NotNull(message = "Responsible party type is required")
        String type,

        @NotBlank(message = "Relationship is required")
        @Size(max = 50, message = "Relationship must not exceed 50 characters")
        String relationship,

        @NotBlank(message = "Contact number is required")
        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String contactNumber,

        @Valid
        AddressRequest address

) {}