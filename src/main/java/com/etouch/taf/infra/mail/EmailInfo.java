package com.etouch.taf.infra.mail;

import org.apache.commons.logging.Log;

import com.etouch.taf.util.LogUtil;

/**
 * 
 * EmailInfo object carries email information including sender / recipients details.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class EmailInfo {
	
	static Log log = LogUtil.getLog(EmailInfo.class);
	
	String userName = null;
	String passWord = null;
	String host = null;
	String port = null;
	String starttls = null; 
	String auth = null; 
	boolean debug = false; 
	String socketFactoryClass = null;
	String fallback = null;
	String from = null;
	String[] to = null;
	String[] cc = null;
	String[] bcc = null;
	String subject = null; 
	String text = null;
	String attachmentPath = null;
	String attachmentName = null;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassWord() {
		return passWord;
	}
	
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getStarttls() {
		return starttls;
	}
	
	public void setStarttls(String starttls) {
		this.starttls = starttls;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}
	
	public void setSocketFactoryClass(String socketFactoryClass) {
		this.socketFactoryClass = socketFactoryClass;
	}
	
	public String getFallback() {
		return fallback;
	}
	
	public void setFallback(String fallback) {
		this.fallback = fallback;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String[] getTo() {
		return to;
	}
	
	public void setTo(String[] to) {
		this.to = to;
	}
	
	public String[] getCc() {
		return cc;
	}
	
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	
	public String[] getBcc() {
		return bcc;
	}
	
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getAttachmentPath() {
		return attachmentPath;
	}
	
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	
	public String getAttachmentName() {
		return attachmentName;
	}
	
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

}
