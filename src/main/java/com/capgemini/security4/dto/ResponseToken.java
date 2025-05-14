package com.capgemini.security4.dto;

public class ResponseToken {
	String token;

	public ResponseToken() {
		super();
	}

	public ResponseToken(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
