package com.gestioninventario.backend.infrastructure.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private static final String ADMINISTRADOR = "ADMINISTRADOR";
    private static final String SUPERVISOR = "SUPERVISOR";
    private static final String OPERADOR = "OPERADOR";
    private static final String CATEGORIES_PATH = "/api/categories/**";
    private static final String SUPPLIERS_PATH = "/api/suppliers/**";
    private static final String PRODUCTS_PATH = "/api/products/**";
    private static final String MOVEMENTS_PATH = "/api/movements/**";

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**")
                .permitAll()

                .requestMatchers("/api/users/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers("/api/roles/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers("/api/permissions/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers(GET, CATEGORIES_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR,
                    OPERADOR
                )

                .requestMatchers(POST, CATEGORIES_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PUT, CATEGORIES_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PATCH, CATEGORIES_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(GET, SUPPLIERS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR,
                    OPERADOR
                )

                .requestMatchers(POST, SUPPLIERS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PUT, SUPPLIERS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PATCH, SUPPLIERS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(GET, PRODUCTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR,
                    OPERADOR
                )

                .requestMatchers(POST, PRODUCTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PUT, PRODUCTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(PATCH, PRODUCTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR
                )

                .requestMatchers(GET, MOVEMENTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    SUPERVISOR,
                    OPERADOR
                )

                .requestMatchers(POST, MOVEMENTS_PATH)
                .hasAnyRole(
                    ADMINISTRADOR,
                    OPERADOR
                )

                .requestMatchers(PUT, MOVEMENTS_PATH)
                .hasRole(ADMINISTRADOR)

                .requestMatchers(PATCH, MOVEMENTS_PATH)
                .hasRole(ADMINISTRADOR)

                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .build();
    }
}