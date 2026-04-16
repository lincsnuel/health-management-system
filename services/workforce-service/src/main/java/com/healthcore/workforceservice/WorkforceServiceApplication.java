package com.healthcore.workforceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.healthcore.workforceservice",    // Scan the current service
        "com.healthcore.healthcorecommon"   // Scan the common library for Interceptors/Filters
})
public class WorkforceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkforceServiceApplication.class, args);
    }

}
