package com.example.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <pre>
 *  Swagger Ui configurations. Configure bean of the {@link SwaggerResourcesProvider} to
 *   read data from in-memory context
 * </pre>
 */
@Configuration
public class SwaggerUIConfiguration {

    @Autowired
    private Environment env;

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

    @Bean
    public FilterRegistrationBean createApiFilter() {
        FilterRegistrationBean b = new FilterRegistrationBean(new SwaggerFixFilter(env));
        b.setName("SwaggerJSONFilter");
        b.setUrlPatterns(Arrays.asList(new String[]{"/v2/api-docs"}));
        return b;
    }
}
