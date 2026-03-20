package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.exception.*;
import com.healthcore.patientservice.domain.model.enums.*;
import com.healthcore.patientservice.domain.model.vo.*;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
 * Patient Aggregate Root
 */
@ToString
public class Patient {

    /* ================= IDENTIFIERS ================= */

    @Getter
    private final PatientId id;

    @Getter
    private final TenantId tenantId;

    @Getter
    private final HospitalPatientNumber hospitalPatientNumber;

    @Getter
    private final PersonName name;

    @Getter
    private EmailAddress email;

    @Getter
    private PhoneNumber phoneNumber;

    @Getter
    private final DateOfBirth dateOfBirth;

    @Getter
    private final Gender gender;


    /* ================= DEMOGRAPHICS ================= */

    @Getter
    private MaritalStatus maritalStatus;

    @Getter
    private String nationality;

    @Getter
    private String occupation;

    @Getter
    private Religion religion;


    /* ================= CLINICAL PROFILE ================= */

    @Getter
    private BloodGroup bloodGroup;

    @Getter
    private Genotype genotype;

    @Getter
    private DisabilityStatus disabilityStatus;


    /* ================= NATIONAL ID ================= */

    @Getter
    private NationalIdentity nationalIdentity;


    /* ================= DOMAIN COMPONENTS ================= */

    private final ResponsiblePartyRegistry responsibleParties = new ResponsiblePartyRegistry();

    private final AddressBook addresses = new AddressBook();

    private final InsurancePortfolio insurancePolicies = new InsurancePortfolio();


    /* ================= DOCUMENTS ================= */

    private final List<PatientDocument> documents = new ArrayList<>();


    /* ================= STATUS ================= */

    @Getter
    private PatientStatus status = PatientStatus.ACTIVE;


    /* ================= CONSTRUCTOR ================= */

    private Patient(
            PatientId id,
            TenantId tenantId,
            HospitalPatientNumber hospitalPatientNumber,
            PersonName name,
            DateOfBirth dateOfBirth,
            Gender gender,
            PhoneNumber phoneNumber
    ) {
        this.id = Objects.requireNonNull(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.hospitalPatientNumber = Objects.requireNonNull(hospitalPatientNumber);
        this.name = Objects.requireNonNull(name);
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth);
        this.gender = Objects.requireNonNull(gender);
        this.phoneNumber = Objects.requireNonNull(phoneNumber);
    }


    /* ================= FACTORY ================= */

    public static Patient register(
            TenantId tenantId,
            HospitalPatientNumber hospitalPatientNumber,
            PersonName name,
            DateOfBirth dateOfBirth,
            Gender gender,
            PhoneNumber phoneNumber,
            List<ResponsibleParty> parties
    ) {

        Patient patient = new Patient(
                PatientId.newId(),
                tenantId,
                hospitalPatientNumber,
                name,
                dateOfBirth,
                gender,
                phoneNumber
        );

        if (parties != null) {
            parties.forEach(patient.responsibleParties::add);
        }

        patient.validateInvariants();

        return patient;
    }


    public static Patient reconstitute(
            PatientId id,
            TenantId tenantId,
            HospitalPatientNumber hospitalPatientNumber,
            PersonName name,
            DateOfBirth dateOfBirth,
            Gender gender,
            PhoneNumber phoneNumber,
            PatientStatus status
    ) {

        Patient patient = new Patient(
                id,
                tenantId,
                hospitalPatientNumber,
                name,
                dateOfBirth,
                gender,
                phoneNumber
        );

        patient.status = status;

        return patient;
    }


    /* ================= DEMOGRAPHIC UPDATE ================= */

    public void updateDemographics(
            MaritalStatus maritalStatus,
            String nationality,
            String occupation,
            Religion religion
    ) {
        ensureActive();

        this.maritalStatus = maritalStatus;
        this.nationality = nationality;
        this.occupation = occupation;
        this.religion = religion;
    }


    /* ================= CLINICAL UPDATE ================= */

    public void updateClinicalProfile(
            BloodGroup bloodGroup,
            Genotype genotype,
            DisabilityStatus disabilityStatus
    ) {
        ensureActive();

        this.bloodGroup = bloodGroup;
        this.genotype = genotype;
        this.disabilityStatus = disabilityStatus;
    }


    /* ================= CONTACT ================= */

    public void updateEmail(EmailAddress newEmail) {
        ensureActive();
        this.email = Objects.requireNonNull(newEmail);
    }

    public void updatePhoneNumber(PhoneNumber newPhone) {
        ensureActive();
        this.phoneNumber = Objects.requireNonNull(newPhone);
    }

    public void updateContactInfo(PhoneNumber phone, EmailAddress email) {
        this.phoneNumber = phone;
        this.email = email;
    }


    /* ================= RESPONSIBLE PARTIES ================= */

    public void addResponsibleParty(ResponsibleParty party) {
        ensureActive();
        responsibleParties.add(party);
        validateInvariants();
    }

    public void updateResponsibleParty(ResponsibleParty party) {
        ensureActive();

        Objects.requireNonNull(party, "Responsible party cannot be null");

        // Ensure the party already exists
        if (!responsibleParties.contains(party.getType())) {
            throw new ResponsiblePartyNotFoundException(
                    "Responsible party of type " + party.getType() + " does not exist"
            );
        }

        responsibleParties.remove(party.getType());
        responsibleParties.add(party);

        validateInvariants();
    }

