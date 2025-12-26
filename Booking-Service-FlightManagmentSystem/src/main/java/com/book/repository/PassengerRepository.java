package com.book.repository;

import com.book.entity.Passenger;

import feign.Param;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
	@Query("SELECT p.seatNumber FROM Passenger p WHERE p.booking.flightNumber = :flightNumber and p.booking.status='BOOKED'")
	List<Integer> getBookedSeatNumbers(String flightNumber);
	
	 @Query("""
		        SELECT p.seatNumber
		        FROM Passenger p
		        WHERE p.booking.flightNumber = :flightNumber
		        AND p.booking.departureDate = :date
		    """)
		    List<Integer> findBookedSeats(
		        @Param("flightNumber") String flightNumber,
		        @Param("date") LocalDate date
		    );

}
