package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.patientservice.domain.model.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private DocumentType type; // e.g., PHOTO_ID, MEDICAL_REPORT

    @Column(nullable = false)
    private String filePath; // or store as BLOB if needed

    @Column(nullable = false)
    private LocalDate uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;
}