package com.etouch.yaml.example;

import java.util.List;

public abstract class DriverImpl implements Driver{

	private String hub;
	private String port;
	private String tool;
	private String[] currentTestBeds;
	private List<TestBed> testBeds;
	public String getHub() {
		return hub;
	}
	public void setHub(String hub) {
		this.hub = hub;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getTool() {
		return tool;
	}
	public void setTool(String tool) {
		this.tool = tool;
	}
	public String[] getCurrentTestBeds() {
		return currentTestBeds;
	}
	public void setCurrentTestBeds(String[] currentTestBeds) {
		this.currentTestBeds = currentTestBeds;
	}
	public List<TestBed> getTestBeds() {
		return testBeds;
	}
	public void setTestBeds(List<TestBed> testBeds) {
		this.testBeds = testBeds;
	}
	public Object getWebDriver(String driverType) {
		// TODO Auto-generated method stub
		return null;
	}
	public Object getMobileDriver(String driverType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
