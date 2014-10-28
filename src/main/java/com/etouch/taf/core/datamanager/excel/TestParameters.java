package com.etouch.taf.core.datamanager.excel;

import java.util.HashMap;

/**
 * Read test data(Excel row) and assigned to TestParameters
 * @author eTouch
 *
 */
public class TestParameters {
	HashMap<String, String> paramMap;
	
	public TestParameters(){
		paramMap = new HashMap<String, String>();
	}
	
	public void setParamMap(String colName, String colVal){
		paramMap.put(colName, colVal);
	}
	
	public HashMap<String, String> getParamMap(){
		return paramMap;
	}
	
}
