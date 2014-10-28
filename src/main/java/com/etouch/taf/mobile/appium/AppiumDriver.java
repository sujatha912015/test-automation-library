package com.etouch.taf.mobile.appium;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.DeviceType;
import com.etouch.taf.core.resources.PlatformType;
import com.etouch.taf.util.LogUtil;


public class AppiumDriver {

	private static Log log = LogUtil.getLog(AppiumDriver.class);
	
	public static RemoteWebDriver buildDriver(TestBed testBed) throws DriverException
	{
		
		RemoteWebDriver appiumWebDriver=null;
		TestBedManagerConfiguration tbMgrConfig=TestBedManagerConfiguration.getInstance();
		 //Create RemoteWebDriver instance and connect to the Appium server.
		try {
			DesiredCapabilities cap=createCapabilities(testBed);
			appiumWebDriver=new RemoteWebDriver(new URL("http://"+ tbMgrConfig.getMobileConfig().getHub() +":" + tbMgrConfig.getMobileConfig().getPort() + "/wd/hub"),cap);
		} catch (MalformedURLException e) {
			log.error("failed to create appium remote webDriver : " + e.getMessage());
			throw new DriverException("Could not create appium remote webdriver :: " + e.getMessage());
		}
		return  appiumWebDriver;
		
	}

	private static DesiredCapabilities createCapabilities(TestBed testBed) {
		DesiredCapabilities capabilities=null;
		if(testBed.getPlatform().getName().equalsIgnoreCase(PlatformType.Android.getName())){
			capabilities= createAndroidDriver(testBed);
		
		}else if(testBed.getPlatform().getName().equalsIgnoreCase(PlatformType.iOS.getName())){
			capabilities= createiOSDriver(testBed);
		}
		return capabilities;
		
	}

	private static DesiredCapabilities createiOSDriver(TestBed testBed) {
		
		DesiredCapabilities capabilities=new DesiredCapabilities();
		
		capabilities.setCapability("bundleID", testBed.getApp().getBundleId());
		capabilities.setCapability("app", testBed.getApp().getBundleId());
		capabilities.setCapability(CapabilityType.BROWSER_NAME, testBed.getBrowser().getName());
		capabilities.setCapability(CapabilityType.VERSION, testBed.getBrowser().getVersion());
		capabilities.setCapability("platformName", testBed.getPlatform().getName());
		
		if(testBed.getApp().getAppPath()!=null){
			capabilities.setCapability("app", testBed.getApp().getAppPath());
		}
		
		
		if((testBed.getDevice().getType()!=null) && (testBed.getDevice().getType().equalsIgnoreCase(DeviceType.Simulator.toString())))
		{
			updateiOSSimulator(testBed,capabilities);
		}else {
			updateiOSDevice(testBed, capabilities);
			
		}
		return capabilities;
		
	}

	private static void updateiOSDevice(TestBed testBed,DesiredCapabilities capabilities) {
		capabilities.setCapability("UDID",testBed.getDevice().getUdid());
		capabilities.setCapability("deviceName",testBed.getDevice().getName());
	}

	private static void updateiOSSimulator(TestBed testBed,DesiredCapabilities capabilities) {
		capabilities.setCapability("deviceName", testBed.getDevice().getName());
		
	}

	
	
	
	
	
	private static DesiredCapabilities createAndroidDriver(TestBed testBed) {
		DesiredCapabilities capabilities=new DesiredCapabilities();
		
		capabilities.setCapability(CapabilityType.VERSION, testBed.getBrowser().getVersion());
		capabilities.setCapability(CapabilityType.PLATFORM, testBed.getPlatform().getName());
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		
		capabilities.setCapability("app-package", testBed.getApp().getAppPackage()); 
		capabilities.setCapability("app-activity", testBed.getApp().getAppActivity());
		if(testBed.getDevice().getType().equalsIgnoreCase(DeviceType.Emulator.toString()))
		{
			updateAndroidEmulator(testBed,capabilities);
		}else {
			updateAndroidDevice(testBed,capabilities);
		}
	
		return capabilities;
		
	}

	private static void updateAndroidDevice(TestBed testBed,DesiredCapabilities capabilities) {
		capabilities.setCapability("device",testBed.getDevice().getName());
		
	}

	private static void updateAndroidEmulator(TestBed testBed,DesiredCapabilities capabilities) {
		capabilities.setCapability("device",testBed.getDevice().getName());
		// have to find out a way how to prioritize the given device 
		capabilities.setCapability("device-id",testBed.getDevice().getId());
		
		
	}
	
//	public static io.appium.java_client.AppiumDriver buildAppiumDriver(DesiredCapabilities capabilities) throws ProfileBuilderException
//	{
//		
//		io.appium.java_client.AppiumDriver appiumDriver=new io.appium.java_client.AppiumDriver(new URL(""), capabilities);
//		
//		
//	}


}
