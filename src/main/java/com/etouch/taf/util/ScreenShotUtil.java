package com.etouch.taf.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * This class takes screen shot.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class ScreenShotUtil {
	
	private static Log log = LogUtil.getLog(ScreenShotUtil.class);

	/**
	 * This method captures the screen shot. It supports both local and remote configurations 
	 * for web driver
	 * @param driver
	 * @param screenShotFile
	 */
	public void takeScreenShot(WebDriver driver, String screenShotFile) {
		try {
			log.info("WebDriver - " + driver + " Screen Shot file name - " + screenShotFile);
			File scrFile = null;
			if(driver instanceof RemoteWebDriver) {
				
				scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			
			} else {
				WebDriver augmentedDriver = new Augmenter().augment( driver );
				scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
			}
			
			// Now you can do whatever you need to do with it, for example copy
			// somewhere
			File destnFile =  new File(screenShotFile);
			FileUtils.copyFile(scrFile, destnFile);
			log.info("Screen Shot File is saved as " + destnFile.getAbsolutePath());
		} catch (IOException e) {
			log.error("Encountered exception " + e.getMessage()	+ " on taking screen shot");
		}
	}

	/*public void takeScreenShot(WebDriver driver, String screenShotFile) {
		try {
			log.info("WebDriver - " + driver + " Screen Shot file name - " + screenShotFile);
			File scrFile = null;
			if(driver instanceof RemoteWebDriver) {
				WebDriver augmentedDriver = new Augmenter().augment( driver );
				scrFile = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
			} else {
				scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			}
			
			// Now you can do whatever you need to do with it, for example copy
			// somewhere
			FileUtils.copyFile(scrFile, new File(screenShotFile));
			log.info("Screen Shot File is saved as " + scrFile.getAbsolutePath());
		} catch (IOException e) {
			log.error("Encountered exception " + e.getMessage()	+ " on taking screen shot");
		}
	} */
	
	

}
