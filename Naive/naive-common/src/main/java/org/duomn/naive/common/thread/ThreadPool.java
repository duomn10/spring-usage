package org.duomn.naive.common.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参照dwzmvc的工具类
 * @author Hu Qiang
 *
 */
public class ThreadPool implements ThreadPoolDiagnoes {
	/** Application allow mutiple thread pool, the log doesn't use static decorate */
	private Logger log = LoggerFactory.getLogger(ThreadPool.class);

	private int threadMaxSize;
	private String threadPrefix;
	private boolean running;
	
	private ThreadPoolExecutor threadPool = null;
	private BlockingQueue<Runnable> workQueue = new SynchronousQueue<Runnable>();
	
	/** 用于对诊断信息的读写锁 */
	private ReadWriteLock lock = new ReentrantReadWriteLock(false);
	/** 保存诊断信息的map */
	private Map<Runnable, ThreadDiagnose> diagnoseMap = new HashMap<Runnable, ThreadDiagnose>();
	
	public ThreadPool() {
		
	}
	
	public ThreadPool(int threadMaxSize, String threadPrefix) {
		if (threadMaxSize < 0) throw new IllegalArgumentException("maxsize < 0");
		this.threadMaxSize = threadMaxSize;
		this.threadPrefix = threadPrefix;
		log.debug("New ThreadPool given: coresize=" + threadMaxSize
				+ ", maxsize=" + threadMaxSize + ", thread_prefix="
				+ threadPrefix);
	}
	
	public synchronized void start() {
		if (!running) {
			log.debug("ThreadPool.start() starting");
		
			long waitTime = 30 * 60 * 1000L; // 30min
			threadPool = new ThreadPoolExecutor(threadMaxSize, threadMaxSize, 
					waitTime, TimeUnit.MILLISECONDS, workQueue);
			threadPool.setThreadFactory(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName(threadPrefix + "_" + next());
					t.setDaemon(true);
					if (t.getPriority() != Thread.NORM_PRIORITY) {
						t.setPriority(Thread.NORM_PRIORITY);
					}
					return t;
				}
			});
			running = true;
		} else {
			log.debug("ThreadPool.start() is already running");
		}
	}
	
	public synchronized void stop() {
		if (running) {
			log.debug("ThreadPool.stop() stopping");
			
			threadPool.shutdownNow();
			threadPool = null;
			running = false;
			try {
				lock.writeLock().lock();
				diagnoseMap.clear();
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			log.debug("ThreadPool.stop() is already stop");	
		}
	}
	
	public void execute(final Runnable torun) {
		if (running && torun != null) {
			Runnable wapper = new Runnable() {
				public void run() {
					ThreadDiagnose diagnose = new ThreadDiagnose();
					diagnose.className = torun.getClass().getName();
					diagnose.startTime = System.currentTimeMillis();
					diagnose.torun = torun;
					try {
						lock.writeLock().lock();
						diagnoseMap.put(this, diagnose);
					} finally {
						lock.writeLock().unlock();
					}
					try {
						torun.run();
					} finally {
						try {
							lock.writeLock().lock();
							diagnoseMap.remove(this);
						} finally {
							lock.writeLock().unlock();
						}
						log.debug("ThreadPool.execute:torun="
								+ diagnose.className
								+ ", Elapsed Time="
								+ (System.currentTimeMillis() - diagnose.startTime)
								+ "(ms)");
					}
				}
			};
			threadPool.execute(wapper);
		} else {
			log.debug("ThreadPool.execute() error:" + running+"="+running+", Runable="+torun);
			throw new IllegalThreadStateException("not running");
		}
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int maxSize() {
		return threadMaxSize;
	}

	@Override
	public int size() {
		if (threadPool != null) {
			return threadPool.getPoolSize();
		}
		return 0;
	}

	@Override
	public ThreadDiagnose[] getDiagnose() {
		List<ThreadDiagnose> list = new ArrayList<ThreadDiagnose>();
		try {
			lock.readLock().lock();
			for (ThreadDiagnose diagnose : diagnoseMap.values()) {
				list.add(diagnose.copyOne());
			}
		} finally {
			lock.readLock().unlock();
		}
		
		return list.toArray(new ThreadDiagnose[list.size()]);
	}



	private static AtomicInteger num = new AtomicInteger(0);
	/** 供线程命名时使用 */
	private static int next() {
		return num.getAndAdd(1);
	}
	
	/** 线程诊断信息 */
	public static class ThreadDiagnose {
		/** 正在运行的任务的类名 */
		private String className;
		/** 开始时间 */
		private long startTime;
		/** 要执行的任务 */
		private Runnable torun;
		
		private ThreadDiagnose copyOne() {
			ThreadDiagnose diagnose = new ThreadDiagnose();
			diagnose.className = className;
			diagnose.startTime = startTime;
			diagnose.torun = torun;
			return diagnose;
		}
	}
}
