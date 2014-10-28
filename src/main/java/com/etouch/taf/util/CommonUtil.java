package com.etouch.taf.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.config.TestBedManagerConfiguration;
import com.etouch.taf.core.exception.ListException;
import com.etouch.taf.core.exception.PageException;
import com.etouch.taf.core.resources.TestEnvironmentType;

/**
 * This class contains common utility functions.
 * 
 * @author eTouch Systems Corporation
 * version 1.0
 *
 */
public class CommonUtil {
	
	static Log log=LogUtil.getLog(CommonUtil.class);
	
	
	
	public static synchronized boolean isListInOrder(List<String> sourceList1,List<String> sourceList2) throws ListException{
		boolean result = true;
		if(isNull(sourceList1) || isNull(sourceList2)){
			log.error("failed to check order, source list 1 or 2 is not initialized - ("+sourceList1+","+sourceList2+")");
			throw new ListException("failed to check order, source list 1 or 2 is not initialized");
		}
		if(sourceList1.size() != sourceList2.size()){
			log.info("failed to check order, source list 1 or 2 size is different - ("+sourceList1.size()+","+sourceList2.size()+")");
			result = false;
		}else{
			for(int i=0; i < sourceList1.size(); i++){
				if(!sourceList1.get(i).equals(sourceList2.get(i)) ){
					result = false; 
				}
			}
		}	
		return result;
	}
	
	public static synchronized boolean compareText(String str1,String str2){
		if(isNull(str1) || isNull(str2) ){
			log.info("string 1 or 2 is not initialized - ("+str1+","+str2+")");
		}
		return str1.equals(str2);
	}
	
	public static synchronized boolean isNull(Object objVal){
		if(objVal == null)
			return true;
		return false;
	}
	
	public static synchronized boolean isLinkBroken(String linkUrl) throws PageException{
		log.info("Start - isLinkBroken");
		boolean result = false;
		if(CommonUtil.isNull(linkUrl)){
			log.error("Link URL is missing - "+ linkUrl);			
			throw new PageException("Link URL is missing");
		}		
		try {
			URL url = new URL(linkUrl);
			HttpURLConnection httpURLConnect=(HttpURLConnection)url.openConnection();
			httpURLConnect.setConnectTimeout(6000);
		    httpURLConnect.connect();
		    if(httpURLConnect.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND){
		    	result = true;
		    }
		} catch (MalformedURLException e) {
			log.error("Malformed URL - "+ linkUrl + " Message - "+e.toString());	
			throw new PageException("Failed to check broken link - "+ linkUrl + " Message - "+e.toString());
		} catch (IOException e) {
			log.error("IO Exception - URL - "+linkUrl);	
			throw new PageException("Failed to check broken link - "+ linkUrl + " Message - "+e.toString());
		}finally{
			log.info("End - isLinkBroken");
		}
		return result;
	}	
	
	

}
