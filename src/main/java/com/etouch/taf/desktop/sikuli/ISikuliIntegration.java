package com.etouch.taf.desktop.sikuli;

import java.net.URL;

import org.sikuli.api.MultiStateTarget;
import org.sikuli.api.ScreenRegion;

/**
 * Interface to integrate test cases with Sikuli API.
 *  
 * @author eTouch Systems Corporation
 * @version 1.0
 * 
 */
public interface ISikuliIntegration {

	/**
	 * 
	 * Retrieve the default Desktop screen image.
	 * 
	 * @return
	 */
	public ScreenRegion defaultScreen() throws Exception;
	
	/**
	 * Retrieve the desktop screen area using the co-ordinates supplied.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public ScreenRegion defaultScreenByCoordinates(int x, int y, int width, int height) throws Exception;
	
	/**
	 * Find the Target image using the URL
	 * if one url is supplied then return screen area based on that
	 * if multiple url is input then create a instance of MultiStateTarget using that. 
	 * Then use the state array and add the state for the MultiStateTarget in the same sequence.
	 * 
	 * @param screenRegionToSearch
	 * @param images
	 * @param state
	 * @return
	 */
	public ScreenRegion findScreenRegionByImage(ScreenRegion screenRegionToSearch, URL[] images,String... state) throws Exception;
	
	/**
	 * Wait for the page to load for the given wait time, and then try to find the image on the Screen region.
	 * if one url is supplied then return screen area based on that image.
	 * if multiple url is input then create a instance of MultiStateTarget using that. 
	 * Then use the state array and add the state for the MultiStateTarget in the same sequence.
	 * 
	 * @param screenRegionToSearch
	 * @param wait
	 * @param images
	 * @param state
	 * @return
	 */
	public ScreenRegion waitAndLoadScreenRegionByImage(ScreenRegion screenRegionToSearch, int wait,URL[] images,String... state) throws Exception;
}
