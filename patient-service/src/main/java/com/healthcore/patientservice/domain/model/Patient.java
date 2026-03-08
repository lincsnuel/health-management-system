package com.healthcore.patientservice.domain.model;

import com.healthcore.patientservice.domain.exception.*;
import com.healthcore.patientservice.domain.model.enums.*;
import com.healthcore.patientservice.domain.model.vo.*;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
 * Patient aggregate root
 */
@ToString
public class Patient {

    /* ================== IDENTIFIERS ================== */
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

    /* ================== CLINICAL PROFILE ================== */
    @Getter
    private BloodGroup bloodGroup;
    @Getter
    private Genotype genotype;

    /* ================== NATIONAL ID ================== */
    @Getter
    private NationalIdentity nationalIdentity;

    /* ================== RESPONSIBLE PARTIES ================== */
    private final List<ResponsibleParty> responsibleParties = new ArrayList<>();

    /* ================== ADDRESSES ================== */
    private final List<Address> addresses = new ArrayList<>();

    /* ================== INSURANCE ================== */
    private final List<InsurancePolicy> insurancePolicies = new ArrayList<>();

    /* ================== DOCUMENTS ================== */
    private final List<PatientDocument> documents = new ArrayList<>();

    /* ================== PATIENT STATUS ================== */
    @Getter
    private PatientStatus status = PatientStatus.ACTIVE;

    /* ================== CONSTRUCTOR ================== */
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

    /* ================== FACTORY ================== */
    public static Patient register(
            TenantId tenantId,
            HospitalPatientNumber hospitalPatientNumber,
            PersonName name,
            DateOfBirth dateOfBirth,
            Gender gender,
            PhoneNumber phoneNumber,
            List<ResponsibleParty> responsibleParties
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

        if (responsibleParties != null) {
            responsibleParties.forEach(patient::addResponsibleParty);
        }

        patient.ensureResponsiblePartyForMinor();
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

    /* ================== BUSINESS BEHAVIOURS ================== */
    public int calculateAge() {
        return dateOfBirth.calculateAge();
    }

    public boolean isMinor() {
        return dateOfBirth.isMinor();
    }

    public void updateEmail(EmailAddress newEmail) {
        ensureActive();
        this.email = Objects.requireNonNull(newEmail);
    }

    public void updatePhoneNumber(PhoneNumber newPhone) {
        ensureActive();
        this.phoneNumber = Objects.requireNonNull(newPhone);
    }

    public void updateClinicalProfile(BloodGroup bloodGroup, Genotype genotype) {
        ensureActive();
        this.bloodGroup = bloodGroup;
        this.genotype = genotype;
    }

    /* ================== RESPONSIBLE PARTIES ================== */
    public void addResponsibleParty(ResponsibleParty party) {
        ensureActive();
        Objects.requireNonNull(party, "Responsible party cannot be null");

        // Replace existing responsible party of same type
        responsibleParties.removeIf(p -> p.type() == party.type());

        responsibleParties.add(party);
    }

    public void removeResponsibleParty(ResponsiblePartyType type) {
        ensureActive();

        boolean exists = responsibleParties.stream()
                .anyMatch(p -> p.type() == type);

        if (!exists) return;

        ensureResponsiblePartyForMinor();

        responsibleParties.removeIf(p -> p.type() == type);
    }

    public List<ResponsibleParty> getResponsibleParties() {
        return Collections.unmodifiableList(responsibleParties);
    }

    private void ensureResponsiblePartyForMinor() {
        if (isMinor() && responsibleParties.size() <= 1) {
            throw new MinorRequiresResponsiblePartyException(
                    "Minor patient must have at least one responsible party"
            );
        }
    }

    /* ================== ADDRESSES ================== */
    public void addAddress(Address address) {
        ensureActive();
        Objects.requireNonNull(address);

        if (addresses.size() >= 3) {
            throw new AddressLimitExceededException("Maximum of 3 addresses allowed");
        }

        if (address.isPrimary()) {
            List<Address> updated = addresses.stream()
                    .map(Address::unsetPrimary)
                    .toList();
            addresses.clear();
            addresses.addAll(updated);
        } else if (addresses.isEmpty()) {
            address = address.setPrimary();
        }

        addresses.add(address);
    }

    public void removeAddress(Address address) {
        ensureActive();
        if (!addresses.contains(address)) throw new AddressNotFoundException("Address not found");
        if (address.isPrimary() && addresses.size() == 1) {
            throw new CannotRemovePrimaryAddressException(
                    "To remove this address, add another address and make it primary"
            );
        }
        addresses.remove(address);
    }

    public void setPrimaryAddress(Address address) {
        ensureActive();
        if (!addresses.contains(address)) throw new AddressNotFoundException("Address not found");

        List<Address> updated = addresses.stream().map(Address::unsetPrimary).toList();
        addresses.clear();
        addresses.addAll(updated);

        int index = addresses.indexOf(address.unsetPrimary());
        addresses.set(index, address.setPrimary());
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    /* ================== NATIONAL ID ================== */
    public void assignNationalIdentity(NationalIdentity identity) {
        ensureActive();
        if (this.nationalIdentity != null) {
            throw new NationalIdentityAlreadyAssignedException("Identity already assigned");
        }
        this.nationalIdentity = Objects.requireNonNull(identity);
    }

    /* ================== INSURANCE ================== */
    public void addInsurance(InsurancePolicy policy) {
        ensureActive();
        Objects.requireNonNull(policy, "Insurance cannot be null");

        if (!policy.isActive()) throw new InvalidInsurancePolicyException("Cannot add inactive insurance");

        if (policy.isMain()) {
            insurancePolicies.stream()
                    .filter(InsurancePolicy::isMain)
                    .forEach(InsurancePolicy::unmarkAsMain);
        }

        insurancePolicies.add(policy);
    }

    public void removeInsurance(InsurancePolicy policy) {
        if (!insurancePolicies.remove(policy)) return;

        if (policy.isMain() && !insurancePolicies.isEmpty()) {
            insurancePolicies.getFirst().markAsMain();
        }
    }

    public Optional<InsurancePolicy> getMainInsurance() {
        return insurancePolicies.stream()
                .filter(InsurancePolicy::isMain)
                .findFirst();
    }

    public boolean hasActiveInsurance() {
        return insurancePolicies.stream().anyMatch(InsurancePolicy::isActive);
    }

    public List<InsurancePolicy> getInsurancePolicies() {
        return Collections.unmodifiableList(insurancePolicies);
    }

    /* ================== DOCUMENTS ================== */
    public void addDocument(PatientDocument document) {
        ensureActive();
        documents.add(Objects.requireNonNull(document));
    }

    public List<PatientDocument> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /* ================== LIFECYCLE ================== */
    public void ensureActive() {
        if (status != PatientStatus.ACTIVE) {
            throw new InactivePatientOperationException("Patient is not active");
        }
    }

    public void deactivate() {
        if (status == PatientStatus.INACTIVE) return;
        status = PatientStatus.INACTIVE;
        insurancePolicies.forEach(InsurancePolicy::deactivate);
    }

    public void reactivate() {
        if (status == PatientStatus.ACTIVE) return;
        ensureResponsiblePartyForMinor();
        status = PatientStatus.ACTIVE;
        insurancePolicies.forEach(InsurancePolicy::activate);
    }

    public void markDeceased() {
        status = PatientStatus.DECEASED;
        insurancePolicies.forEach(InsurancePolicy::deactivate);
    }
}