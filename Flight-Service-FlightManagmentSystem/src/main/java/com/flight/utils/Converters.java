package com.flight.utils;

import java.sql.Date;
import java.sql.Time;

import org.springframework.stereotype.Component;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;

@Component
public class Converters {
	public FlightWrapper mapToWrapper(FlightInventory flight) {

		FlightWrapper wrapper = new FlightWrapper();
		wrapper.setArrival(flight.getArrival());
		wrapper.setFlightNumber(flight.getFlightNumber());
		wrapper.setDeparture(flight.getDeparture());
		wrapper.setTravelDate(flight.getTravelDate());
		wrapper.setDepartureTime(flight.getDepartureTime());
		wrapper.setTicketPrice(flight.getTicketPrice());
		return wrapper;
	}
}
