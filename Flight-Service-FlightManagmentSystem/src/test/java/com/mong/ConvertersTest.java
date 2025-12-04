package com.mong;

//package com.flight.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.utils.Converters;

class ConvertersTest {

    private Converters converters;
    private FlightInventory flight;

    @BeforeEach
    void setUp() {
        converters = new Converters();

        flight = new FlightInventory();
        flight.setId(1);
        flight.setFlightNumber("AI102");
        flight.setDeparture("Hyderabad");
        flight.setArrival("Delhi");
        flight.setTravelDate(Date.valueOf("2025-12-30"));
        flight.setDepartureTime(Time.valueOf("10:30:00"));
        flight.setTicketPrice(5999.0f);
    }

    @Test
    void testMapToWrapper() {
        FlightWrapper wrapper = converters.mapToWrapper(flight);

        assertNotNull(wrapper);
        assertEquals("AI102", wrapper.getFlightNumber());
        assertEquals("Hyderabad", wrapper.getDeparture());
        assertEquals("Delhi", wrapper.getArrival());
        assertEquals(Date.valueOf("2025-12-30"), wrapper.getTravelDate());
        assertEquals(Time.valueOf("10:30:00"), wrapper.getDepartureTime());
        assertEquals(5999.0f, wrapper.getTicketPrice());
    }
}
