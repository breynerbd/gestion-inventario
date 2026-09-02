package com.gestioninventario.backend.infrastructure.security;

import static org.springframework.http.HttpMethod.DELETE;
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

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

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

                .requestMatchers("/api/usuarios/**")
                .hasRole("ADMINISTRADOR")

                .requestMatchers("/api/roles/**")
                .hasRole("ADMINISTRADOR")

                .requestMatchers("/api/permisos/**")
                .hasRole("ADMINISTRADOR")

                .requestMatchers(GET, "/api/categorias/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR",
                    "OPERADOR"
                )

                .requestMatchers(POST, "/api/categorias/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PUT, "/api/categorias/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PATCH, "/api/categorias/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(DELETE, "/api/categorias/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(GET, "/api/proveedores/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR",
                    "OPERADOR"
                )

                .requestMatchers(POST, "/api/proveedores/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PUT, "/api/proveedores/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PATCH, "/api/proveedores/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(DELETE, "/api/proveedores/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(GET, "/api/productos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR",
                    "OPERADOR"
                )

                .requestMatchers(POST, "/api/productos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PUT, "/api/productos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(PATCH, "/api/productos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(DELETE, "/api/productos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR"
                )

                .requestMatchers(GET, "/api/movimientos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "SUPERVISOR",
                    "OPERADOR"
                )

                .requestMatchers(POST, "/api/movimientos/**")
                .hasAnyRole(
                    "ADMINISTRADOR",
                    "OPERADOR"
                )

                .requestMatchers(PUT, "/api/movimientos/**")
                .hasRole("ADMINISTRADOR")

                .requestMatchers(PATCH, "/api/movimientos/**")
                .hasRole("ADMINISTRADOR")

                .requestMatchers(DELETE, "/api/movimientos/**")
                .hasRole("ADMINISTRADOR")

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