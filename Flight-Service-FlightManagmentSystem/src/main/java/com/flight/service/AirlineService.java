package com.flight.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Airline;
import com.flight.entity.AirlineWrapper;
import com.flight.exceptions.AirlineAlreadyExist;
import com.flight.repository.AirlineRepository;

@Service
public class AirlineService {
	@Autowired
	private AirlineRepository airlineRepository;
	public Integer airline(AirlineWrapper a) {
		Optional<Airline> a1=airlineRepository.findByAirlineName(a.getAirlineName());
		if(!a1.isPresent()) {
		Airline airline  = new Airline();
		airline.setAirlineName(a.getAirlineName());
		return airlineRepository.save(airline).getId();
		}
		else {
			throw new AirlineAlreadyExist("ailine already exist");
		}
	
}
	public List<Airline> getAirlines(){
		return airlineRepository.findAll();
	}
	
	
}
