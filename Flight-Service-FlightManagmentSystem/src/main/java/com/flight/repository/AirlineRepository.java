package com.flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flight.entity.Airline;

public interface AirlineRepository extends JpaRepository<Airline,Integer> {

}
