package com.book;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.book.config.RabbitMQConfig;
import com.book.entity.Booking;
import com.book.entity.BookingWrapper;
import com.book.entity.FlightInventory;
import com.book.exceptions.AlreadyCancelled;
import com.book.exceptions.NoEnoughSeatNumbers;
import com.book.exceptions.PnrNotFoundException;
import com.book.exceptions.SeatsNotAvailableException;
import com.book.feign.FlightClient;
import com.book.repository.BookingRepository;
import com.book.service.BookingEmailEvent;
import com.book.service.BookingService;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightClient flightClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BookingService bookingService;

    private FlightInventory flight;
    private BookingWrapper bookingWrapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        flight = new FlightInventory();
        flight.setId(1);
        flight.setTicketPrice(5000.0f);

        bookingWrapper = new BookingWrapper();
        bookingWrapper.setFlightId(1);
        bookingWrapper.setEmail("test@email.com");
        bookingWrapper.setSeatsBooked(2);
    }

    @Test
    void testSaveBookingSuccess() throws SeatsNotAvailableException, NoEnoughSeatNumbers {
        when(flightClient.getInventoryById(1)).thenReturn(flight);

        Booking saved = new Booking();
        saved.setPnr("PNR1234567");
        saved.setEmail("test@email.com");
        saved.setStatus("Booked");
        saved.setBookingDate(Date.valueOf(LocalDate.now()));
        saved.setSeatsBooked(2);
        saved.setInventoryId(1);
        saved.setTotalAmount((float) 10000.0);

        when(bookingRepository.save(any(Booking.class))).thenReturn(saved);

        String pnr = bookingService.saveBooking(bookingWrapper);

        assertNotNull(pnr);
        verify(flightClient, times(1)).getInventoryById(1);
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.EMAIL_EXCHANGE),
                        eq(RabbitMQConfig.EMAIL_ROUTING_KEY),
                        any(BookingEmailEvent.class));
    }

    @Test
    void testSaveBookingFlightNotFound() {
        when(flightClient.getInventoryById(1)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> bookingService.saveBooking(bookingWrapper));
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void testGetBooking() {
        Booking booking = new Booking();
        booking.setPnr("ABC123");
        when(bookingRepository.findById("ABC123")).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBooking("ABC123");

        assertEquals("ABC123", result.getPnr());
    }

    @Test
    void testCancelBookingSuccess() {
        Booking booking = new Booking();
        booking.setPnr("ABC123");
        booking.setStatus("Booked");

        when(bookingRepository.findById("ABC123")).thenReturn(Optional.of(booking));

        bookingService.cancelBooking("ABC123");

        assertEquals("Cancelled", booking.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void testCancelBookingAlreadyCancelled() {
        Booking booking = new Booking();
        booking.setPnr("ABC123");
        booking.setStatus("Cancelled");

        when(bookingRepository.findById("ABC123")).thenReturn(Optional.of(booking));

        assertThrows(AlreadyCancelled.class, () -> bookingService.cancelBooking("ABC123"));
    }

    @Test
    void testCancelBookingPnrNotFound() {
        when(bookingRepository.findById("XYZ999")).thenReturn(Optional.empty());

        assertThrows(PnrNotFoundException.class, () -> bookingService.cancelBooking("XYZ999"));
    }

    @Test
    void testGetBookingByEmail() {
        Booking booking = new Booking();
        booking.setEmail("abc@email.com");

        when(bookingRepository.findByEmail("abc@email.com"))
                .thenReturn(Arrays.asList(booking));

        assertEquals(1, bookingService.getBookingByE("abc@email.com").size());
    }
}
