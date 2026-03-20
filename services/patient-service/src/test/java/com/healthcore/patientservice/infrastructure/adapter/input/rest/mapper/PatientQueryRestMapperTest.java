//package com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper;
//
//import com.healthcore.patientservice.domain.model.*;
//import com.healthcore.patientservice.domain.model.enums.BloodGroup;
//import com.healthcore.patientservice.domain.model.enums.Genotype;
//import com.healthcore.patientservice.domain.model.enums.IdentityType;
//import com.healthcore.patientservice.domain.model.enums.Gender;
//import com.healthcore.patientservice.domain.model.vo.*;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response.*;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//class PatientQueryRestMapperTest {
//
//    private final PatientQueryRestMapper mapper = new PatientQueryRestMapper();
//
//    /* ================== REGISTER RESPONSE ================== */
//    @Test
//    void should_map_patient_to_register_response() {
//        Patient patient = createSamplePatient();
//        RegisterPatientResponse response = mapper.toRegisterResponse(patient);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getPatientId()).isEqualTo(patient.getId().value());
//        assertThat(response.getHospitalPatientNumber()).isEqualTo(patient.getHospitalPatientNumber().value());
//        assertThat(response.getFullName()).isEqualTo(patient.getName().getFullName());
//        assertThat(response.isActive()).isEqualTo(patient.isActive());
//    }
//
//    @Test
//    void should_return_null_register_response_when_patient_is_null() {
//        assertThat(mapper.toRegisterResponse(null)).isNull();
//    }
//
//    /* ================== SUMMARY RESPONSE ================== */
//    @Test
//    void should_map_patient_to_summary_response() {
//        Patient patient = createSamplePatient();
//        PatientSummaryResponse response = mapper.toSummaryResponse(patient);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getPatientId()).isEqualTo(patient.getId().value());
//        assertThat(response.getFullName()).isEqualTo(patient.getName().getFullName());
//        assertThat(response.getAge()).isEqualTo(patient.calculateAge());
//        assertThat(response.getGender()).isEqualTo(patient.getGender().toString());
//        assertThat(response.getContactNumber()).isEqualTo(patient.getPhoneNumber().value());
//        assertThat(response.isActive()).isEqualTo(patient.isActive());
//    }
//
//    @Test
//    void should_return_empty_list_when_summary_response_list_is_null_or_empty() {
//        assertThat(mapper.toSummaryResponseList(null)).isEmpty();
//        assertThat(mapper.toSummaryResponseList(List.of())).isEmpty();
//    }
//
//    @Test
//    void should_map_patient_list_to_summary_response_list() {
//        Patient patient = createSamplePatient();
//        List<PatientSummaryResponse> responses = mapper.toSummaryResponseList(List.of(patient));
//
//        assertThat(responses).hasSize(1);
//        assertThat(responses.getFirst().getPatientId()).isEqualTo(patient.getId().value());
//    }
//
//    /* ================== DETAILS RESPONSE ================== */
//    @Test
//    void should_map_patient_to_details_response() {
//        Patient patient = createSamplePatient();
//        PatientDetailsResponse response = mapper.toDetailsResponse(patient);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getFullName()).isEqualTo(patient.getName().getFullName());
//        assertThat(response.getFullName()).isEqualTo(patient.getName().getFullName());
//        assertThat(response.getAddresses()).hasSize(patient.getAddresses().size());
//        assertThat(response.getInsurances()).hasSize(patient.getInsurancePolicies().size());
//        assertThat(response.getNextOfKin()).isNotNull();
//        assertThat(response.getNextOfKin().getFullName()).isEqualTo(patient.getNextOfKin().fullName());
//    }
//
//    @Test
//    void should_return_null_details_response_when_patient_is_null() {
//        assertThat(mapper.toDetailsResponse(null)).isNull();
//    }
//
//    /* ================== HELPERS ================== */
//    private Patient createSamplePatient() {
//        TenantId tenantId = TenantId.of("HOSP-00001");
//        HospitalPatientNumber hpn = HospitalPatientNumber.of("HP-001");
//        PersonName name = PersonName.of("John", "Doe");
//        DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 1, 1));
//        Gender gender = Gender.Male;
//        PhoneNumber phone = PhoneNumber.of("08012345678");
//
//        Patient patient = Patient.register(
//                tenantId, hpn, name, dob, gender, phone, createSampleNextOfKin()
//        );
//
//        patient.updateEmail(EmailAddress.of("john.doe@example.com"));
//        patient.updateClinicalProfile(BloodGroup.O_POSITIVE, Genotype.AA);
//
//        // National ID
//        patient.assignNationalIdentity(new NationalIdentity(IdentityType.NIN, "11111111111"));
//
//        // Address
//        Address address = Address.of("12 Allen Ave", "Ikeja", "Lagos", "Nigeria", true);
//        patient.addAddress(address);
//
//        // Next of kin
//        patient.assignNextOfKin(NextOfKin.of("John Okafor", "Father", PhoneNumber.of("08131321290"), "address"));
//
//        // Insurance
//        InsurancePolicy insurance = new InsurancePolicy(
//                InsurancePolicyId.newId(),
//                "AXA Mansard",
//                "POL-001",
//                "Premium",
//                LocalDate.now().minusDays(1),
//                LocalDate.now().plusDays(30),
//                true,
//                true
//        );
//        patient.addInsurance(insurance);
//
//        return patient;
//    }
//
//    private NextOfKin createSampleNextOfKin() {
//        return NextOfKin.of(
//                "Jane Doe",
//                "Mother",
//                PhoneNumber.of("08099999999"),
//                "65 Address"
//        );
//    }
//}