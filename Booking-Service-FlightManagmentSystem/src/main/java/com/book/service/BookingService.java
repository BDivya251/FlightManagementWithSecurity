package com.book.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.book.config.RabbitMQConfig;
//import com.boo.dto.BookingEmailEvent;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.book.exceptions.NotAValidFlightId;
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
//	@CircuitBreaker(name = "flight-service", fallbackMethod = "flightServerFallBack")
	public String saveBooking(BookingWrapper bookingWrapper) throws SeatsNotAvailableException, NoEnoughSeatNumbers {
//		Optional<FlightInventory> flightOptional = Optional.ofNullable(flightClient.getInventoryById(bookingWrapper.getFlightId()));
//		if (!flightOptional.isPresent()) {
//			throw new NotAValidFlightId("Flight inventory not found");
//		}
		FlightInventory flight;
		System.out.println("Calling Flight Service...");

//		try {
			System.out.println(bookingWrapper.getFlightId());
		    flight = getFlightWithCircuitBreaker(bookingWrapper.getFlightId());
//		} catch (Exception ex) {
		    if(flight==null) {
		    throw new NotAValidFlightId("Flight service is unavailable.Try again later ");
		    }
//		}

//		FlightInventory flight=flightOptional.get();
		if(bookingWrapper.getPassenger().size()!=bookingWrapper.getSeatsBooked()) {
			throw new NoEnoughSeatNumbers("seat numbers and passenger details are not matching");
		}
		Booking book = new Booking();
//		LocalDate ld = LocalDate.now();
		String pnr = UUID.randomUUID().toString();
		book.setPnr(pnr);
		LocalDate d = LocalDate.now();
		book.setBookingDate(d);
		book.setBookingTime(LocalTime.now());
		
		book.setEmail(bookingWrapper.getEmail());
	
		book.setFlightNumber(flight.getFlightNumber());
//		flight.getArrivalTime()
//		flight.getDepartureTime()
		book.setDepartureDate(flight.getTravelDate());
		book.setDepartureTime(flight.getArrivalTime());
		book.setSeatsBooked(bookingWrapper.getSeatsBooked());
		List<Passenger> passengers = new ArrayList<>();
		List<Integer> bookedSeats = passengerRepository.getBookedSeatNumbers(flight.getFlightNumber());
		int seats=flight.getAvailableSeats();
		System.out.println(seats);
		System.out.println(bookingWrapper.getSeatsBooked());
		if(seats-bookingWrapper.getSeatsBooked()<=0) {
			throw new SeatsNotAvailableException("seats not available");
		}
		else {
		flight.setAvailableSeats(seats-bookingWrapper.getSeatsBooked());
		System.out.println("Available seats in "+flight.getId()+" "+flight.getAvailableSeats());
		}
		for(PassengerWrapper p:bookingWrapper.getPassenger()) {
			 if (bookedSeats.contains(p.getSeatNumber())) {
			        throw new NoEnoughSeatNumbers("Seat " + p.getSeatNumber() + " already booked");
			    }
			Passenger psg = converters.convertToPassenger(p);
			passengers.add(psg);
			psg.setBooking(book);
		
		}
		book.setPassengers(passengers);
		book.setDepartureDate(flight.getTravelDate());
		book.setDepartureTime(flight.getArrivalTime());
//		book.setBookingDate();
		book.setStatus("Booked");
		book.setTotalAmount(bookingWrapper.getSeatsBooked() * flight.getTicketPrice());
		Booking b = bookingRepository.save(book);
//		passengerRepository.saveAll(passengers);
		System.out.println(" About to get response");

		BookingEmailEvent event = new BookingEmailEvent(b.getPnr(), b.getEmail(),
				"Your booking with PNR " + b.getPnr() + " is confirmed!"+b.getPassengers());
		rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_ROUTING_KEY, event);
		return pnr;
	}
	@CircuitBreaker(name = "flight-service", fallbackMethod = "flightFallback")
	public FlightInventory getFlightWithCircuitBreaker(Integer id) {
	    return flightClient.getInventoryById(id);
	}

	public FlightInventory flightFallback(Integer id, Throwable ex) {
		FlightInventory dummy = new FlightInventory();
	    dummy.setId(id);
	    dummy.setFlightNumber("N/A");
	    dummy.setTicketPrice(0);
	    dummy.setAvailableSeats(0);
	    return null;
	}

	public Booking getBooking(String pnr) {
		return bookingRepository.findById(pnr).orElseThrow(()->
		new PnrNotFoundException("pnr not found"));
	}

	public void cancelBooking(String pnr) {
		Booking b = bookingRepository.findById(pnr).orElseThrow(() -> new PnrNotFoundException("wrong pnr number"));
		if (b.getStatus().equals("Cancelled")) {
			throw new AlreadyCancelled("Flight already cancelled");
		}
//		FlightInventory flight = getFlightWithCircuitBreaker(b.getFlightNumber());
//		b.getDepartureDate()
//		b.getDepartureTime()
//		b.getBookingDate()
//		b.getBookingTime()
		LocalDateTime departureDateTime =
		        LocalDateTime.of(b.getDepartureDate(), b.getDepartureTime());

		LocalDateTime bookingDateTime =
		        LocalDateTime.of(b.getBookingDate(), b.getBookingTime());

		long hoursLeft = Duration.between(bookingDateTime, departureDateTime).toHours();
		if(hoursLeft<24) {
			throw new AlreadyCancelled("Booking cannot be cancelled...  YOur flight will departure in less than 24 hours");
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
	
	public List<Integer> getBookedSeats(String flightNumber, LocalDate date) {
	    return passengerRepository.findBookedSeats(flightNumber, date);
	}


}