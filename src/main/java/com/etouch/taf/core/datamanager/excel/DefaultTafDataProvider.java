package com.etouch.taf.core.datamanager.excel;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.testng.annotations.Test;

import com.etouch.taf.util.ExcelUtil;
import com.etouch.taf.util.LogUtil;

/**
 * Reads test data.
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class DefaultTafDataProvider implements ITafDataProviderStrategy<String> {

	private static Log log = LogUtil.getLog(DefaultTafDataProvider.class);
	
	private String testDataFilePath;
	private String testClassName;
	private String testDataSheet;
	private Map<String, String[][]> testDataMap = null;
	private static String S_DOT_XLS_EXTN = ".xls";


	public DefaultTafDataProvider(String testDataFilePath,String testClassName, String testDataSheet) {
		super();
		if(null == testDataFilePath){
			// By default find a excel file with name of the Test class
			URL testDataExcelUrl = Thread.currentThread().getContextClassLoader().getResource(getSimpleTestClassName(testClassName) + S_DOT_XLS_EXTN); 
			testDataFilePath = testDataExcelUrl.getFile();
		}
		
		if(null == testDataSheet){
			testDataSheet = getSimpleTestClassName(testClassName);
		}
		
		this.testDataFilePath = testDataFilePath;
		this.testClassName = testClassName;
		this.testDataSheet = testDataSheet;
	}
	
	private String getSimpleTestClassName(String testClassName) {
		return testClassName.substring(testClassName.lastIndexOf(".") + 1);
	}

	public DefaultTafDataProvider(String testClassName){
		this(null, testClassName, null);
	}
	
	public DefaultTafDataProvider(String testDataFilePath, String testClassName) {
		this(testDataFilePath, testClassName, null);
	}

	public String getTestDataFilePath() {
		return testDataFilePath;
	}

	public String getTestClassName() {
		return testClassName;
	}

	public String getTestDataSheet() {
		return testDataSheet;
	}

	public Map<String, String[][]> readTestData() {
		// Return the map if it is already populated
		if(null != testDataMap){
			return testDataMap;
		}
		
		synchronized (this) {
			// Load only once
			if(null == testDataMap){
				// Doubly checking to avoiding loading twice
				testDataMap = new HashMap<String, String[][]>();
				for (String testMethod : findAllTestMethodsInClass()) {
					String[][] testData = ExcelUtil.readExcelData(
							getTestDataFilePath(), getTestDataSheet(), testMethod);
					testDataMap.put(testMethod, testData);
				}
			}
		}
		
		return testDataMap;
	}

	public Map<String, String[][]> readTestData(String sheet,String id) {
		// Return the map if it is already populated
		Map<String,String[][]> map;
		synchronized (this) {
			map = new HashMap<String,String[][]>();
			String[][] testData = ExcelUtil.readExcelData(getTestDataFilePath(), sheet, id);
			map.put(id, testData);
		}
		return map;
	}	

	private List<String> findAllTestMethodsInClass() {
		List<String> testMethodsList = new ArrayList<String>();
		Class<?> clazz = null;

		// Load the given class
		try {
			clazz = Class.forName(getTestClassName());
		} catch (ClassNotFoundException e) {
			/*
			 * Impossible to reach here as the test class exists for sure.. and
			 * that's why dataprovider was called so ignore and move on
			 */
			e.printStackTrace();
		}

		if (null != clazz) {
			// Find all the test methods in the loaded class
			for (Method method : clazz.getMethods()) {
				if (method.isAnnotationPresent(Test.class)) {
					testMethodsList.add(method.getName());
				}
			}
		}

		return testMethodsList;
	}
}
