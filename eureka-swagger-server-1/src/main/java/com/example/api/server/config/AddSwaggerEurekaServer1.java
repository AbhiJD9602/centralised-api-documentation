package com.example.api.server.config;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableSwagger2
@ComponentScan(basePackages = {"com.example.api.server"})
@PropertySource("classpath:enable-swagger-eureka-server-1.properties")
@EnableEurekaServer
public @interface AddSwaggerEurekaServer1 {
}
