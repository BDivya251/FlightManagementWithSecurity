package com.book.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.book.config.RabbitMQConfig;
//import com.boo.dto.BookingEmailEvent;

import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.service.BookingEmailEvent;
import com.book.util.Converters;
import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.entity.FlightInventory;
import com.book.entity.Passenger;
import com.book.entity.PassengerWrapper;
import com.book.exceptions.AlreadyCancelled;
import com.book.exceptions.NoEnoughSeatNumbers;
import com.book.exceptions.PnrNotFoundException;
import com.book.exceptions.SeatsNotAvailableException;
import com.book.feign.FlightClient;
import com.book.repository.BookingRepository;
import com.book.repository.PassengerRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service

public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private FlightClient flightClient;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private PassengerRepository passengerRepository;
	@Autowired
	private Converters converters;
	@CircuitBreaker(name = "flight-service", fallbackMethod = "flightServerFallBack")
	public String saveBooking(BookingWrapper bookingWrapper) throws SeatsNotAvailableException, NoEnoughSeatNumbers {
		FlightInventory flight = flightClient.getInventoryById(bookingWrapper.getFlightId());
		if (flight == null) {
			throw new RuntimeException("Flight inventory not found");
		}
		
		Booking book = new Booking();
		LocalDate ld = LocalDate.now();
		String pnr = UUID.randomUUID().toString();
		book.setPnr(pnr);
//		String pnr1 = UUID.randomUUID().toString();
//		book.setPnr(pnr1);

		Date d = Date.valueOf(ld);
		book.setBookingDate(d);
		book.setEmail(bookingWrapper.getEmail());
		book.setFlightNumber(flight.getFlightNumber());
		book.setSeatsBooked(bookingWrapper.getSeatsBooked());
		List<Passenger> passengers = new ArrayList<>();
		for(PassengerWrapper p:bookingWrapper.getPassenger()) {
			Passenger psg = converters.convertToPassenger(p);
			passengers.add(psg);
			passengerRepository.save(psg);
		}
		book.setPassengers(passengers);
		int seats=flight.getAvailableSeats();
		if(seats-bookingWrapper.getSeatsBooked()<=0) {
			throw new SeatsNotAvailableException("seats not available");
		}
		else {
		flight.setAvailableSeats(seats-bookingWrapper.getSeatsBooked());
		}
		book.setStatus("Booked");
		book.setTotalAmount(bookingWrapper.getSeatsBooked() * flight.getTicketPrice());
		Booking b = bookingRepository.save(book);
		System.out.println(" About to get response");

		BookingEmailEvent event = new BookingEmailEvent(b.getPnr(), b.getEmail(),
				"Your booking with PNR " + b.getPnr() + " is confirmed!");
		rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY, event);
		return pnr;
	}

	public String flightServerFallBack(BookingWrapper bookingWrapper, Throwable ex) {
		return "Flight Service is Down";
	}

	public Booking getBooking(String pnr) {
		return bookingRepository.findById(pnr).get();
	}

	public void cancelBooking(String pnr) {
		Booking b = bookingRepository.findById(pnr).orElseThrow(() -> new PnrNotFoundException("wrong pnr number"));
		if (b.getStatus().equals("Cancelled")) {
			throw new AlreadyCancelled("Flight already cancelled");
		}
		b.setStatus("Cancelled");
		BookingEmailEvent event = new BookingEmailEvent(b.getPnr(), b.getEmail(),
				"Your booking with PNR " + b.getPnr() + " is cancelled!");
		rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY, event);
		
		bookingRepository.save(b);
	}

	public List<Booking> getBookingByE(String email) {
		return bookingRepository.findByEmail(email);
	}
}