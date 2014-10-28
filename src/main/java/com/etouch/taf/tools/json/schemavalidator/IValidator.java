package com.etouch.taf.tools.json.schemavalidator;

import com.etouch.taf.core.exception.ValidationException;

/**
 * Contract for validating data.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public interface IValidator {
	
	boolean validate(Object schema,Object data) throws ValidationException;

	void generateReport() throws ValidationException;
	
}
