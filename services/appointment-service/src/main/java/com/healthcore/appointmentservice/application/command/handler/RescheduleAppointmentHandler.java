package com.healthcore.appointmentservice.application.command.handler;

import com.healthcore.appointmentservice.application.command.model.RescheduleAppointmentCommand;
import com.healthcore.appointmentservice.application.command.port.ScheduleServiceClient;
import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.policy.BookingPolicy;
import com.healthcore.appointmentservice.domain.repository.AppointmentCommandRepository;
import com.healthcore.appointmentservice.domain.service.AppointmentDomainService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RescheduleAppointmentHandler {

    private final AppointmentCommandRepository repository;
    private final ScheduleServiceClient scheduleClient;
    private final AppointmentDomainService domainService;
    private final BookingPolicy bookingPolicy;

    public void handle(RescheduleAppointmentCommand cmd) {

        Appointment appointment = repository.findById(
                cmd.appointmentId, cmd.tenantId
        ).orElseThrow();

        // availability check
        Availability availability = scheduleClient.checkAvailability(
                appointment.getDepartmentId().value(),
                cmd.newDate,
                cmd.newSlot
        );

        domainService.validateReschedule(availability);

        // domain logic
        appointment.reschedule(cmd.newDate, cmd.newSlot, bookingPolicy);

        repository.save(appointment);
    }
}