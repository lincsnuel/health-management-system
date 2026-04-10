package com.healthcore.patientservice.application.query.service;

import com.healthcore.patientservice.application.query.model.PatientContact;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.query.PatientReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientLookupService {

    private final PatientReadRepository repository;

    public PatientContact findByPhone(String phone) {

        return repository.findByTenantIdAndPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
}