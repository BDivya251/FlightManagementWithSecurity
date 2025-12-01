package com.book.service;


import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.entity.FlightInventory;
import com.book.feign.FlightClient;
import com.book.repository.BookingRepository;

@Service

public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private FlightClient flightClient;
	public UUID saveBooking(BookingWrapper bookingWrapper) {
		FlightInventory flight = flightClient.getInventoryById(bookingWrapper.getFlightId());
		if(flight==null) {
			throw new RuntimeException("Flight inventory not found");
		}
		Booking book = new Booking();
		LocalDate ld= LocalDate.now();
		Date d = Date.valueOf(ld);
		book.setBookingDate(d);
		book.setEmail(bookingWrapper.getEmail());
		book.setInventoryId(flight.getId());
		book.setSeatsBooked(bookingWrapper.getSeatsBooked());
		book.setStatus("Booked");
		book.setTotalAmount(bookingWrapper.getSeatsBooked()*flight.getTicketPrice());
		Booking b=bookingRepository.save(book);
		return b.getPnr();
		}
	
	public Booking getBooking(UUID pnr){
		return bookingRepository.findById(pnr).get();
	}
	public void cancelBooking(UUID pnr) {
		Booking b=bookingRepository.findById(pnr).orElseThrow(()-> new RuntimeException("wrong pnr number"));
		b.setStatus("Cancelled");
		bookingRepository.save(b);
	}
	
	public List<Booking> getBookingByE(String email){
		return bookingRepository.findByEmail(email);
	}
}