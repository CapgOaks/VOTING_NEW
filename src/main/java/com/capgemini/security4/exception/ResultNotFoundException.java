package com.capgemini.security4.exception;

public class ResultNotFoundException extends RuntimeException {
	public ResultNotFoundException(Long id) {
		super("Result with ID:" + id + " not found!");
	}
}
