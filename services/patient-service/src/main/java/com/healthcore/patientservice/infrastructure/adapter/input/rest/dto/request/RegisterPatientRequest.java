package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request;

import com.healthcore.patientservice.domain.model.enums.IdentityType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record RegisterPatientRequest(

        @NotBlank(message = "Hospital patient number is required")
        String hospitalPatientNumber,

        /* ================== CORE IDENTITY ================== */
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 40)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 40)
        String lastName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Gender is required")
        String gender,

        @Email(message = "Email must be valid")
        @Size(max = 100)
        String email,

        @NotBlank(message = "Contact number is required")
        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String contactNumber,

        /* ================== NATIONAL ID (OPTIONAL) ================== */
        IdentityType identityType,

        @Size(max = 50)
        String nationalIdNumber,

        /* ================== ADDRESS ================== */
        @Valid
        AddressRequest address,

        /* ================== RESPONSIBLE PARTIES ================== */
        @Valid
        List<ResponsiblePartyRequest> responsibleParties,

        /* ================== INSURANCE (OPTIONAL) ================== */
        @Valid
        InsurancePolicyRequest insurancePolicy

) {

        /* ================== CROSS FIELD VALIDATION ================== */

        @AssertTrue(message = "Both identityType and nationalIdNumber must be provided together")
        public boolean isNationalIdPairValid() {

                if (identityType == null && (nationalIdNumber == null || nationalIdNumber.isBlank())) {
                        return true;
                }

                return identityType != null
                        && nationalIdNumber != null
                        && !nationalIdNumber.isBlank();
        }

}