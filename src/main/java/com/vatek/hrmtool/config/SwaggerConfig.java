package com.vatek.hrmtool.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;



@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Environment env;

    @Bean
    public OpenAPI hrmToolOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(
                        new Info()
                                .title("Vatek Internal Backend")
                                .description("Vatek HRM Tool for HRM Web")
                                .version(env.getProperty("build.version","v1.0.0"))
                                .license(
                                        new License()
                                                .name("OpenApi v1.7.0")
                                                .url("https://springdoc.org")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                        .description("Vatek JSC Asia")
                        .url("https://vatek.asia"))
                ;
    }
}
