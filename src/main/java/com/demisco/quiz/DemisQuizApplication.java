package com.demisco.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class DemisQuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemisQuizApplication.class, args);
    }

}
