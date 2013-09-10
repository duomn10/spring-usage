package org.duomn.naive.common.thread;

import org.duomn.naive.common.thread.ThreadPool.ThreadDiagnose;

/**
 * 参照dwzmvc的工具类
 * @author Hu Qiang
 *
 */
public interface ThreadPoolDiagnoes {
	
	boolean isRunning();
	
	int maxSize();
	
	int size();
	
	public ThreadDiagnose[] getDiagnose();
}
