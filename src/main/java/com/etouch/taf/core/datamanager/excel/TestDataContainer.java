package com.etouch.taf.core.datamanager.excel;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.testng.annotations.DataProvider;

import com.etouch.taf.core.exception.TafDataProviderException;
import com.etouch.taf.util.LogUtil;

/**
 * 
 * Container to store:
 * 
 * 1. The test data in format Object[][] with {@link TestDataKey} as the key to access the test data. Any TestNG
 * {@link DataProvider} implementation that reads data is expected to store the
 * data here for sharing with other test methods requiring the same data.
 * 
 * 2. The data files in a map - <file_key, absolute_path_to_fil>
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class TestDataContainer {
	
	private static Log log = LogUtil.getLog(TestDataContainer.class);
	
	private static TestDataContainer s_instance = null;
	
	private Map<String, String> dataFilesInfo = new ConcurrentHashMap<String, String>();
	
	private Map<TestDataKey, Object[][]> tafTestData = new ConcurrentHashMap<TestDataKey, Object[][]>();
	
	/**
	 * Empty, no-parameter private constructor for creating singleton
	 */
	private TestDataContainer(){
	}
	
	/**
	 * Returns the singleton instance of this class
	 * 
	 * @return
	 */
	public static TestDataContainer getInstance(){
		if(s_instance!=null){
			return s_instance;
		}
		
		if(s_instance==null){
			synchronized (TestDataContainer.class) {
				if(s_instance==null){
					s_instance = new TestDataContainer();
				}
			}
		}
		
		return s_instance;
	}
	
	/**
	 * Adds the test data object 2d array [][] with specified {@link TestDataKey} as key to the underlying map.
	 * 
	 * @param testDataKey
	 * @param testData
	 * @return
	 */
	public boolean addTestData(TestDataKey testDataKey, Object[][] testData){
		return tafTestData.put(testDataKey, testData)!=null;
	}
	
	/**
	 * Gets the test data object 
	 * 
	 * @param testDataKey
	 * @return
	 */
	public Object[][] getTestData(TestDataKey testDataKey){
		return tafTestData.get(testDataKey);
	}
	
	/**
	 * Checks if the container already has the test data loaded
	 * 
	 * @param testDataKey
	 * @return
	 */
	public boolean hasTestData(TestDataKey testDataKey){
		return tafTestData.containsKey(testDataKey);
	}
	
	/**
	 * 
	 * @param fileKey
	 * @param filePath
	 * @return
	 * @throws TafDataProviderException 
	 */
	public boolean addDataFile(String fileKey, String filePath) throws TafDataProviderException{
		if(null == fileKey || null == filePath){
			return false;
		}
		
		// Create a file
		File dataFile = new File(filePath);
		
		// Check if it exists and the current user process can read
		if(!dataFile.exists() && !dataFile.canRead()){
			throw new TafDataProviderException("Data File with Key=" + fileKey + " File Path = " + filePath + " does not exist");
		}
		
		// add it to the map
		return dataFilesInfo.put(fileKey, dataFile.getAbsolutePath())!=null;
	}
	
	/**
	 * 
	 * @param fileKey
	 * @return
	 */
	public String getDataFile(String fileKey){
		return dataFilesInfo.get(fileKey);
	}
	
	/**
	 * 
	 * @param fileKey
	 * @return
	 */
	public boolean hasDataFile(String fileKey){
		return dataFilesInfo.containsKey(fileKey);
	}
}