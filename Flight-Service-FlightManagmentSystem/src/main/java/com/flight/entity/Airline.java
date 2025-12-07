package com.flight.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

public class Airline {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String airlineNumber;
	  @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
	    private List<FlightInventory> flights;
}
