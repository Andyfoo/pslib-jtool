package com.pslib.jtool.framework.hutool.json;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;

public class HutoolJsonUtil {
	public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static JSONConfig jsonConfig() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat(dateFormat);
		return jsonConfig;
	}
	public static JSONConfig jsonConfigForOrder() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat(dateFormat);
		jsonConfig.setOrder(true);
		return jsonConfig;
	}

	public static JSONConfig jsonConfigForOrderAllowNull() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat(dateFormat);
		jsonConfig.setOrder(true);
		jsonConfig.setIgnoreNullValue(false);
		return jsonConfig;
	}
	public static JSONConfig jsonConfigAllowNull() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat(dateFormat);
		jsonConfig.setIgnoreNullValue(false);
		return jsonConfig;
	}

	
	public static String toJsonPrettyStr(Object obj) {
		return JSONUtil.toJsonPrettyStr(JSONUtil.parse(obj, jsonConfig()));
	}
}
