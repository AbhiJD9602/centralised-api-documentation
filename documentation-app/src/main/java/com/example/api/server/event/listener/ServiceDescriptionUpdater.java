package com.example.api.server.event.listener;

import com.example.api.server.service.ServiceDefinitionsContext;
import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * <pre>
 *   Periodically poll the service instances and update the in memory store as key value pair
 * </pre>
 */
@Slf4j
@Component
public class ServiceDescriptionUpdater {

    private static final String KEY_SWAGGER_URL = "swagger_url";
    private static final String DEFAULT_SWAGGER_URL = "/v2/api-docs.json";

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceRegisteredEvent event) {
        log.info("Starting Service Definition Context refresh");
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.info("Attempting service definition refresh for Service : {} ", instanceInfo.getAppName());

        String swaggerUrl = getSwaggerURL(instanceInfo);
        log.info("Swagger Url:- ", swaggerUrl);
        definitionContext.addServiceDefinition(instanceInfo.getVIPAddress(), swaggerUrl);
        log.info("Service Definition Context Refreshed at :  {}", LocalDate.now());
    }

    private String getSwaggerURL(InstanceInfo instance) {
        String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
        log.info("DEFAULT_SWAGGER_URL: {}", DEFAULT_SWAGGER_URL);
        log.info("Home page_URL: {}", instance.getHomePageUrl());
        return swaggerURL != null ? swaggerURL + DEFAULT_SWAGGER_URL : instance.getHomePageUrl() + DEFAULT_SWAGGER_URL;
    }

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceCanceledEvent event) {
        String[] split = event.getServerId().split(":");
        definitionContext.removeServiceDefinition(split[1]);
        log.info("App removed:" + split[1]);
    }

}