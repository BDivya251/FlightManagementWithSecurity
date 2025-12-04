package com.book;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.book.entity.Booking;

class BookingTest {

    @Test
    void testBookingEntityFields() {
        Booking booking = new Booking();

        booking.setPnr("PNR1234567");
        booking.setEmail("demo@test.com");
        booking.setStatus("Booked");
        booking.setSeatsBooked(3);
        booking.setTotalAmount(15000.0f);
        booking.setInventoryId(10);
        Date today = Date.valueOf(LocalDate.now());
        booking.setBookingDate(today);

        assertEquals("PNR1234567", booking.getPnr());
        assertEquals("demo@test.com", booking.getEmail());
        assertEquals("Booked", booking.getStatus());
        assertEquals(3, booking.getSeatsBooked());
        assertEquals(15000.0f, booking.getTotalAmount());
        assertEquals(10, booking.getInventoryId());
        assertEquals(today, booking.getBookingDate());
    }

    @Test
    void testAllArgsConstructor() {
        Date today = Date.valueOf(LocalDate.now());

        Booking booking = new Booking(
                "PNR9876543",
                "test@mail.com",
                "Cancelled",
                2,
                8000.0f,
                12,
                today
        );

        assertEquals("PNR9876543", booking.getPnr());
        assertEquals("test@mail.com", booking.getEmail());
        assertEquals("Cancelled", booking.getStatus());
        assertEquals(2, booking.getSeatsBooked());
        assertEquals(8000.0f, booking.getTotalAmount());
        assertEquals(12, booking.getInventoryId());
        assertEquals(today, booking.getBookingDate());
    }
}
