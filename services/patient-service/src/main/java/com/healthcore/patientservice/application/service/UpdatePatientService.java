//package com.healthcore.patientservice.application.service;
//
//import com.healthcore.patientservice.application.command.model.UpdatePatientCommand;
//import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
//import com.healthcore.patientservice.application.loader.PatientLoader;
//import com.healthcore.patientservice.application.usecase.UpdatePatientUseCase;
//import com.healthcore.patientservice.domain.model.patient.InsurancePolicy;
//import com.healthcore.patientservice.domain.model.patient.Patient;
//import com.healthcore.patientservice.domain.model.vo.*;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.mapper.PatientCommandRestMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class UpdatePatientService implements UpdatePatientUseCase {
//
//    private final PatientCommandRestMapper requestMapper;
//    private final PatientCommandRepository patientRepository;
//    private final PatientLoader patientLoader;
//
//    @Override
//    public void updatePatient(UUID patientId, UpdatePatientCommand command) {
//
//        // Load aggregate
//        Patient patient = patientLoader.load(command.patientId(), command.tenantId());
//
//        // Apply optional simple updates
//        command.emailOpt().ifPresent(email -> patient.updateEmail(EmailAddress.of(email)));
//        command.contactNumberOpt().ifPresent(phone -> patient.updatePhoneNumber(PhoneNumber.of(phone)));
//        command.bloodGroupOpt().ifPresent(blood -> patient.updateClinicalProfile(blood, command.genotype()));
//        command.genotypeOpt().ifPresent(geno -> patient.updateClinicalProfile(command.bloodGroup(), geno));
//        command.religionOpt().ifPresent(patient::updateReligion);
//
//        // Update national identity if present
//        if (command.identityTypeOpt().isPresent() && command.nationalIdNumberOpt().isPresent()) {
//            NationalIdentity identity = NationalIdentity.of(
//                    command.identityTypeOpt().get(),
//                    command.nationalIdNumberOpt().get()
//            );
//            if (patient.getNationalIdentity() == null) {
//                patient.assignNationalIdentity(identity);
//            }
//        }
//
//        // Update responsible parties if present
//        command.responsiblePartiesOpt().ifPresent(list -> {
//            List<ResponsibleParty> parties = list.stream()
//                    .map(requestMapper::toResponsibleParty)
//                    .collect(Collectors.toList());
//            patient.addResponsibleParty(parties);
//        });
//
//        // Update addresses if present
//        command.addressesOpt().ifPresent(list -> {
//            List<Address> addresses = list.stream()
//                    .map(requestMapper::toAddress)
//                    .collect(Collectors.toList());
//            patient.updateAddresses(addresses);
//        });
//
//        // Update insurances if present
//        command.insurancesOpt().ifPresent(list -> {
//            List<InsurancePolicy> policies = list.stream()
//                    .map(requestMapper::toInsurancePolicy)
//                    .collect(Collectors.toList());
//            patient.updateInsurances(policies);
//        });
//
//        // Persist changes
//        patientRepository.save(patient);
//    }
//}