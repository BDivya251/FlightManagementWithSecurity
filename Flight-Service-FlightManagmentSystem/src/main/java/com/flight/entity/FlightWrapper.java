package com.flight.entity;

//import java.sql.Date;
import java.time.LocalDate;
//import java.sql.Date;
//
import java.time.LocalTime;
//import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightWrapper {
	@NotBlank(message="flight number is required")
	private String flightNumber;
	@NotBlank(message="departure placce is required")
	private String departure;
	@NotBlank(message="arrival is required")
	private String arrival;
	@NotNull(message="travel date is required")

@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate travelDate;
	@NotNull(message="departure time is required")
	private LocalTime departureTime;
	@NotNull(message="arrival time is required")
	private LocalTime arrivalTime;
	@NotNull(message="availableseats are required")
	private Integer availableSeats;
	@NotNull(message="ticketprice is required")
	private Float ticketPrice;
	@NotNull(message="airline id of this flight is required")
	private Integer airline;
//	@NotNull(message="roundTrip details are required")
//	private Boolean roundTrip;
}
