package org.duomn.naive.common.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.duomn.naive.common.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SSHClient {

	private static final Logger logger = LoggerFactory
			.getLogger(SSHClient.class);

	private String host = "192.168.56.2";
	private String user = "root";
	private String passwd = "123456";

	int count = 0;

	private Session sess;

	public void connect() throws BaseException {
		JSch jsch = new JSch();
		count = 0;
		try {
			sess = jsch.getSession(user, host, 22);
			sess.setPassword(passwd);

			UserInfo ui = new MyUserInfo();
			sess.setUserInfo(ui);
			sess.connect();
		} catch (Exception e) {
			throw new BaseException("JSch create session error.", e);
		}
	}

	public void disconnect() {
		if (sess != null) {
			sess.disconnect();
		}
	}

	public synchronized String sendCommand(String cmd) throws BaseException {
		count++;

		BufferedReader br = null;
		Channel channel = null;
		StringBuffer buf = new StringBuffer();

		try {
			channel = sess.openChannel("exec");
			((ChannelExec) channel).setCommand(cmd);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();

			br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line).append("@test@" + count + "@");
			}
			logger.debug("cmd:" + cmd + "----return:" + buf.toString());
		} catch (JSchException e) {
			throw new BaseException("session exception", e);
		} catch (IOException e) {
			throw new BaseException("stream exception", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (channel != null)
				channel.disconnect();
		}
		return buf.toString();
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String[] promptKeyboardInteractive(String destination,
				String name, String instruction, String[] prompt, boolean[] echo) {
			return null;
		}

		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return "123456";
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptYesNo(String message) {
			return true;
		}

		public void showMessage(String message) {}

	}

}
