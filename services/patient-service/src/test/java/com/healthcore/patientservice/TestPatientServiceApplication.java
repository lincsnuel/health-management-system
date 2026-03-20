package com.healthcore.patientservice;

import org.springframework.boot.SpringApplication;


public class TestPatientServiceApplication {



    public static void main(String[] args) {
        SpringApplication
                .from(PatientServiceApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}