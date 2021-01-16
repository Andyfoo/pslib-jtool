package com.pslib.jtool.httpclient.monitor;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class IdleConnMonitorThread extends Thread {
	protected final static Logger logger = LoggerFactory.getLogger(IdleConnMonitorThread.class);

	private final HttpClientConnectionManager connMgr;
	private volatile boolean shutdown;
	private int idleTimeout = 60;// 秒
	private int cycleTime = 30000;// 毫秒

	public IdleConnMonitorThread(HttpClientConnectionManager connMgr) {
		super();
		this.setName("IDLE-CONN-MONITOR");
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(cycleTime);
					logger.info("Check connections");
					// 关闭失效的连接
					connMgr.closeExpiredConnections();
					// 可选的, 关闭30秒内不活动的连接
					if (idleTimeout > 0)
						connMgr.closeIdleConnections(idleTimeout, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {

		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	/**
	 * 设置空闲超时时间(秒)
	 * 
	 * @param idleTimeout
	 */
	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	/**
	 * 清理周期(毫秒)
	 * 
	 * @param idleTimeout
	 */
	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}
}
