package com.financialapplication.tijori.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;


// Spring Security configuration.

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Public endpoints that don't require authentication
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/users/signin",
            "/api/v1/users/signup",
            "/api/users/signin",
            "/api/users/signup",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/emailVerification/sendOtp",
            "/api/emailVerification/verifyOtp",
            "/actuator/**"  // Allow all actuator endpoints for Spring Boot Admin monitoring for testing purpose we can secure them as well
    };

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Security Headers
                    .headers(headers -> headers
                            .contentTypeOptions(contentType -> {})
                            .contentSecurityPolicy(csp->csp.policyDirectives("default-src 'self'"))
                            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                            .referrerPolicy(referrer -> referrer
                                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                            .permissionsPolicyHeader(permissions -> permissions
                                    .policy("geolocation=(), microphone=(), camera=()"))
                    )
                .exceptionHandling(ex ->ex.authenticationEntryPoint(
                        (request, response, authException) ->
                        {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        }
                        )

                )
                .authorizeHttpRequests(httpRequest -> httpRequest
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}