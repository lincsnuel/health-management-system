package com.healthcore.patientservice.infrastructure.adapter.output.persistence;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.application.query.repository.PatientQueryRepository;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.AddressEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientInsuranceEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.ResponsiblePartyAddressEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.ResponsiblePartyEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.PatientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientJpaQueryAdapter implements PatientQueryRepository {

    private final PatientEntityRepository repository;

    /* =========================
       SEARCH BY NAME (FIRST & LAST)
       ========================= */
    @Override
    public Page<PatientSummary> searchByName(
            String p1,
            String p2,
            String tenantId,
            Pageable pageable
    ) {
        // Repository should return Page<PatientSummary> via JPQL constructor expression
        return repository.searchPatients(p1, p2, tenantId, pageable);
    }

    /* =========================
       LIST PATIENTS FOR TENANT (SUMMARY / LIST VIEW)
       ========================= */
    @Override
    public Page<PatientListItem> findByIdAndTenantId(String tenantId, Pageable pageable) {
        // TODO: Implement repository method that returns Page<PatientListItem>
        // e.g., repository.findAllByTenant(tenantId, pageable)
        return repository.findAllByTenant(tenantId, pageable);
    }

    /* =========================
       GET PATIENT DETAIL BY ID
       ========================= */
    public Optional<PatientDetails> findPatientById(UUID patientId, String tenantId) {
        return repository.findPatientById(patientId, tenantId)
                .map(entity -> new PatientDetails(
                        entity.getPatientId(),
                        entity.getHospitalPatientNumber(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getDateOfBirth(),
                        entity.getGender(),
                        entity.getContactNumber(),
                        entity.getEmail(),
                        entity.getBloodGroup(),
                        entity.getGenotype(),
                        entity.getReligion(),
                        entity.getIdentityType(),
                        entity.getNationalIdNumber(),
                        entity.getResponsibleParties()
                                .stream()
                                .map(this::mapToResponsibleParty)
                                .toList(),
                        entity.getAddresses().stream().map(this::mapToAddress).toList(),
                        entity.getInsurancePolicies().stream().map(this::mapToInsurance).toList(),
                        entity.getStatus(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                ));
    }

    /* =========================
       GET PATIENT CONTACT INFO BY EMAIL
       ========================= */
    @Override
    public Optional<PatientContactInfo> findContactInfoByEmail(String email, String tenantId) {
        // TODO: Implement JPQL constructor expression returning PatientContactInfo
        return repository.findContactInfoByEmail(email, tenantId);
    }

    /* =========================
       CHECK IF PATIENT EXISTS BY EMAIL
       ========================= */
    @Override
    public boolean existsByEmail(String email, String tenantId) {
        return repository.existsByTenantIdAndEmail(email, tenantId);
    }

    // Mapping method for Addresses
    private AddressDetail mapToAddress(AddressEntity entity) {
        return new AddressDetail(
                entity.getAddressId(),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.isPrimaryAddress()
        );
    }

    // Mapping method for Insurances
    private InsuranceDetail mapToInsurance(PatientInsuranceEntity entity) {
        return new InsuranceDetail(
                entity.getInsuranceId(),
                entity.getProviderName(),
                entity.getPolicyNumber(),
                entity.getPlanType(),
                entity.getCoverageStart(),
                entity.getCoverageEnd(),
                entity.isMain(),
                entity.isActive()
        );
    }

    private ResponsiblePartyDetail mapToResponsibleParty(ResponsiblePartyEntity entity) {

        return new ResponsiblePartyDetail(
                entity.getFirstName(),
                entity.getLastName(),
                entity.getContactNumber(),
                entity.getRelationship(),
                entity.getType(),
                mapToResponsiblePartyAddress(entity.getAddress())
        );
    }

    private AddressDetail mapToResponsiblePartyAddress(ResponsiblePartyAddressEntity entity) {

        if (entity == null) return null;

        return new AddressDetail(
                entity.getId(),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                true
        );
    }
}