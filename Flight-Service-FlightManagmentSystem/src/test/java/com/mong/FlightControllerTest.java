package com.mong;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.FlightController;
import com.flight.entity.FlightInventory;
import com.flight.entity.FlightWrapper;
import com.flight.service.FlightService;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private FlightInventory flight;
    private FlightWrapper wrapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();
        objectMapper = new ObjectMapper();

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
    void testAddFlights() throws Exception {
        when(flightService.addFlightInventory(any())).thenReturn(flight);

        mockMvc.perform(post("/flight/airline/inventary/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AI102"))
                .andExpect(jsonPath("$.departure").value("Hyderabad"))
                .andExpect(jsonPath("$.arrival").value("Delhi"));

        verify(flightService, times(1)).addFlightInventory(any());
    }

    @Test
    void testSearchFlight() throws Exception {
        when(flightService.getFlightDetails("Hyderabad", "Delhi"))
                .thenReturn(Arrays.asList(wrapper));

        mockMvc.perform(get("/flight/search")
                .param("departure", "Hyderabad")
                .param("arrival", "Delhi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("AI102"))
                .andExpect(jsonPath("$[0].departure").value("Hyderabad"))
                .andExpect(jsonPath("$[0].arrival").value("Delhi"));

        verify(flightService, times(1)).getFlightDetails("Hyderabad", "Delhi");
    }

    @Test
    void testGetInventoryById() throws Exception {
        when(flightService.getInventoryById(1)).thenReturn(flight);

        mockMvc.perform(get("/flight/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("AI102"))
                .andExpect(jsonPath("$.arrival").value("Delhi"))
                .andExpect(jsonPath("$.ticketPrice").value(5999.0));

        verify(flightService, times(1)).getInventoryById(1);
    }
}
