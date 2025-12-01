package com.book.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.book.entity.FlightInventory;

@FeignClient(name="FLIGHT-SERVICE")
public interface FlightClient {
	@GetMapping("/flight/inventory/{id}")
	FlightInventory getInventoryById(@PathVariable Integer id);
}
