package org.duomn.naive.common.ssh;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.duomn.naive.common.util.SpringContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SSHClientTest {

	@Test
	public void test() {
//		SSHPool pool = SSHPool.getInstance();

		ExecutorService invokePool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 100; i++) {
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
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	static class SSHAccess extends Thread {
		private String cmd;

		public SSHAccess(String cmd) {
			this.cmd = cmd;
		}

		public void run() {
			String msg = SpringContextHolder.getBean(SSHPool.class).sendCommand(cmd);
			System.out.println(msg);
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
