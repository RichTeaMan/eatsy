package org.eatsy.appservice.controller.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger and Spring dependency injection configuration
 * Define base packages for Spring DI bean configuration
 */
@EnableSwagger2
@Configuration
@ComponentScan(basePackages = {"org.eatsy.appservice"})
public class SpringConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    final ApiInfo apiInfo = new ApiInfo("Eatsy", "Basic API for Eatsy", "0.1", null, null, "MIT license",
            "https://github.com/DM1st/eatsy/blob/main/licence.txt",
            new ArrayList<>());

}
