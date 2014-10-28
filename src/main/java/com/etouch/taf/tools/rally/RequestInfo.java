package com.etouch.taf.tools.rally;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * This class stores Request information.
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class RequestInfo {
	
	String requestType;
	String fetch[]; 
	ArrayList<String> queryFilter;
	String refField;
	
	Hashtable<String,String> queryOrder;
	int pageSize;
	int limit;
	Map<String,Map<String,String>> entry;
	boolean attachmentPresent;

	String imgFilePath;
	String imgFileName;
	String accessMode;
	
	String projectOID;
	boolean scopeDown;

	public boolean isAttachmentPresent() {
		return attachmentPresent;
	}

	public void setAttachmentPresent(boolean attachmentPresent) {
		this.attachmentPresent = attachmentPresent;
	}
	
	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public String getImgFilePath() {
		return imgFilePath;
	}

	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public RequestInfo(){
		pageSize = -1;
		limit = -1;
	}
	
	public RequestInfo(String reqType, String key, Map<String, String> reqEntries){
		setRequestType(reqType);
		for(Map.Entry<String, String> entry: reqEntries.entrySet()){
			addEntry(key, entry.getKey(), entry.getValue());
		}
	}
	
	public String getRequestType() {
		return requestType;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String[] getFetch() {
		return fetch;
	}
	
	public void setFetch(String[] fetch) {
		this.fetch = fetch;
	}	
	
	public ArrayList<String> getQueryFilter() {
		return queryFilter;
	}

	public void setQueryFilter(ArrayList<String> queryFilter) {
		this.queryFilter = queryFilter;
	}
	
	public String getRefField() {
		return refField;
	}

	public void setRefField(String refField) {
		this.refField = refField;
	}
	
	public void addOrder(String field,String order) {
		if(queryOrder == null)
			queryOrder = new Hashtable<String, String>();
		queryOrder.put(field,order);
	}

	public Map<String, String> getQueryOrder() {
		return queryOrder;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void addEntry(String key, String name,String value){
		
		if(entry == null)
			entry = new HashMap<String, Map<String,String>>();
		
		Map<String, String> map = null;
		if(entry.containsKey(key))
			map = entry.get(key);
		
		if(map == null)
			map =  new HashMap<String, String>();
		
		map.put(name, value);
		
		entry.put(key, map
				);
	}
	public Map<String, Map<String,String>> getEntry() {
		return entry;
	}
	public void setEntry(Map<String, Map<String,String>> newEntry) {
		this.entry = newEntry;
	}

	public String getProjectOID() {
		return projectOID;
	}

	public void setProjectOID(String projectOID) {
		this.projectOID = projectOID;
	}

	public boolean isScopeDown() {
		return scopeDown;
	}

	public void setScopeDown(boolean scopeDown) {
		this.scopeDown = scopeDown;
	}


}
