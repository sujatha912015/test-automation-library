package com.etouch.taf.core.driver.web;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.driver.DriverBuilder;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestEnvironmentType;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.webui.selenium.SeleniumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class SafariDriver.
 */
public class SafariDriver extends DriverBuilder {

	/**
	 * Instantiates a new safari driver.
	 *
	 * @param testBed the test bed
	 * @throws DriverException the driver exception
	 */
	public SafariDriver(TestBed testBed) throws DriverException {
		super(testBed);
		buildDriver();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates Driver for Safari.
	 *
	 * @throws DriverException the driver exception
	 */
	@Override
	public void buildDriver() throws DriverException {
		if (ConfigUtil.isLocalEnv()) {

			// if the tool is mentioned as selenium in devconfig.yml then create a Selenium safari Driver
			if (ConfigUtil.isSelenium()) {
				SeleniumDriver.buildSafariDriver();
			}
		} else if (ConfigUtil.isRemoteEnv()) {

			capabilities = DesiredCapabilities.safari();
			capabilities.setBrowserName(testBed.getBrowser().getName());
			capabilities.setPlatform(Platform.MAC);
			buildRemoteCapabilities();

		} else if (ConfigUtil.isBrowserStackEnv()) {

			capabilities = DesiredCapabilities.safari();
			buildBrowserstackCapabilities();
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
