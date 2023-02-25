package org.eatsy.appservice.controller.application;

import org.eatsy.appservice.controller.application.annotations.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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

    //For injecting the allowed consumer URLs from the respective (Dev/prod) application.properties
    @Autowired
    private Environment env;

    /**
     * Entry point of the Spring Boot application
     */
    @Generated
    public static void main(final String[] args) {

        SpringApplication.run(ServerRunner.class, args);
    }

    /**
     * Configure Cross-Origin Resource Sharing (CORS) in the Spring Boot application
     * CORS needs to be enabled on the server side so that data can be fetched from the React UI
     * <p>
     * .allowedOrigins(url) means the application will only accept requests from those listed in the
     * application.properties such as "http://localhost:3000" and "http://localhost:8080"
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                final String url = "cors.url";
                final String allowedConsumers = env.getProperty(url);
                registry.addMapping("/**").allowedOrigins(allowedConsumers);
            }
        };
    }
}
