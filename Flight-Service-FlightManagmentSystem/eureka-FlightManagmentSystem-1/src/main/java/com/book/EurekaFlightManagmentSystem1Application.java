package com.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class EurekaFlightManagmentSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(EurekaFlightManagmentSystem1Application.class, args);
	}

}
