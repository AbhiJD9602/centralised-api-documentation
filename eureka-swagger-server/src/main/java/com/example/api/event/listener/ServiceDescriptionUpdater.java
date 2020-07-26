package com.example.api.config;

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
 *   Poll the service instances when created or destroyed and update the in memory store as key value pair
 * </pre>
 */
@Slf4j
@Component
public class ServiceDescriptionUpdater {

    private static final String DEFAULT_SWAGGER_URL = "/v2/api-docs";
    private static final String KEY_SWAGGER_URL = "swagger_url";

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceRegisteredEvent event) {
        log.debug("Starting Service Definition Context refresh");
        InstanceInfo instanceInfo = event.getInstanceInfo();
        log.debug("Attempting service definition refresh for Service : {} ", instanceInfo.getAppName());

        String swaggerUrl = getSwaggerURL(instanceInfo);
        log.info("Swagger Url:- ", swaggerUrl);
        definitionContext.addServiceDefinition(instanceInfo.getVIPAddress(), swaggerUrl);
        log.info("Service Definition Context Refreshed at :  {}", LocalDate.now());
    }

    private String getSwaggerURL(InstanceInfo instance) {
        String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
        return swaggerURL != null ? instance.getHomePageUrl() + swaggerURL : instance.getHomePageUrl() + DEFAULT_SWAGGER_URL;
    }

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceCanceledEvent event) {
        String[] split = event.getServerId().split(":");
        definitionContext.removeServiceDefinition(split[1]);
        System.out.println("App removed:" + split[1]);
    }

}
