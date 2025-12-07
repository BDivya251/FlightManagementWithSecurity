package com.book.repository;

import com.book.entity.Passenger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
	@Query("SELECT p.seatNumber FROM Passenger p WHERE p.booking.flightNumber = :flightNumber")
	List<Integer> getBookedSeatNumbers(String flightNumber);

}
