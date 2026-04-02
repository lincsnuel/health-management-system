package com.healthcore.authservice.config;

import com.healthcore.contracts.patient.PatientServiceGrpc;
import com.healthcore.contracts.workforce.WorkforceServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    /**
     * The GrpcChannelFactory automatically reads configuration from your
     * application.yml (under spring.grpc.client.patient-service.*)
     * to build the underlying ManagedChannel.
     */

    @Bean
    public PatientServiceGrpc.PatientServiceFutureStub patientServiceFutureStub(GrpcChannelFactory channels) {
        // Creates an asynchronous stub
        return PatientServiceGrpc.newFutureStub(channels.createChannel("patient-service"));
    }

    @Bean
    public PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channels) {
        // Creates a synchronous stub (usually easier for standard REST-to-gRPC bridging)
        return PatientServiceGrpc.newBlockingStub(channels.createChannel("patient-service"));
    }

    @Bean
    public WorkforceServiceGrpc.WorkforceServiceFutureStub workforceServiceFutureStub(GrpcChannelFactory channels) {
        // Creates an asynchronous stub
        return WorkforceServiceGrpc.newFutureStub(channels.createChannel("workforce-service"));
    }

    @Bean
    public WorkforceServiceGrpc.WorkforceServiceBlockingStub workforceServiceBlockingStub(GrpcChannelFactory channels) {
        // Creates a synchronous stub (usually easier for standard REST-to-gRPC bridging)
        return WorkforceServiceGrpc.newBlockingStub(channels.createChannel("workforce-service"));
    }
}