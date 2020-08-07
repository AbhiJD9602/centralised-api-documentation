package com.example.api.client.config;

import com.example.api.utility.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile(Constants.DEACTIVATE_SWAGGER_EUREKA)
@PropertySource("classpath:disable-swagger-eureka-client.properties")
@Configuration
public class DisableEurekaClientConfig {
}
