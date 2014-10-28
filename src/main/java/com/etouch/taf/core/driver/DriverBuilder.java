package com.etouch.taf.core.driver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestBedType;
import com.etouch.taf.core.resources.TestEnvironmentType;
import com.etouch.taf.util.LogUtil;

import org.apache.commons.logging.Log;
import org.openqa.selenium.android.AndroidDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.Platform;
//import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public abstract class DriverBuilder {

	protected TestBedManagerConfiguration tbMgrConfig=TestBedManagerConfiguration.getInstance();
	protected TestBed testBed = TestBedManager.INSTANCE.getCurrentTestBed();
	protected DesiredCapabilities capabilities=null;
	protected String hubLocation=null;
	protected Object driver=null;
	protected String browserStackURL = null;
	protected String testEnvironment= tbMgrConfig.getWebConfig().getTestEnv();
	
	static Log log = LogUtil.getLog(DriverBuilder.class);

	/**
	 * @param testBed
	 * @throws DriverException
	 */
	public DriverBuilder(TestBed testBed) throws DriverException {
		this.testBed = testBed;
	}

	 /**
	 * @throws DriverException
	 */
	public abstract void buildDriver() throws DriverException;
	 
	 /**
	 * @return Object of an Driver
	 * @throws DriverException
	 */
	public abstract Object getDriver() throws DriverException;


	/*protected void setHubAndPort() {
		String hub = testBed.getHub();
		String port = testBed.getPort();

		// defalut hub to localhost
		if (hub == null || hub.isEmpty()) {
			hub = "localhost";
		}

		// default port to 4444
		if (port == null || port.isEmpty()) {
			port = "4444";
		}

		// add proxy setting to the webdriver
		String proxyString = testBed.getProxy();
		String noProxyString = testBed.getNoProxy();
		if (proxyString != null && !proxyString.isEmpty()) {
			Proxy proxysetting = new Proxy();
			proxysetting.setHttpProxy(proxyString)
					.setFtpProxy(proxyString).setSslProxy(proxyString);
			if (noProxyString != null && !proxyString.isEmpty()) {
				proxysetting.setNoProxy(noProxyString);
			}

			capabilities.setCapability(CapabilityType.PROXY,
					proxysetting);
		}

		hubLocation = "http://" + hub + ":" + port + "/wd/hub";
	}*/


	/**
	 * This method set the details for BrowserStackDriver
	 * @throws DriverException
	 */
	public void buildBrowserstackCapabilities() throws DriverException
	{
		capabilities.setCapability("osVersion", testBed.getPlatform().getVersion());
		capabilities.setCapability("os", testBed.getPlatform().getName());
		capabilities.setCapability("version", testBed.getBrowser().getVersion());
		capabilities.setCapability("browserName", testBed.getBrowser().getName());
		capabilities.setCapability("browserstack.debug", "true");
		try {
			driver = new RemoteWebDriver(new URL(buildBrowserStackUrl()), capabilities);
		} catch (MalformedURLException e) {
			log.error("failed to create driver : " + e.getMessage());
			throw new DriverException("failed to create driver : " + e.getMessage());
		}

	}
	/**
	 * This method set details for RemoteWebDriver
	 * @throws DriverException 
	 * @throws ProfileBuilderException
	 */
	public void buildRemoteCapabilities() throws DriverException
	{
		try {
			driver = new RemoteWebDriver(new URL(hubLocation), capabilities);
		} catch (MalformedURLException e) {
			log.error("failed to create driver : " + e.getMessage());
			throw new DriverException("failed to create driver : " + e.getMessage());
		}
	}
	
	/**
	 * Get the BrowserStackURL from configuration file
	 * @return browserStackURL
	 */
	public String buildBrowserStackUrl(){
		return browserStackURL;
	}
	
	
}
