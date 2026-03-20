package com.healthcore.patientservice.application.command.model;

public record RegisterAddressCommand(

        String street,
        String city,
        String state,
        String country,
        boolean primary

) {}