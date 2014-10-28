package com.etouch.taf.tools.defect;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.resources.DefectToolType;
import com.etouch.taf.tools.rally.RallyConstants;
import com.etouch.taf.tools.rally.RallyInfo;
import com.etouch.taf.tools.rally.Rally;
import com.etouch.taf.util.LogUtil;

public class DefectManagerFactory {
	
	static Log log = LogUtil.getLog(DefectManagerFactory.class);

	public static DefectManager manageDefect(String defect) throws DefectException {
		
		if(defect.equalsIgnoreCase(DefectToolType.RALLY.getTool())){
			RallyInfo rInfo = new RallyInfo(RallyConstants.RALLYURL, RallyConstants.RALLYUSERNAME, RallyConstants.RALLYPASSWD, RallyConstants.RALLYAPP);
			try {
				return new Rally(rInfo);
			} catch (DefectException e) {
				log.error(e.getMessage());
				throw new DefectException(e.getMessage());
			}
		}else if(defect.equals(DefectToolType.JIRA.getTool())){
			
		}
		return null;
	}

}
