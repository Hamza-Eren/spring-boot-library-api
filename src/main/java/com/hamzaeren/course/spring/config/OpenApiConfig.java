package com.hamzaeren.course.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI apiInfo() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Kütüphane Yönetim Sistemi API")
                                                .version("1.0.0")
                                                .description(
                                                                "Spring Boot ile geliştirilmiş, JWT doğrulamalı Kütüphane Yönetim Sistemi REST API Dokümantasyonu")
                                                .contact(new Contact()
                                                                .name("Hamza Eren Sarpdağ")
                                                                .email("herensarpdag@gmail.com")
                                                                .url("https://hamzaeren.com.tr"))
                                                .termsOfService("https://hamzaeren.com.tr")
                                                .license(new License()
                                                                .name("Apache 2.0")
                                                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
        }
}
