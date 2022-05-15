package org.eatsy.appservice.controller.application;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger and Spring dependency injection configuration
 * Define base packages for Spring DI bean configuration
 */
@Configuration
@ComponentScan(basePackages = {"org.eatsy.appservice"})
public class openApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Eatsy API")
                .description("Basic REST API for Eatsy Service")
                .version("0.1")
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name("MIT Licence")
                .url("https://github.com/DM1st/eatsy/blob/main/licence.txt");
    }


}
