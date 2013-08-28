package org.springusage.mybatis.generator.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class DBPool {

	public static Queue<Connection> queue = new LinkedList<Connection>();
	private static final int POOL_SIZE = 5;
	private static volatile boolean shutdown = false;

	public static void init() {
		synchronized (queue) {
			try {
				for (int i = 0; i < POOL_SIZE; i++) {
					queue.offer(new DBPool().getConnection());
				}
				queue.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getFreeConnCount() {
		return queue.size();
	}

	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(PropertyUtils.getProperty("jdbc.driverClassName"));
		return DriverManager.getConnection(
				PropertyUtils.getProperty("jdbc.url"),
				PropertyUtils.getProperty("jdbc.username"),
				PropertyUtils.getProperty("jdbc.password"));
	}

	public static Connection acquireConnection() {
		synchronized (queue) {
			Connection conn = queue.poll();
			while (conn == null && !shutdown) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				conn = queue.poll();
			}
			return conn;
		}
	}

	public static void releaseConnection(Connection conn) {
		synchronized (queue) {
			queue.offer(conn);
			queue.notifyAll();
		}
	}

	public static void destory() {
		shutdown = true;
		int count = 0;
		while (count < POOL_SIZE) {
			Connection conn = null;
			synchronized (queue) {
				conn = queue.poll();
				while (conn == null) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					conn = queue.poll();
				}
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			count++;
		}
	}

	public static void main(String[] args) {
		PropertyUtils.init();

		DBPool.init();
		System.out.println("pool free size:" + getFreeConnCount());
		Connection conn = DBPool.acquireConnection();
		System.out.println(conn == null);
		System.out.println("pool free size:" + getFreeConnCount());
		DBPool.releaseConnection(conn);
		System.out.println("pool free size:" + getFreeConnCount());
		DBPool.destory();
	}
}
