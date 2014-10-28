package com.etouch.taf.tools.rally;

/**
 * This class stores information used to connect rally instance.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class RallyInfo {
	
	String url;
	String userName;
	String password;
	String appName;
	String wsapiVersion;
	
	public RallyInfo(String url, String userName, String password, String appName) {
		super();
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.appName = appName;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getWsapiVersion() {
		return wsapiVersion;
	}
	public void setWsapiVersion(String wsapiVersion) {
		this.wsapiVersion = wsapiVersion;
	}

}
