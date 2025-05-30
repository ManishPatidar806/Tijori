package com.financialapplication.expansesanalysis.Util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Security {


    private final JwtFilter jwtFilter;

    public Security(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest -> httpRequest.
                        requestMatchers("/api/users/signin", "/api/users/signup",
                                "/swagger-ui/index.html"  ,"/v3/api-docs/**",
                                "/swagger-ui/**", "/api/emailVerification/sendOtp", "/api/emailVerification/verifyOtp").
                        permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();


    }

}