package com.etouch.taf.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.xml.sax.SAXException;

import com.etouch.taf.core.config.DefectConfig;
import com.etouch.taf.core.config.TestngConfig;
import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.config.WebConfig;
import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.DefectToolType;
import com.etouch.taf.tools.defect.DefectManager;
import com.etouch.taf.tools.defect.DefectManagerFactory;
import com.etouch.taf.util.ConfigUtil;
import com.etouch.taf.util.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * Test Bed Manager loads the profile for the test environment. This class
 * initializes {@link TestBed}
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public enum TestBedManager {
	
	/** The instance to create a Singleton object of TestbedManager */
	INSTANCE;

	/** The log. */
	static Log log = LogUtil.getLog(TestBedManager.class);
	
	/** This object helps to hold information about the testbed which is executing right now */
	private TestBed currentTestBed=null;
	
	/**
	 * Gets the current test bed.
	 *
	 * @return the current test bed
	 */
	public TestBed getCurrentTestBed() {
		return currentTestBed;
	}

	/**
	 * Sets the current test bed.
	 *
	 * @param currentTestBed the new current test bed
	 */
	public void setCurrentTestBed(TestBed currentTestBed) {
		this.currentTestBed = currentTestBed;
	}

	/** The defect. */
	private DefectManager defect = null;


	/**
	 * Reads configuration file and create profile and sets in test bed.
	 *
	 * @param ipStream the new config
	 * @throws DriverException the driver exception
	 * @throws DefectException the defect exception
	 */

	public void setConfig(InputStream ipStream) {
		
			TestBedManagerConfiguration.setIpStream(ipStream);
			TestBedManagerConfiguration.getInstance();
	}
	
	
	/**
	 * 
	 * @throws DefectException
	 */
	public void createDefectManager() throws DefectException{
		
		if(ConfigUtil.isDefectToolSupported()){
			
		
		this.defect = DefectManagerFactory
				.manageDefect(TestBedManagerConfiguration.getInstance().getDefectConfig().getDefectTool());
	
		}
	}



	
	/**
 * Execute test ng.
 */
public void executeTestNG() {
		
		TestNG testNG = TestSuiteManager.INSTANCE
				.buildTestSuites();
		
		log.info("Test suites begin to run");
		testNG.run();
		
	}
	
	
	
//	/**
//	 * Handle for existing suites.
//	 *
//	 * @param ipStream the ip stream
//	 * @return the list
//	 */
//	private static List<XmlSuite> handleForExistingSuites(InputStream ipStream) {
//		List<XmlSuite> existingSuite = null;
//		try {
//
//			existingSuite = (List<XmlSuite>) new Parser(ipStream).parse();
//			
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			System.out.println("ParserConfigurationException occured "
//					+ e.getMessage());
//			e.printStackTrace();
//
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			System.out.println("SAXException occured " + e.getMessage());
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println("IOException occured " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		return existingSuite;
//	}

	

	

	/**
	 * Returns profile.
	 * 
	 * @return Profile configuration instance.
	 */
	public TestBedManagerConfiguration getProfile() {
		return TestBedManagerConfiguration.getInstance();
	}

	
	/**
	 * Returns base URL.
	 * 
	 * @return profile base URL
	 */
	public String loadBaseURL() {
		WebConfig webConfig=TestBedManagerConfiguration.getInstance().getWebConfig();
			return webConfig.getURL();
	}
	
	

	/**
	 * Returns Test bed.
	 * 
	 * @return TestBed
	 */
	public  TestBed getTestBed() {
		return currentTestBed;
	}

	/**
	 * Returns defect instance.
	 * 
	 * @return DefectManager
	 */
	public DefectManager getDefect() {
		return this.defect;
	}
	
	
	/**
	 * Gets the defect config.
	 *
	 * @return the defect config
	 */
	public DefectConfig getDefectConfig()
	{
		return TestBedManagerConfiguration.INSTANCE.getDefectConfig();
	}
	
	/**
	 * Gets the testng config.
	 *
	 * @return the testng config
	 */
	public TestngConfig getTestngConfig()
	{
		return TestBedManagerConfiguration.INSTANCE.getTestngConfig();
	}

}
