package com.healthcore.appointmentservice.domain.policy;

import com.healthcore.appointmentservice.domain.model.enums.AppointmentStatus;

public interface PaymentPolicy {
    boolean canAttend(AppointmentStatus status);
}