package com.book.util;

import org.springframework.stereotype.Component;

import com.book.entity.Passenger;
import com.book.entity.PassengerWrapper;

@Component
public class Converters {
	public Passenger convertToPassenger(PassengerWrapper p) {
		Passenger passenger = new Passenger();
		passenger.setAge(p.getAge());
		passenger.setName(p.getName());
		passenger.setSeatNumber(p.getSeatNumber());
		return passenger;
	}
}
