package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BrandingSettingsEntity {

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "primary_color", nullable = false)
    private String primaryColor;

    @Column(name = "secondary_color", nullable = false)
    private String secondaryColor;

    @Column(name = "theme", nullable = false)
    private String theme;

    /* ================= HELPERS ================= */
    public void updateLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void updatePrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void updateSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void updateTheme(String theme) {
        this.theme = theme;
    }
}