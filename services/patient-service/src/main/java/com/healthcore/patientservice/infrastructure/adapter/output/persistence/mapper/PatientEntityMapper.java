package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.patient.*;
import com.healthcore.patientservice.domain.model.vo.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PatientEntityMapper {

    private final InsuranceEntityMapper insuranceEntityMapper;
    private final AddressEntityMapper addressEntityMapper;
    private final DocumentEntityMapper documentEntityMapper;
    private final ResponsiblePartyEntityMapper responsiblePartyEntityMapper;

    /* ================= DOMAIN -> ENTITY ================= */
    public PatientEntity toEntity(Patient patient) {
        if (patient == null) return null;

        // build base PatientEntity using builder
        PatientEntity.PatientEntityBuilder builder = PatientEntity.builder()
                .patientId(patient.getId().value())
                .tenantId(patient.getTenantId().value())
                .hospitalPatientNumber(patient.getHospitalPatientNumber().value())
                .firstName(patient.getName().firstName())
                .lastName(patient.getName().lastName())
                .email(patient.getEmail() != null ? patient.getEmail().value() : null)
                .contactNumber(patient.getPhoneNumber().value())
                .dateOfBirth(patient.getDateOfBirth().value())
                .gender(patient.getGender())
                .maritalStatus(patient.getMaritalStatus())
                .nationality(patient.getNationality())
                .occupation(patient.getOccupation())
                .bloodGroup(patient.getBloodGroup())
                .genotype(patient.getGenotype())
                .disabilityStatus(patient.getDisabilityStatus())
                .religion(patient.getReligion())
                .identityType(patient.getNationalIdentity() != null ? patient.getNationalIdentity().type() : null)
                .nationalIdNumber(patient.getNationalIdentity() != null ? patient.getNationalIdentity().number() : null)
                .status(patient.getStatus());

        // map addresses
        if (patient.getAddresses() != null) {
            Set<AddressEntity> addresses =
                    addressEntityMapper.toEntitySet(patient.getAddresses());
            builder.addresses(addresses);
            addresses.forEach(a -> a.assignToPatient(builder.build()));
        }

        // map responsible parties
        if (patient.getResponsibleParties() != null) {
            Set<ResponsiblePartyEntity> parties =
                    responsiblePartyEntityMapper.toEntitySet(
                            patient.getResponsibleParties().stream().toList()
                    );
            builder.responsibleParties(parties);
            parties.forEach(p -> p.assignPatient(builder.build()));
        }

        // map insurance policies
        if (patient.getInsurancePolicies() != null) {
            Set<PatientInsuranceEntity> policies =
                    insuranceEntityMapper.toEntitySet(patient.getInsurancePolicies());
            builder.insurancePolicies(policies);
            policies.forEach(i -> i.assignToPatient(builder.build()));
        }

        // map documents
        if (patient.getDocuments() != null) {
            Set<PatientDocumentEntity> documents = documentEntityMapper
                    .toEntitySet(patient.getDocuments());
            builder.documents(documents);
            documents.forEach(d -> d.assignPatient(builder.build()));
        }

        return builder.build();
    }

    /* ================= ENTITY -> DOMAIN ================= */
    public Patient toDomain(PatientEntity entity) {
        if (entity == null) return null;

        Patient patient = Patient.reconstitute(
                new PatientId(entity.getPatientId()),
                new TenantId(entity.getTenantId()),
                new HospitalPatientNumber(entity.getHospitalPatientNumber()),
                new PersonName(entity.getFirstName(), entity.getLastName()),
                new DateOfBirth(entity.getDateOfBirth()),
                entity.getGender(),
                new PhoneNumber(entity.getContactNumber()),
                entity.getStatus()
        );

        if (entity.getEmail() != null) patient.updateEmail(new EmailAddress(entity.getEmail()));

        patient.updateDemographics(
                entity.getMaritalStatus(),
                entity.getNationality(),
                entity.getOccupation(),
                entity.getReligion()
        );

        patient.updateClinicalProfile(
                entity.getBloodGroup(),
                entity.getGenotype(),
                entity.getDisabilityStatus()
        );

        if (entity.getAddresses() != null) {
            entity.getAddresses().forEach(a ->
                    patient.addAddress(addressEntityMapper.toDomain(a))
            );
        }

        if (entity.getResponsibleParties() != null) {
            entity.getResponsibleParties().forEach(r ->
                    patient.addResponsibleParty(responsiblePartyEntityMapper.toDomain(r)));
        }

        if (entity.getInsurancePolicies() != null) {
            entity.getInsurancePolicies().forEach(i ->
                    patient.addInsurance(insuranceEntityMapper.toDomain(i)));
        }

        if (entity.getDocuments() != null) {
            entity.getDocuments().forEach(d ->
                    patient.addDocument(documentEntityMapper.toDomain(d)));
        }

        if (entity.getIdentityType() != null) {
            patient.assignNationalIdentity(new NationalIdentity(entity.getIdentityType(), entity.getNationalIdNumber()));
        }

        return patient;
    }
}