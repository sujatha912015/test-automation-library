package com.etouch.taf.core.exception;

import java.io.PrintWriter;

/**
 * Custom exception thrown if exception is raised during driver build
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class DriverException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4467116203951497332L;
	
	private String message;
	
	/**
	 * @param message
	 */
	public DriverException(String message) {
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
