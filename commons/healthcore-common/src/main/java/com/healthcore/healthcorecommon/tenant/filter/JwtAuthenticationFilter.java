//package com.healthcore.healthcorecommon.tenant.filter;
//
//import com.healthcore.healthcorecommon.tenant.context.TenantContext;
//import com.healthcore.healthcorecommon.tenant.context.UserContext;
//import com.healthcore.healthcorecommon.tenant.jwt.HealthcorePrincipal;
//import com.healthcore.healthcorecommon.tenant.jwt.JwtClaimsExtractor;
//import com.healthcore.healthcorecommon.tenant.jwt.JwtValidator;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.jspecify.annotations.NonNull;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtValidator validator;
//    private final JwtClaimsExtractor extractor;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//
//        try {
//            String authHeader = request.getHeader("Authorization");
//
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//                String token = authHeader.substring(7);
//
//                Jwt jwt = validator.validate(token);
//                HealthcorePrincipal principal = extractor.extract(jwt);
//
//                // Store in contexts
//                TenantContext.setTenantId(principal.getTenantId());
//                UserContext.setUser(principal);
//            }
//
//            // if no Authorization header, just skip — let Spring Security handle permitAll
//            filterChain.doFilter(request, response);
//
//        } finally {
//            UserContext.clear();
//            TenantContext.clear();
//        }
//    }
//}