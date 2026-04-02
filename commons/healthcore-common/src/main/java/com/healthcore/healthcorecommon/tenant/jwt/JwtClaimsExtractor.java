//package com.healthcore.healthcorecommon.tenant.jwt;
//
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class JwtClaimsExtractor {
//
//    public HealthcorePrincipal extract(Jwt jwt) {
//
//        String userId = jwt.getSubject();
//        String tenantId = jwt.getClaim("tenantId");
//        String userType = jwt.getClaim("userType");
//
//        List<String> roles = jwt.getClaim("roles");
//
//        return new HealthcorePrincipal(
//                userId,
//                tenantId,
//                userType,
//                roles
//        );
//    }
//}