package com.etouch.taf.core.exception;

import java.io.PrintWriter;

/**
 * Custom exception thrown by {@link DateUtil}.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class DateException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8725397299202741008L;
	
	private String message;
	
	
	/**
	 * @param message
	 */
	public DateException(String message) {
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
