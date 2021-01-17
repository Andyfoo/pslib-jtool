package com.pslib.jtool.util;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.pslib.jtool.db.DbTool;
import com.pslib.jtool.util.verify.VerifyUtil;

/**
 * 
 * 
 *         功能说明：变量相关函数
 */
public class VarUtil {

	/**
	 * 字符串转换为整数
	 * 
	 * @param str
	 *                字符串
	 * @return int(如果不是整数返回0)
	 */
	public static int intval(String str, int def) {
		if (!VerifyUtil.isInt(str))
			return def;
		try {
			return new Integer(str);
		} catch (Exception e) {
			return def;
		}
	}

	public static int intval(String str) {
		return intval(str, 0);
	}

	/**
	 * 字符串转换为long
	 * 
	 * @param str
	 *                字符串
	 * @return long (如果不是long 返回0)
	 */
	public static long longval(String str, int def) {
		return longval(str, Long.valueOf(def));
	}

	public static long longval(String str, long def) {
		if (!VerifyUtil.isInt(str))
			return def;
		try {
			return new Long(str);
		} catch (Exception e) {
			return def;
		}
	}

	public static long longval(String str) {
		return longval(str, 0);
	}

	/**
	 * 字符串转换为double
	 * 
	 * @param str
	 *                字符串
	 * @return long (如果不是double返回0)
	 */
	public static double doubleval(String str, float def) {
		return doubleval(str, Double.valueOf(def));
	}

	public static double doubleval(String str, double def) {
		if (!VerifyUtil.isFloat(str))
			return def;
		try {
			return new Double(str);
		} catch (Exception e) {
			return def;
		}
	}

	public static double doubleval(String str) {
		return doubleval(str, 0f);
	}

	/**
	 * 字符串转换为float
	 * 
	 * @param str
	 *                字符串
	 * @return long (如果不是float返回0)
	 */
	public static float floatval(String str, float def) {
		if (!VerifyUtil.isFloat(str))
			return def;
		try {
			return new Float(str);
		} catch (Exception e) {
			return def;
		}
	}

	public static float floatval(String str) {
		return floatval(str, 0f);
	}

	/**
	 * 字符串处理，如果为null返回空字符串
	 * 
	 * @param str
	 *                字符串
	 * @return 字符串
	 */
	public static String strval(String str) {
		if (str == null)
			return "";
		return str;
	}

	public static String strval(String str, String defaultStr) {
		if (str == null || str.length() == 0)
			return defaultStr;
		return str;
	}

	/**
	 * Request字符串变量处理
	 * 
	 * @param request
	 *                HttpServletRequest对象
	 * @param key
	 *                字符串
	 * 
	 * @return 字符串
	 */
	public static String RequestStr(HttpServletRequest request, String key) {
		return strval(request.getParameter(key));
	}

	public static String RequestStr(HttpServletRequest request, String key, String defaultStr) {
		return strval(request.getParameter(key), defaultStr);
	}

	public static String[] RequestStrArray(HttpServletRequest request, String key) {
		String[] val = request.getParameterValues(key);
		for (int i = 0; i < val.length; i++) {
			val[i] = strval(val[i]);
		}
		return val;
	}

	/**
	 * 获取并处理 sql like 查询的字符串 sql需要加 ESCAPE '/'：<br/>
	 * sql += " and team_name like ? ESCAPE '/'";<br/>
	 * param.add("%"+team_name+"%");
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String RequestSqlLikeStr(HttpServletRequest request, String key) {
		return DbTool.escapeSQLLike(strval(request.getParameter(key)));
	}

	/**
	 * 获取并处理 sql like 查询的字符串，增加安全过滤 sql需要加 ESCAPE '/'：<br/>
	 * sql += " and team_name like ? ESCAPE '/'";<br/>
	 * param.add("%"+team_name+"%");
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String RequestSafeSqlLikeStr(HttpServletRequest request, String key) {
		return DbTool.escapeSQLLike(DbTool.getQuerySafeStr(strval(request.getParameter(key))));
	}

	/**
	 * Request整数变量处理
	 * 
	 * @param request
	 *                HttpServletRequest对象
	 * @param key
	 *                字符串
	 * 
	 * @return int (如果不是整数返回0)
	 */
	public static int RequestInt(HttpServletRequest request, String key) {
		return intval(request.getParameter(key));
	}

	public static int[] RequestIntArray(HttpServletRequest request, String key) {
		String[] val = request.getParameterValues(key);
		int[] val2 = new int[val.length];
		for (int i = 0; i < val.length; i++) {
			val2[i] = intval(val[i]);
		}
		return val2;
	}

	public static int RequestInt(HttpServletRequest request, String key, int defaultInt) {
		int i = RequestInt(request, key);
		if (i == 0) {
			i = defaultInt;
		}
		return i;
	}

	public static int[] RequestIntArray(HttpServletRequest request, String key, int defaultInt) {
		String[] val = request.getParameterValues(key);
		int[] val2 = new int[val.length];
		for (int i = 0; i < val.length; i++) {
			val2[i] = intval(val[i]);
			if (val2[i] == 0) {
				val2[i] = defaultInt;
			}
		}
		return val2;
	}

	/**
	 * Request Long变量处理
	 * 
	 * @param request
	 *                HttpServletRequest对象
	 * @param key
	 *                字符串
	 * 
	 * @return int (如果不是Long返回0)
	 */
	public static long RequestLong(HttpServletRequest request, String key) {
		return longval(request.getParameter(key));
	}

	public static long RequestLong(HttpServletRequest request, String key, long defaultVal) {
		return longval(request.getParameter(key), defaultVal);
	}

	public static long[] RequestLongArray(HttpServletRequest request, String key) {
		String[] val = request.getParameterValues(key);
		long[] val2 = new long[val.length];
		for (int i = 0; i < val.length; i++) {
			val2[i] = intval(val[i]);
		}
		return val2;
	}

	/**
	 * 清除多余路径
	 * 
	 * @param str
	 * @return
	 */
	private static Pattern p_clearPath = Pattern.compile("[\\\\/]+");

	public static String clearPath(String str) {
		if (str == null)
			return str;
		return p_clearPath.matcher(str).replaceAll("/");
	}

	/**
	 * 返回字符串的文件名，过滤目录字符
	 * 
	 * @param str
	 *                字符串
	 * 
	 * @return 字符串
	 */
	public static String basename(String str) {
		// str = str.replaceAll("\\\\", "/");
		// String arr[] = str.split("/");

		// return arr.length > 0 ? arr[arr.length-1] : "";
		int pos = str.lastIndexOf('/');
		int pos2 = str.lastIndexOf('\\');
		if (str.length() == 0)
			return "";
		if (pos > pos2) {
			return str.substring(pos + 1);
		} else {
			return str.substring(pos2 + 1);
		}
	}

	/**
	 * 返回字符串的目录，过滤文件名字符
	 * 
	 * @param str
	 *                字符串
	 * 
	 * @return 字符串
	 */
	public static String dirname(String str) {
		int pos = str.lastIndexOf('/');
		int pos2 = str.lastIndexOf('\\');
		if (str.length() == 0)
			return "";
		if (pos > pos2) {
			return str.substring(0, pos + 1);
		} else {
			return str.substring(0, pos2 + 1);
		}
	}

	public static void main(String argc[]) {
		System.out.println(intval("33.23"));
		System.out.println((int)floatval("33.23"));
		System.out.println(doubleval("33.23"));
	}

}
