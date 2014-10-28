package com.etouch.taf.infra.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

public class MailFromAddressSearch extends SearchTerm implements IMailSearchCriteria{

	private static Log log = LogUtil.getLog(MailToAddressSearch.class);
	private Properties prop;

	@Override
	public boolean match(Message message) {
		try {

			String checkFromAddress = prop.getProperty(IEMailConstants.MATCH_MAIL_FROMADD);

			log.info(" checkFromAddress "+checkFromAddress);

			List<String> fromAddresses = new ArrayList<String>();
			Address[] recipients = message.getFrom();
			for (Address address : recipients) {
				fromAddresses.add(address.toString());
			}
			log.info(" fromAddresses "+fromAddresses);


			for(String fromAddress : fromAddresses)
			{
				if(fromAddress.contains(checkFromAddress))
					return true;
			}
			
		} catch (MessagingException ex) {
			log.error("Error "+ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	
	public boolean isMatch(Message message,Properties prop) {
		// TODO Auto-generated method stub
		this.prop= prop;
		return match(message);
	}
	
	
}
