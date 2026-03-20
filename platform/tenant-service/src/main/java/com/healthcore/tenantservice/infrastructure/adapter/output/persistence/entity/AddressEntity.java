package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AddressEntity {

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    /* ================= HELPERS ================= */
    public void updateStreet(String street) {
        this.street = street;
    }

    public void updateCity(String city) {
        this.city = city;
    }

    public void updateState(String state) {
        this.state = state;
    }

    public void updateCountry(String country) {
        this.country = country;
    }

    public void updatePostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}