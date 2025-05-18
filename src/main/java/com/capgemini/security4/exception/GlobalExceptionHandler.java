package com.capgemini.security4.exception;

import java.util.HashMap;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
	

	@ExceptionHandler(Exception.class)
    public Object handleGlobalException(HttpServletRequest request, Exception ex) {
        boolean isApiRequest = request.getHeader("Accept") != null &&
                               request.getHeader("Accept").contains("application/json");

        if (!isApiRequest) {
            if (ex.getMessage() != null && ex.getMessage().contains("No static resource")) {
                return new ModelAndView("redirect:/error/404.html");
            }
            return new ModelAndView("redirect:/error/500.html");
        }

        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ✅ Specific custom exception handlers
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(404).body(error);
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

	
}
