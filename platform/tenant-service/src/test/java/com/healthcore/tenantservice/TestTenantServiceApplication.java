package com.healthcore.tenantservice;

import org.springframework.boot.SpringApplication;

public class TestTenantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(TenantServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
