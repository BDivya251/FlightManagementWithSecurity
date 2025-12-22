package com.flight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flight.entity.Airline;
import com.flight.entity.AirlineWrapper;
import com.flight.entity.FlightInventory;
import com.flight.service.AirlineService;

import jakarta.validation.Valid;

@RestController
public class AirlineController {
	
	@Autowired
	private AirlineService airlineService;
	@PostMapping("/flight/airline/add")
	public ResponseEntity<Integer> addAirline(@Valid @RequestBody AirlineWrapper a) {
		return new ResponseEntity<>(airlineService.airline(a),HttpStatus.CREATED);
	}
	
	@GetMapping("/flight/airline")
	public List<Airline> allAirlines(){
		return airlineService.getAirlines();
	}
}
