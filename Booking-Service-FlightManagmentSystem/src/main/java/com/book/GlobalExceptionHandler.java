package com.book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import com.book.exceptions.AlreadyCancelled;
import com.book.exceptions.PnrNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyCancelled.class)
    public ResponseEntity<String> handleCancelledFlight(AlreadyCancelled ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); 
    }
    
    @ExceptionHandler(PnrNotFoundException.class)
    public ResponseEntity<String> handlePnrNotFound(PnrNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
}
