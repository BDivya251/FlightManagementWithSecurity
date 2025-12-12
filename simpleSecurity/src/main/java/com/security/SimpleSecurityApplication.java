package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SimpleSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSecurityApplication.class, args);
	}

}
