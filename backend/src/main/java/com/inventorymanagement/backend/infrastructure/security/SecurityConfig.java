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
                        requiresOperation("LISTAR_ROLES"),
                        requiresOperation("CREAR_USUARIO"),
                        requiresOperation("EDITAR_USUARIO"),
                        requiresOperation("ASIGNAR_PERMISOS")
                ))

                .requestMatchers(GET, "/api/roles/{roleId:[0-9]+}")
                .access(requiresOperation("LISTAR_ROLES"))

                .requestMatchers(POST, "/api/roles")
                .access(requiresOperation("CREAR_ROL"))

                .requestMatchers(PUT, "/api/roles/{roleId:[0-9]+}")
                .access(requiresOperation("EDITAR_ROL"))

                .requestMatchers(PATCH, "/api/roles/{roleId}/status")
                .access(requiresOperation("INACTIVAR_ROL"))

                .requestMatchers(GET, "/api/roles/{roleId}/permissions")
                .access(requiresOperation("ASIGNAR_PERMISOS"))

                .requestMatchers(PUT, "/api/roles/{roleId}/permissions")
                .access(requiresOperation("ASIGNAR_PERMISOS"))

                .requestMatchers(GET, "/api/permissions")
                .access(AuthorizationManagers.anyOf(
                        AuthorityAuthorizationManager.hasRole(ADMINISTRADOR),
                        requiresOperation("ASIGNAR_PERMISOS")
                ))

                .requestMatchers("/api/permissions/**")
                .hasRole(ADMINISTRADOR)

                .requestMatchers(GET, "/api/categories", "/api/categories/{categoryId:[0-9]+}")
                .access(requiresOperation("LISTAR_CATEGORIAS"))

                .requestMatchers(POST, "/api/categories")
                .access(requiresOperation("CREAR_CATEGORIA"))

                .requestMatchers(PUT, "/api/categories/{categoryId}")
                .access(requiresOperation("EDITAR_CATEGORIA"))

                .requestMatchers(PATCH, "/api/categories/{categoryId}/status")
                .access(requiresOperation("INACTIVAR_CATEGORIA"))

                .requestMatchers(GET, "/api/suppliers/active-suppliers")
                .access(requiresOperation("VER_REPORTES"))

                .requestMatchers(GET, "/api/suppliers", "/api/suppliers/{supplierId:[0-9]+}")
                .access(requiresOperation("LISTAR_PROVEEDORES"))

                .requestMatchers(POST, "/api/suppliers")
                .access(requiresOperation("CREAR_PROVEEDOR"))

                .requestMatchers(PUT, "/api/suppliers/{supplierId}")
                .access(requiresOperation("EDITAR_PROVEEDOR"))

                .requestMatchers(PATCH, "/api/suppliers/{supplierId}/status")
                .access(requiresOperation("INACTIVAR_PROVEEDOR"))

                .requestMatchers(GET,
                        "/api/products/low-stock",
                        "/api/products/products-category",
                        "/api/products/active-products",
                        "/api/products/inventory-value",
                        "/api/products/inventory-investment")
                .access(requiresOperation("VER_REPORTES"))

                .requestMatchers(GET, "/api/products", "/api/products/{productId:[0-9]+}")
                .access(requiresOperation("LISTAR_PRODUCTOS"))

                .requestMatchers(POST, "/api/products")
                .access(requiresOperation("CREAR_PRODUCTO"))

                .requestMatchers(PUT, "/api/products/{productId}")
                .access(requiresOperation("EDITAR_PRODUCTO"))

                .requestMatchers(PATCH, "/api/products/{productId}/status")
                .access(requiresOperation("INACTIVAR_PRODUCTO"))

                .requestMatchers(GET,
                        "/api/movements/summary",
                        "/api/movements/movements-value",
                        "/api/movements/products-movement")
                .access(requiresOperation("VER_REPORTES"))

                .requestMatchers(GET, "/api/movements", "/api/movements/{movementId:[0-9]+}")
                .access(requiresOperation("LISTAR_MOVIMIENTOS"))

                .requestMatchers(POST, "/api/movements")
                .access(requiresOperation("CREAR_MOVIMIENTO"))

                .requestMatchers(PUT, "/api/movements/{movementId}")
                .access(requiresOperation("EDITAR_MOVIMIENTO"))

                .requestMatchers(PATCH, "/api/movements/{movementId}/status")
                .access(requiresOperation("INACTIVAR_MOVIMIENTO"))

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