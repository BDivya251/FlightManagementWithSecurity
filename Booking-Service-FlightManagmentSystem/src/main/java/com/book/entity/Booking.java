package com.book.entity;


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Booking {
	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
    private String pnr;

    private String email;          

    private String status;         

    private Integer seatsBooked;   

    private Float totalAmount;     

//    @ManyToOne
//    @JoinColumn(name = "inventory_id")
//    private FlightInventory flightInventory; 
    private Integer inventoryId;
    private Date bookingDate;
}
