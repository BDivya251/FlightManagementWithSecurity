package com.book.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.book.config.RabbitMQConfig;
//import com.boo.dto.BookingEmailEvent;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.service.BookingEmailEvent;

import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.entity.FlightInventory;
import com.book.feign.FlightClient;
import com.book.repository.BookingRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service

public class BookingService {
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private FlightClient flightClient;
	  @Autowired
	    private RabbitTemplate rabbitTemplate;
	@CircuitBreaker(name="flight-service",fallbackMethod="flightServerFallBack")
	public String saveBooking(BookingWrapper bookingWrapper) {
		FlightInventory flight = flightClient.getInventoryById(bookingWrapper.getFlightId());
		if(flight==null) {
			throw new RuntimeException("Flight inventory not found");
		}
		Booking book = new Booking();
		LocalDate ld= LocalDate.now();
		String pnr = UUID.randomUUID().toString().substring(0,10);
		book.setPnr(pnr);
//		String pnr1 = UUID.randomUUID().toString();
//		book.setPnr(pnr1);

		Date d = Date.valueOf(ld);
		book.setBookingDate(d);
		book.setEmail(bookingWrapper.getEmail());
		book.setInventoryId(flight.getId());
		book.setSeatsBooked(bookingWrapper.getSeatsBooked());
		book.setStatus("Booked");
		book.setTotalAmount(bookingWrapper.getSeatsBooked()*flight.getTicketPrice());
		Booking b=bookingRepository.save(book);
		System.out.println(" About to get response");

		BookingEmailEvent event=new BookingEmailEvent(   b.getPnr(),
                b.getEmail(),
                "Your booking with PNR " + b.getPnr() + " is confirmed!");
		 rabbitTemplate.convertAndSend(
	                RabbitMQConfig.EMAIL_EXCHANGE,
	                RabbitMQConfig.EMAIL_ROUTING_KEY,
	                event
	        );
		return pnr;
		}
	public String flightServerFallBack(BookingWrapper bookingWrapper,Throwable ex) {
		return null;
	}
	public Booking getBooking(String pnr){
		return bookingRepository.findById(pnr).get();
	}
	public void cancelBooking(String pnr) {
		Booking b=bookingRepository.findById(pnr).orElseThrow(()-> new RuntimeException("wrong pnr number"));
		b.setStatus("Cancelled");
		bookingRepository.save(b);
	}
	
	public List<Booking> getBookingByE(String email){
		return bookingRepository.findByEmail(email);
	}
}