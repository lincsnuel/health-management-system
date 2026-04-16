package com.healthcore.appointmentservice.domain.model.availability;

import lombok.Getter;

@Getter
public class Availability {

    private final boolean available;
    private final int capacity;
    private final int booked;

    private final String reason; // NEW (debugging, audit, UX)

    public Availability(boolean available, int capacity, int booked, String reason) {
        this.available = available;
        this.capacity = capacity;
        this.booked = booked;
        this.reason = reason;
    }

    public boolean isSlotAvailable() {
        return available && booked < capacity;
    }

    public int remaining() {
        return Math.max(0, capacity - booked);
    }

    public static Availability unavailable(String reason) {
        return new Availability(false, 0, 0, reason);
    }
}