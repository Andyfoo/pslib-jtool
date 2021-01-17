package com.pslib.jtool.util.thread;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分组线程池工具类
 * 
 * @author FH
 *
 */
public class ThreadPoolGroup {
	protected final static Logger logger = LoggerFactory.getLogger(ThreadPoolGroup.class);

	private ConcurrentSkipListMap<String, ExecutorService> esMap = new ConcurrentSkipListMap<String, ExecutorService>();
	public static final ThreadPoolGroup INSTANCE;
	static {
		INSTANCE = new ThreadPoolGroup();
	}

	/**
	 * 添加指定类型的线程池
	 * 
	 * @param groupName
	 * @param nThreads
	 */
	public void addGroup(String groupName, int nThreads) {
		if (esMap.containsKey(groupName)) {
			return;
		}
		logger.info(String.format("add group: groupName=%s, nThreads=%d", groupName, nThreads));
		esMap.put(groupName, Executors.newFixedThreadPool(nThreads));
	}

	/**
	 * 添加线程
	 * 
	 * @param groupName
	 * @param task
	 */
	public void addThread(String groupName, Runnable task) {
		addGroup(groupName, 20);
		logger.info(String.format("add thread to group: %s", groupName));
		esMap.get(groupName).submit(task);
	}

	/**
	 * 清除所有任务
	 */
	public void clear() {
		logger.info("clear all group");
		Set<String> keys = esMap.keySet();
		for (String key : keys) {
			ExecutorService es = esMap.get(key);
			es.shutdownNow();
			esMap.remove(key);
		}
	}

	/**
	 * 清除指定任务
	 */
	public void clear(String groupName) {
		logger.info(String.format("clear group: %s", groupName));
		ExecutorService es = esMap.get(groupName);
		if (es != null) {
			es.shutdownNow();
			esMap.remove(groupName);
		}
	}

	/**
	 * 开启监控
	 */
	public void monitor() {
		monitor(5000);
	}

	public void monitor(int time) {
		new Thread("MONITOR") {
			public void run() {
				try {
					logger.info("monitor start");
					while (true) {
						Set<String> keys = esMap.keySet();
						logger.info(String.format("group num：%d", keys.size()));
						for (String key : keys) {
							ThreadPoolExecutor es = (ThreadPoolExecutor) esMap.get(key);
							// System.out.println(JSON.toJSON(es));
							logger.info(String.format("%s：maxPool=%d, pool=%d, active=%d, task=%d, completedTask=%d", key, es.getMaximumPoolSize(), es.getPoolSize(), es.getActiveCount(),
									es.getTaskCount(), es.getCompletedTaskCount()));
						}
						try {
							sleep(time);
						} catch (InterruptedException e) {
							logger.error("", e);
						}
					}
				} catch (Exception e) {
					logger.error("", e);
				}

			}
		}.start();

	}

	public void sleep(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	public static void main(String[] args) {
		ThreadPoolGroup tpg = new ThreadPoolGroup();
		tpg.addThread("task1", new Thread() {
			public void run() {
				while (!Thread.interrupted()) {
					System.out.println("beat");
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		tpg.addThread("task1", new Thread() {
			public void run() {
				while (!Thread.interrupted()) {
					System.out.println("beat2222");
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
					}
				}
			}
		});
		tpg.addThread("task1", new Thread() {
			public void run() {
				System.out.println("beat3333");
			}
		});

		tpg.addThread("task2", new Thread() {
			public void run() {
				try {
					while (!Thread.interrupted()) {
						System.out.println("22222");
						try {
							sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
					}
				} catch (Exception e) {

				}
			}
		});
		tpg.monitor();
		// sleep(5000);
		// tpg.clear();

	}

}
