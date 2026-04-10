package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.query;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientJpaReadRepository implements PatientReadRepository {

    private final EntityManager entityManager;

    /* =========================================================
       SEARCH PATIENTS BY NAME (FILTER-AWARE)
       ========================================================= */
    @Override
    public Page<PatientSummary> searchPatients(String p1, String p2, Pageable pageable) {
        // TenantFilter automatically scopes entities to the current tenant
        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientSummary(
                p.patientId, p.hospitalPatientNumber, CONCAT(p.firstName,' ',p.lastName),
                p.gender, p.dateOfBirth, p.contactNumber, p.status
            )
            FROM PatientEntity p
            WHERE LOWER(p.firstName) LIKE :p1 OR LOWER(p.lastName) LIKE :p1
               OR LOWER(p.firstName) LIKE :p2 OR LOWER(p.lastName) LIKE :p2
        """ + buildOrderBy(pageable);

        TypedQuery<PatientSummary> query = entityManager.createQuery(jpql, PatientSummary.class)
                .setParameter("p1", p1)
                .setParameter("p2", p2)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        // Count query
        Long total = entityManager.createQuery(
                        "SELECT COUNT(p) FROM PatientEntity p WHERE LOWER(p.firstName) LIKE :p1 OR LOWER(p.lastName) LIKE :p1 OR LOWER(p.firstName) LIKE :p2 OR LOWER(p.lastName) LIKE :p2",
                        Long.class)
                .setParameter("p1", p1)
                .setParameter("p2", p2)
                .getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    /* =========================================================
       GET ALL PATIENTS (LIST PAGE)
       ========================================================= */
    @Override
    public Page<PatientListItem> findPatientsForTenant(Pageable pageable) {
        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientListItem(
                p.patientId, p.hospitalPatientNumber, p.firstName, p.lastName,
                p.gender, p.dateOfBirth, p.contactNumber, p.status
            )
            FROM PatientEntity p
        """ + buildOrderBy(pageable);

        List<PatientListItem> results = entityManager.createQuery(jpql, PatientListItem.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Long total = entityManager.createQuery("SELECT COUNT(p) FROM PatientEntity p", Long.class)
                .getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    /* =========================================================
       GET PATIENT DETAILS (FETCH JOIN TO AVOID N+1)
       ========================================================= */
    @Override
    public Optional<PatientDetails> findPatientDetails(UUID patientId) {

        String jpql = """
            SELECT DISTINCT p
            FROM PatientEntity p
            LEFT JOIN FETCH p.addresses
            LEFT JOIN FETCH p.responsibleParties
            LEFT JOIN FETCH p.insurancePolicies
            WHERE p.patientId = :patientId
        """;

        return entityManager.createQuery(jpql, PatientEntity.class)
                .setParameter("patientId", patientId)
                .getResultStream()
                .findFirst()
                .map(this::mapToDetails);
    }

    /* =========================================================
       CONTACT INFO & PHONE LOOKUPS
       ========================================================= */
    @Override
    public Optional<PatientContactInfo> findContactInfoByEmail(String email) {

        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientContactInfo(
                p.patientId, CONCAT(p.firstName,' ',p.lastName), p.email, p.contactNumber
            )
            FROM PatientEntity p
            WHERE p.email = :email
        """;

        return entityManager.createQuery(jpql, PatientContactInfo.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<PatientContact> findByTenantIdAndPhoneNumber(String phoneNumber) {

        String jpql = """
            SELECT new com.healthcore.patientservice.application.query.model.PatientContact(
                p.patientId, p.firstName, p.lastName, p.contactNumber, p.email
            )
            FROM PatientEntity p
            WHERE p.contactNumber = :phoneNumber
        """;

        return entityManager.createQuery(jpql, PatientContact.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultStream()
                .findFirst();
    }

    /* =========================================================
       ELIGIBLE PATIENTS (BY AGE)
       ========================================================= */
    @Override
    public List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge) {

        LocalDate minDate = LocalDate.now().minusYears(maxAge);
        LocalDate maxDate = LocalDate.now().minusYears(minAge);

        return entityManager.createQuery("""
            SELECT new com.healthcore.patientservice.application.query.model.EligiblePatientProjection(
                p.patientId, p.firstName, p.lastName, p.dateOfBirth
            )
            FROM PatientEntity p
            WHERE p.status = com.healthcore.patientservice.domain.model.enums.PatientStatus.ACTIVE
            AND p.dateOfBirth BETWEEN :minDate AND :maxDate
        """, EligiblePatientProjection.class)
                .setParameter("minDate", minDate)
                .setParameter("maxDate", maxDate)
                .getResultList();
    }

    /* =========================================================
       HELPER: DYNAMIC SORT BUILDER
       ========================================================= */
    private String buildOrderBy(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) return " ORDER BY p.createdAt DESC";

        StringBuilder sb = new StringBuilder(" ORDER BY ");
        pageable.getSort().forEach(order ->
                sb.append("p.").append(order.getProperty()).append(" ")
                        .append(order.getDirection().name()).append(", "));
        return sb.substring(0, sb.length() - 2);
    }

    /* =========================================================
       ENTITY -> DTO MAPPERS
       ========================================================= */
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
                entity.getResponsibleParties() == null ? List.of() :
                        entity.getResponsibleParties().stream().map(this::mapResponsibleParty).toList(),
                entity.getAddresses() == null ? List.of() :
                        entity.getAddresses().stream().map(this::mapAddress).toList(),
                entity.getInsurancePolicies() == null ? List.of() :
                        entity.getInsurancePolicies().stream().map(this::mapInsurance).toList(),
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
                entity.getType(),
                entity.getAddress() != null ? mapAddress(entity.getAddress()) : null
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
}