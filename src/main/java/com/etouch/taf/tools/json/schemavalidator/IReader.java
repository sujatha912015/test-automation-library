package com.etouch.taf.tools.json.schemavalidator;

import com.etouch.taf.core.exception.ResourceLoadException;

/**
 * Reader contract for loading resource.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public interface IReader {

	void loadFromResource(String resource) throws ResourceLoadException;	

	void loadFromURL(String url) throws ResourceLoadException;
	
	void loadFromPath(String path) throws ResourceLoadException;
	
	void loadFromString(String json) throws ResourceLoadException;
	
	Object getNode();

}
