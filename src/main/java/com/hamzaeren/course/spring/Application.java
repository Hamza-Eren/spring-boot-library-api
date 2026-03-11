package com.hamzaeren.course.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// SpringBootConfiguration, EnableAutoConfiguration ve ComponentScan birleşimidir.
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		// Spring Boot uygulamasını başlatır.
		// ApplicationContext oluşturur, Tomcat başlatılır,
		// Sınıflar taranır ve Bean'leri yükler.
		SpringApplication.run(Application.class, args);
	}
}
