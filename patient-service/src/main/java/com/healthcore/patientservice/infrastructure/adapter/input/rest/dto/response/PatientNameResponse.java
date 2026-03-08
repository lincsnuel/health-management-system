package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientNameResponse {

    private UUID patientId;

    private String hospitalPatientNumber;

    private String fullName;
}