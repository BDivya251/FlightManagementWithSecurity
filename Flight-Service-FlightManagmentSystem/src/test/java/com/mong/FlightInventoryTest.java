package com.mong;

//package com.flight.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.flight.entity.FlightInventory;

import java.sql.Date;
import java.sql.Time;

public class FlightInventoryTest {

    @Test
    void testConstructorAndGetters() {
        Date date = Date.valueOf("2025-01-20");
        Time dep = Time.valueOf("10:00:00");
        Time arr = Time.valueOf("12:00:00");

        FlightInventory fi = new FlightInventory(
                1,
                "AI202",
                "Delhi",
                "Mumbai",
                date,
                dep,
                arr,
                100,
                4500.50f
        );

        assertEquals(1, fi.getId());
        assertEquals("AI202", fi.getFlightNumber());
        assertEquals("Delhi", fi.getDeparture());
        assertEquals("Mumbai", fi.getArrival());
        assertEquals(date, fi.getTravelDate());
        assertEquals(dep, fi.getDepartureTime());
        assertEquals(arr, fi.getArrivalTime());
        assertEquals(100, fi.getAvailableSeats());
        assertEquals(4500.50f, fi.getTicketPrice());
    }

    @Test
    void testSetters() {
        FlightInventory fi = new FlightInventory();
        fi.setAvailableSeats(50);
        fi.setTicketPrice(3000.0f);

        assertEquals(50, fi.getAvailableSeats());
        assertEquals(3000.0f, fi.getTicketPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        FlightInventory f1 = new FlightInventory();
        FlightInventory f2 = new FlightInventory();
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    void testToString() {
        FlightInventory fi = new FlightInventory();
        assertNotNull(fi.toString());
    }
}
