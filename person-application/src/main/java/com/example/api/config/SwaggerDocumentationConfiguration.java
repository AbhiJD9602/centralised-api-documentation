package com.example.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfiguration {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Person REST CRUD operations API in Spring-Boot 2")
                .description(
                        "Sample REST API for centralized documentation using Spring Boot and spring-fox swagger 2 ")
                .termsOfServiceUrl("").version("0.0.1-SNAPSHOT").contact(new Contact("Team Trailblazers", "https://github.com", "https://github.com")).build();
    }

    @Bean
    public Docket configureControllerPackageAndConvertors() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.api.controller")).build()
                .apiInfo(apiInfo());
    }

}
