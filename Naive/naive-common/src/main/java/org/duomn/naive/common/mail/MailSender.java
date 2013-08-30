package org.duomn.naive.common.mail;

import org.duomn.naive.common.exception.BaseException;

public interface MailSender {

	/**
	 * 发送简单样式的邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param attach
	 * @throws BaseException
	 */
	void sendSimpleEmail(String to, String subject, String content) throws BaseException;
	
	/**
	 * 发送带附件的邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @param attaches
	 * @throws BaseException
	 */
	void sendMutipartEmail(String to, String subject, String content, String[] attaches) throws BaseException;
	
	/**
	 * 发送HTML样式的邮件
	 * @param to
	 * @param subject
	 * @param htmlText
	 * @param plainText
	 * @param attach
	 * @throws BaseException
	 */
	void sendHtmlEmail(String to, String subject, String plainText, String htmlText, String[] attach) throws BaseException;
	
}
