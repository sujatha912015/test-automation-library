package com.etouch.taf.infra.mail;

/**
 * Interface for holding Email integration constants.
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public interface IEMailConstants {

	
	public static final String MAIL_FILTER_CRITERIA="mailFilterCriteria";
	
	public static final String MATCH_MAIL_SUBJECT= "matchSubJectLike";
	public static final String MATCH_MAIL_TOADD= "matchToAddress";
	public static final String MATCH_MAIL_FROMADD= "matchFromAddress";
	
	
	
	public static final String CRITERIA_SUBJECT_KEY="SUBJECT";
	public static final String CRITERIA_TOADDRESS_KEY="TOADDRESS";
	public static final String CRITERIA_FROMADRESS_KEY="FROMADDRESS";
	
	
	public static final String READ_MAIL_BOX_NAME="INBOX";
	
	public static final String MAIL_SERVER_PROP_NAME="mailServerName";
	public static final String USER_NAME_PROP_NAME="userName";
	public static final String USER_PASSWORD_PROP_NAME="password";
	public static final String PROTOCOL_PROP_NAME="protocol";
	
	public static final String MAIL_LOOKUP_STRING="mailLookupString";
}
