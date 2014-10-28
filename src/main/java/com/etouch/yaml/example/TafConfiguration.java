package com.etouch.yaml.example;


public class TafConfiguration {
	
	private String[] testTypes;
	private WebDriver web;
	private MobileDriver mobile;
	public String[] getTestTypes() {
		return testTypes;
	}
	public void setTestTypes(String[] testTypes) {
		this.testTypes = testTypes;
	}
	public WebDriver getWeb() {
		return web;
	}
	public void setWeb(WebDriver web) {
		this.web = web;
	}
	public MobileDriver getMobile() {
		return mobile;
	}
	public void setMobile(MobileDriver mobile) {
		this.mobile = mobile;
	}

	
	
}
