package com.sosag.dronefleet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 *
 * <p>Defines the metadata for the Drone Fleet Platform API,
 * including title, version, description, and contact information.</p>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI bean.
     *
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI droneFleetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Drone Fleet Platform API")
                        .description("REST API for managing drone fleet, telemetry, and missions. " +
                                "Part of a Data Engineering portfolio project.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Pablo Sosa")
                                .email("tu.email@ejemplo.com") // Cambia esto por tu email real o quítalo
                                .url("https://github.com/tu-usuario/drone-fleet-platform")) // Cambia por tu repo
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}