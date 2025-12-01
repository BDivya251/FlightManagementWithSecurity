package com.flight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;

@Repository
public interface FlightRepository extends JpaRepository<FlightInventory,Integer>{
	public List<FlightInventory> findByDepartureAndArrival(String departure,String arrival);
//	public List<FlightWrapper> find
}
