package com.etouch.taf.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.xml.sax.SAXException;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.config.TestngConfig;
import com.etouch.taf.util.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Enum TestSuiteManager.
 */
public enum TestSuiteManager {
	
	/** The instance. */
	INSTANCE;

	/** The log. */
	static Log log = LogUtil.getLog(TestSuiteManager.class);

	/**
	 * This method will read testNG information from TestBedManagerConfiguration
	 * and Will create a first testSuite and will createRest of the testSuites, 
	 * based on the given testbed details .
	 *
	 * @return the test ng
	 */
	public TestNG buildTestSuites() {
		TestNG testng = new TestNG();
		
		/** Creates first testBed */
		List<XmlSuite> existingSuiteList = createFirstTestNGSuite();
		testng.setXmlSuites(existingSuiteList);
		addListenerToTestNG(existingSuiteList);

		return testng;
	}
	
	
	/**
	 * Creates the first TestSuite based on the information given in 
	 * devConfig.yml under testngConfig
	 *
	 * @return the list
	 */
	private List<XmlSuite> createFirstTestNGSuite() {

		TestBedManagerConfiguration testBedManagerConfig=TestBedManagerConfiguration.getInstance();
		TestngConfig testngConfig = testBedManagerConfig.getTestngConfig();
		
		List<XmlSuite> existingSuite = new ArrayList<XmlSuite>();
		List<XmlTest> testList = new ArrayList<XmlTest>();
		List<XmlClass> classList = new ArrayList<XmlClass>();
		List<XmlPackage> xmlPackages = new ArrayList<XmlPackage>();
		List<String> listeners = new ArrayList<String>();
		Map<String, String> paramsMap = new HashMap<String, String>();
	
		// Add Listeners
		listeners.add(testngConfig.getListener());

		// Populate the XmlClass and add to classList using the list of class
		// Names mentioned in the devconfig.yml->TestngConfig
		try {
			String[] className = testngConfig.getClassName();

			// Populate the class Names that need to be tested from TestNG
			// config.
			for (String s : className) {
				XmlClass xmlClass = new XmlClass();
				Class<?> c = Class.forName(s);
				xmlClass.setClass(c);
				xmlClass.setName(s);
				classList.add(xmlClass);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		/** Create web related testNG test suites which is mentioned in WebConfig>CurrentTestBeds */
		
		for (String testBedName : testBedManagerConfig.getWebConfig()
				.getCurrentTestBeds()) {
			XmlSuite suite = new XmlSuite();

		// Create XmlTest
		suite= createXMLTest(testList, classList, listeners, paramsMap, suite,
				testBedName);
		existingSuite.add(suite);
		}
		
		
		/** Create Mobile related testng test suites which is mentioned in MobileConfig>CurrentTestBeds */
		for (String testBedName : testBedManagerConfig.getMobileConfig()
				.getCurrentTestBeds()) {
			XmlSuite suite = new XmlSuite();

			suite= createXMLTest(testList, classList, listeners, paramsMap, suite,
					testBedName);
			existingSuite.add(suite);
		}

		
		log.info("Start - building TestSuits according to the configuration");
		
		return existingSuite;

	}


	/**
	 * Creates the xml test.
	 *
	 * @param testList the test list
	 * @param classList the testNG class list
	 * @param listeners the listeners
	 * @param paramsMap the params map ( Only param we are using is testBedName)
	 * @param suite the suite
	 * @param testBedName the test bed name
	 * @return the xml suite
	 */
	private XmlSuite createXMLTest(List<XmlTest> testList,
			List<XmlClass> classList, List<String> listeners,
			Map<String, String> paramsMap, XmlSuite suite, String testBedName) {
		try {
			paramsMap.put("testBedName", testBedName);
			
			XmlTest test = new XmlTest();
			test.setClasses(classList);
			test.setName(testBedName+ " Test");
			test.setVerbose(1);
			test.setPreserveOrder("true");
			test.setSuite(suite);
			suite.addListener(listeners.get(0));

			if (!test.equals(null))
				testList.add(test);
			else
				log.error("Test beds cannot be created");

			// Set XmlSuite

			// suite.addTest(test);
			suite.setName(testBedName +" Suite");
			suite.setVerbose(1);
			suite.setTests(testList);
			suite.setPreserveOrder("true");
			suite.setParameters(paramsMap);
			suite.setListeners(listeners);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Error occured while creating first testng Suite");
		}
		return suite;
	}
	
	/**
	 * Helps to add the reporter details to testSuite.
	 *
	 * @param suiteList the suite list
	 */
	private void addListenerToTestNG( List<XmlSuite> suiteList) {
		for (XmlSuite xmlSuite : suiteList) {
			xmlSuite.getListeners();
			xmlSuite.addListener(TestBedManagerConfiguration
					.getInstance().getTestngConfig().getReporter());
		}
	}

	

	
//	/**
//	 * Helps to create the structure of Test Suite information
//	 * Helper method.
//	 *
//	 * @param xmlTestList the xml test list
//	 * @param testBedName the test bed name
//	 * @return the xml suite
//	 */
//	private static XmlSuite createTestSuite(List<XmlTest> xmlTestList,
//			String testBedName) {
//		XmlSuite testSuite = new XmlSuite();
//		Map<String, String> paramsMap = new HashMap<String, String>();
//
//		testSuite.setName(testBedName + " Suite");
//		testSuite.setParallel("false");
//
//		paramsMap.put("testBedName", testBedName);
//		testSuite.setParameters(paramsMap);
//
//		for (XmlTest xmlTest : xmlTestList) {
//			XmlTest newXmlTest = new XmlTest(testSuite);
//			newXmlTest.setName(xmlTest.getName());
//			newXmlTest.setXmlClasses(xmlTest.getXmlClasses());
//
//		}
//
//		return testSuite;
//	}
	
	
	
	// private static List<XmlSuite> handleForExistingSuites(String
	// suiteFileName) {
	// List<XmlSuite> existingSuite = null;
	//
	// try {
	// existingSuite = (List<XmlSuite>) new Parser(suiteFileName).parse();
	// } catch (ParserConfigurationException e) {
	// // TODO Auto-generated catch block
	// System.out.println("ParserConfigurationException occured "
	// + e.getMessage());
	// e.printStackTrace();
	//
	// } catch (SAXException e) {
	// // TODO Auto-generated catch block
	// System.out.println("SAXException occured " + e.getMessage());
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// System.out.println("IOException occured " + e.getMessage());
	// e.printStackTrace();
	// }
	//
	// return existingSuite;
	// }

}
