package com.etouch.taf.core.exception;

import java.io.PrintWriter;

/**
 * Custom exception thrown by {@link Rally}.
 * 
 * @author eTouch Systems Corporation
 * version 1.0
 *
 */
public class DefectException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2557093441392862998L;
	
	private String message;
	
	/**
	 * @param message
	 */
	public DefectException(String message) {
		super();
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
	}

}
