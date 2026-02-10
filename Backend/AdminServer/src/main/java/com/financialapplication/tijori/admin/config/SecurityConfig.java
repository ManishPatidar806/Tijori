package com.financialapplication.tijori.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminServerProperties adminServerProperties;

    public SecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminServerProperties = adminServerProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        SavedRequestAwareAuthenticationSuccessHandler successHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();

        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminServerProperties.path("/"));

        http

                // Authorization
                .authorizeHttpRequests(auth -> auth

                        // Public URLs
                        .requestMatchers(
                                adminServerProperties.path("/assets/**"),
                                adminServerProperties.path("/login"),
                                adminServerProperties.path("/logout"),
                                adminServerProperties.path("/actuator/info"),
                                adminServerProperties.path("/actuator/health")
                        ).permitAll()

                        // Method-specific
                        .requestMatchers(
                                HttpMethod.POST,
                                adminServerProperties.path("/instances")
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                adminServerProperties.path("/instances/*")
                        ).permitAll()

                        // All other requests
                        .anyRequest().authenticated()
                )

                // Login
                .formLogin(form -> form
                        .loginPage(adminServerProperties.path("/login"))
                        .successHandler(successHandler)
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutUrl(adminServerProperties.path("/logout"))
                        .logoutSuccessUrl(adminServerProperties.path("/login"))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                        .clearAuthentication(true)
                        .permitAll()
                )

                // CSRF
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )

                        // Ignore CSRF
                        .ignoringRequestMatchers(
                                adminServerProperties.path("/instances"),
                                adminServerProperties.path("/instances/*"),
                                adminServerProperties.path("/actuator/**"),
                                adminServerProperties.path("/logout")
                        )
                );

        return http.build();
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In-Memory Users
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(
            PasswordEncoder passwordEncoder
    ) {

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
