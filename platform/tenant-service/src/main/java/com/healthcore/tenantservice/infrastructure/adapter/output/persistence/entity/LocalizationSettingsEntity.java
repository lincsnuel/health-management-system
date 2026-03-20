package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LocalizationSettingsEntity {

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "date_format", nullable = false)
    private String dateFormat;

    @Column(name = "language", nullable = false)
    private String language;

    /* ================= HELPERS ================= */
    public void updateTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void updateCurrency(String currency) {
        this.currency = currency;
    }

    public void updateDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void updateLanguage(String language) {
        this.language = language;
    }
}