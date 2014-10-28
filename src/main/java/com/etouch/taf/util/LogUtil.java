package com.etouch.taf.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class contains log methods.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class LogUtil {

	/*
	 * Method for log4j implementation, here the input parameter is the class name
	 * It reads the log4j.xml and prints logging information accordingly.
 	 * Usage is as follows:
	 * Add this line in your class:
	 * private static Log logger = getLog(<class name>);
	 */
	public static Log getLog (Class<?> className, String fileName) {
		Log logger = LogFactory.getLog(className);
		File myFile = new File(fileName);
		DOMConfigurator.configure(myFile.getAbsolutePath());
		return logger;
	}
	
	/**
	 * This will create the log with the default file name log4j.xml
	 * @param className
	 * @return
	 */
	public static Log getLog (Class<?> className) {
		return getLog(className, "log4j.xml");
	}
	
	/*
	 *Method for log4j formatting of the message
	 *
	 */
	public String printLogMessage(String message){
		return message;
	}
  
}
