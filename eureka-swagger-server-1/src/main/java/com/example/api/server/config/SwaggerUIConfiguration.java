package com.example.api.server.config;

import com.example.api.server.service.ServiceDefinitionsContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *  Swagger Ui configurations. Configure bean of the {@link SwaggerResourcesProvider} to
 *   read data from in-memory context
 * </pre>
 */
@Configuration
public class SwaggerUIConfiguration {

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }

    @Primary
    @Bean
    @Lazy
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider, ServiceDefinitionsContext definitionContext) {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            resources.clear();
            resources.addAll(definitionContext.getSwaggerDefinitions());
            return resources;
        };
    }

}
