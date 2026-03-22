package com.healthcore.healthcorecommon.tenant.config;

import com.healthcore.healthcorecommon.tenant.filter.JwtAuthenticationFilter;
import com.healthcore.healthcorecommon.tenant.jwt.JwtClaimsExtractor;
import com.healthcore.healthcorecommon.tenant.jwt.JwtValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public JwtValidator jwtValidator(JwtDecoder jwtDecoder) {
        return new JwtValidator(jwtDecoder); // or inject dependencies if needed
    }

    @Bean
    public JwtClaimsExtractor jwtClaimsExtractor() {
        return new JwtClaimsExtractor();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtValidator validator,
            JwtClaimsExtractor extractor) {
        return new JwtAuthenticationFilter(validator, extractor);
    }
}