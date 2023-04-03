package com.vatek.hrmtool.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
        return new OpenAPI()
                .info(new Info().title("Vatek Internal Backend")
                        .description("Vatek HRM Tool for HRM Web")
                        .version(env.getProperty("build.version","v1.0.0"))
                        .license(new License().name("OpenApi v1.7.0").url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Vatek JSC Asia")
                        .url("https://vatek.asia"));
    }
}
