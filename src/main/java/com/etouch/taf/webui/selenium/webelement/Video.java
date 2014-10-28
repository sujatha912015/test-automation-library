package com.etouch.taf.webui.selenium.webelement;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebElement;

import com.etouch.taf.util.LogUtil;

/**
 * Renders Video PageObject. . 
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class Video extends Element{

	static Log log = LogUtil.getLog(Video.class);
	
	public Video(WebElement webElement){
		super(webElement);
	}
	
	

}