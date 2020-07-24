package com.example.api.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
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

    private final ConcurrentHashMap<String, String> serviceDescriptions;
    @Autowired
    private RestTemplate template;

    public ServiceDefinitionsContext() {
        serviceDescriptions = new ConcurrentHashMap<>();
    }

    public void addServiceDefinition(String serviceName, String serviceDescription) {
        serviceDescriptions.put(serviceName, serviceDescription);
    }

    public void removeServiceDefinition(String serviceName) {
        serviceDescriptions.remove(serviceName);
    }

    public String getSwaggerDefinition(String serviceId, String token) {
        String swaggerURL = this.serviceDescriptions.get(serviceId);
        ResponseEntity<String> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL, token);

        if (jsonData.getStatusCodeValue() != 204) {
            return jsonData.getBody();
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

    private ResponseEntity<String> getSwaggerDefinitionForAPI(String serviceName, String url, String token) {
        log.info("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", token);
        }
        HttpEntity entity = new HttpEntity(headers);

        try {
            log.info("URL:- {}", url);
            ResponseEntity<String> jsonData = template.exchange(url, HttpMethod.GET, entity, String.class);
            return jsonData;
        } catch (RestClientException ex) {
            log.error("Error while getting service definition for service : {} Error : {} ", serviceName, ex.getMessage());
            return ResponseEntity.noContent().build();
        }

    }
}
