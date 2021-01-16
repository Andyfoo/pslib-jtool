package com.pslib.jtool.cache;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pslib.jtool.util.EncodeUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class CacheMapManager {
	protected final static Logger logger = LoggerFactory.getLogger(CacheMapManager.class);

	private static ConcurrentHashMap<String, CacheMapManager_Item> CacheItemMap = new ConcurrentHashMap<String, CacheMapManager_Item>();

	// 自动清除过期//每次设置时清除
	public static boolean autoCleanExpired = true;

	protected static long defaultLimitTime = 5 * 60 * 1000;

	protected static long autoClearPriod = 10 * 60 * 1000;
	// protected static long autoClearPriod = 10 * 1000;

	static {
		autoClearExpired();
	}

	public static void init() {

	}

	/**
	 * 取得缓存信息
	 * 
	 * @param key
	 *                关键字
	 * @return
	 */
	public static Object get(String key) {
		CacheMapManager_Item item = CacheItemMap.get(key);
		if (item == null || item.needRemove()) {
			return null;
		}
		return item.getData();
	}

	public static String getString(String key) {
		return (String) get(key);
	}
	public static JSONObject getJSONObject(String key){
		return (JSONObject)get(key);
	}
	public static JSONArray getJSONArray(String key){
		return (JSONArray)get(key);
	}
	public static Integer getInteger(String key){
		return (Integer)get(key);
	}
	public static Long getLong(String key){
		return (Long)get(key);
	}
	public static Float getFloat(String key){
		return (Float)get(key);
	}
	public static Double getDouble(String key){
		return (Double)get(key);
	}
	public static Boolean getBoolean(String key){
		return (Boolean)get(key);
	}
	/**
	 * 删除数据
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		CacheItemMap.remove(key);
	}

	/**
	 * 判断缓存对象是否有效
	 * 
	 * @param key
	 *                关键字
	 * @return
	 */
	public static boolean isValid(String key) {
		return get(key) != null;
	}

	/**
	 * 设置缓存信息
	 * 
	 * @param key
	 *                关键字
	 * @param val
	 *                缓存内容
	 * @return
	 */

	public static void put(String key, Object val, long limitTime) {
		CacheMapManager_Item item = new CacheMapManager_Item(val, limitTime);
		CacheItemMap.put(key, item);
	}

	public static void put(String key, Object val) {
		put(key, val, defaultLimitTime);
	}

	public static void put(String key, String val, long limitTime) {
		put(key, (Object) val, limitTime);
	}

	public static void put(String key, String val) {
		put(key, (Object) val, defaultLimitTime);
	}

	/**
	 * 将url生成key name
	 */
	public static String getKey(String key, HttpServletRequest request) {
		String params = "";
		if (request != null) {
			Enumeration<String> enu = (Enumeration<String>) request.getParameterNames();
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				params += "&" + paraName + "=" + request.getParameter(paraName);
			}
			if (params.length() > 0)
				params = params.substring(1);
		}
		key = key + "?" + params;
		key = EncodeUtil.MD5(key);
		return key;
	}
	public static String getKey(String key) {
		return EncodeUtil.MD5(key);
	}

	/**
	 * 清空所有缓存对象
	 * 
	 * @return
	 */
	public static void clearAll() {
		CacheItemMap.clear();
	}

	/**
	 * 获取缓存对象占用空间
	 * 
	 * @return
	 */
	public static int getSize() {
		return CacheItemMap.size();
	}

	/**
	 * 定时更新内存
	 */
	public static void autoClearExpired() {
		// schedule 在x秒后执行此任务,每次间隔y秒,如果传递一个Date参数,就可以在某个固定的时间执行这个任务.
		logger.info("autoClearExpired 开始");
		Timer timer = new Timer();
		timer.schedule(new CacheMapManager_Task(), 10 * 1000, autoClearPriod);
	}

	public static void clearExpired() {
		logger.info("clear expired start:" + "map size=" + CacheMapManager.getSize());
		Iterator<String> iter = CacheItemMap.keySet().iterator();
		String key;
		while (iter.hasNext()) {
			key = iter.next();
			if (CacheItemMap.get(key).needRemove()) {
				logger.info("clear expired key:" + key);
				CacheItemMap.remove(key);
			}
		}
		logger.info("clear expired finish:" + "map size=" + CacheMapManager.getSize());

	}

	public static void main(String args[]) {
		String str = "asdfasdas";
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			String key = CacheMapManager.getKey("/buy/a.do?i=" + i, null);

			CacheMapManager.put(key, str, 30 * 1000);
		}
		System.out.println("10");
		String key1 = CacheMapManager.getKey("/buy/a.do?i=10", null);
		CacheMapManager.put(key1, str, 1000);

		System.out.println("getSize=" + CacheMapManager.getSize());

		for (int i = 0; i < 11; i++) {
			System.out.println(i);
			String key = CacheMapManager.getKey("/buy/a.do?i=" + i, null);

			String val = CacheMapManager.getString(key);

			System.out.println(key + "=" + val);
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		CacheMapManager.clearExpired();
		System.out.println("getSize=" + CacheMapManager.getSize());

	}

}

/**
 * 定时更新
 */
class CacheMapManager_Task extends java.util.TimerTask {
	protected final static Logger logger = LoggerFactory.getLogger(CacheMapManager_Task.class);

	@Override
	public void run() {
		try {
			CacheMapManager.clearExpired();
		} catch (Throwable e) {
			logger.error("SafeInterceptorTask:", e);
		}
	}
}

class CacheMapManager_Item {
	long startTime;
	long lastTime;
	long limitTime;
	Object data;

	public CacheMapManager_Item(Object data, long limitTime) {
		this.data = data;
		this.limitTime = limitTime;
		this.startTime = System.currentTimeMillis();
		this.lastTime = this.startTime;
	}

	public void update() {
		lastTime = System.currentTimeMillis();
	}

	public boolean needRemove() {
		if (System.currentTimeMillis() - lastTime > limitTime) {
			return true;
		} else {
			return false;
		}

	}

	public Object getData() {
		return data;
	}
}
