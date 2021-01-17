package com.pslib.jtool.util;

import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONUtil;

public class HttpUrlParser {
	protected final static Logger logger = LoggerFactory.getLogger(HttpUrlParser.class);

	private URL url = null;
	private Map<String, Object> queryMap = null;

	public static void main(String[] args) {
		Map<String, Object> map;
		map = HttpUrlParser.parse("aa.php?a=1&b=2&c[]=1&c[]=2#33", "UTF-8").getQueryMap();
		System.out.println(JSONUtil.toJsonPrettyStr(map));

	}

	public static Map<String, Object> parse_str(String urlStr, String encode) {
		try {
			return new HttpUrlParser(urlStr, encode).getQueryMap();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static Map<String, Object> parse_str(String urlStr) {
		try {
			return new HttpUrlParser(urlStr).getQueryMap();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static HttpUrlParser parse(String urlStr, String encode) {
		try {
			return new HttpUrlParser(urlStr, encode);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static HttpUrlParser parse(String urlStr) {
		try {
			return new HttpUrlParser(urlStr);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public HttpUrlParser(String urlStr) throws Exception {
		parseUrl(urlStr, "UTF-8");
	}

	public HttpUrlParser(String urlStr, String encode) throws Exception {
		parseUrl(urlStr, encode);
	}

	public void parseUrl(String urlStr, String encode) throws Exception {
		if (urlStr.indexOf(":") > 0) {
			url = new URL(urlStr);
			queryMap = parseStr(url.getQuery(), encode);
		} else {
			queryMap = parseStr(urlStr, encode);
		}

	}

	public Map<String, Object> parseStr(String queryString, String enc) throws Exception {
		int find1 = queryString.indexOf("?");
		if (find1 > -1) {
			queryString = queryString.substring(find1 + 1);
		}
		find1 = queryString.indexOf("#");
		if (find1 > -1) {
			queryString = queryString.substring(0, find1);
		}
		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		if (queryString != null && queryString.length() > 0) {
			int ampersandIndex, lastAmpersandIndex = 0;
			String subStr, param, value;
			String[] paramPair, values, newValues;
			do {
				ampersandIndex = queryString.indexOf('&', lastAmpersandIndex) + 1;
				if (ampersandIndex > 0) {
					subStr = queryString.substring(lastAmpersandIndex, ampersandIndex - 1);
					lastAmpersandIndex = ampersandIndex;
				} else {
					subStr = queryString.substring(lastAmpersandIndex);
				}
				paramPair = subStr.split("=");
				param = paramPair[0];
				value = paramPair.length == 1 ? "" : paramPair[1];
				try {
					value = URLDecoder.decode(value, enc);
				} catch (Exception ignored) {
					logger.error("", ignored);
				}
				if (paramsMap.containsKey(param)) {
					values = (String[]) paramsMap.get(param);
					int len = values.length;
					newValues = new String[len + 1];
					System.arraycopy(values, 0, newValues, 0, len);
					newValues[len] = value;
				} else {
					newValues = new String[] { value };
				}
				paramsMap.put(param, newValues);
			} while (ampersandIndex > 0);
		}
		return paramsMap;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Map<String, Object> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, Object> queryMap) {
		this.queryMap = queryMap;
	}

}
