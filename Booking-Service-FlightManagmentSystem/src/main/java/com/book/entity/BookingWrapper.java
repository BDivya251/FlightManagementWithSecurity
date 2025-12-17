package com.book.entity;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingWrapper {
	@NotNull(message="Email Id is reuired")
	private String email;
	@Min(message="minimum it has to be one", value = 0)
	private Integer seatsBooked;
	@NotNull(message="FlightId is required")
	private Integer flightId;
	@NotNull(message="passenger details are required")
	private List<PassengerWrapper> passenger;
//	private List<Integer> seatNumbers;
//	private boolean roundTrip;
}
