package com.example.cicd_demo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("Simple Spring Boot REST API used as a learning project for CI/CD with GitHub Actions.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Dani"))
                        .license(new License().name("MIT")));
    }
}
