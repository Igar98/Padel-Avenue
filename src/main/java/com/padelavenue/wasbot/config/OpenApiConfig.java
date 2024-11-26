package com.padelavenue.wasbot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:Padle Avenue}")
    private String applicationName;

    @Bean
    public OpenAPI mercadonaOpenAPI() {
        final Info apiInfo = new Info()
                .title(applicationName)
                .description("Application to manage an amateur padle league.")
                .version("1.0.0");

        // Initialize server configuration
        final Server localServer = new Server()
                .url("/")
                .description("Local Server");

        return new OpenAPI()
                .info(apiInfo)
                .addServersItem(localServer);
    }
}