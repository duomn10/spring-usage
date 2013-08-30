package org.duomn.naive.common.mail;

import java.io.IOException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.duomn.naive.common.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSenderImpl extends JavaMailSenderImpl implements MailSender {
	
	private static final Logger logger = LoggerFactory.getLogger(MailSenderImpl.class);
	
	private String from;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	
	@Override
	public void sendSimpleEmail(String to, String subject, String content) throws BaseException {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(this.getFrom());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		try {
			super.send(message);
			logger.debug("Send mail Success.Mail:[" + message + "].");
		} catch (MailException e) {
			logger.error("Send mail error! Mail:[" + message + "].", e);
			throw new BaseException(e);
		}
	}
	

	@Override
	public void sendMutipartEmail(String to, String subject, String content,
			String[] attaches) throws BaseException {
		sendWithAttach(to, subject, null, content, attaches);
	}

	@Override
	public void sendHtmlEmail(String to, String subject, String plainText, String htmlText,
			String[] attaches) throws BaseException {
		sendWithAttach(to, subject, plainText, htmlText, attaches);
	}
	
	/** 调用相同的处理方法用于处理异常 */
	private void sendWithAttach(String to, String subject, String plainText, String htmlText,
			String[] attaches) throws BaseException {
		MimeMessage mimeMessage = super.createMimeMessage();
		try {
			MimeMessageHelper helper = assmbleMimeHelper(mimeMessage, to, subject);
			if (plainText != null) { // 以html方式发送
				helper.setText(plainText, htmlText);
			} else {
				helper.setText(htmlText);
			}
			
			addAttachment(helper, attaches);
			
			super.send(mimeMessage);
			logger.debug("Send mail Success. Mail:[" + mimeMessage + "].");
		} catch (MessagingException e) {
			logger.error("Create MimeMessageHelp error", e);
			throw new BaseException(e);
		} catch (MailException e) {
			logger.error("Send mail error! Mail:[" + mimeMessage + "].", e);
			throw new BaseException(e);
		} catch (IOException e) {
			logger.error("Add attachment error!", e);
			throw new BaseException(e);
		}
	}

	/** 组装需要的参数 */
	private MimeMessageHelper assmbleMimeHelper(MimeMessage mimeMessage, String to, String subject) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);
		helper.setFrom(this.getFrom());
		helper.setTo(to);
		helper.setSentDate(new Date());
		helper.setSubject(subject);
		return helper;
	}

	/** 添加附件 */
	private void addAttachment(MimeMessageHelper helper, String[] attaches) throws MessagingException, IOException {
		if (attaches != null && attaches.length > 0) {
			for (String attach : attaches) {
				Resource res = new FileSystemResource(attach);
				if (!res.exists()) {
					throw new IOException("The Attachment can not be found!");
				}
				helper.addAttachment(res.getFilename(), res.getFile());
			}
		}
	}
}
