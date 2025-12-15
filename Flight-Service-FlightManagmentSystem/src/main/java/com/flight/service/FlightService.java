package com.flight.service;

//import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Airline;
import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.exceptions.FlightAlreadyExisted;
import com.flight.exceptions.FlightNotFoundException;
import com.flight.repository.AirlineRepository;
import com.flight.repository.FlightRepository;
import com.flight.utils.Converters;

@Service
public class FlightService {
	@Autowired
	private AirlineRepository airlineRepository;
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private Converters converters;


	public FlightInventory addFlightInventory(FlightWrapper flight) {
		FlightInventory flightIn = new FlightInventory();
		Integer airlineNo=flight.getAirline();
		System.out.println("after gettng airline");
		Airline airline=airlineRepository.findById(airlineNo)
				.orElseThrow(()->new FlightNotFoundException("Airline Not Found"));
		Optional<FlightInventory> flightDup = flightRepository.getByFlightNumberAndDepartureAndArrival(flight.getFlightNumber() ,flight.getDeparture(),flight.getArrival());
			System.out.println("after repo");
		if(!flightDup.isPresent()) {
			System.out.println("at starting");
		flightIn.setAirline(airline);
		
		flightIn.setArrival(flight.getArrival());
		flightIn.setArrivalTime(flight.getArrivalTime());
		flightIn.setAvailableSeats(flight.getAvailableSeats());
		flightIn.setDeparture(flight.getDeparture());
		flightIn.setDepartureTime(flight.getDepartureTime());
		flightIn.setFlightNumber(flight.getFlightNumber());
		flightIn.setTicketPrice(flight.getTicketPrice());
		flightIn.setTravelDate(flight.getTravelDate());
//		flightIn.setAirline(flight);
		System.out.println("at end");
		return flightRepository.save(flightIn);}
		else {
			throw new FlightAlreadyExisted("Flight already existed");
		}

	}

	public List<FlightWrapper> getFlightDetails(String departure, String arrival) {
//		return flightRepository.
		List<FlightInventory> flights = flightRepository.findByDepartureAndArrival(departure, arrival);
		List<FlightWrapper> flightWrappers = new ArrayList<>();
		for (int i = 0; i < flights.size(); i++) {
			FlightWrapper wrap = converters.mapToWrapper(flights.get(i));
			flightWrappers.add(wrap);
		}
		return flightWrappers;
	}

	public FlightInventory getInventoryById(Integer id) {
		return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight Inventory not found"));
	}
}
