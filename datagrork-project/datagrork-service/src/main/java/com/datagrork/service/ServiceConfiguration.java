package com.datagrork.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.datagrork.service")
@EntityScan("com.datagrork.service.entity")
public class ServiceConfiguration {
}
