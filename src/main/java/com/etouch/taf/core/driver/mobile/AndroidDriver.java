package com.etouch.taf.core.driver.mobile;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestEnvironmentType;
import com.etouch.taf.mobile.appium.AppiumDriver;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.core.resources.TestToolType;

// TODO: Auto-generated Javadoc
/**
 * The Class AndroidDriver.
 */
public class AndroidDriver extends DriverBuilder{
	
	/** The log. */
	static Log log=LogUtil.getLog(AndroidDriver.class);

	/**
	 * Instantiates a new android driver.
	 *
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public AndroidDriver(TestBed testBed)
			throws DriverException {
		super(testBed);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Creates driver for Android.
	 *
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException
	{
		try{
			if (ConfigUtil.isLocalEnv()) {
				
				if(ConfigUtil.isAppium()){
					
					driver = AppiumDriver.buildDriver(testBed);
				}
				
			} else if (ConfigUtil.isRemoteEnv()) {
					DesiredCapabilities browser = DesiredCapabilities.android();
					driver = new RemoteWebDriver(new URL(hubLocation), browser);
					
			} else if(ConfigUtil.isBrowserStackEnv()){

				capabilities = DesiredCapabilities.android();
				capabilities.setCapability("device",testBed.getDevice().getName());
				buildBrowserstackCapabilities();

			}


	} catch (MalformedURLException e) {
			log.error("failed to create driver for android : " + e.getMessage());
			throw new DriverException("Could not create driver :: " + e.getMessage());
	}

	}
	
	
	/* (non-Javadoc)
	 * @see com.etouch.taf.core.driver.DriverBuilder#getDriver()
	 */
	@Override
	public Object getDriver() throws DriverException {
		// TODO Auto-generated method stub
		return driver;
	}

}
