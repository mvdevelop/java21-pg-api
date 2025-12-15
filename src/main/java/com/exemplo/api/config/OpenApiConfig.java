package com.exemplo.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Java 21 com PostgreSQL")
                        .version("1.0.0")
                        .description("API de exemplo com Java 21, Spring Boot, PostgreSQL e Swagger")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@exemplo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
