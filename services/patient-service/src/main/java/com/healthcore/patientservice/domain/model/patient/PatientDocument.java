package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.model.enums.DocumentType;
import com.healthcore.patientservice.domain.model.vo.DocumentId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class PatientDocument {

    private final DocumentId id;
    private final DocumentType type;
    private final String filePath;
    private final LocalDate uploadedAt;
    private boolean verified;

    /* ================== CONSTRUCTOR ================== */
    private PatientDocument(
            DocumentId id,
            DocumentType type,
            String filePath,
            LocalDate uploadedAt,
            boolean verified
    ) {
        this.id = Objects.requireNonNull(id, "Document ID cannot be null");
        this.type = Objects.requireNonNull(type, "Document type cannot be null");
        this.filePath = Objects.requireNonNull(filePath, "File path cannot be null");
        this.uploadedAt = uploadedAt != null ? uploadedAt : LocalDate.now();
        this.verified = verified;
    }

    /* ================== FACTORY METHODS ================== */

    /**
     * Factory for NEW PatientDocument (generates ID)
     */
    public static PatientDocument create(
            DocumentType type,
            String filePath
    ) {
        return new PatientDocument(
                DocumentId.newId(),
                type,
                filePath,
                LocalDate.now(),
                false
        );
    }

    /**
     * Reconstruct EXISTING PatientDocument from persistence
     */
    public static PatientDocument reconstruct(
            DocumentId id,
            DocumentType type,
            String filePath,
            LocalDate uploadedAt,
            boolean verified
    ) {
        return new PatientDocument(
                id,
                type,
                filePath,
                uploadedAt,
                verified
        );
    }

    /* ================== BUSINESS LOGIC ================== */

    /**
     * Mark the document as verified by hospital staff.
     * Only unverified documents can be verified.
     */
    public void markVerified() {
        if (verified) {
            throw new IllegalStateException("Document is already verified");
        }
        this.verified = true;
    }

    /**
     * Mark the document as unverified, for example if staff finds an issue.
     */
    public void markUnverified() {
        this.verified = false;
    }

    /**
     * Check if the document is a required type for patient registration or insurance.
     */
    public boolean isMandatory() {
        return type == DocumentType.PHOTO_ID || type == DocumentType.INSURANCE_CARD;
    }
}