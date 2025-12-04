package com.book;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.book.entity.Booking;
import com.book.repository.BookingRepository;

class BookingRepositoryTest {

	@Mock
	private BookingRepository bookingRepository;

	private Booking booking1;
	private Booking booking2;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		booking1 = new Booking();
		booking1.setPnr("PNR001");
		booking1.setEmail("demo@test.com");

		booking2 = new Booking();
		booking2.setPnr("PNR002");
		booking2.setEmail("demo@test.com");
	}

	@Test
	void testFindById() {
		when(bookingRepository.findById("PNR001")).thenReturn(Optional.of(booking1));

		Booking result = bookingRepository.findById("PNR001").orElse(null);

		assertNotNull(result);
		assertEquals("demo@test.com", result.getEmail());
		verify(bookingRepository, times(1)).findById("PNR001");
	}

	@Test
	void testFindByEmail() {
		when(bookingRepository.findByEmail("demo@test.com")).thenReturn(Arrays.asList(booking1, booking2));

		List<Booking> result = bookingRepository.findByEmail("demo@test.com");

		assertEquals(2, result.size());
		verify(bookingRepository, times(1)).findByEmail("demo@test.com");
	}

	@Test
	void testSave() {
		when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

		Booking saved = bookingRepository.save(booking1);

		assertEquals("PNR001", saved.getPnr());
		verify(bookingRepository, times(1)).save(booking1);
	}
}
