package com.inventorymanagement.backend.infrastructure.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.inventorymanagement.backend.domain.entity.OperationPermission;
import com.inventorymanagement.backend.domain.entity.Permission;
import com.inventorymanagement.backend.infrastructure.persistence.repository.OperationPermissionRepository;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.authorization.AuthorityAuthorizationManager;

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
    private final OperationPermissionRepository operationPermissionRepository;

    private AuthorizationManager<RequestAuthorizationContext> requiresOperation(
            String operationCode) {
        return (authentication, context) -> {
            var user = authentication.get();

            boolean allowed = user != null && user.isAuthenticated()
                    && operationPermissionRepository.findById(operationCode)
                    .map(OperationPermission::getPermission)
                    .filter(permission ->
                            permission.getStatus() == Permission.Status.ACTIVO)
                    .map(permission ->
                            "PERMISSION_" + permission.getPermissionCode())
                    .map(required -> user.getAuthorities().stream()
                            .anyMatch(authority ->
                                    required.equals(authority.getAuthority())))
                    .orElse(false);

            return new AuthorizationDecision(allowed);
        };
    }

    public SecurityConfig(JwtFilter jwtFilter,OperationPermissionRepository operationPermissionRepository) {
        this.jwtFilter = jwtFilter;
        this.operationPermissionRepository = operationPermissionRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/actuator/health")
                .permitAll()

                .requestMatchers(
                    "/api/docs",
                    "/api/docs/**",
                    "/api/swagger-ui.html",
                    "/api/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                )
                .permitAll()
                .requestMatchers(GET, "/api/auth/operations")
                .authenticated()

                .requestMatchers("/api/auth/**")
                .permitAll()


                .requestMatchers(GET, "/api/users/**")
                .access(requiresOperation("LISTAR_USUARIOS"))

                .requestMatchers(POST, "/api/users")
                .access(requiresOperation("CREAR_USUARIO"))

                .requestMatchers(PUT, "/api/users/{userId}")
                .access(requiresOperation("EDITAR_USUARIO"))

                .requestMatchers(PATCH, "/api/users/{userId}/status")
                .access(requiresOperation("INACTIVAR_USUARIO"))

                .requestMatchers(GET, "/api/roles")
                .access(AuthorizationManagers.anyOf(
                        AuthorityAuthorizationManager.hasRole(ADMINISTRADOR),
                        requiresOperation("CREAR_USUARIO"),
                        requiresOperation("EDITAR_USUARIO")
                ))
                .requestMatchers("/api/roles/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers("/api/permissions/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers(GET, "/api/categories")
                .access(requiresOperation("LISTAR_CATEGORIAS"))

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

                .requestMatchers(GET, "/api/suppliers")
                .access(requiresOperation("LISTAR_PROVEEDORES"))

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

                .requestMatchers(GET, "/api/products")
                .access(requiresOperation("LISTAR_PRODUCTOS"))
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

                .requestMatchers(GET, "/api/movements")
                .access(requiresOperation("LISTAR_MOVIMIENTOS"))
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}