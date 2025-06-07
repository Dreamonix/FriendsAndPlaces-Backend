package de.whs.wi.friends_and_places.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for OpenAPI documentation.
 * This sets up comprehensive API documentation for the Friends and Places (FAP) project.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation with detailed information about the API.
     *
     * @return An OpenAPI configuration object
     */
    @Bean
    public OpenAPI friendsAndPlacesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Friends and Places API Documentation")
                        .description("Backend API documentation for the 'Friends and Places' (FAP) project, " +
                                "managing user registration, login, and location tracking. This documentation " +
                                "provides detailed information about all available endpoints, request/response " +
                                "models, and authentication requirements.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("WHS Wi")
                                .email("contact@example.com"))
                        .license(new License()
                                .name("API License")
                                .url("https://example.com/license")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Server URL")));
    }
}
