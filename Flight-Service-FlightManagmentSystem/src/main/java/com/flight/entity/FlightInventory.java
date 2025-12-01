package com.flight.entity;

import java.sql.Date;
import java.sql.Time;

import jakarta.annotation.Generated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightInventory {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    private String flightNumber;
    private String departure;           
    private String arrival;             
    private Date travelDate;          
    private Time departureTime;      
    private Time arrivalTime;         
    private Integer availableSeats;
    private float ticketPrice;
}
