package com.flight.service;
import java.time.LocalDate;
//import java.sql.Date;
//import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.dto.FlightSearchResponseRoundTrip;
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

	public List<FlightWrapper> getFlightDetails(String departure, String arrival,LocalDate date) {
//		return flightRepository.
		List<FlightInventory> flights = flightRepository.findByDepartureAndArrivalAndTravelDate(departure, arrival,date);
		List<FlightWrapper> flightWrappers = new ArrayList<>();
		System.out.println(flights);
		for (int i = 0; i < flights.size(); i++) {
			FlightWrapper wrap = converters.mapToWrapper(flights.get(i));
			flightWrappers.add(wrap);
		}
		return flightWrappers;
	}
	public FlightSearchResponseRoundTrip getRoundTripFlights(
	        String departure,
	        String arrival,
	        LocalDate goingDate,
	        LocalDate comingDate
	) {

		FlightSearchResponseRoundTrip response = new FlightSearchResponseRoundTrip();

	    // GOING FLIGHTS
	    List<FlightInventory> goingFlights =
	            flightRepository.findByDepartureAndArrivalAndTravelDate(
	                    departure, arrival, goingDate);

	    List<FlightWrapper> goingWrappers = goingFlights.stream()
	            .map(converters::mapToWrapper)
	            .toList();

	    response.setGoing(goingWrappers);

	    // RETURN FLIGHTS
	    List<FlightInventory> comingFlights =
	            flightRepository.findByDepartureAndArrivalAndTravelDate(
	                    arrival, departure, comingDate);

	    List<FlightWrapper> comingWrappers = comingFlights.stream()
	            .map(converters::mapToWrapper)
	            .toList();

	    response.setComing(comingWrappers);

	    return response;
	}


	public FlightInventory getInventoryById(Integer id) {
		return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight Inventory not found"));
	}
}
