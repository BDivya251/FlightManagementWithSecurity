package com.book;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.book.entity.BookingWrapper;

public class BookingWrapperTest {

//    @Test
//    void testConstructorAndGetters() {
//        BookingWrapper bw = new BookingWrapper(
//                "test@gmail.com",
//                2,
//                10
//        );
//
//        assertEquals("test@gmail.com", bw.getEmail());
//        assertEquals(2, bw.getSeatsBooked());
//        assertEquals(10, bw.getFlightId());
//    }

    @Test
    void testSetters() {
        BookingWrapper bw = new BookingWrapper();
        bw.setEmail("abc@gmail.com");
        bw.setSeatsBooked(3);
        bw.setFlightId(99);

        assertEquals("abc@gmail.com", bw.getEmail());
        assertEquals(3, bw.getSeatsBooked());
        assertEquals(99, bw.getFlightId());
    }

    @Test
    void testEqualsAndHashCode() {
        BookingWrapper b1 = new BookingWrapper();
        BookingWrapper b2 = new BookingWrapper();

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testToString() {
        BookingWrapper bw = new BookingWrapper();
        assertNotNull(bw.toString());
    }
}
