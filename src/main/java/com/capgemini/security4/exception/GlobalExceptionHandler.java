package com.capgemini.security4.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(CandidateNotFound.class)
	public ResponseEntity<String> handleCandidateNotFoundException(CandidateNotFound ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(ElectionNotFoundException.class)
	public ResponseEntity<String> handleElectionNotFoundException(ElectionNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(ElectionAlreadyExistException.class)
	public ResponseEntity<String> handleElectionAlreadyExistException(ElectionAlreadyExistException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(PartyAlreadyExistsException.class)
	public ResponseEntity<String> handlePartyAlreadyExistsException(PartyAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(PartyNotFoundException.class)
	public ResponseEntity<String> handlePartyNotFoundException(PartyNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(ResultNotFoundException.class)
	public ResponseEntity<String> handleResultNotFoundException(ResultNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(DuplicateVoteException.class) 
	public ResponseEntity<String> handleDuplicateVoteException(DuplicateVoteException ex) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidVoteException.class)
	public ResponseEntity<String> handleInvalidVoteException(InvalidVoteException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  	}

	@ExceptionHandler(CandidateAlreadyExistException.class)
	public ResponseEntity<String> handleCandidateAlreadyExistsException(CandidateAlreadyExistException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	// Generic fallback for any other unhandled exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred: " + ex.getMessage());
	}
}
