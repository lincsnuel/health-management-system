//package com.healthcore.patientservice.infrastructure.config;
//
//import com.healthcore.healthcorecommon.tenant.filter.TenantFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public FilterRegistrationBean<TenantFilter> tenantFilter(TenantFilter filter) {
//        FilterRegistrationBean<TenantFilter> reg = new FilterRegistrationBean<>();
//        reg.setFilter(filter);
//        reg.addUrlPatterns("/*");
//        reg.setOrder(1);
//        return reg;
//    }
//}