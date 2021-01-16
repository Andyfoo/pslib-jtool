package com.pslib.jtool.cache;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pslib.jtool.util.EncodeUtil;
import com.pslib.jtool.util.FileUtil;

public class CacheFileManager {
	protected final static Logger log = LoggerFactory.getLogger(CacheFileManager.class);

	public static String _basePath;
	public static boolean useMonthPartition = true;// 是否按月分目录，这样上一月的数据直接删除一个目录即可

	public String _cache_path = "data/cache_site/";

	public String _path;
	public String _filename;
	public long _limitCacheTime;

	/**
	 * 初始化路径
	 */
	public CacheFileManager(String path) {
		_path = path;
		_limitCacheTime = 5 * 60;
	}

	public CacheFileManager(String path, long limitTime) {
		_path = path;
		_limitCacheTime = limitTime;
	}

	public CacheFileManager() {
		_path = "";
		_limitCacheTime = 5 * 60;
	}

	/**
	 * 设置缓存时间
	 * 
	 * @param time 缓存时间 (秒)
	 * @return
	 */
	public void setLimitCacheTime(long time) {
		_limitCacheTime = time;
	}

	/**
	 * 取得缓存信息
	 * 
	 * @param key 关键字
	 * @return
	 */
	public String get() {
		return get(_filename);
	}

	public String get(String filename) {
		long nowTime = new Date().getTime();
		File file = new File(filename);

		long ftime = file.lastModified();
		if (file.exists() && file.isFile() && file.length() > 0 && (nowTime - ftime) < _limitCacheTime * 1000) {
			return FileUtil.fileRead(filename);
		} else {
			return null;
		}

	}

	/**
	 * 判断缓存文件是否有效
	 * 
	 * @param key 关键字
	 * @return
	 */
	public boolean isValid() {
		return isValid(_filename);
	}

	public boolean isValid(String filename) {
		long nowTime = new Date().getTime();
		File file = new File(filename);

		long ftime = file.lastModified();
		if (file.exists() && file.isFile() && file.length() > 0 && (nowTime - ftime) < _limitCacheTime * 1000) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 设置缓存信息
	 * 
	 * @param key 关键字
	 * @param val 缓存内容
	 * @return
	 */
	public boolean set(String val) {
		return set(_filename, val);
	}

	public boolean set(String filename, String val) {
		return FileUtil.fileWrite(filename, val);
	}

	/**
	 * 获取文件名
	 */
	public String getFilename(String filename, HttpServletRequest request) {
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

		String path = _basePath + _cache_path;
		if (useMonthPartition) {
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			path += df.format(new java.util.Date()) + "/";
		}
		path += _path;
		// System.out.println(path);

		filename = filename + "?" + params;
		filename = EncodeUtil.MD5(path + filename);

		String subPath1 = _limitCacheTime + "";
		String subPath2 = filename.substring(0, 2);

		path += "/" + subPath1 + "/" + subPath2 + "/";
		FileUtil.dirCreate(path, true);
		_filename = path + filename;

		return _filename;
	}

	public void setFilename(String filename) {
		_filename = filename;
	}

	public static void main(String args[]) {
		CacheFileManager._basePath = "E:/test/";
		CacheFileManager cm = new CacheFileManager("order");
		String str = FileUtil.fileRead("E:/test/sitemap.xml");
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			cm.getFilename("/buy/a.do?i=" + i, null);

			cm.set(str);
			// FileUtil.fileWrite(cm._filename, str);//0.61
			// FileUtil.fileWrite(cm._filename, str);

		}

	}

}
