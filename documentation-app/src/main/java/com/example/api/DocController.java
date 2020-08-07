package com.example.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocController {

    @Value("${eureka.dashboard.path:not imported}")
    private String hello;

    @GetMapping("hello")
    public String sayHello(){
        return hello;
    }
}
