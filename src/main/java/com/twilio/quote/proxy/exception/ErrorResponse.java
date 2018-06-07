package com.twilio.quote.proxy.exception;

public class ErrorResponse {
	private String message;

	ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
