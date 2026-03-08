package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.*;
import com.healthcore.patientservice.domain.model.enums.Gender;
import com.healthcore.patientservice.domain.model.enums.PatientStatus;
import com.healthcore.patientservice.domain.model.vo.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;

import java.util.List;

public class PatientEntityMapper {

    /* ================== ENTITY -> DOMAIN ================== */
    public static Patient toDomain(PatientEntity entity) {
        if (entity == null) return null;

        PatientId patientId = new PatientId(entity.getPatientId());
        TenantId tenantId = new TenantId(entity.getTenantId());
        HospitalPatientNumber hospitalPatientNumber = new HospitalPatientNumber(entity.getHospitalPatientNumber());
        PersonName name = new PersonName(entity.getFirstName(), entity.getLastName());
        DateOfBirth dateOfBirth = new DateOfBirth(entity.getDateOfBirth());
        Gender gender = entity.getGender();
        PhoneNumber phoneNumber = new PhoneNumber(entity.getContactNumber());

        Patient patient = Patient.reconstitute(
                patientId,
                tenantId,
                hospitalPatientNumber,
                name,
                dateOfBirth,
                gender,
                phoneNumber,
                entity.getStatus()
        );

        // Optional fields
        if (entity.getEmail() != null) patient.updateEmail(new EmailAddress(entity.getEmail()));
        if (entity.getBloodGroup() != null || entity.getGenotype() != null)
            patient.updateClinicalProfile(entity.getBloodGroup(), entity.getGenotype());
        if (entity.getIdentityType() != null && entity.getNationalIdNumber() != null) {
            NationalIdentity nationalIdentity = new NationalIdentity(
                    entity.getIdentityType(),
                    entity.getNationalIdNumber()
            );
            patient.assignNationalIdentity(nationalIdentity);
        }

        // Addresses
        List<Address> addresses = entity.getAddresses().stream()
                .map(AddressMapper::toDomain)
                .toList();
        addresses.forEach(patient::addAddress);

        // Responsible Parties
        List<ResponsibleParty> parties = entity.getResponsibleParties().stream()
                .map(ResponsiblePartyMapper::toDomain)
                .toList();
        parties.forEach(patient::addResponsibleParty);

        // Insurance
        List<InsurancePolicy> policies = entity.getInsurancePolicies().stream()
                .map(InsurancePolicyMapper::toDomain)
                .toList();
        policies.forEach(patient::addInsurance);

        // Documents
        List<PatientDocument> documents = entity.getDocuments().stream()
                .map(PatientDocumentMapper::toDomain)
                .toList();
        documents.forEach(patient::addDocument);

        return patient;
    }

    /* ================== DOMAIN -> ENTITY ================== */
    public static PatientEntity toEntity(Patient domain) {
        if (domain == null) return null;

        PatientEntity patientEntity = PatientEntity.builder()
                .patientId(domain.getId().value())
                .tenantId(domain.getTenantId().value())
                .hospitalPatientNumber(domain.getHospitalPatientNumber().value())
                .firstName(domain.getName().firstName())
                .lastName(domain.getName().lastName())
                .dateOfBirth(domain.getDateOfBirth().value())
                .gender(domain.getGender())
                .contactNumber(domain.getPhoneNumber().value())
                .status(domain.getStatus())
                .email(domain.getEmail() != null ? domain.getEmail().value() : null)
                .bloodGroup(domain.getBloodGroup())
                .genotype(domain.getGenotype())
                .identityType(domain.getNationalIdentity() != null ? domain.getNationalIdentity().type() : null)
                .nationalIdNumber(domain.getNationalIdentity() != null ? domain.getNationalIdentity().number() : null)
                .build();

        // Relationships
        // Map and link Addresses
        if (domain.getAddresses() != null) {
            domain.getAddresses().forEach(address -> {
                AddressEntity ae = AddressMapper.toEntity(address);
                patientEntity.addAddress(ae);
            });
        }

        domain.getResponsibleParties().forEach(party -> {
            ResponsiblePartyEntity rpe = ResponsiblePartyMapper.toEntity(party);
            patientEntity.addResponsibleParty(rpe);
        });

        domain.getInsurancePolicies().forEach(policy -> {
            PatientInsuranceEntity pie = InsurancePolicyMapper.toEntity(policy);
            patientEntity.addInsurance(pie);
        });

        domain.getDocuments().forEach(doc -> {
            PatientDocumentEntity pde = PatientDocumentMapper.toEntity(doc);
            patientEntity.addDocument(pde);
        });

        return patientEntity;
    }
}