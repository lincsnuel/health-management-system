//package com.healthcore.patientservice.infrastructure.security;
//
//import com.healthcore.healthcorecommon.tenant.context.UserContext;
//import com.healthcore.healthcorecommon.tenant.jwt.HealthcorePrincipal;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.jspecify.annotations.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class PatientOnlyFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(@NonNull HttpServletRequest request,
//                                    @NonNull HttpServletResponse response,
//                                    @NonNull FilterChain filterChain)
//            throws ServletException, IOException {
//
//        HealthcorePrincipal user = UserContext.getUser();
//
//        if (user != null && !"PATIENT".equals(user.getUserType())) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN,
//                    "Access denied: PATIENT only");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}