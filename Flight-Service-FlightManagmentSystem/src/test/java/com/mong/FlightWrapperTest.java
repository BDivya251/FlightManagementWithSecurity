package com.mong;

//package com.flight.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.flight.entity.FlightWrapper;

import java.sql.Date;
import java.sql.Time;

public class FlightWrapperTest {

    @Test
    void testConstructorAndGetters() {
        Date date = Date.valueOf("2025-01-20");
        Time dep = Time.valueOf("10:00:00");

        FlightWrapper fw = new FlightWrapper(
                "AI202",
                "Delhi",
                "Mumbai",
                date,
                dep,
                4500.50f
        );

        assertEquals("AI202", fw.getFlightNumber());
        assertEquals("Delhi", fw.getDeparture());
        assertEquals("Mumbai", fw.getArrival());
        assertEquals(date, fw.getTravelDate());
        assertEquals(dep, fw.getDepartureTime());
        assertEquals(4500.50f, fw.getTicketPrice());
    }

    @Test
    void testSetters() {
        FlightWrapper fw = new FlightWrapper();
        fw.setFlightNumber("TG109");
        fw.setTicketPrice(2500.0f);

        assertEquals("TG109", fw.getFlightNumber());
        assertEquals(2500.0f, fw.getTicketPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        FlightWrapper w1 = new FlightWrapper();
        FlightWrapper w2 = new FlightWrapper();
        assertEquals(w1, w2);
        assertEquals(w1.hashCode(), w2.hashCode());
    }

    @Test
    void testToString() {
        FlightWrapper fw = new FlightWrapper();
        assertNotNull(fw.toString());
    }
}
