package com.etouch.taf.webui.selenium.webelement;

import org.apache.commons.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.Color;

import com.etouch.taf.util.BrowserInfoUtil;
import com.etouch.taf.util.LogUtil;
import com.etouch.taf.webui.selenium.WebPage;

/**
 * Renders Text PageObject. .
 *
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class Text extends Element {

	static Log log = LogUtil.getLog(Text.class);

	/**
	 * @param webElement
	 */
	public Text(WebElement webElement) {
		super(webElement);
	}

	Actions action = new Actions(driver);


	public void textHover(){
		Mouse mouse=((HasInputDevices)driver).getMouse();
		mouse.mouseMove(((Locatable) this.webElement).getCoordinates());
	}
	/**
	 * @return
	 */
	public String textColor() {
		if(BrowserInfoUtil.INSTANCE.isSafari()){
			action.moveToElement(webElement);
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, webElement);
			System.out.println("In hover");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("OUT hover");
		}else{
			textHover();
		}
		String hex = Color.fromString(webElement.getCssValue("color")).asHex();
		return hex;
	}

	public String textStyle(){
		// TODO
		return null;
	}
	
	public String textFont(){
		//TODO 
		return null;
	}
	/**
	 * @return
	 */
	public String textTitle() {
		textHover();
		String titleText = webElement.getAttribute("title");
		return titleText;
	}

	/**
	 * @return
	 */
	public String borderColor() {
		textHover();
		String hex = Color.fromString(webElement.getCssValue("color")).asHex();
		return hex;
	}

}
