package com.flight.entity;

import java.sql.Date;
import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightWrapper {
	
	private String flightNumber;
	private String departure; 
	private String arrival;
	private Date travelDate;
	private Time departureTime;
	private float ticketPrice;
}
