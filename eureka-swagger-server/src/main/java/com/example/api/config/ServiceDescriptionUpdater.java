package com.example.api.config;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
@Component
public class ServiceDescriptionUpdater {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDescriptionUpdater.class);

    private static final String DEFAULT_SWAGGER_URL = "/v2/api-docs";
    private static final String KEY_SWAGGER_URL = "swagger_url";

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceRegisteredEvent event) {
        logger.debug("Starting Service Definition Context refresh");
        InstanceInfo instanceInfo = event.getInstanceInfo();
        logger.debug("Attempting service definition refresh for Service : {} ", instanceInfo.getAppName());

        String swaggerUrl = getSwaggerURL(instanceInfo);
        logger.info("Swagger Url:- ", swaggerUrl);
        definitionContext.addServiceDefinition(instanceInfo.getVIPAddress(), swaggerUrl);
        logger.info("Service Definition Context Refreshed at :  {}", LocalDate.now());
    }

    private String getSwaggerURL(InstanceInfo instance) {
        String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
        return swaggerURL != null ? "http://"+instance.getHostName()+":"+instance.getPort() + swaggerURL : "http://"+instance.getHostName()+":"+instance.getPort() + DEFAULT_SWAGGER_URL;
    }

    @EventListener
    public void refreshSwaggerConfigurations(EurekaInstanceCanceledEvent event) {
        String[] split = event.getServerId().split(":");
        definitionContext.removeServiceDefinition(split[1]);
        System.out.println("App removed:" + split[1]);
    }

}
