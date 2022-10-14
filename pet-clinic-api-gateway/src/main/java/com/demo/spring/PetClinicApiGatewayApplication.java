package com.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PetClinicApiGatewayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PetClinicApiGatewayApplication.class, args);
	}
	
	
}
