package com.book.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.book.entity.Booking;

import feign.Param;

//import com.flight.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
	public List<Booking> findByEmail(String email);
	
	
}
