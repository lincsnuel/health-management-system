package com.healthcore.patientservice.infrastructure.config;

import com.healthcore.healthcorecommon.tenant.grpc.GrpcRequestContextInterceptor;
import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.GlobalServerInterceptor;

@Configuration
public class GrpcConfig {

    /**
     * By annotating the ServerInterceptor bean with @GlobalServerInterceptor,
     * Spring gRPC automatically registers it to all services.
     */
    @Bean
    @GlobalServerInterceptor
    public ServerInterceptor grpcRequestContextInterceptor() {
        return new GrpcRequestContextInterceptor();
    }
}
