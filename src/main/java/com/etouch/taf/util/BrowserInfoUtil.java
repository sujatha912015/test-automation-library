package com.etouch.taf.util;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.resources.TestBedType;

/**
 * This enum class checks on browser type.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public enum BrowserInfoUtil {
	INSTANCE;
	
	private String testBedName;
	
	private String getBrowserName() {
		return TestBedManager.INSTANCE.getCurrentTestBed().getTestBedName();
	}

	public void setBrowserName(String browserName) {
		this.testBedName = browserName;
	}

	/**
	 * check the current test is running on IE or non-IE browsers
	 * 
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isIE() {
		return getBrowserName().toUpperCase().contains("IE");
	}

	/**
	 * check the current test is running on IE or non-IE browsers
	 * 
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isIE8() {
		return TestBedType.InternetExplorer.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	

	/**
	 * check the current test is running on IE or non-IE browsers
	 * 
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isFF() {
		return TestBedType.Firefox.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	/**
	 * check the current test is running on IE or non-IE browsers
	 * 
	 * @return return true if it's a IE browser test, else it will return false
	 */
	public boolean isChrome() {
		return TestBedType.Chrome.getTestBedName().equalsIgnoreCase(getBrowserName());
	}

	/**
	 * check the current test is running on Safari or non-Safari browsers
	 * 
	 * @return return true if it's a Safari browser test, else it will return false
	 */
	public boolean isSafari() {
		return TestBedType.Safari.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	
	public boolean isAndroid() {
		return TestBedType.AndroidWebEm.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	public boolean isIOS() {
		return TestBedType.iOS.getTestBedName().equalsIgnoreCase(getBrowserName());
	}
	
}