    public void removeResponsibleParty(ResponsiblePartyType type) {
        ensureActive();
        responsibleParties.remove(type);
        validateInvariants();
    }

    public Collection<ResponsibleParty> getResponsibleParties() {
        return responsibleParties.all();
    }


    /* ================= ADDRESSES ================= */

    public void addAddress(Address address) {
        ensureActive();
        addresses.add(address);
    }

    public void updateAddress(Address address) {
        ensureActive();
        Objects.requireNonNull(address, "Address cannot be null");

        // remove existing address of same location if present
        addresses.remove(address);

        // add the updated one
        addresses.add(address);

        // if the command marks it as primary
        if (address.isPrimary()) {
            addresses.setPrimary(address);
        }
    }


    public void removeAddress(Address address) {
        ensureActive();
        addresses.remove(address);
    }

    public void setPrimaryAddress(Address address) {
        ensureActive();
        addresses.setPrimary(address);
    }

    public List<Address> getAddresses() {
        return addresses.all();
    }


    /* ================= INSURANCE ================= */

    public void addInsurance(InsurancePolicy policy) {
        ensureActive();
        insurancePolicies.add(policy);
    }

    public void updateInsurance(InsurancePolicy policy) {

        ensureActive();

        Objects.requireNonNull(policy, "Insurance policy cannot be null");

        Optional<InsurancePolicy> existingPolicy = insurancePolicies.all()
                .stream()
                .filter(p -> p.getPolicyNumber()
                        .equals(policy.getPolicyNumber()))
                .findFirst();

        if (existingPolicy.isEmpty()) {
            throw new InsurancePolicyNotFoundException(
                    "Insurance policy not found: " + policy.getPolicyNumber()
            );
        }

        insurancePolicies.remove(existingPolicy.get());
        insurancePolicies.add(policy);
    }


    public void removeInsurance(InsurancePolicy policy) {
        insurancePolicies.remove(policy);
    }

    public Optional<InsurancePolicy> getMainInsurance() {
        return insurancePolicies.getMainPolicy();
    }

    public List<InsurancePolicy> getInsurancePolicies() {
        return insurancePolicies.all();
    }


    /* ================= DOCUMENTS ================= */

    public void addDocument(PatientDocument document) {
        ensureActive();
        documents.add(Objects.requireNonNull(document));
    }

    public List<PatientDocument> getDocuments() {
        return Collections.unmodifiableList(documents);
    }


    /* ================= NATIONAL ID ================= */

    public void assignNationalIdentity(NationalIdentity identity) {

        ensureActive();

        if (this.nationalIdentity != null) {
            throw new NationalIdentityAlreadyAssignedException("Identity already assigned");
        }

        this.nationalIdentity = Objects.requireNonNull(identity);
    }


    /* ================== BUSINESS RULE CHECKS ================== */

    /**
     * Checks if a patient is eligible for a clinical service
     * based on age and insurance coverage.
     */
    public boolean isEligibleForService(int minAge, int maxAge) {

        ensureActive();

        int age = dateOfBirth.calculateAge();

        return age >= minAge && age <= maxAge;
    }


    /* ================= LIFECYCLE ================= */

    public void updateStatus(PatientStatus newStatus) {
        if (newStatus == null || newStatus == status) return;

        switch (newStatus) {
            case ACTIVE -> reactivate();
            case INACTIVE -> deactivate();
            case DECEASED -> markDeceased();
            default -> throw new InvalidPatientStatusException("Unknown status: " + newStatus);
        }
    }

    public void deactivate() {
        if (status == PatientStatus.INACTIVE) return;

        status = PatientStatus.INACTIVE;
        insurancePolicies.deactivateAll();
    }

    public void reactivate() {

        if (status == PatientStatus.ACTIVE) return;

        validateInvariants();

        status = PatientStatus.ACTIVE;
        insurancePolicies.activateAll();
    }

    public void markDeceased() {
        status = PatientStatus.DECEASED;
        insurancePolicies.deactivateAll();
    }

    private void ensureActive() {
        if (status != PatientStatus.ACTIVE) {
            throw new InactivePatientOperationException("Patient is not status");
        }
    }


    /* ================= INVARIANTS ================= */

    private void validateInvariants() {

        if (responsibleParties.isEmpty()) {
            throw new ResponsiblePartyRequiredException(
                    "Patient must have at least one responsible party"
            );
        }

        boolean hasNextOfKin = responsibleParties.contains(ResponsiblePartyType.NEXT_OF_KIN);
        boolean hasGuarantor = responsibleParties.contains(ResponsiblePartyType.GUARANTOR);

        if (dateOfBirth.isMinor()) {

            if (!(hasNextOfKin || hasGuarantor)) {
                throw new MinorRequiresResponsiblePartyException(
                        "Minor must have either Next of Kin or Guarantor"
                );
            }

        } else {

            if (!hasNextOfKin) {
                throw new AdultRequiresNextOfKinException(
                        "Adult must have a Next of Kin"
                );
            }

        }
    }
}