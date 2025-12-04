package com.mong;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.repository.FlightRepository;
import com.flight.service.FlightService;
import com.flight.utils.Converters;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private Converters converters;

    @InjectMocks
    private FlightService flightService;

    private FlightInventory flight;
    private FlightWrapper wrapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flight = new FlightInventory();
        flight.setId(1);
        flight.setFlightNumber("AI102");
        flight.setDeparture("Hyderabad");
        flight.setArrival("Delhi");
        flight.setTravelDate(Date.valueOf("2025-12-30"));
        flight.setDepartureTime(Time.valueOf("10:30:00"));
        flight.setTicketPrice(5999.0f);

        wrapper = new FlightWrapper(
                "AI102",
                "Hyderabad",
                "Delhi",
                Date.valueOf("2025-12-30"),
                Time.valueOf("10:30:00"),
                5999.0f
        );
    }

    @Test
    void testAddFlightInventory() {
        when(flightRepository.save(flight)).thenReturn(flight);

        FlightInventory result = flightService.addFlightInventory(flight);

        assertNotNull(result);
        assertEquals("AI102", result.getFlightNumber());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testGetFlightDetails() {
        when(flightRepository.findByDepartureAndArrival("Hyderabad", "Delhi"))
                .thenReturn(Arrays.asList(flight));

        when(converters.mapToWrapper(flight)).thenReturn(wrapper);

        List<FlightWrapper> result =
                flightService.getFlightDetails("Hyderabad", "Delhi");

        assertEquals(1, result.size());
        FlightWrapper w = result.get(0);

        assertEquals("AI102", w.getFlightNumber());
        assertEquals("Hyderabad", w.getDeparture());
        assertEquals("Delhi", w.getArrival());
        assertEquals(Date.valueOf("2025-12-30"), w.getTravelDate());
        assertEquals(Time.valueOf("10:30:00"), w.getDepartureTime());
        assertEquals(5999.0f, w.getTicketPrice());

        verify(flightRepository, times(1))
                .findByDepartureAndArrival("Hyderabad", "Delhi");
    }

    @Test
    void testGetInventoryByIdSuccess() {
        when(flightRepository.findById(1)).thenReturn(Optional.of(flight));

        FlightInventory result = flightService.getInventoryById(1);

        assertNotNull(result);
        assertEquals("AI102", result.getFlightNumber());
        verify(flightRepository, times(1)).findById(1);
    }

    @Test
    void testGetInventoryByIdNotFound() {
        when(flightRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> flightService.getInventoryById(99));

        assertEquals("Flight Inventory not found", ex.getMessage());
        verify(flightRepository, times(1)).findById(99);
    }
}
