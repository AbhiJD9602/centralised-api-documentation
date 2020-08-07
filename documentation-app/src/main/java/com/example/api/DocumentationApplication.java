package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DocumentationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocumentationApplication.class, args);
    }
}
