package com.healthcore.workforceservice.staff.domain.model.vo;

import java.util.Objects; /**
 * Nigerian-specific address context
 */
public record ResidentialAddress(
        String street,
        String city,
        String state,
        String country
) {
    public ResidentialAddress {
        Objects.requireNonNull(state, "State is required");
        Objects.requireNonNull(city, "City is required");
    }
}
