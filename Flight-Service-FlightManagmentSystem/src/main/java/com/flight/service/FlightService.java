package com.flight.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.repository.FlightRepository;
import com.flight.utils.Converters;

@Service
public class FlightService {
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private Converters converters;
	public FlightInventory addFlightInventory(FlightInventory flight) {
		return flightRepository.save(flight);
//		return f.getId();
	}
	
	public List<FlightWrapper> getFlightDetails(String departure,String arrival){
//		return flightRepository.
				List<FlightInventory> flights= flightRepository.findByDepartureAndArrival(departure, arrival);
				List<FlightWrapper> flightWrappers = new ArrayList<>();
				for(int i=0;i<flights.size();i++) {
					FlightWrapper wrap = converters.mapToWrapper(flights.get(i));
					flightWrappers.add(wrap);
				}
				return flightWrappers;	
	}
	  public FlightInventory getInventoryById(Integer id) {
	        return flightRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Flight Inventory not found"));
	    }
}
