package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.service.FlightService;

@RestController
@RequestMapping("/flight")
public class FlightController {
	@Autowired
	private FlightService flightService;

	@PostMapping("/airline/inventary/add")
	public FlightInventory addFlights(@RequestBody FlightInventory flight) {
		return flightService.addFlightInventory(flight);
	}

	@GetMapping("/search")
	public List<FlightWrapper> searchFligght(@RequestParam String departure, @RequestParam String arrival) {
		return flightService.getFlightDetails(departure, arrival);
	}

	@GetMapping("/inventory/{id}")
	public FlightInventory getInventoryById(@PathVariable Integer id) {
		return flightService.getInventoryById(id);
	}

}
