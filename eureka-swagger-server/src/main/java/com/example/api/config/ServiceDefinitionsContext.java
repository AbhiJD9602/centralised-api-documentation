package com.example.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <pre>
 *   	In-Memory store to hold API-Definition JSON
 * </pre>
 */
@Slf4j
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ServiceDefinitionsContext {
    @Autowired
    private RestTemplate template;

    private final ConcurrentHashMap<String, String> serviceDescriptions;

    private ServiceDefinitionsContext() {
        serviceDescriptions = new ConcurrentHashMap<>();
    }

    public void addServiceDefinition(String serviceName, String serviceDescription) {
        serviceDescriptions.put(serviceName, serviceDescription);
    }

    public void removeServiceDefinition(String serviceName) {
        serviceDescriptions.remove(serviceName);
    }

    public String getSwaggerDefinition(String serviceId) {
        String swaggerURL = this.serviceDescriptions.get(serviceId);
        Optional<String> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL);

        if (jsonData.isPresent()) {
            return jsonData.get();
        } else {
            log.error("Skipping service id : {} Error : Could not get Swagger definition from API ", serviceId);
        }
        return null;
    }

    public List<SwaggerResource> getSwaggerDefinitions() {
        return serviceDescriptions.entrySet().stream().map(serviceDefinition -> {
            SwaggerResource resource = new SwaggerResource();
            resource.setLocation("/service/" + serviceDefinition.getKey());
            resource.setName(serviceDefinition.getKey());
            resource.setSwaggerVersion("2.0");
            return resource;
        }).collect(Collectors.toList());
    }

    private Optional<String> getSwaggerDefinitionForAPI(String serviceName, String url) {
        log.info("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);
        try {
            String jsonData = template.getForObject(url, String.class);
            return Optional.of(jsonData);
        } catch (RestClientException ex) {
            log.error("Error while getting service definition for service : {} Error : {} ", serviceName, ex.getMessage());
            return Optional.empty();
        }

    }
}
