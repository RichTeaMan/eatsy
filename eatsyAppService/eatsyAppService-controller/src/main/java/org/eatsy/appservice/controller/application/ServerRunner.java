package org.eatsy.appservice.controller.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Server runner starts the application (via the main class).
 *
 * @SpringBootApplication configures the application (based on the classpath dependencies
 * located in the gradle build files)
 * It also carries out a component scan to find configurations, controllers and services.
 */
@SpringBootApplication
public class ServerRunner {

    /**
     * Entry point of the Spring Boot application
     */
    public static void main(final String[] args) {

        SpringApplication.run(ServerRunner.class, args);
    }

    /**
     * Configure Cross-Origin Resource Sharing (CORS) in the Spring Boot application
     * CORS needs to be enabled on the server side so that data can be fetched from the React UI
     * <p>
     * .allowedOrigins("http://localhost:3000", "http://localhost:8080") means the application will only accept requests from
     * "http://localhost:3000" and "http://localhost:8080"
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "http://localhost:8080");
            }
        };
    }
}
