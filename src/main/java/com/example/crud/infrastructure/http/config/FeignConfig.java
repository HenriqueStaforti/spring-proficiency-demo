package com.example.crud.infrastructure.http.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.example.crud.client")
public class FeignConfig {
}
