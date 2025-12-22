package com.book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer age;
    private Integer seatNumber;

    @ManyToOne
    @JoinColumn(name = "booking_id",nullable=false)
    @JsonBackReference
    private Booking booking;
}
