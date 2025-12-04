package com.book.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlreadyCancelled extends RuntimeException{
	public AlreadyCancelled(String message) {
		super(message);
	}
}
