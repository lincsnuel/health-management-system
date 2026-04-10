package com.healthcore.appointmentservice.application.command.handler;

import com.healthcore.appointmentservice.application.command.model.MarkArrivalCommand;
import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.policy.PaymentPolicy;
import com.healthcore.appointmentservice.domain.repository.AppointmentCommandRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkArrivalHandler {

    private final AppointmentCommandRepository repository;
    private final PaymentPolicy paymentPolicy;

    public void handle(MarkArrivalCommand cmd) {

        Appointment appointment = repository.findById(
                cmd.appointmentId, cmd.tenantId
        ).orElseThrow();

        appointment.markAsArrived(paymentPolicy);

        repository.save(appointment);
    }
}