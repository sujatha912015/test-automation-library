package com.etouch.taf.webui.selenium;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.driver.Driver;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.resources.PlatformType;



// TODO: Auto-generated Javadoc
/**
 * The Class SeleniumDriver.
 */
public class SeleniumDriver {

	
	/**
	 * Builds the fire fox driver.
	 *
	 * @return the firefox driver
	 */
	public static FirefoxDriver buildFireFoxDriver(){
		return new FirefoxDriver();
	}
	
	/**
	 * Builds the fire fox driver.
	 *
	 * @param ffProfile the ff profile
	 * @return the firefox driver
	 */
	public static FirefoxDriver buildFireFoxDriver(FirefoxProfile ffProfile){
		return new FirefoxDriver(ffProfile);
	}

	/**
	 * Builds the safari driver.
	 *
	 * @return the safari driver
	 */
	public static SafariDriver buildSafariDriver(){
		System.out.println(" Safari is building Driver");
		//DesiredCapabilities dc = DesiredCapabilities.safari();
		return new SafariDriver();
	}
	
	/**
	 * Builds the chrome driver.
	 *
	 * @param file the file
	 * @return the chrome driver
	 */
	public static ChromeDriver buildChromeDriver(File file){

		if(file!=null){
			
			System.setProperty("webdriver.ie.driver",file.getPath());
		}
		
		return new ChromeDriver();
	}

	
	/**
	 * Builds the ie driver.
	 *
	 * @return the internet explorer driver
	 */
	public static InternetExplorerDriver buildIEDriver(){
		File file = new File("/Users/eTouch/Documents/workspace-mobile/test-automation-library/resources/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath()); 
		return new InternetExplorerDriver();
		
	}
}
