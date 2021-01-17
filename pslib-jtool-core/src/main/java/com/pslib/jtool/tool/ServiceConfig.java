package com.pslib.jtool.tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pslib.jtool.util.FileUtil;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class ServiceConfig {
	protected final static Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

	private final static ConcurrentSkipListMap<String, Map<String, Object>> configMap = new ConcurrentSkipListMap<String, Map<String, Object>>();
	private static String cfgBasePath;

	public static void init(String basePath) {
		cfgBasePath = basePath;
	}

	/**
	 * 读取配置文件
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static JSONObject get(String path, String name) {
		try {
			String fileName = cfgBasePath + path + "/" + name + ".json";
			Map<String, Object> cfgFile = configMap.get(fileName);
			if (cfgFile == null) {
				cfgFile = new HashMap<String, Object>();
				File file = new File(fileName);
				cfgFile.put("file", file);
				cfgFile.put("lastModify", 0);
				configMap.put(fileName, cfgFile);
			}
			File file = (File)cfgFile.get("lastModify");
			long lastModify = (long)cfgFile.get("lastModify");
			if (file == null || !file.exists()) {
				logger.info("file no exists");
				return null;
			}
			if (file.lastModified() != lastModify) {
				logger.info("load config:" + fileName);
				cfgFile.put("lastModify", file.lastModified());
				String fileStr = FileUtil.fileRead(file);
				if (fileStr == null) {
					logger.info("file is empty");
					return null;
				}
				cfgFile.put("data", JSONUtil.parseObj(fileStr));
			}
			return (JSONObject)cfgFile.get("data");
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static JSONObject get(String path) {
		return get(path, "base");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
