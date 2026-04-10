package com.healthcore.appointmentservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.appointmentservice.domain.model.enums.AppointmentStatus;
import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "department_id", nullable = false)
    private UUID departmentId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot", nullable = false)
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Embedded
    private SymptomDescriptionEmbeddable symptomDescription;

    @Embedded
    private ReferralDetailsEmbeddable referralDetails;

    @ElementCollection
    @CollectionTable(
            name = "appointment_attachments",
            joinColumns = @JoinColumn(name = "appointment_id")
    )
    private List<AttachmentEmbeddable> attachments = new ArrayList<>();

    @Column(name = "assigned_doctor_id")
    private UUID assignedDoctorId;
}