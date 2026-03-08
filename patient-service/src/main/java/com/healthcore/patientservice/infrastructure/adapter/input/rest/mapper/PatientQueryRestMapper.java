//package com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper;
//
//import com.healthcore.patientservice.application.query.model.*;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;
//
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Component
//public class PatientQueryRestMapper {
//
//    public PatientDetailsResponse toDetailsResponse(PatientDetails patient) {
//
//        if (patient == null) {
//            return null;
//        }
//
//        return PatientDetailsResponse.builder()
//                .patientId(patient.patientId())
//                .hospitalPatientNumber(patient.hospitalPatientNumber())
//                .firstName(patient.firstName())
//                .dateOfBirth(patient.dateOfBirth())
//                .age(patient.age()) // computed inside query model
//                .gender(patient.gender().toString())
//                .contactNumber(patient.contactNumber())
//                .email(patient.email())
//                .bloodGroup(patient.bloodGroup().toString())
//                .genotype(patient.genotype().toString())
//                .identityType(patient.identityType().toString())
//                .nationalIdNumber(patient.nationalIdNumber())
//                .addresses(toAddressResponseList(patient.addresses()))
//                .insurances(toInsuranceResponseList(patient.insurances()))
//                .responsibleParties(toResponsiblePartyResponseList(patient.responsibleParties()))
//                .documents(toDocumentResponseList(patient.documents()))
//                .active(patient.active())
//                .build();
//    }
//
//    private List<AddressResponse> toAddressResponseList(List<AddressQueryModel> addresses) {
//
//        if (addresses == null || addresses.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return addresses.stream()
//                .filter(Objects::nonNull)
//                .map(this::toAddressResponse)
//                .collect(Collectors.toList());
//    }
//
//    private AddressResponse toAddressResponse(AddressQueryModel address) {
//
//        return AddressResponse.builder()
//                .street(address.street())
//                .city(address.city())
//                .state(address.state())
//                .country(address.country())
//                .primary(address.primary())
//                .build();
//    }
//
//    private List<InsuranceResponse> toInsuranceResponseList(List<InsuranceQueryModel> policies) {
//
//        if (policies == null || policies.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return policies.stream()
//                .filter(Objects::nonNull)
//                .map(this::toInsuranceResponse)
//                .collect(Collectors.toList());
//    }
//
//    private InsuranceResponse toInsuranceResponse(InsuranceQueryModel policy) {
//
//        return InsuranceResponse.builder()
//                .insuranceId(policy.insuranceId())
//                .providerName(policy.providerName())
//                .policyNumber(policy.policyNumber())
//                .startDate(policy.coverageStart())
//                .endDate(policy.coverageEnd())
//                .active(policy.isActive())
//                .main(policy.isMain())
//                .build();
//    }
//
//    private List<ResponsiblePartyResponse> toResponsiblePartyResponseList(
//            List<ResponsiblePartyQueryModel> parties
//    ) {
//
//        if (parties == null || parties.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return parties.stream()
//                .filter(Objects::nonNull)
//                .map(this::toResponsiblePartyResponse)
//                .collect(Collectors.toList());
//    }
//
//    private ResponsiblePartyResponse toResponsiblePartyResponse(
//            ResponsiblePartyQueryModel party
//    ) {
//
//        return ResponsiblePartyResponse.builder()
//                .partyId(party.partyId())
//                .firstName(party.firstName())
//                .relationship(party.relationship())
//                .contactNumber(party.contactNumber())
//                .email(party.email())
//                .type(party.type())
//                .build();
//    }
//
//    private List<DocumentResponse> toDocumentResponseList(
//            List<DocumentQueryModel> documents
//    ) {
//
//        if (documents == null || documents.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return documents.stream()
//                .filter(Objects::nonNull)
//                .map(this::toDocumentResponse)
//                .collect(Collectors.toList());
//    }
//
//    private DocumentResponse toDocumentResponse(DocumentQueryModel document) {
//
//        return DocumentResponse.builder()
//                .documentId(document.documentId())
//                .documentType(document.documentType())
//                .fileName(document.fileName())
//                .uploadedAt(document.uploadedAt())
//                .build();
//    }
//}