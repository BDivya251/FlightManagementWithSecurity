package com.mong;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flight.entity.FlightInventory;
import com.flight.repository.FlightRepository;
import com.flight.service.FlightService;
import com.flight.utils.Converters;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private Converters converters;

    @InjectMocks
    private FlightService flightService;

    @Test
    void testAddFlightInventory() {
        FlightInventory flight = new FlightInventory();
        flight.setId(1);
        when(flightRepository.save(flight)).thenReturn(flight);

        FlightInventory result = flightService.addFlightInventory(flight);

        assertEquals(1, result.getId());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testGetInventoryById() {
        FlightInventory flight = new FlightInventory();
        flight.setId(10);
        when(flightRepository.findById(10)).thenReturn(Optional.of(flight));

        FlightInventory result = flightService.getInventoryById(10);

        assertEquals(10, result.getId());
        verify(flightRepository, times(1)).findById(10);
    }

   
}
