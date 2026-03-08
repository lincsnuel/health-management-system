//package com.healthcore.patientservice.application.service;
//
//import com.healthcore.patientservice.application.command.model.UpdatePatientCommand;
//import com.healthcore.patientservice.application.usecase.UpdatePatientUseCase;
//import com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request.UpdatePatientRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class UpdatePatientService implements UpdatePatientUseCase {
//    @Override
//    public void updatePatient(Long patientId, UpdatePatientRequest request) {
//        UpdatePatientCommand command = new UpdatePatientCommand(
//                request.tenantId(),
//                patientId,
//                request.email(),
//                request.contactNumber(),
//                request.bloodGroup(),
//                request.genotype(),
//                request.religion(),
//                request.identityType(),
//                request.nationalIdNumber(),
//                request.responsiblePartyRequest() != null ? requestMapper.toResponsiblePartyCommand(request.nextOfKin()) : null,
//                null, // guarantor for now
//                null  // insurance policy if optional
//        );
//    }
//}
