package com.book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import com.book.exceptions.AlreadyCancelled;
import com.book.exceptions.NoEnoughSeatNumbers;
import com.book.exceptions.NotAValidFlightId;
import com.book.exceptions.PnrNotFoundException;
import com.book.exceptions.SeatsNotAvailableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyCancelled.class)
    public ResponseEntity<String> handleCancelledFlight(AlreadyCancelled ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); 
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        System.out.println("Validation failed: " + msg); 
        return ResponseEntity.badRequest().body(msg);
    }
    
    @ExceptionHandler(PnrNotFoundException.class)
    public ResponseEntity<String> handlePnrNotFound(PnrNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(SeatsNotAvailableException.class)
    public ResponseEntity<String> handleSeatsLimit(SeatsNotAvailableException ex){
    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NotAValidFlightId.class)
    public ResponseEntity<String> handleNotAValidFlight(NotAValidFlightId ex){
    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoEnoughSeatNumbers.class)
    public ResponseEntity<String> handleNoSeatNumbers(NoEnoughSeatNumbers ex){
    	return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
