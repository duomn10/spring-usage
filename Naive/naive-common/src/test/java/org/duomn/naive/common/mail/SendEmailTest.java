package org.duomn.naive.common.mail;

import junit.framework.Assert;

import org.duomn.naive.common.exception.BaseException;
import org.duomn.naive.common.util.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SendEmailTest {
	
	private static final String to = "hbhuqiang@sina.com";

	@Test
	public void testSendMail() {
		String[] attach = {"D:/WORK/SELF/lic/06054CA_8246-L2D-10.lic", 
				"D:/WORK/SELF/lic/060550A_8246-L2D-12.lic"};
		try {
			SpringContextHolder.getMailSender().sendHtmlEmail(to, 
						"带附件的测试邮件", 
						"无样式的内容", 
						"<b>带样式的内容</b>", 
						attach);
		} catch (BaseException e) {
			Assert.fail("发送邮件发生异常！");
		}
	}
	
	@Test
	public void testSampleSendMail() {
		SpringContextHolder.getMailSender().sendSimpleEmail(to, "简单文本邮件", "文本邮件内容");
	}

}
