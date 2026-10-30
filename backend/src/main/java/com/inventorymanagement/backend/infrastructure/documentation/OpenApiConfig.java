
package com.inventorymanagement.backend.infrastructure.documentation;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(
            new Components().addSecuritySchemes(
                "bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer bearerAuthCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> {
                if (path.equals("/api/auth") || path.startsWith("/api/auth/")) {
                    return;
                }

                pathItem.readOperations().forEach(operation -> operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth")));
            });
        };
    }
}
