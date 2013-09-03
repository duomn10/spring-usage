package org.duomn.naive.common.ssh;

import java.util.ArrayList;
import java.util.List;

import org.duomn.naive.common.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHPool {
	
	private static final Logger logger = LoggerFactory.getLogger(SSHPool.class);
	
	List<SSHClient> clients = new ArrayList<SSHClient>();
	
	/* 资源池的上限 */
	private int poolSize = 10;
	/* 资源池的初始化大小 */
	private int initSize = 0;
	/* 缩减资源池的线程频率 */
	private int shrinkCheckFrequency = 3; 
	
	private int used = 0;
	/* 用来判断池的上限，是否可以继续创建资源 */
	private int total = 0; 
	/* 池里面的实际容量，取list的size不准确 */
	private int actualTotal = 0;
	
	private volatile boolean isShutdown = false;
	
	public void init() {
		logger.info("Begin to init SSHPool...");
		logger.info("PoolSize is :" + poolSize);
		while (total < initSize) {
			total++;
			SSHClient ssh = new SSHClient();
			ssh.connect();
			
			clients.add(ssh);
			actualTotal++;
			logger.info("Init-Pool " + getPoolStatus());
			clients.notify();
		}
		
		/** Shrink */
		new Thread() {
			public void run() {
				while (!isShutdown) {
					try {
						Thread.sleep(shrinkCheckFrequency * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					while (actualTotal != 0 && actualTotal > initSize && used * 10 / actualTotal < 4) {
						total--;
						SSHClient ssh = null;
						try {
							synchronized (clients) {
								ssh = clients.remove(0);
								actualTotal--;
								clients.notify();
							}
							ssh.disconnect();
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("Shrink-Pool error.", e);
							total++;
						}
						logger.info("Shrink-Pool " + getPoolStatus());
					}
				}
			}
		}.start();
	}
	
	private String getPoolStatus() {
		return "The count of SSHClient[total:" + total + ", actual:" + actualTotal + ", used:" + used + ", clients size:" + clients.size() + "].";
	}
	
	/** 动态扩容 */
	public void enlarge() {
		new Thread() {
			public void run() {
				if (total < poolSize) {
					total++;
					try {
						SSHClient ssh = new SSHClient();
						ssh.connect();
						synchronized (clients) {
							clients.add(ssh);
							actualTotal++;
							logger.info("Enlarge-Pool " + getPoolStatus());
							clients.notify();
						}
					} catch (Exception e) {
//						e.printStackTrace();
						logger.error("Enlarge-Pool error.", e);
						total--;
					}
				}
			}
		}.start();
	}

	public SSHClient getConnection() {
		SSHClient ssh = null;
		synchronized (clients) {
			while (clients.isEmpty()) {
				try {
					enlarge();
					clients.wait(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.error("System thread(getConnection) wait error.", e);
				} 
			}
			ssh = clients.remove(0);
			used++;
			logger.info("Getconnection-Pool " + getPoolStatus());
		}
		return ssh;
	}
	
	public void releaseConnection(SSHClient ssh) {
		synchronized (clients) {
			used--;
			clients.add(ssh);
			logger.info("ReleaseConnection-Pool " + getPoolStatus());
			clients.notify();
		}
	}
	
	public void destory() {
		isShutdown = true;
		while (total > 0) {
			SSHClient ssh = null;
			synchronized (clients) {
				while (clients.isEmpty()) {
					try {
						clients.wait(1 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						logger.error("System thread(destory) wait error.", e);
					} 
				}
				ssh = clients.remove(0);
				actualTotal--;
				ssh.disconnect();
				total--;
			}
			
			logger.info("Destory-Pool " + getPoolStatus());
		}
	}
	
	public String sendCommand(String cmd) {
		String msg = null;
		SSHClient ssh = this.getConnection();
		try {
			msg = ssh.sendCommand(cmd);
		} catch (BaseException e) {
			e.printStackTrace();
			logger.error("SSHClient exec happenned error.", e);
		}
		this.releaseConnection(ssh);
		return msg;
	}
	
//	private SSHPool() {
//	init();
//}
	
//	public static SSHPool getInstance() {
//		return SingletonHolder.pool;
//	}
	
//	private static class SingletonHolder {
//		private static final SSHPool pool = new SSHPool();
//	}

}
