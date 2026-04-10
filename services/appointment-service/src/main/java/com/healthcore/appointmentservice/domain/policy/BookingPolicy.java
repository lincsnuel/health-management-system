package com.healthcore.appointmentservice.domain.policy;

import java.time.LocalDate;

public interface BookingPolicy {
    boolean isWithinBookingWindow(LocalDate date);
}