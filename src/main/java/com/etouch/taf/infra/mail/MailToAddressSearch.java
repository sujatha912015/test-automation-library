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

public class MailToAddressSearch extends SearchTerm implements IMailSearchCriteria{

	private static Log log = LogUtil.getLog(MailToAddressSearch.class);

	

	private Properties prop;

	@Override
	public boolean match(Message message) {
		try {

			String checkToAddress = prop.getProperty(IEMailConstants.MATCH_MAIL_TOADD);

			log.info(" checkToAddress "+checkToAddress);

			List<String> toAddresses = new ArrayList<String>();
			Address[] recipients = message.getRecipients(Message.RecipientType.TO);
			for (Address address : recipients) {
				toAddresses.add(address.toString());
			}
			log.info("To Address "+toAddresses);


			for(String toAddress : toAddresses)
			{
				if(toAddress.contains(checkToAddress))
					return true;
			}


			
		} catch (MessagingException ex) {
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
