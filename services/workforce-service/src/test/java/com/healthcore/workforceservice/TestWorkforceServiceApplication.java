package com.healthcore.workforceservice;

import org.springframework.boot.SpringApplication;

public class TestWorkforceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(WorkforceServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
