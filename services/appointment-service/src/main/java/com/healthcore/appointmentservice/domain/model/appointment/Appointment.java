package com.healthcore.appointmentservice.domain.model.appointment;

import com.healthcore.appointmentservice.domain.exception.BookingWindowExceededException;
import com.healthcore.appointmentservice.domain.exception.InvalidAppointmentDateException;
import com.healthcore.appointmentservice.domain.exception.InvalidAppointmentStateTransitionException;
import com.healthcore.appointmentservice.domain.exception.PaymentRequiredException;
import com.healthcore.appointmentservice.domain.model.enums.AppointmentStatus;
import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import com.healthcore.appointmentservice.domain.model.vo.*;
import com.healthcore.appointmentservice.domain.policy.BookingPolicy;
import com.healthcore.appointmentservice.domain.policy.PaymentPolicy;
import lombok.Getter;

import java.time.*;
import java.util.*;

public class Appointment {

    @Getter
    private final AppointmentId id;

    private final TenantId tenantId;
    private final PatientId patientId;

    @Getter
    private final DepartmentId departmentId;

    private LocalDate appointmentDate;
    private TimeSlot timeSlot;

    @Getter
    private AppointmentStatus status;

    private SymptomDescription symptomDescription;
    private ReferralDetails referralDetails;

    private final List<Attachment> attachments;

    private UUID assignedDoctorId;

    // ========================
    // CONSTRUCTOR (PRIVATE)
    // ========================
    private Appointment(
            AppointmentId id,
            TenantId tenantId,
            PatientId patientId,
            DepartmentId departmentId,
            LocalDate appointmentDate,
            TimeSlot timeSlot,
            SymptomDescription symptomDescription,
            ReferralDetails referralDetails,
            List<Attachment> attachments
    ) {
        this.id = Objects.requireNonNull(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.patientId = Objects.requireNonNull(patientId);
        this.departmentId = Objects.requireNonNull(departmentId);

        validateDate(appointmentDate);

        this.appointmentDate = appointmentDate;
        this.timeSlot = Objects.requireNonNull(timeSlot);

        this.symptomDescription = symptomDescription;
        this.referralDetails = referralDetails;

        this.attachments = new ArrayList<>(attachments != null ? attachments : List.of());

        this.status = AppointmentStatus.PENDING_PAYMENT;
    }

    // ========================
    // FACTORY
    // ========================
    public static Appointment create(
            TenantId tenantId,
            PatientId patientId,
            DepartmentId departmentId,
            LocalDate date,
            TimeSlot slot,
            String symptom,
            ReferralDetails referral,
            List<Attachment> attachments,
            BookingPolicy bookingPolicy
    ) {

        if (!bookingPolicy.isWithinBookingWindow(date)) {
            throw new BookingWindowExceededException();
        }

        return new Appointment(
                AppointmentId.generate(),
                tenantId,
                patientId,
                departmentId,
                date,
                slot,
                SymptomDescription.of(symptom),
                referral,
                attachments
        );
    }

    // ========================
    // STATE TRANSITIONS
    // ========================

    public void confirmPayment() {
        if (status != AppointmentStatus.PENDING_PAYMENT) {
            throw new InvalidAppointmentStateTransitionException(
                    "Only PENDING_PAYMENT can be confirmed"
            );
        }

        this.status = AppointmentStatus.CONFIRMED;
    }

    public void cancel() {
        if (isTerminal()) {
            throw new InvalidAppointmentStateTransitionException(
                    "Cannot cancel appointment in state: " + status
            );
        }

        this.status = AppointmentStatus.CANCELLED;
    }

    public void reschedule(LocalDate newDate, TimeSlot newSlot, BookingPolicy policy) {

        assertNotTerminal();

        if (!policy.isWithinBookingWindow(newDate)) {
            throw new BookingWindowExceededException();
        }

        validateDate(newDate);

        this.appointmentDate = newDate;
        this.timeSlot = newSlot;

        // Reset payment if needed
        this.status = AppointmentStatus.PENDING_PAYMENT;
    }

    public void markAsArrived(PaymentPolicy paymentPolicy) {

        if (!paymentPolicy.canAttend(status)) {
            throw new PaymentRequiredException("");
        }

        if (status != AppointmentStatus.CONFIRMED &&
                status != AppointmentStatus.PENDING_PAYMENT) {
            throw new InvalidAppointmentStateTransitionException(
                    "Invalid state for arrival"
            );
        }

        this.status = AppointmentStatus.ARRIVED;
    }

    public void complete() {
        if (status != AppointmentStatus.ARRIVED) {
            throw new InvalidAppointmentStateTransitionException(
                    "Only ARRIVED appointments can be completed"
            );
        }

        this.status = AppointmentStatus.COMPLETED;
    }

    public void markNoShow(LocalDateTime now, Duration gracePeriod) {

        if (status != AppointmentStatus.CONFIRMED) return;

        LocalDateTime apptTime = appointmentDate.atTime(
                timeSlot == TimeSlot.MORNING ? LocalTime.of(9, 0) : LocalTime.of(14, 0)
        );

        if (now.isAfter(apptTime.plus(gracePeriod))) {
            this.status = AppointmentStatus.NO_SHOW;
        }
    }

    public void assignDoctor(UUID doctorId) {

        if (status != AppointmentStatus.ARRIVED) {
            throw new InvalidAppointmentStateTransitionException(
                    "Doctor assignment only allowed after arrival"
            );
        }

        this.assignedDoctorId = doctorId;
    }

    // ========================
    // INVARIANTS
    // ========================

    private void validateDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidAppointmentDateException("Date cannot be in the past");
        }
    }

    private void assertNotTerminal() {
        if (isTerminal()) {
            throw new InvalidAppointmentStateTransitionException(
                    "Operation not allowed in terminal state: " + status
            );
        }
    }

    private boolean isTerminal() {
        return status == AppointmentStatus.CANCELLED ||
                status == AppointmentStatus.COMPLETED ||
                status == AppointmentStatus.NO_SHOW;
    }

    // ========================
    // REHYDRATION
    // ========================

    public static Appointment rehydrate(
            AppointmentId id,
            TenantId tenantId,
            PatientId patientId,
            DepartmentId departmentId,
            LocalDate date,
            TimeSlot slot,
            AppointmentStatus status,
            String symptom,
            ReferralDetails referral,
            List<Attachment> attachments,
            UUID doctorId,
            Instant createdAt,
            Instant updatedAt
    ) {

        Appointment appt = new Appointment(
                id, tenantId, patientId, departmentId,
                date, slot,
                SymptomDescription.of(symptom),
                referral,
                attachments
        );

        appt.status = status;
        appt.assignedDoctorId = doctorId;

        return appt;
    }
}