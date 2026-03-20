package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationSettingsEntity {

    @Column(name = "sms_enabled", nullable = false)
    private Boolean smsEnabled;

    @Column(name = "email_enabled", nullable = false)
    private Boolean emailEnabled;

    @Column(name = "appointment_reminder_hours", nullable = false)
    private Integer appointmentReminderHours;

    /* ================= HELPERS ================= */
    public void updateSmsEnabled(Boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public void updateEmailEnabled(Boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public void updateAppointmentReminderHours(Integer hours) {
        this.appointmentReminderHours = hours;
    }
}