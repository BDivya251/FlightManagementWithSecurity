package com.book.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude="passengers")
public class Booking {
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private String pnr;

	private String email;

	private String status;

	private Integer seatsBooked;

	private Float totalAmount;
	
//	private List<Integer> seatNumbers;
	 @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval=true)
	 @JsonBackReference
	    private List<Passenger> passengers;

//    @ManyToOne
//    @JoinColumn(name = "inventory_id",nullable=false)
//    private FlightInventory flightInventory; 
	private String flightNumber;
	private LocalDate bookingDate;
	private LocalDate departureDate;
	private LocalTime departureTime;
	private LocalTime bookingTime;
}
