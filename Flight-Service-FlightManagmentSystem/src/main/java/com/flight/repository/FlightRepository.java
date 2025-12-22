package com.flight.repository;
import java.time.LocalDate;
//import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;

@Repository
public interface FlightRepository extends JpaRepository<FlightInventory, Integer> {
	public List<FlightInventory> findByDepartureAndArrival(String departure, String arrival);
//	public List<FlightWrapper> find

	public Optional<FlightInventory> getByFlightNumberAndDepartureAndArrival(String flightNumber, String departure,
			String arrival);

	public List<FlightInventory> findByDepartureAndArrivalAndTravelDate(String departure, String arrival, LocalDate date);
	public List<FlightInventory> findByAirline_Id(Integer airlineid);
}
