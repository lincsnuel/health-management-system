package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.query;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientJpaReadRepository implements PatientReadRepository {

    private final EntityManager entityManager;

    @Override
    public Page<PatientSummary> searchPatients(
            String p1,
            String p2,
            String tenantId,
            Pageable pageable
    ) {

        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientSummary(
                p.patientId,
                p.hospitalPatientNumber,
                CONCAT(p.firstName,' ',p.lastName),
                p.gender,
                p.dateOfBirth,
                p.contactNumber,
                p.status
            )
            FROM PatientEntity p
            WHERE p.tenantId = :tenantId
              AND (
                (LOWER(p.firstName) LIKE :p1 AND LOWER(p.lastName) LIKE :p2)
                OR
                (LOWER(p.firstName) LIKE :p2 AND LOWER(p.lastName) LIKE :p1)
                OR
                LOWER(p.firstName) LIKE :p1
                OR
                LOWER(p.lastName) LIKE :p1
              )
        """;

        TypedQuery<PatientSummary> query =
                entityManager.createQuery(jpql, PatientSummary.class);

        query.setParameter("tenantId", tenantId);
        query.setParameter("p1", p1);
        query.setParameter("p2", p2);

        List<PatientSummary> results = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Page<PatientListItem> findPatientsForTenant(
            String tenantId,
            Pageable pageable
    ) {

        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientListItem(
                p.patientId,
                p.hospitalPatientNumber,
                p.firstName,
                p.lastName,
                p.gender,
                p.dateOfBirth,
                p.contactNumber,
                p.status
            )
            FROM PatientEntity p
            WHERE p.tenantId = :tenantId
        """;

        TypedQuery<PatientListItem> query =
                entityManager.createQuery(jpql, PatientListItem.class);

        query.setParameter("tenantId", tenantId);

        List<PatientListItem> results = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(results, pageable, results.size());
    }

    @Override
    public Optional<PatientDetails> findPatientDetails(
            UUID patientId,
            String tenantId
    ) {

        String jpql = """
            SELECT p
            FROM PatientEntity p
            WHERE p.patientId = :patientId
            AND p.tenantId = :tenantId
        """;

        return entityManager
                .createQuery(jpql, PatientEntity.class)
                .setParameter("patientId", patientId)
                .setParameter("tenantId", tenantId)
                .getResultStream()
                .findFirst()
                .map(this::mapToDetails);
    }

    @Override
    public Optional<PatientContactInfo> findContactInfoByEmail(
            String email,
            String tenantId
    ) {

        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientContactInfo(
                p.patientId,
                CONCAT(p.firstName,' ',p.lastName),
                p.email,
                p.contactNumber
            )
            FROM PatientEntity p
            WHERE p.email = :email
            AND p.tenantId = :tenantId
        """;

        return entityManager
                .createQuery(jpql, PatientContactInfo.class)
                .setParameter("email", email)
                .setParameter("tenantId", tenantId)
                .getResultStream()
                .findFirst();
    }

    private PatientDetails mapToDetails(PatientEntity entity) {

        return new PatientDetails(
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

                entity.getResponsibleParties() == null
                        ? List.of()
                        : entity.getResponsibleParties()
                        .stream()
                        .map(this::mapResponsibleParty)
                        .toList(),

                entity.getAddresses() == null
                        ? List.of()
                        : entity.getAddresses()
                        .stream()
                        .map(this::mapAddress)
                        .toList(),

                entity.getInsurancePolicies() == null
                        ? List.of()
                        : entity.getInsurancePolicies()
                        .stream()
                        .map(this::mapInsurance).toList(),

                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private ResponsiblePartyDetail mapResponsibleParty(ResponsiblePartyEntity entity) {

        return new ResponsiblePartyDetail(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getContactNumber(),
                entity.getRelationship(),
                entity.getType(), // ResponsiblePartyType enum from domain
                entity.getAddress() != null
                        ? mapAddress(entity.getAddress())
                        : null
        );
    }

    private AddressDetail mapAddress(AddressEntity entity) {

        return new AddressDetail(
                entity.getAddressId(),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                entity.isPrimaryAddress()
        );
    }

    private AddressDetail mapAddress(ResponsiblePartyAddressEntity entity) {

        return new AddressDetail(
                entity.getId(),
                entity.getStreet(),
                entity.getCity(),
                entity.getState(),
                entity.getCountry(),
                true
        );
    }

    private InsuranceDetail mapInsurance(PatientInsuranceEntity entity) {

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

    @Override
    public List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge) {

        return entityManager.createQuery("""
            SELECT new com.healthcore.patientservice.application.query.model.EligiblePatientProjection(
                p.id,
                p.firstName,
                p.lastName,
                p.dateOfBirth
            )
            FROM PatientEntity p
            WHERE p.status = com.healthcore.patientservice.domain.model.enums.PatientStatus.ACTIVE
            AND FUNCTION('TIMESTAMPDIFF', 'YEAR', p.dateOfBirth, CURRENT_DATE) BETWEEN :minAge AND :maxAge
        """, EligiblePatientProjection.class)
                .setParameter("minAge", minAge)
                .setParameter("maxAge", maxAge)
                .getResultList();
    }
}