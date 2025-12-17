package com.flight.controller;

//import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.flight.dto.FlightSearchResponseRoundTrip;
import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.service.FlightService;
import java.time.LocalDate;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/flight")
public class FlightController {
	@Autowired
	private FlightService flightService;

	@PostMapping("/airline/inventary/add")
	public ResponseEntity<FlightInventory> addFlights(@Valid @RequestBody FlightWrapper flight) {
		return new ResponseEntity<>(flightService.addFlightInventory(flight),HttpStatus.CREATED);
	}

	@GetMapping("/search")
	public List<FlightWrapper> searchFligght(@RequestParam String departure, @RequestParam String arrival,@RequestParam LocalDate date) {
		return flightService.getFlightDetails(departure, arrival,date);
	}
	
	@GetMapping("/search/round-trip")
	public FlightSearchResponseRoundTrip searchRoundTripFlights(
	        @RequestParam String departure,
	        @RequestParam String arrival,
	        @RequestParam LocalDate goingDate,
	        @RequestParam LocalDate comingDate
	) {
	    return flightService.getRoundTripFlights(
	            departure, arrival, goingDate, comingDate
	    );
	}

	@GetMapping("/inventory/{id}")
	public FlightInventory getInventoryById(@PathVariable Integer id) {
		return flightService.getInventoryById(id);
	}

}
