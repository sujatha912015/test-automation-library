package com.etouch.taf.infra.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.EmailException;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.LogUtil;

import java.util.*;

/**
 * 
 * EmailNotification sends email notification to the users.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class EmailNotification {
	
	static Log log = LogUtil.getLog(EmailNotification.class);
	
	/**
	 * Returns true if email send successfully.
	 * @param emailInfo stores property values for sending email.
	 * @return true if email send successfully.
	 * @throws EmailException if error in sending email
	 */
	
	public static boolean sendMail(EmailInfo emailInfo) throws EmailException {
		log.info("Start - sendEmail");
		boolean result=false;
		if(CommonUtil.isNull(emailInfo)){
			log.error("Email Info is null - "+ emailInfo);			
			throw new EmailException("failed to send email, required information to send email is missing");
		}
		
		Properties props = new Properties();
		props.put("mail.smtp.user", emailInfo.getUserName());
		props.put("mail.smtp.host", emailInfo.getHost());
		if(!"".equals(emailInfo.getPort()))
			props.put("mail.smtp.port", emailInfo.getPort());
		if(!"".equals(emailInfo.getStarttls()))
			props.put("mail.smtp.starttls.enable",emailInfo.getStarttls());
		props.put("mail.smtp.auth", emailInfo.getAuth());
		if(emailInfo.isDebug()){
			props.put("mail.smtp.debug", "true");
		}else{
			props.put("mail.smtp.debug", "false");         
		}
		if(!"".equals(emailInfo.getPort()))
			props.put("mail.smtp.socketFactory.port", emailInfo.getPort());
		if(!"".equals(emailInfo.getSocketFactoryClass()))
			props.put("mail.smtp.socketFactory.class",emailInfo.getSocketFactoryClass());
		if(!"".equals(emailInfo.getFallback()))
			props.put("mail.smtp.socketFactory.fallback", emailInfo.getFallback());
			
		try{
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(emailInfo.isDebug());
			MimeMessage msg = new MimeMessage(session);
			msg.setText(emailInfo.getText());
			msg.setSubject(emailInfo.getSubject());
			//attachment start
			// create the message part 
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(emailInfo.getAttachmentPath());
			messageBodyPart.setDataHandler(new DataHandler(source));
			multipart.addBodyPart(messageBodyPart);
			// attachment ends
			// Put parts in message
			msg.setContent(multipart);
			msg.setFrom(new InternetAddress(emailInfo.getFrom()));
			for(int i=0;i<emailInfo.getTo().length;i++){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInfo.getTo()[i]));
			}
			for(int i=0;i<emailInfo.getCc().length;i++){
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(emailInfo.getCc()[i]));
			}
			for(int i=0;i<emailInfo.getBcc().length;i++){
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(emailInfo.getBcc()[i]));
			}
			msg.saveChanges();
				
			Transport transport = session.getTransport("smtp");
			transport.connect(emailInfo.getHost(), emailInfo.getUserName(), emailInfo.getPassWord());
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			result = true;
		}catch (Exception mex){
			log.error("failed to send email, message : "+ mex.toString() + " Email Info : "+emailInfo);			
			throw new EmailException("failed to send email, message : " + mex.toString());
		}finally{
			log.info("End - sendEmail");
		}
		return result;
	}
}
