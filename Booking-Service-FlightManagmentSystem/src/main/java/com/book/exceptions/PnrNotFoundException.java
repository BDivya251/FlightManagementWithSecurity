package com.book.exceptions;

public class PnrNotFoundException extends RuntimeException {
	public PnrNotFoundException(String message) {
		super(message);
	}
}
