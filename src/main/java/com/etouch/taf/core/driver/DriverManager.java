package com.etouch.taf.core.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.TestBed;
import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.core.driver.web.ChromeDriver;
import com.etouch.taf.core.driver.web.SafariDriver;
import com.etouch.taf.core.driver.web.FirefoxDriver;
import com.etouch.taf.core.driver.web.IEDriver;
import com.etouch.taf.core.driver.mobile.AndroidDriver;
import com.etouch.taf.core.driver.mobile.iOSDriver;
import com.etouch.taf.core.exception.DriverException;
import com.etouch.taf.core.resources.TestBedType;
import com.etouch.taf.util.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * Gets the driver based on the profile information.
 *
 * @author eTouch Systems Corporation
 * @version 1.0
 */
public class DriverManager {

	/** The log. */
	static Log log = LogUtil.getLog(DriverManager.class);
	
	/** The driver map. */
	private static HashMap<String, Class<? extends DriverBuilder>> driverMap =null;

	/** The driver. */
	public static Driver driver;
	
	
	
	/**
	 * Builds the driver map.
	 */
	private static void buildDriverMap(){
		driverMap=new HashMap<String, Class<? extends DriverBuilder>>();
		// Web Browser class
		driverMap.put(TestBedType.Chrome.toString(), ChromeDriver.class);
		driverMap.put(TestBedType.Safari.toString(), SafariDriver.class);
		driverMap.put(TestBedType.Firefox.toString(), FirefoxDriver.class);
		driverMap.put(TestBedType.InternetExplorer.toString(), IEDriver.class);
		
		// Mobile Browser class
		driverMap.put(TestBedType.Android.toString(), AndroidDriver.class);
		driverMap.put(TestBedType.iPhoneNativeSim.toString(), iOSDriver.class);
		driverMap.put(TestBedType.iPhoneNative.toString(), iOSDriver.class);
	}
		
		/**
		 * Creates a TestDFriver according to the specification in testbed.
		 *
		 * @param testBedName the test bed name
		 * @return the driver
		 * @throws DriverException the driver exception
		 */
		public static DriverBuilder buildDriver(String testBedName)  throws DriverException
		{        
			buildDriverMap();
			
		
			Class<? extends DriverBuilder> clazz=	driverMap.get(testBedName);
		   System.out.println("Clazzzzz is " + clazz);
		   
		   return driver;
		}
	
		/**
		 * Creates the driver.
		 *
		 * @param className the class name
		 * @return the driver builder
		 */
		private static DriverBuilder createDriver( String className){
			Class<?> clazz;
			Object driverObject=null;
			try {
				System.out.println("Class Name " + className);
				clazz = Class.forName(className);
				Constructor<?> ctor = clazz.getConstructor(TestBed.class);
				System.out.println(" Safari is building Driver in DriverManager");
				driverObject =(DriverBuilder) ctor.newInstance(TestBedManager.INSTANCE.getCurrentTestBed());
				//driverObject.buildDriver();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return (DriverBuilder) driverObject;
		}
	
	
}