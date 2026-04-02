//package com.healthcore.patientservice.infrastructure.config;
//
//import com.healthcore.healthcorecommon.tenant.filter.JwtAuthenticationFilter;
//import com.healthcore.patientservice.infrastructure.security.PatientOnlyFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http,
//                                                   JwtAuthenticationFilter jwtFilter,
//                                                   PatientOnlyFilter patientOnlyFilter) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable) // Updated to modern lambda style
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/v1/patients/register").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // Position your JWT filter where the Bearer token logic lives
//                .addFilterBefore(jwtFilter, BearerTokenAuthenticationFilter.class)
//                .addFilterAfter(patientOnlyFilter, JwtAuthenticationFilter.class)
//
//                // Ensure you tell Spring this is a Resource Server
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
//                .build();
//    }
//}