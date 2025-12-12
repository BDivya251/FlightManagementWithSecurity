package com.book;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
@EnableMethodSecurity
@SpringBootApplication
public class BookingServiceFlightManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceFlightManagmentSystemApplication.class, args);
	}

}
