package com.example.api.server.controller;


import com.example.api.server.service.ServiceDefinitionsContext;
import com.example.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  Controller to serve the JSON from our in-memory store. So that UI can render the API-Documentation
 * </pre>
 */
@Profile(Constants.DEACTIVATE_SWAGGER_EUREKA)
@RestController
public class ServiceDefinitionController {

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @GetMapping("/service/{servicename}")
    public String getServiceDefinition(@PathVariable("servicename") String serviceName, @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {

        return definitionContext.getSwaggerDefinition(serviceName, token);

    }
}