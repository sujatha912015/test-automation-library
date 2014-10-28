package com.etouch.taf.tools.json.schemavalidator;

import java.net.URL;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.ResourceLoadException;
import com.etouch.taf.util.LogUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.util.JsonLoader;

/**
 * This class reads JSON date.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class JsonDataReader implements IReader {
	 
	private static Log log = LogUtil.getLog(JsonDataReader.class);
	
	private JsonNode data;
	
	public void loadFromResource(String resource) throws ResourceLoadException{
		try{
			data = JsonLoader.fromResource(resource);
		}catch(Exception ioe){
			// Throw Exception
			throw new ResourceLoadException(ioe.toString());
		}
	}

	public void loadFromURL(String url)  throws ResourceLoadException{
		try{
			URL urlObj = new URL(url);
			data = JsonLoader.fromURL(urlObj);
		}catch(Exception ioe){
			// Throw Exception
			throw new ResourceLoadException(ioe.toString());
		}
	}
	
	public void loadFromPath(String path)  throws ResourceLoadException{
		try{
			data = JsonLoader.fromPath(path);
		}catch(Exception ioe){
			// Throw Exception
			throw new ResourceLoadException(ioe.toString());
		}
	}
	
	public void loadFromString(String json)  throws ResourceLoadException{
		try{
			data = JsonLoader.fromString(json);
		}catch(Exception ioe){
			// Throw Exception
			throw new ResourceLoadException(ioe.toString());
		}
	}
	
	public Object getNode() {
		return data;
	}
	
}
