package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OperationalSettingsEntity {

    @Column(name = "appointment_slot_duration", nullable = false)
    private Integer appointmentSlotDuration;

    @Column(name = "working_days", nullable = false)
    private String workingDays;

    @Column(name = "working_hours_start", nullable = false)
    private String workingHoursStart;

    @Column(name = "working_hours_end", nullable = false)
    private String workingHoursEnd;

    @Column(name = "allow_overbooking", nullable = false)
    private Boolean allowOverbooking;

    /* ================= HELPERS ================= */
    public void updateAppointmentSlotDuration(Integer duration) {
        this.appointmentSlotDuration = duration;
    }

    public void updateWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public void updateWorkingHoursStart(String start) {
        this.workingHoursStart = start;
    }

    public void updateWorkingHoursEnd(String end) {
        this.workingHoursEnd = end;
    }

    public void updateAllowOverbooking(Boolean allow) {
        this.allowOverbooking = allow;
    }
}