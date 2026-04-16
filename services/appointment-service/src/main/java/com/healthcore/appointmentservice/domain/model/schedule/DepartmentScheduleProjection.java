package com.healthcore.appointmentservice.domain.model.schedule;

import com.healthcore.appointmentservice.domain.model.availability.Availability;
import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class DepartmentScheduleProjection {

    private final UUID id;
    private final UUID departmentId;
    private final UUID tenantId;

    // Key: date
    private final Map<LocalDate, DailySchedule> schedules;

    private long version;
    private Date lastUpdatedAt;

    public DepartmentScheduleProjection(UUID id, UUID tenantId, UUID departmentId) {
        this.id = id;
        this.tenantId = tenantId;
        this.departmentId = departmentId;
        this.schedules = new HashMap<>();
    }

    // ========================
    // CORE BEHAVIOR
    // ========================

    public Availability checkAvailability(
            LocalDate date,
            TimeSlot slot,
            int booked
    ) {

        DailySchedule schedule = schedules.get(date);

        if (schedule == null) {
            return Availability.unavailable("Department not active on this date");
        }

        Optional<TimeSlotCapacity> slotCapacity = schedule.findSlot(slot);

        if (slotCapacity.isEmpty()) {
            return Availability.unavailable("Slot not configured");
        }

        int capacity = slotCapacity.get().getCapacity();

        return new Availability(true, capacity, booked, "OK");
    }

    // ========================
    // EVENT HANDLERS (APPLY)
    // ========================

    public void apply(DailyScheduleCreatedEvent event) {
        schedules.put(
                event.getDate(),
                DailySchedule.from(event)
        );
        updateVersion(event.getVersion());
    }

    public void apply(SlotCapacityUpdatedEvent event) {
        DailySchedule schedule = schedules.get(event.getDate());

        if (schedule != null) {
            schedule.updateCapacity(event.getSlot(), event.getCapacity());
        }

        updateVersion(event.getVersion());
    }

    public void apply(DepartmentDeactivatedEvent event) {
        schedules.remove(event.getDate());
        updateVersion(event.getVersion());
    }

    private void updateVersion(long newVersion) {
        if (newVersion <= this.version) return; // idempotency
        this.version = newVersion;
        this.lastUpdatedAt = new Date();
    }
}