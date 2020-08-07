package com.example.api.client.config;

import com.example.api.utility.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile(Constants.ACTIVATE_SWAGGER_EUREKA)
@PropertySource("classpath:enable-swagger-eureka-client.properties")
@Import({CorsSecurityConfiguration.class})
@EnableSwagger2
@Configuration
public class EnableEurekaClientConfig {
}
