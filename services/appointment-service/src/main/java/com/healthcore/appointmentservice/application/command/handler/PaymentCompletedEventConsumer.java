package com.healthcore.appointmentservice.application.command.handler;

import com.healthcore.appointmentservice.application.event.PaymentCompletedEvent;
import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.model.enums.AppointmentStatus;
import com.healthcore.appointmentservice.domain.repository.AppointmentCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCompletedEventConsumer {

    private final AppointmentCommandRepository repository;

    @KafkaListener(
            topics = "payment.completed.v1",
            groupId = "appointment-service"
    )
    public void consume(PaymentCompletedEvent event) {

        Appointment appointment = repository.findById(event.getAppointmentId(), event.getTenantId())
                .orElse(null);

        if (appointment == null) {
            // log + ignore OR dead-letter
            return;
        }

        // Idempotency protection (important)
        if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
            return;
        }

        appointment.confirmPayment();

        repository.save(appointment);
    }
}