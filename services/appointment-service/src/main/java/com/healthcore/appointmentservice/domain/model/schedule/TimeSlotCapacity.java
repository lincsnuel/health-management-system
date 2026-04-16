package com.healthcore.appointmentservice.domain.model.schedule;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import lombok.Getter;

@Getter
public class TimeSlotCapacity {

    private final TimeSlot timeSlot;
    private int capacity;

    public TimeSlotCapacity(TimeSlot timeSlot, int capacity) {
        this.timeSlot = timeSlot;
        this.capacity = capacity;
    }

    public void updateCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }
}