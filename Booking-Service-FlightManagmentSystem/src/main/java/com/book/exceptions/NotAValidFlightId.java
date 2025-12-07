package com.book.exceptions;

public class NotAValidFlightId extends RuntimeException{
	public NotAValidFlightId(String msg) {
		super(msg);
	}
}
