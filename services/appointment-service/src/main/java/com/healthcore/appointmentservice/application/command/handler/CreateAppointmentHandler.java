package com.healthcore.appointmentservice.application.command.handler;

import com.healthcore.appointmentservice.application.command.dto.AppointmentResponse;
import com.healthcore.appointmentservice.application.command.model.CreateAppointmentCommand;
import com.healthcore.appointmentservice.application.command.port.BillingServiceClient;
import com.healthcore.appointmentservice.application.command.port.IdempotencyService;
import com.healthcore.appointmentservice.application.command.port.ScheduleServiceClient;
import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.policy.BookingPolicy;
import com.healthcore.appointmentservice.domain.repository.AppointmentCommandRepository;
import com.healthcore.appointmentservice.domain.service.AppointmentDomainService;
import com.healthcore.appointmentservice.domain.service.Availability;
import com.healthcore.appointmentservice.domain.model.vo.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateAppointmentHandler {

    private final AppointmentCommandRepository repository;
    private final ScheduleServiceClient scheduleClient;
    private final BillingServiceClient billingClient;
    private final AppointmentDomainService domainService;
    private final BookingPolicy bookingPolicy;
    private final IdempotencyService idempotencyService;

    public AppointmentResponse handle(CreateAppointmentCommand cmd) {

        // Idempotency check
        if (idempotencyService.isProcessed(cmd.idempotencyKey)) {
            UUID existingId = idempotencyService.getResourceId(cmd.idempotencyKey);
            return AppointmentResponse.from(existingId, "PENDING_PAYMENT");
        }

        // 1. Check availability (gRPC)
        Availability availability = scheduleClient.checkAvailability(
                cmd.departmentId,
                cmd.date,
                cmd.timeSlot
        );

        domainService.validateBooking(availability);

        // 2. Create domain object
        Appointment appointment = Appointment.create(
                new TenantId(cmd.tenantId),
                new PatientId(cmd.patientId),
                new DepartmentId(cmd.departmentId),
                cmd.date,
                cmd.timeSlot,
                cmd.symptom,
                null,
                List.of(),
                bookingPolicy
        );

        // 3. Persist
        repository.save(appointment);

        // 4. Create billing (gRPC)
        UUID billingId = billingClient.createInvoice(
                cmd.patientId,
                appointment.getId().value()
        );

        // 5. Mark idempotent
        idempotencyService.markProcessed(
                cmd.idempotencyKey,
                appointment.getId().value()
        );

        return AppointmentResponse.from(
                appointment.getId().value(),
                appointment.getStatus().name(),
                billingId
        );
    }
}