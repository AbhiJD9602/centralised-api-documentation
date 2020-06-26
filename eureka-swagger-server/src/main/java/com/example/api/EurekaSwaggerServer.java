package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaServer
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class EurekaSwaggerServer {

    public static void main(String[] args) {
        SpringApplication.run(EurekaSwaggerServer.class, args);
    }
}
