package com.etouch.taf.tools.defect;

import com.etouch.taf.core.resources.DefectParameters;
import com.etouch.taf.tools.rally.RallyConstants;
import com.etouch.taf.tools.rally.RequestInfo;

public interface DefectManager {
	
	public void createDefectBuilder(String defName, String testcaseId, String projId, String DefSeverity,
			String DefOwner, String DefNotes, String storyId);
	public boolean verifyDefectExists(String testcaseId, String projId, String storyId, String defName);
	public boolean verifyIfdefectClosed();
	
	public void reopenDefect();
	public String getDefectStatus();
		
	public String queryDefectID(String testcaseId, String storyId, String projId, String defName);
	public String queryDefectStatus(String testcaseId, String storyId, String projId, String defName);
	
	// generic method to Update/set any of the defect field/attribute
	public boolean updateDefect(String testcaseId, String projId, String storyId, String defName, DefectParameters.IDefect updateKey, String updateValue);
	//generic method to Query/retrieve any defect field/attribute
	public String queryDefect(String testcaseId, String storyId, String projId, String defName, DefectParameters.IDefect queryByParam);	

	
}
