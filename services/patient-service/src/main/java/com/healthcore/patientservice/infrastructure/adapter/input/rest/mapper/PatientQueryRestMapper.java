package com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientQueryRestMapper {

    /* ============================= Patient Details ============================= */

    public PatientDetailsResponse toDetailsResponse(PatientDetails patient) {
        if (patient == null) return null;

        return new PatientDetailsResponse(
                uuidToString(patient.patientId()),
                patient.hospitalPatientNumber(),
                patient.firstName(),
                patient.lastName(),
                patient.fullName(),
                dateToIso(patient.dateOfBirth()),
                patient.age(),
                enumToString(patient.gender()),
                patient.contactNumber(),
                patient.email(),
                enumToString(patient.bloodGroup()),
                enumToString(patient.genotype()),
                enumToString(patient.religion()),
                enumToString(patient.identityType()),
                patient.nationalIdNumber(),
                toResponsiblePartyResponseList(patient.responsibleParties()),
                toAddressResponseList(patient.addresses()),
                toInsuranceResponseList(patient.insurancePolicies()),
                enumToString(patient.status()),
                dateTimeToString(patient.createdAt()),
                dateTimeToString(patient.updatedAt())
        );
    }

    /* ============================= Patient List Item ============================= */

    public PatientListItemResponse toPatientListItemResponse(PatientListItem patient) {
        if (patient == null) return null;

        return new PatientListItemResponse(
                uuidToString(patient.patientId()),
                patient.hospitalPatientNumber(),
                patient.firstName(),
                patient.lastName(),
                patient.fullName(),
                dateToIso(patient.dateOfBirth()),
                patient.age(),
                enumToString(patient.gender()),
                patient.contactNumber(),
                patient.status()
        );
    }

    /* ============================= Patient Summary ============================= */

    public PatientSummaryResponse toPatientSummaryResponse(PatientSummary patient) {
        if (patient == null) return null;

        return new PatientSummaryResponse(
                uuidToString(patient.patientId()),
                patient.hospitalPatientNumber(),
                patient.fullName(),
                enumToString(patient.gender()),
                dateToIso(patient.dateOfBirth()),
                patient.age(),
                patient.contactNumber(),
                patient.status()
        );
    }

    /* ============================= Patient Name ============================= */

    public PatientNameResponse toPatientNameResponse(PatientNameQuery query) {
        if (query == null) return null;
        return new PatientNameResponse(
                uuidToString(query.patientId()),
                query.hospitalPatientNumber(),
                query.fullName()
        );
    }

    /* ============================= Address ============================= */

    public AddressResponse toAddressResponse(AddressDetail address) {
        if (address == null) return null;

        return new AddressResponse(
                uuidToString(address.addressId()),
                address.street(),
                address.city(),
                address.state(),
                address.country(),
                address.primary()
        );
    }

    /* ============================= Insurance ============================= */

    public InsuranceResponse toInsuranceResponse(InsuranceDetail policy) {
        if (policy == null) return null;

        return new InsuranceResponse(
                uuidToString(policy.insuranceId()),
                policy.providerName(),
                policy.policyNumber(),
                policy.planType(),
                dateToIso(policy.coverageStart()),
                dateToIso(policy.coverageEnd()),
                policy.main(),
                policy.active()
        );
    }

    /* ============================= Responsible Party ============================= */

    public ResponsiblePartyResponse toResponsiblePartyResponse(ResponsiblePartyDetail party) {
        if (party == null) return null;

        return new ResponsiblePartyResponse(
                uuidToString(party.id()),
                party.firstName(),
                party.lastName(),
                party.contactNumber(),
                party.relationship(),
                enumToString(party.type()),
                toAddressResponse(party.address())
        );
    }

    /* ============================= Document ============================= */

    public DocumentResponse toDocumentResponse(DocumentDetail doc) {
        if (doc == null) return null;

        return new DocumentResponse(
                uuidToString(doc.documentId()),
                enumToString(doc.type()),
                doc.filePath(),
                dateToIso(doc.uploadedAt()),
                doc.verified()
        );
    }

    /* ============================= COLLECTION MAPPERS ============================= */

    public List<AddressResponse> toAddressResponseList(List<AddressDetail> addresses) {
        if (addresses == null) return List.of();
        return addresses.stream().map(this::toAddressResponse).collect(Collectors.toList());
    }

    public List<InsuranceResponse> toInsuranceResponseList(List<InsuranceDetail> policies) {
        if (policies == null) return List.of();
        return policies.stream().map(this::toInsuranceResponse).collect(Collectors.toList());
    }

    public List<ResponsiblePartyResponse> toResponsiblePartyResponseList(List<ResponsiblePartyDetail> parties) {
        if (parties == null) return List.of();
        return parties.stream().map(this::toResponsiblePartyResponse).collect(Collectors.toList());
    }

    public List<DocumentResponse> toDocumentResponseList(List<DocumentDetail> documents) {
        if (documents == null) return List.of();
        return documents.stream().map(this::toDocumentResponse).collect(Collectors.toList());
    }

    /* ============================= Helper Converters ============================= */

    public static String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    public static String enumToString(Enum<?> e) {
        return e != null ? e.name() : null;
    }

    public static String dateToIso(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ISO_DATE) : null;
    }

    public static String dateTimeToString(Instant dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}