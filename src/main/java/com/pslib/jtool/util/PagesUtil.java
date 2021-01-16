package com.pslib.jtool.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONObject;


public class PagesUtil {
	protected final static Logger logger = LoggerFactory.getLogger(PagesUtil.class);

	/**
	 * 
	 * @param url
	 * @param curPage
	 * @param totalPage
	 * @param totalNum
	 * @param params
	 *                传递的参数
	 * @return
	 */
	public static JSONObject page(String url, int curPage, int pageSize, int totalNum, LinkedHashMap<String, Object> params) {
		JSONObject json = new JSONObject();
		json.set("totalNum", totalNum);
		json.set("pageSize", pageSize);
		json.set("curPage", curPage);
		json.set("pageNum", totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1));
		if (url != null)
			json.set("pageUrl", parseParam(url, params));
		return json;
	}
	public static JSONObject page(String url, int curPage, int pageSize, int totalNum, JSONObject params) {
		JSONObject json = new JSONObject();
		json.set("totalNum", totalNum);
		json.set("pageSize", pageSize);
		json.set("curPage", curPage);
		json.set("pageNum", totalNum / pageSize + (totalNum % pageSize == 0 ? 0 : 1));
		if (url != null)
			json.set("pageUrl", parseParam(url, params));
		return json;
	}

	public static JSONObject page(String url, int curPage, int pageSize, int totalNum) {
		return page(url, curPage, pageSize, totalNum, (JSONObject)null);
	}

	public static JSONObject page(int curPage, int pageSize, int totalNum) {
		return page(null, curPage, pageSize, totalNum, (JSONObject)null);
	}

	/**
	 * 组装URL
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String parseParam(String url, Map<String, Object> params) {
		if (null == url) {
			return null;
		}
		if (null == params) {
			return url;
		}
		int type = 0;
		if (url.endsWith("?")) {
			type = 0;
		} else if (url.indexOf('?') > -1) {
			type = 1;
		} else
			type = 2;
		StringBuilder sb = new StringBuilder(url);
		int i = 0;
		for (String key : params.keySet()) {
			switch (type) {
			case 0:
				if (i != 0) {
					sb.append("&");
				}
				i++;
				break;
			case 1:
				sb.append("&");
				i++;
				break;
			case 2:
				if (i == 0) {
					sb.append("?");
				} else {
					sb.append("&");
				}
				i++;
				break;
			}
			sb.append(key);
			sb.append("=");
			// decode 参数值
			sb.append(encode(params.get(key).toString()));
		}
		return sb.toString();
	}

	private static String encode(String value) {
		// ....
		if (null == value)
			return "";
		else
			try {
				value = URLEncoder.encode(value, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}
		return value;
	}

	public static void main(String[] argc) {

	}
}
