package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

/**
 * Response DTO for patient documents
 */
public record DocumentResponse(
        String documentId,
        String type,           // DocumentType as String
        String filePath,
        String uploadedAt,     // ISO-8601 yyyy-MM-dd
        boolean verified
) {}