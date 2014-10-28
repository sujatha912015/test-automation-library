package com.etouch.taf.core.datamanager.excel;

import java.util.Map;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

/**
 * 
 * Class to store excel data file name, sheet name and data key. Objects of this
 * class are to be used as the "KEY" for the elements added to {@link Map} in
 * {@link TestDataContainer}
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public class TestDataKey {
	
	private static Log log = LogUtil.getLog(TestDataKey.class);
	
	private String dataFileName;
	
	private String dataSheetName;
	
	private String dataKey;

	public TestDataKey(String dataFileName, String dataSheetName, String dataKey) {
		super();
		this.dataFileName = dataFileName;
		this.dataSheetName = dataSheetName;
		this.dataKey = dataKey;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public String getDataSheetName() {
		return dataSheetName;
	}

	public String getDataKey() {
		return dataKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataFileName == null) ? 0 : dataFileName.hashCode());
		result = prime * result + ((dataKey == null) ? 0 : dataKey.hashCode());
		result = prime * result
				+ ((dataSheetName == null) ? 0 : dataSheetName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestDataKey other = (TestDataKey) obj;
		if (dataFileName == null) {
			if (other.dataFileName != null)
				return false;
		} else if (!dataFileName.equals(other.dataFileName))
			return false;
		if (dataKey == null) {
			if (other.dataKey != null)
				return false;
		} else if (!dataKey.equals(other.dataKey))
			return false;
		if (dataSheetName == null) {
			if (other.dataSheetName != null)
				return false;
		} else if (!dataSheetName.equals(other.dataSheetName))
			return false;
		return true;
	}

}
