package com.healthcore.appointmentservice.application.command.handler;

import com.healthcore.appointmentservice.application.command.model.CancelAppointmentCommand;
import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.repository.AppointmentCommandRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelAppointmentHandler {

    private final AppointmentCommandRepository repository;

    public void handle(CancelAppointmentCommand cmd) {

        Appointment appointment = repository.findById(
                cmd.appointmentId, cmd.tenantId
        ).orElseThrow();

        appointment.cancel();

        repository.save(appointment);
    }
}