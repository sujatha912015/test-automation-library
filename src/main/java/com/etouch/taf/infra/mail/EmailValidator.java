package com.etouch.taf.infra.mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.ValidationException;
import com.etouch.taf.util.LogUtil;

/**
 * EmailValidator exposes API to integrate with Mails.
 * 
 * 
 * 
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */

public class EmailValidator implements IEmailValidator
{

	private static Log log = LogUtil.getLog(EmailValidator.class);

	/**
	 * Read the mail box for a given user and return a list of Messages 
	 * based on the matched filter criteria
	 * 
	 * @param props
	 * @return
	 * @throws Exception
	 */

	public Message[] readMail(Properties props) throws Exception
	{


		String mailServerName = props.getProperty(IEMailConstants.MAIL_SERVER_PROP_NAME);
		String userName = props.getProperty(IEMailConstants.USER_NAME_PROP_NAME);
		String password = props.getProperty(IEMailConstants.USER_PASSWORD_PROP_NAME);
		String protocol = props.getProperty(IEMailConstants.PROTOCOL_PROP_NAME);

		log.info(IEMailConstants.MAIL_SERVER_PROP_NAME +" --> "+mailServerName);
		log.info(IEMailConstants.USER_NAME_PROP_NAME +" --> "+userName);
		log.info(IEMailConstants.USER_PASSWORD_PROP_NAME +" --> "+password);
		log.info(IEMailConstants.PROTOCOL_PROP_NAME +" --> "+protocol);

		Session session = Session.getDefaultInstance(new Properties(),null);
		Store store = session.getStore(protocol); 

		store.connect(mailServerName, userName,password);

		Folder folder = store.getFolder(IEMailConstants.READ_MAIL_BOX_NAME);
		folder.open(Folder.READ_WRITE);

		System.out.println("Total Message:" + folder.getMessageCount());
		System.out.println("Unread Message:" + folder.getUnreadMessageCount());

		List<Message> messageList = new ArrayList<Message>();
		IMailSearchCriteria criterria = getMailSearcher(props);
		Message[]  messages=null;
		for(Message mail : folder.getMessages())
		{
			if(criterria.isMatch(mail, props))
			{
				messageList.add(mail);
			}

		}
		messages = messageList.toArray(new Message[messageList.size()]);

		return messages;
	}

	public boolean validateMailMessages(Message[]  messages,Properties props) throws Exception
	{
		boolean isMailFound = false;
		boolean result = true;
		if(messages.length > 0)
			isMailFound = true;

		if (!isMailFound) {
			log.error("NO EMAILS TO READ");
			throw new Exception("Could not find any mail");

		} 
		else 
		{
			for (Message mail : messages)
			{
				StringBuffer buffer = new StringBuffer();

				buffer = readMailContents(mail);

				if(!inspectMail(buffer,props))
				{
					log.error("EMAIL NOT CONTAINTING THE REQUIRED TEXT");
					result = false;
					break;
				}

			}
		}
		return result;
	}

	private StringBuffer readMailContents(Message mail) throws Exception
	{
		StringBuffer buffer = new StringBuffer();
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(mail.getInputStream()));

		while ((line = reader.readLine()) != null)
		{
			buffer.append(line+" ");
		}


		return buffer;
	}

	public boolean inspectMail(StringBuffer mailContent,Properties props)
	{

		boolean result = false;

		String mailLookupString = props.getProperty(IEMailConstants.MAIL_LOOKUP_STRING);

		if(mailContent.toString().contains(mailLookupString))
			result = true;

		return result;
	}

	public IMailSearchCriteria getMailSearcher(Properties props)
	{
		return MailSearchCriteriaFactory.getMailSearchCrieteria(props);

	}

	public boolean validateEmail(Properties props) throws ValidationException {
		// TODO Auto-generated method stub
		boolean result=false;
		try
		{

			Message[]  messages = readMail(props);

			result = validateMailMessages(messages, props);

		}catch(Exception e)
		{
			e.printStackTrace();
			throw new ValidationException("Mail Validation failed");
		}

		return result;
	}

	public List<String> retriveEmailBodyContentFromMessages(Properties props)
			throws ValidationException {
		List<String> matchedLinks = new ArrayList<String>();
		try
		{
			Message[]  messages = readMail(props);
			matchedLinks= retriveEmailBodyContentFromMessages(messages, props);

		}catch(Exception e)
		{
			e.printStackTrace();
			throw new ValidationException("Mail Validation failed");
		}
		return matchedLinks;
	}


	private List<String> retriveEmailBodyContentFromMessages(Message[]  messages , Properties prop) throws Exception{

		List<String> emailBodyContent = new ArrayList<String>();
		boolean isMailFound = false;

		if(messages.length > 0)
			isMailFound = true;

		if (!isMailFound) {
			throw new Exception("Could not find any mail");

		} 
		else 
		{
			for (Message mail : messages)
			{
				StringBuffer buffer = new StringBuffer();
				buffer = readMailContents(mail);
				emailBodyContent.add(buffer.toString());

			}
		}
		return emailBodyContent;
	}

}
