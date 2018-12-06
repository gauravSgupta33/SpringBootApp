package com.cognizant.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cognizant")
@AutoConfigurationPackage
public class Application {

    public static void main(String[] args) {
    	System.setProperty("server.port", "9080");
        SpringApplication.run(Application.class, args);
    }
}