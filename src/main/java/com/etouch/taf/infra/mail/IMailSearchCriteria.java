package com.etouch.taf.infra.mail;

import java.util.Properties;

import javax.mail.Message;

public interface IMailSearchCriteria {

	public boolean isMatch(Message message,Properties prop);
}
