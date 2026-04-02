package com.healthcore.apigateway.config;

import com.healthcore.contracts.tenant.TenantServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public TenantServiceGrpc.TenantServiceFutureStub tenantServiceFutureStub(GrpcChannelFactory channels) {
        // "tenant-service" matches the name in your application.yml
        return TenantServiceGrpc.newFutureStub(channels.createChannel("tenant-service"));
    }
}