package com.example.api.server.config;

import com.example.api.server.service.ServiceDefinitionsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *  Swagger Ui configurations. Configure bean of the {@link SwaggerResourcesProvider} to
 *   read data from in-memory context
 * </pre>
 */
@ComponentScan(basePackages = {"com.example.api.server"})
@PropertySource("classpath:enable-swagger-eureka-server.properties")
@EnableSwagger2
@Configuration
public class SwaggerUIConfiguration {

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @Bean
    public RestTemplate configureTemplate() {
        return new RestTemplate();
    }

    @Primary
    @Bean
    @Lazy
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider, RestTemplate temp) {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            resources.clear();
            resources.addAll(definitionContext.getSwaggerDefinitions());
            return resources;
        };
    }

}
