package com.pslib.jtool.httpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pslib.jtool.httpclient.monitor.IdlePoolingConnMonitorThread;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 多服务类型的接口客户端
 * 
 * @author FH
 *
 */
public class MHttpClient {
	protected final static Logger logger = LoggerFactory.getLogger(MHttpClient.class);
	private String serviceName;
	private static ConcurrentHashMap<String, MHttpClient> S = new ConcurrentHashMap<String, MHttpClient>();

	private IdlePoolingConnMonitorThread connMonitor = null;
	public PoolingHttpClient httpClient = null;
	private Vector<MHttpHost> hosts = new Vector<MHttpHost>();

	/**
	 * ping任务中的检测URL
	 */
	public String pingPath = null;
	/**
	 * ping任务中查询是否包含指定关键字
	 */
	public String pingContainStr = null;

	/**
	 * 增加服务类型
	 * 
	 * @param serviceName
	 */
	public static void addService(String serviceName) {
		S.put(serviceName, new MHttpClient(serviceName));
	}

	/**
	 * 获取服务类型
	 * 
	 * @param serviceName
	 */
	public static MHttpClient getService(String serviceName) {
		return S.get(serviceName);
	}

	/**
	 * 获取服务类型
	 * 
	 * @param serviceName
	 */
	public static MHttpClient S(String serviceName) {
		return getService(serviceName);
	}

	/**
	 * 初始化
	 */
	public MHttpClient(String serviceName) {
		this.serviceName = serviceName;
		httpClient = new PoolingHttpClient();
		httpClient.setMaxTotal(1000);
		httpClient.setDefaultMaxPerRoute(500);

		httpClient.setSocketTimeout(10000);
		httpClient.setConnectTimeout(10000);
		httpClient.setConnectionRequestTimeout(20000);

		httpClient.init();

		connMonitor = new IdlePoolingConnMonitorThread(httpClient.getCm(), 30000, 30);
		connMonitor.setDaemon(true);
		connMonitor.start();

	}

	public void addHost(String host) {
		if (host == null || host.length() == 0) {
			logger.error("host为空，请检查设置");
			return;
		}
		if (!host.contains("://")) {
			host = "http://" + host;
		}
		hosts.add(new MHttpHost(host));
	}

	public boolean hostMonitorShutdown = false;

	public void hostMonitor() {
		Thread daemon = new Thread("MHTTP-HOST-MONITOR-" + this.serviceName) {
			@Override
			public void run() {
				try {
					synchronized (this) {
						while (!hostMonitorShutdown) {
							wait(1000);
							// logger.info("host check");
							MHttpHost mhHost;
							boolean pingStatus;
							for (int i = 0; i < hosts.size(); i++) {
								mhHost = hosts.get(i);
								if (pingPath != null) {
									logger.debug("host check:" + mhHost.getHost() + pingPath + "---(" + pingContainStr + ")");
									pingStatus = httpClient.ping(mhHost.getHost() + pingPath, pingContainStr);
								} else {
									logger.debug("host check:" + mhHost.getHost());
									pingStatus = httpClient.ping(mhHost.getHost());
								}

								if (pingStatus) {
									mhHost.setAvailable(true);
								} else {
									logger.error("host down:" + mhHost.getHost());
									mhHost.setAvailable(false);
								}
							}
							// System.out.println(JSON.toJSON(hosts));
						}
					}
				} catch (Exception ex) {
					logger.error("hostMonitor", ex);
				}
			}
		};
		daemon.setDaemon(true);
		daemon.start();
	}

	public String getAvailableHost() {
		List<String> availableHosts = new ArrayList<String>();
		MHttpHost mhHost;
		Random random = new Random();
		for (int i = 0; i < hosts.size(); i++) {
			mhHost = hosts.get(i);
			if (mhHost.isAvailable()) {
				availableHosts.add(mhHost.getHost());
			}
		}
		int size = availableHosts.size();
		if (size == 0)
			return null;
		return availableHosts.get(random.nextInt(size));
	}

	/**
	 * get方式提交
	 * 
	 * @param path
	 * @param vars
	 * @return
	 */
	public String get(String path, List<NameValuePair> vars) {
		String url = getAvailableHost();
		if (url == null) {
			logger.error("没有可用服务器");
			return null;
		}
		return httpClient.get(url + path, vars);
	}

	public String get(String url) {
		return get(url, null);
	}

	public JSONObject get_JSONObject(String path, JSONObject req) {
		return JSONUtil.parseObj(get(path, convPair(req)));
	}

	public JSONArray get_JSONArray(String path, JSONObject req) {
		return JSONUtil.parseArray(get(path, convPair(req)));
	}

	/**
	 * post方式提交
	 * 
	 * @param path
	 * @param vars
	 * @return
	 */
	public String post(String path, List<NameValuePair> vars) {
		String url = getAvailableHost();
		if (url == null) {
			logger.error("没有可用服务器");
			return null;
		}
		return httpClient.post(url + path, vars);
	}

	public JSONObject post_JSONObject(String path, JSONObject req) {
		return JSONUtil.parseObj(post(path, convPair(req)));
	}

	public JSONArray post_JSONArray(String path, JSONObject req) {
		return JSONUtil.parseArray(post(path, convPair(req)));
	}

	/**
	 * 数据流方式提交
	 * 
	 * @param path
	 * @param vars
	 * @return
	 */
	public String postStream(String path, String dataStr) {
		String url = getAvailableHost();
		if (url == null) {
			logger.error("没有可用服务器");
			return null;
		}
		return httpClient.postStream(url + path, dataStr);
	}

	public JSONObject postStream_JSONObject(String path, String dataStr) {
		return JSONUtil.parseObj(postStream(path, dataStr));
	}

	public JSONArray postStream_JSONArray(String path, String dataStr) {
		return JSONUtil.parseArray(postStream(path, dataStr));
	}

	/**
	 * json转换为List<NameValuePair>
	 * 
	 * @param parms
	 * @return
	 */
	public List<NameValuePair> convPair(JSONObject parms) {
		List<NameValuePair> vars = new ArrayList<NameValuePair>();
		if (parms != null) {
			Set<String> keySet = parms.keySet();
			NameValuePair vPair;
			for (String key : keySet) {
				vPair = new BasicNameValuePair(key, parms.getStr(key));
				vars.add(vPair);
			}
		}
		return vars;
	}

	public static void main(String[] args) {
		MHttpClient.addService("server1");
		// MHttpClient.S("server1").addHost("http://192.168.17.172:10018");
		MHttpClient.S("server1").pingPath = "/check";
		// MHttpClient.S("server1").pingContainStr="ok";
		MHttpClient.S("server1").addHost("http://192.168.126.182");
		MHttpClient.S("server1").hostMonitor();

		while (true) {
			MHttpClient.S("server1").httpClient.printTotalStats();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
		}
	}

}

class MHttpHost {
	private String host;
	private boolean available;

	public MHttpHost(String host) {
		this.host = host;
		this.available = true;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
