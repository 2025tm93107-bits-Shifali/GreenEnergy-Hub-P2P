package com.energyhub.exception;

import java.io.IOException;

//Create a @ControllerAdvice class to handle IllegalArgumentException and return a clean JSON response with error message and status code
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.energyhub.exception.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
		ErrorResponse errorResponse = new ErrorResponse("A runtime error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
		ErrorResponse errorResponse = new ErrorResponse("A null pointer error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
		ErrorResponse errorResponse = new ErrorResponse("An illegal state error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedOperationException(UnsupportedOperationException ex) {
		ErrorResponse errorResponse = new ErrorResponse("An unsupported operation error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<ErrorResponse> handleArithmeticException(ArithmeticException ex) {
		ErrorResponse errorResponse = new ErrorResponse("An arithmetic error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ArrayIndexOutOfBoundsException.class)
	public ResponseEntity<ErrorResponse> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
				"An array index out of bounds error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ClassNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleClassNotFoundException(ClassNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse("A class not found error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
		ErrorResponse errorResponse = new ErrorResponse("An I/O error occurred: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
