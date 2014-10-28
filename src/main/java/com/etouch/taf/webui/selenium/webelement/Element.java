package com.etouch.taf.webui.selenium.webelement;

import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import com.etouch.taf.core.TestBedManager;
import com.etouch.taf.util.BrowserInfoUtil;
import com.etouch.taf.util.LogUtil;

/**
 * PageElement that all PageObjectType should extend. This class wraps around
 * the WebElement
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public abstract class Element {
	private static Log	 log	       = LogUtil.getLog(Element.class);
	protected WebElement	webElement	= null;
	protected WebDriver driver = null;

	public WebElement getWebElement() {
		return webElement;
	}

	public Element(WebElement webElement) {
		this.webElement = webElement;
		if (this.driver == null) {
			this.driver=(WebDriver)TestBedManager.INSTANCE.getTestBed().getDriver();
		}
	}
	/**
	 * Double click on web element
	 * 
	 * @param driver
	 *            selenium web driver instance.
	 */
	public void doubleClick() {
		Actions builder = new Actions(driver);
		Action mouseOverHome = builder.moveToElement(webElement).click().doubleClick(webElement).build();
		mouseOverHome.perform();
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			log.error("Exception in thread sleep, message, " + e.toString());
		}
		return;
	}

	/**
	 * Returns true if element is visible.
	 * 
	 * @return true if elements is visible.
	 */
	public boolean isElementVisible() {
		try {
			if (webElement == null)
				return false;
			if (!webElement.isEnabled())
				return false;
			webElement.sendKeys(" ");
		} catch (org.openqa.selenium.ElementNotVisibleException ex) {
			log.error("Element No Visible - Error is " + ex.getMessage());
			return false;
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			log.error("No Such Element - Error is " + ex.getMessage());
			return false;
		} catch (org.openqa.selenium.StaleElementReferenceException ex) {
			log.error("Stale Element Reference for element - Error is " + ex.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Click on web element
	 */
	public void click() {
		try {
			webElement.click();
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			log.error("Exception in StaleElementReference message, " + e.toString());
		}catch (InterruptedException e) {
		log.error("Exception in thread sleep, message, " + e.toString());
	    }
	}

	/**
	 * Return coordinates for given web element.
	 * 
	 * @return coordinates
	 */
	public Point getCoordinates() {
		return webElement.getLocation();
	}

	/**
	 * Returns size of the element.
	 * 
	 * @return size of web element.
	 */
	public Dimension getSize() {
		return webElement.getSize();
	}

	/**
	 * 
	 * Returns element count.
	 * 
	 * @param Page
	 *            elements list.
	 * @return count page elements.
	 */
	public int getElementCount(List<Element> pageElements) {
		return pageElements.size();
	}

	/**
	 * Returns element text
	 * 
	 * @return element text
	 */
	public String getText() {
		return webElement.getText();
	}

	/**
	 * Return attribute value.
	 * 
	 * @param name
	 *            name of the attribute
	 * @return attribute value
	 */
	public String getAttribute(String attrName) {
		return webElement.getAttribute(attrName);
	}

	/**
	 * Returns true if element is displayed.
	 * 
	 * @return true if element displayed
	 */
	public boolean isDisplayed() {
		return webElement.isDisplayed();
	}

	/**
	 * Returns the css property of a text
	 * 
	 * @param name
	 *            name of the property
	 * @return property value
	 */
	public String getCssValue(String property_name) {
		return webElement.getCssValue(property_name);
	}

	/**
	 * hovers on the element.
	 * 
	 * @param driver
	 *   Selenium web driver instance
	 */
	public void hover() {
		if (BrowserInfoUtil.INSTANCE.isSafari()) {
			String javaScript = "var evObj = document.createEvent('MouseEvents');" + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
			        + "arguments[0].dispatchEvent(evObj);";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(javaScript, webElement);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		/*
		 * else if(BrowserInfoUtil.INSTANCE.isIE9()){ Actions builder=new
		 * Actions(driver); builder.moveToElement(webElement); builder.build();
		 * //builder.click(webElement); builder.perform(); }
		 */
		else {
			Actions builder = new Actions(driver);
			builder.moveToElement(webElement).build().perform();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	public void clickEvent() {
		String javaScript = "var evObj = document.createEvent('MouseEvents');" + "evObj.initMouseEvent(\"click\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
		        + "arguments[0].dispatchEvent(evObj);";
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(javaScript, webElement);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
