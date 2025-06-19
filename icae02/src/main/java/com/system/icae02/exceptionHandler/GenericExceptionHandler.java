package com.system.icae02.exceptionHandler;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GenericExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Error> handleEntityNotFound(EntityNotFoundException exception) {
		Error errorResponse = new Error(exception.getMessage());
		return new ResponseEntity<Error>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Error> handleDuplicateKey(DuplicateKeyException exception) {
		Error errorResponse = new Error(exception.getMessage());
		return new ResponseEntity<Error>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Error> handleCommonExpections(DuplicateKeyException exception) {
		Error errorResponse = new Error(exception.getMessage());
		return new ResponseEntity<Error>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}