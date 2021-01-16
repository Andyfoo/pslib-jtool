package com.pslib.jtool.db;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pslib.jtool.db.sqlformatter.SqlFormatter;
import com.pslib.jtool.framework.hutool.json.HutoolJsonUtil;
import com.pslib.jtool.util.DateUtil;
import com.pslib.jtool.util.StringUtil;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class DbTool {
	protected static final Logger logger = LoggerFactory.getLogger(DbTool.class);
	static String safeFilter = "'|(and|or)\\b.+?(>|<|=|in|like)|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";
	static Pattern safeFilterPattern = Pattern.compile(safeFilter, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	public static void main(String argc[]) {
		// System.out.println(getSafeStr("asfasdf'sadfasf and in and sad UNION a SELECT * FROM xxxx where M_LOGINNAME ='test';s"));

		// System.out.println(htmlspecialchars("\"'&<afas>\"'&<afas>\"'&<afas>"));
		// System.out.println(htmlspecialchars_decode(htmlspecialchars("\"'&<afas>\"'&<afas>\"'&<afas>")));

		System.out.println(getQuerySafeStr("\"'&<afas\\>\"'&<afas>\"'&<afas>"));
	}

	/**
	 * 检测变量
	 * 
	 * @param str
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkSafeStr(String str) {
		Matcher m = safeFilterPattern.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	/*
	 * sql需要加 ESCAPE '/'： SELECT * FROM BOSS_USER_MESSAGE WHERE MESSAGE_TITLE LIKE '%/%%' ESCAPE '/'
	 */
	public static String escapeSQLLike(String likeStr) {
		if(likeStr == null) {
			return null;
		}
		String str = likeStr;
		str = str.trim();
		str = StringUtil.replace(str, "/", "//");
		str = StringUtil.replace(str, "_", "/_");
		str = StringUtil.replace(str, "%", "/%");
		str = StringUtil.replace(str, "％", "/％");
		str = StringUtil.replace(str, "'", "‘");

		// str = StringUtils.replace(str, "?", "_");
		// str = StringUtils.replace(str, "*", "%");
		return str;
	}

	/**
	 * 返回安全的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getQuerySafeStr(String str) {
		if (str == null)
			return "";

		return str.replaceAll(safeFilter, "");
		// return escapeSQLLike(str);
	}

	/**
	 * 返回安全的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getDbSafeStr(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "'", "‘");

		str = StringUtil.replace(str, "<", "＜");
		str = StringUtil.replace(str, ">", "＞");

		return str;
	}

	/**
	 * 编码字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlspecialchars(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "&", "&amp;");
		str = StringUtil.replace(str, "\"", "&quot;");
		str = StringUtil.replace(str, "'", "&#039;");
		str = StringUtil.replace(str, "<", "&lt;");
		str = StringUtil.replace(str, ">", "&gt;");
		return str;
	}

	/**
	 * 解码字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlspecialchars_decode(String str) {
		if (str == null)
			return "";
		str = StringUtil.replace(str, "&amp;", "&");
		str = StringUtil.replace(str, "&quot;", "\"");
		str = StringUtil.replace(str, "&#039;", "'");
		str = StringUtil.replace(str, "&lt;", "<");
		str = StringUtil.replace(str, "&gt;", ">");
		return str;
	}

	public static int getInt(Object obj) {
		int value = 0;
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				value = ((BigDecimal) obj).intValue();
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).intValue();
			} else if (obj instanceof String) {
				return Integer.valueOf((String) obj);
			} else if (obj instanceof Long) {
				value = ((Long) obj).intValue();
			}

		}
		return value;
	}

	public static long getLong(Object obj) {
		long value = 0;
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				value = ((BigDecimal) obj).longValue();
			} else if (obj instanceof Long) {
				value = ((Long) obj).longValue();
			} else if (obj instanceof String) {
				return Long.valueOf((String) obj);
			} else if (obj instanceof Integer) {
				value = ((Integer) obj).longValue();
			} else {
				value = Long.valueOf((String) obj);
			}

		}
		return value;
	}

	public static String getString(Object obj) {
		String value = null;
		if (obj != null) {
			value = String.valueOf(obj);
			if ("null".equals(value)) {
				value = null;
			}
		}
		return value;
	}

	public static String getString(byte[] bytes, String encode) {
		String value = null;
		if (bytes != null) {
			try {
				value = new String(bytes, encode);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if ("null".equals(value)) {
				value = null;
			}
		}
		return value;
	}

	public static String getString(byte[] bytes) {
		return getString(bytes, "utf-8");
	}

	public static double getDouble(Object obj) {
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				return ((BigDecimal) obj).doubleValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).doubleValue();
			} else if (obj instanceof String) {
				return Double.valueOf((String) obj);
			} else if (obj instanceof Long) {
				return ((Long) obj).doubleValue();
			}
			return (Double) obj;
		} else {
			return 0;
		}
	}

	public static float getFloat(Object obj) {
		if (obj != null) {
			if (obj instanceof BigDecimal) {
				return ((BigDecimal) obj).floatValue();
			} else if (obj instanceof Integer) {
				return ((Integer) obj).floatValue();
			} else if (obj instanceof String) {
				return Float.valueOf((String) obj);
			} else if (obj instanceof Long) {
				return ((Long) obj).floatValue();
			}
			return (Float) obj;
		} else {
			return 0;
		}
	}

	public static BigDecimal getBigDecimal(Object obj) {
		if (obj != null) {
			return (BigDecimal) obj;
		} else {
			return BigDecimal.ZERO;
		}
	}

	public static Date getDate(Object obj) {
		if (obj == null)
			return null;
		return (Date) obj;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static double getTime() {
		return System.currentTimeMillis();
	}

	// 计算运行时间
	public static String runTime(double t) {
		return StringUtil.formatRate((getTime() - t) / 1000, "##0.000") + "s";
	}

	/**
	 * 将列表转换为json(字段名小写)
	 * 
	 * @param lists
	 * @return
	 */
	public static JSONArray field2json(List<Map<String, Object>> lists) {
		JSONArray json = new JSONArray(HutoolJsonUtil.jsonConfig());
		for (Map<String, Object> map : lists) {
			json.add(field2json(map));
		}
		return json;
	}

	/**
	 * 将map转换为json(字段名小写)
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject field2json(Map<String, Object> map) {
		if (map == null)
			return null;
		JSONObject json = new JSONObject(HutoolJsonUtil.jsonConfigForOrder());
		String key;
		Object val;
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			val = entry.getValue();
			json.set(key.toLowerCase(), val);
		}
		return json;
	}

	/**
	 * 将列表转换为json(大写小名转换，如user_list字段，这里变为 userList)
	 * 
	 * @param lists
	 * @return
	 */
	public static JSONArray toJSON(List<Map<String, Object>> lists) {
		JSONArray json = new JSONArray(HutoolJsonUtil.jsonConfig());
		for (Map<String, Object> map : lists) {
			json.add(toJSON(map));
		}
		return json;
	}

	/**
	 * 将map转换为json(大写小名转换，如user_list字段，这里变为 userList)
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject toJSON(Map<String, Object> map) {
		if (map == null)
			return null;
		JSONObject json = new JSONObject(HutoolJsonUtil.jsonConfigForOrder());
		String key;
		Object val;
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			key = entry.getKey();
			val = entry.getValue();
			json.set(changeCase(key), val);

		}
		return json;
	}

	/**
	 * 
	 * 功能：将输入字符串的首字母改成大写
	 * 
	 * @param str
	 * 
	 * @return
	 */
	private static String changeCase(String str) {
		String[] arr = str.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		sb.append(arr[0]);
		int len = arr.length;
		for (int i = 1; i < len; i++) {
			sb.append(arr[i].substring(0, 1).toUpperCase());
			sb.append(arr[i].substring(1));
		}
		return sb.toString();
	}
	
	

	/**
	 * 返回debug sql字符串
	 * 
	 * @param sql
	 * @return
	 */
	public static String debugSql(String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	public static String debugSql(String sql, Object... param) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql, param));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	public static String debugSql(String sql, List<Object> param) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n---[sql]<<\n");
		sb.append(formatSql(sql, param));
		sb.append("\n---[sql]>>\n");
		return sb.toString();
	}

	/**
	 * 美化sql
	 * 
	 * @param sql
	 * @return
	 */
	public static String formatSql(String sql, List<Object> param) {
		return formatSql(sql, param.toArray());
	}

	public static String formatSql(String sql, Object... param) {
		List<Object> param2 = new ArrayList<Object>();
		int len = param.length;
		Object item;
		for(int i = 0; i < len;i++) {
			item = param[i];
			if (item instanceof java.util.Date) {
				param2.add(DateUtil.getDate((Date)item, "yyyy-MM-dd HH:mm:ss"));
			}else {
				param2.add(String.valueOf(item));
			}
			
		}
		return SqlFormatter.format(sql, "\t", param2);
	}
}
