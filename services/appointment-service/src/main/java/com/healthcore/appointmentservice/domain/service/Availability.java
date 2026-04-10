package com.healthcore.appointmentservice.domain.service;

import lombok.Getter;

@Getter
public class Availability {

    private final boolean available;
    private final int capacity;
    private final int booked;

    public Availability(boolean available, int capacity, int booked) {
        this.available = available;
        this.capacity = capacity;
        this.booked = booked;
    }

    public boolean isSlotAvailable() {
        return available && booked < capacity;
    }
}