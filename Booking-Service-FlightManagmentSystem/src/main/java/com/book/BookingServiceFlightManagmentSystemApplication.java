package com.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BookingServiceFlightManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceFlightManagmentSystemApplication.class, args);
	}

}
