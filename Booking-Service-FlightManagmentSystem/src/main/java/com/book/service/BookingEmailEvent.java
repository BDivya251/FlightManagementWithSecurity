package com.book.service;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEmailEvent implements Serializable {

	private String pnr;
	private String email;
	private String message; // simple text for now
}
