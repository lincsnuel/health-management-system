package com.healthcore.authservice.application.patient.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record AddressRequest(

        @NotBlank(message = "Street is required")
        @Size(max = 100)
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 50)
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 50)
        String state,

        @NotBlank(message = "Country is required")
        @Size(max = 50)
        String country

) {}