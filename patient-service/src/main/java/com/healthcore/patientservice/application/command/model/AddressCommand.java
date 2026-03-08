package com.healthcore.patientservice.application.command.model;

public record AddressCommand(

        String street,
        String city,
        String state,
        String country,
        boolean primary

) {}