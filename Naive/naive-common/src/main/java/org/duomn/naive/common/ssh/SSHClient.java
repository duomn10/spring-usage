package org.duomn.naive.common.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SSHClient {
	static JSch jsch = new JSch();
	static String host = "192.168.56.2";
	static String user = "root";
	static String passwd = "123456";
	
	private Session sess;
	
	public void connect() throws JSchException {
		JSch jsch = new JSch();

		sess = jsch.getSession(user, host, 22);
		sess.setPassword(passwd);

		UserInfo ui = new MyUserInfo();
		sess.setUserInfo(ui);
		sess.connect();
	}
	
	public void disconnect() {
		if (sess != null) {
			sess.disconnect();
		}
	}

	public String sendCommand(String cmd)
			throws JSchException, IOException {
		String command = cmd;
		Channel channel = sess.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		channel.setInputStream(null);

		((ChannelExec) channel).setErrStream(System.err);

		InputStream in = channel.getInputStream();

		channel.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			buf.append(line).append("@test@");
		}
		br.close();
		channel.disconnect();
		return  buf.toString();
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
