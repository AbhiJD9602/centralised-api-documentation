package com.example.api;

import com.example.api.client.config.AddSwaggerEurekaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@AddSwaggerEurekaClient
@SpringBootApplication
public class PersonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonApplication.class, args);
    }
}