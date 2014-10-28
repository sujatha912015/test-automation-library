package com.etouch.taf.webui.selenium.webelement;

import org.apache.commons.logging.Log;
import org.openqa.selenium.WebElement;

import com.etouch.taf.util.LogUtil;

/**
 * Renders TextBox PageObject. . 
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class TextBox extends Element{
	
	static Log log = LogUtil.getLog(TextBox.class);
	
	public TextBox(WebElement webElement){
		super(webElement);
	}
	
	/**
	 * Enters text in the textbox.
	 * @param text text to be entered.
	 */
	public void enterText(String text){
		this.clear();
		webElement.sendKeys(text);    
	
	}

	
	/**
	 * Enters text in the textbox.
	 * @param text text to be entered.
	 */
	public void clear(){
		webElement.clear();
	}
}

