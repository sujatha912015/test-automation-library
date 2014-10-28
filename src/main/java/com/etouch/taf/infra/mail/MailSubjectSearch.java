package com.etouch.taf.infra.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

public class MailSubjectSearch extends SearchTerm implements IMailSearchCriteria{

	private static Log log = LogUtil.getLog(EmailValidator.class);
	
	private Properties prop;
	
	@Override
	public boolean match(Message message) {
		try {
			
			String subjectLike = prop.getProperty(IEMailConstants.MATCH_MAIL_SUBJECT);
			
			System.out.println("MailSubjectSearch.match() subjectLike "+subjectLike);
			
			if (message.getSubject() != null && message.getSubject().contains(subjectLike)) {
				return true;
			}
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	
	public boolean isMatch(Message message,Properties prop) {
		// TODO Auto-generated method stub
		this.prop=prop;
		return match(message);
	}

}
