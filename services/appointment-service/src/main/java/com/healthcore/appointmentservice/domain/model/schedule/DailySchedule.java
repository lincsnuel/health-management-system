package com.healthcore.appointmentservice.domain.model.schedule;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class DailySchedule {

    private final LocalDate date;
    private final Map<TimeSlot, TimeSlotCapacity> slots;

    public DailySchedule(LocalDate date, List<TimeSlotCapacity> slots) {
        this.date = date;
        this.slots = new EnumMap<>(TimeSlot.class);

        for (TimeSlotCapacity slot : slots) {
            this.slots.put(slot.getTimeSlot(), slot);
        }
    }

    public Optional<TimeSlotCapacity> findSlot(TimeSlot slot) {
        return Optional.ofNullable(slots.get(slot));
    }

    public void updateCapacity(TimeSlot slot, int capacity) {
        TimeSlotCapacity existing = slots.get(slot);

        if (existing != null) {
            existing.updateCapacity(capacity);
        }
    }

    public static DailySchedule from(DailyScheduleCreatedEvent event) {
        return new DailySchedule(
                event.getDate(),
                event.getSlots()
        );
    }
}