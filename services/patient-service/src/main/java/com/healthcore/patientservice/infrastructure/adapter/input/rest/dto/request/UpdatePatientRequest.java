package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request;

import com.healthcore.patientservice.domain.model.enums.BloodGroup;
import com.healthcore.patientservice.domain.model.enums.Genotype;
import com.healthcore.patientservice.domain.model.enums.IdentityType;
import com.healthcore.patientservice.domain.model.enums.Religion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record UpdatePatientRequest(

        @NotBlank(message = "Tenant ID is required")
        String tenantId,

        @Email(message = "Email must be valid")
        @Size(max = 100)
        String email,

        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String contactNumber,

        BloodGroup bloodGroup,

        Genotype genotype,

        Religion religion,

        IdentityType identityType,

        @Size(max = 50)
        String nationalIdNumber,

        @Valid
        List<ResponsiblePartyRequest> responsibleParties,

        @Valid
        List<AddressRequest> addresses,

        @Valid
        List<InsurancePolicyRequest> insurances

) {

    /**
     * Ensures identity type and number are provided together.
     */
    @AssertTrue(message = "Both identityType and nationalIdNumber must be provided together")
    public boolean isNationalIdPairValid() {
        if (identityType == null && (nationalIdNumber == null || nationalIdNumber.isBlank())) {
            return true;
        }
        return identityType != null && nationalIdNumber != null && !nationalIdNumber.isBlank();
    }
}