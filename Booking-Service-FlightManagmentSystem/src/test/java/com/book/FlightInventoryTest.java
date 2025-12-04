package com.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.sql.Time;
import org.junit.jupiter.api.Test;

import com.book.entity.FlightInventory;

public class FlightInventoryTest {

    @Test
    void testGettersAndSetters() {

        FlightInventory flight = new FlightInventory();
        Date travelDate = Date.valueOf("2025-12-10");
        Time depTime = Time.valueOf("10:30:00");
        Time arrTime = Time.valueOf("13:45:00");

        flight.setId(1);
        flight.setFlightNumber("AI101");
        flight.setDeparture("Delhi");
        flight.setArrival("Mumbai");
        flight.setTravelDate(travelDate);
        flight.setDepartureTime(depTime);
        flight.setArrivalTime(arrTime);
        flight.setAvailableSeats(50);
        flight.setTicketPrice(4500.75f);

        assertThat(flight.getId()).isEqualTo(1);
        assertThat(flight.getFlightNumber()).isEqualTo("AI101");
        assertThat(flight.getDeparture()).isEqualTo("Delhi");
        assertThat(flight.getArrival()).isEqualTo("Mumbai");
        assertThat(flight.getTravelDate()).isEqualTo(travelDate);
        assertThat(flight.getDepartureTime()).isEqualTo(depTime);
        assertThat(flight.getArrivalTime()).isEqualTo(arrTime);
        assertThat(flight.getAvailableSeats()).isEqualTo(50);
        assertThat(flight.getTicketPrice()).isEqualTo(4500.75f);
    }

    @Test
    void testAllArgsConstructor() {
        Date travelDate = Date.valueOf("2025-12-10");
        Time depTime = Time.valueOf("10:00:00");
        Time arrTime = Time.valueOf("12:00:00");

        FlightInventory flight = new FlightInventory(
                5, "AI202", "Chennai", "Hyderabad",
                travelDate, depTime, arrTime, 100, 3500.50f
        );

        assertThat(flight.getId()).isEqualTo(5);
        assertThat(flight.getFlightNumber()).isEqualTo("AI202");
        assertThat(flight.getDeparture()).isEqualTo("Chennai");
    }
}
