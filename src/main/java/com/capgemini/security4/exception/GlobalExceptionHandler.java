package com.capgemini.security4.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserNotFoundException ex) {
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

	@ExceptionHandler(PartyAlreadyExistsException.class)
	public ResponseEntity<String> handlePartyAlreadyExistsException(PartyAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(PartyNotFoundException.class)
	public ResponseEntity<String> handlePartyAlreadyExistsException(PartyNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(ResultNotFoundException.class)
	public ResponseEntity<String> handleResultNotFoundException(ResultNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(DuplicateVoteException.class) 
	public ResponseEntity<String> handleDuplicateVote(DuplicateVoteException ex) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidVoteException.class)
	public ResponseEntity<String> handleInvalidVote(InvalidVoteException ex) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
	
	@ExceptionHandler(CandidateAlreadyExistException.class)
	public ResponseEntity<String> handleCandidateAlreadyExistsException(UserAlreadyExistsException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
}
