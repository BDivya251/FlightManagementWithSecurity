package com.book.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.service.BookingService;
@RequestMapping("/flight")
@RestController
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@PostMapping("/booking")
	public UUID saveBook(@RequestBody BookingWrapper bookingWrapper) {
		return bookingService.saveBooking(bookingWrapper);
	}
	
	@GetMapping("/booking/history")
	public List<Booking> getBookingByemail(@RequestParam String email){
		return bookingService.getBookingByE(email);
	}
	
	@GetMapping("/ticket")
	public Booking getBook(@RequestParam UUID id) {
		return bookingService.getBooking(id);
	}
	@DeleteMapping("/booking/cancel")
	public void delBooking(@RequestParam UUID id) {
		bookingService.cancelBooking(id);
	}
}
