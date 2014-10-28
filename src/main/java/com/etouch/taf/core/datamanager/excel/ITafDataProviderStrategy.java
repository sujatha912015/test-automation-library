package com.etouch.taf.core.datamanager.excel;

import java.util.Map;

public interface ITafDataProviderStrategy<T> {
	Map<String, T[][]> readTestData();
	Map<String, T[][]> readTestData(String sheet,String id);
}
