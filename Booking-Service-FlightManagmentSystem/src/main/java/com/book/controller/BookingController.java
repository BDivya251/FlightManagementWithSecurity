package com.book.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.exceptions.NoEnoughSeatNumbers;
import com.book.exceptions.SeatsNotAvailableException;
import com.book.service.BookingService;
@RequestMapping("/flight")
@RestController
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@PostMapping("/booking")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> saveBook(@RequestBody BookingWrapper bookingWrapper) throws SeatsNotAvailableException,NoEnoughSeatNumbers {
		return new ResponseEntity<>(bookingService.saveBooking(bookingWrapper),HttpStatus.CREATED);
	}
	
	@GetMapping("/booking/history")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public List<Booking> getBookingByemail(@RequestParam String email){
		return bookingService.getBookingByE(email);
	}
	
	@GetMapping("/ticket")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public Booking getBook(@RequestParam String id) {
		return bookingService.getBooking(id);
	}
	@DeleteMapping("/booking/cancel")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> delBooking(@RequestParam String id) {
		bookingService.cancelBooking(id);
		return ResponseEntity.noContent().build();
	}
}
