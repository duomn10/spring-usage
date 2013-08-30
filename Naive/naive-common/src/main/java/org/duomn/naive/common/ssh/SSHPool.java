package org.duomn.naive.common.ssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;

public class SSHPool {
	
	private static final Logger logger = LoggerFactory.getLogger(SSHPool.class);
	
	List<SSHClient> clients = new ArrayList<SSHClient>();
	
	private int pool_size = 10;
	private int used = 0;
	
	public void init() {
		logger.debug("Begin to init SSHPool...");
		logger.debug("PoolSize is :" + pool_size);
		for (int i = 0; i < 1; i++) {
			SSHClient ssh = new SSHClient();
			try {
				ssh.connect();
			} catch (JSchException e) {
				e.printStackTrace();
			}
			synchronized (clients) {
				clients.add(ssh);
				logger.debug("Init-Pool count of SSHClient[total:" + getTotalCount() + ", used:" + used + "].");
				clients.notify();
			}
		}
	}
	
	public boolean add() {
		synchronized (clients) {
			if (getTotalCount() < pool_size) {
				SSHClient ssh = new SSHClient();
				try {
					ssh.connect();
				} catch (JSchException e) {
					e.printStackTrace();
				}
				clients.add(ssh);
				logger.debug("Add-Pool count of SSHClient[total:" + getTotalCount() + ", used:" + used + "].");
				clients.notify();
				return true;
			}
			return false;
		}
	}
	
	public int getTotalCount() {
		synchronized (clients) {
			return used + clients.size();
		}
	}
	
	public SSHClient getConnection() {
		SSHClient ssh = null;
		synchronized (clients) {
			while (clients.isEmpty()) {
				try {
					if (!add()) {
						clients.wait(10 * 1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
			ssh = clients.remove(0);
			used++;
			logger.debug("Getconnection-Pool count of SSHClient[total:" + getTotalCount() + ", used:" + used + "].");
		}
		return ssh;
	}
	
	public void releaseConnection(SSHClient ssh) {
		synchronized (clients) {
			clients.add(ssh);
			used--;
			logger.debug("ReleaseConnection-Pool count of SSHClient[total:" + getTotalCount() + ", used:" + used + "].");
			clients.notify();
		}
	}
	
	public void destory() {
		for (int i = 0; i < pool_size; i++) {
			SSHClient ssh = null;
			synchronized (clients) {
				while (clients.isEmpty()) {
					try {
						clients.wait(10 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
				ssh = clients.remove(0);
				ssh.disconnect();
			}
			
			logger.debug("Destory-Pool count of SSHClient[total:" + getTotalCount() + ", used:" + used + "].");
		}
	}
	
	public String sendCommand(String cmd) {
		String msg = null;
		SSHClient ssh = this.getConnection();
		try {
			msg = ssh.sendCommand(cmd);
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
