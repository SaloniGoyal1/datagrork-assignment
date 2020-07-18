package com.datagrork.api;

import com.datagrork.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ServiceConfiguration.class)
public class DatagrorkApiApplication {
    public static void main(String [] args){
        SpringApplication.run(DatagrorkApiApplication.class);
    }
}

// mvn clean install -DskipTests

//mvn clean install -Psetup -DskipTests

//http://localhost:8080/api/swagger-ui.html#/