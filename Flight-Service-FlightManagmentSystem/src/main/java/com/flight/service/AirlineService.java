package com.flight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flight.entity.Airline;
import com.flight.repository.AirlineRepository;

@Service
public class AirlineService {
	@Autowired
	private AirlineRepository airlineRepository;
	public Integer airline(Airline a) {
		return airlineRepository.save(a).getId();
	}
}
