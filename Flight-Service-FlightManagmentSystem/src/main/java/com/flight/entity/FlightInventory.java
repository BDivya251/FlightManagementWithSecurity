package com.flight.entity;

import java.time.LocalDate;
//import java.sql.Date;
//
import java.time.LocalTime;
//
//private LocalTime departureTime;
//private LocalTime arrivalTime;

//import java.sql.Time;

import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String flightNumber;
	private String departure;
	private String arrival;

@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate travelDate;
	private LocalTime departureTime;
	private LocalTime arrivalTime;
	private Integer availableSeats;
	private float ticketPrice;
	
	@ManyToOne
	@JoinColumn(name="airline_id")
	private Airline airline;

	
	
}
