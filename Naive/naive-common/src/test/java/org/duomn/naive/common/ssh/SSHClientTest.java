package org.duomn.naive.common.ssh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.duomn.naive.common.util.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SSHClientTest {
	
	private static Logger logger = LoggerFactory.getLogger(SSHClientTest.class);

	@Test
	public void test() {
//		SSHPool pool = SSHPool.getInstance();
		
		logger.info("----------------invoke1-----------------");
		ExecutorService invokePool = Executors.newFixedThreadPool(8);
		for (int i = 0; i < 1000; i++) {
			if (i % 2 == 0) {
				SSHAccess acc = new SSHAccess("ls /home/duomn/");
				invokePool.execute(acc);
			} else {
				SSHAccess acc = new SSHAccess("ls /home/");
				invokePool.execute(acc);
			}
		}

		invokePool.shutdown();
		while (!invokePool.isTerminated()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("----------------invoke2-----------------");
		ExecutorService invokePool2 = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 200; i++) {
			if (i % 2 == 0) {
				SSHAccess acc = new SSHAccess("ls /home/duomn/");
				invokePool2.execute(acc);
			} else {
				SSHAccess acc = new SSHAccess("ls /home/");
				invokePool2.execute(acc);
			}
		}
		
		invokePool2.shutdown();
		while (!invokePool2.isTerminated()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			Thread.sleep(4 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
	}

	static class SSHAccess extends Thread {
		private String cmd;

		public SSHAccess(String cmd) {
			this.cmd = cmd;
		}

		public void run() {
			String msg = SpringContextHolder.getBean(SSHPool.class).sendCommand(cmd);
			if (msg != null && !msg.equals("null")) {
//				System.out.println(msg);
//				logger.debug(msg);
			} else {
//				System.out.println("cmd=" + cmd + ", result=" + msg);
				logger.error("cmd=" + cmd + ", result=" + msg);
				Thread t = Thread.currentThread();
//				System.out.println("id=" + t.getId() + ", name=" + t.getName()); 
				logger.error("id=" + t.getId() + ", name=" + t.getName()); 
			}
//			SSHClient ssh = SSHPool.getInstance().getConnection();
//			SSHClient ssh = SpringContextHolder.getBean(SSHPool.class).getConnection();
//			try {
//				ssh.sendCommand(cmd);
//			} catch (JSchException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
////				SSHPool.getInstance().releaseConnection(ssh);
//				SpringContextHolder.getBean(SSHPool.class).releaseConnection(ssh);
//			}
		}

	}

}
