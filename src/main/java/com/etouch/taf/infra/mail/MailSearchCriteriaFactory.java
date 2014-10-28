package com.etouch.taf.infra.mail;

import java.util.Properties;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

public class MailSearchCriteriaFactory {

	private static Log log = LogUtil.getLog(MailSearchCriteriaFactory.class);
	
	public static IMailSearchCriteria getMailSearchCrieteria(Properties props)
	{
		IMailSearchCriteria crieteria = null;
		String searchCrieteria = props.getProperty(IEMailConstants.MAIL_FILTER_CRITERIA);
		
		log.info(" searchCrieteria "+searchCrieteria);
		
		if(IEMailConstants.CRITERIA_SUBJECT_KEY.equalsIgnoreCase(searchCrieteria))
			crieteria = new MailSubjectSearch();
		else if (IEMailConstants.CRITERIA_TOADDRESS_KEY.equalsIgnoreCase(searchCrieteria))
			crieteria = new MailToAddressSearch();
		else if (IEMailConstants.CRITERIA_FROMADRESS_KEY.equalsIgnoreCase(searchCrieteria))
			crieteria = new MailFromAddressSearch();
		
		log.info(" crieteria "+crieteria);
		
		return crieteria;
	}
}
