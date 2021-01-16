package com.pslib.jtool.util;

import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * 过滤非法 url字符
 * 
 * @author ******
 * 
 * 
 */
public class SafeRequestUtil {
	protected final static Logger logger = LoggerFactory.getLogger(SafeRequestUtil.class);

	static String getFilter = "(<[/\\s#]*[\\w]+[^>]*>|prompt[\\s]*\\(|alert[\\s]*\\(|confirm[\\s]*\\(|javascript|SELECT[\\s]+|DELETE[\\s]+|ALTER[\\s]+|DROP[\\s]+|TRUNCATE[\\s]+|CREATE[\\s]+|DATABASE[\\s]+|INSERT[\\s]+|UPDATE[\\s]+|EXEC[\\s]+)";
	static Pattern getFilterPattern = Pattern.compile(getFilter, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	static String postFilter = "(<[/\\s#]*[\\w]+[^>]*>|prompt[\\s]*\\(|alert[\\s]*\\(|confirm[\\s]*\\(|javascript|SELECT[\\s]+|DELETE[\\s]+|ALTER[\\s]+|DROP[\\s]+|TRUNCATE[\\s]+|CREATE[\\s]+|DATABASE[\\s]+|INSERT[\\s]+|UPDATE[\\s]+|EXEC[\\s]+)";
	static Pattern postFilterPattern = Pattern.compile(postFilter, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	static String cookieFilter = "(<[/\\s#]*[\\w]+[^>]*>|prompt[\\s]*\\(|alert[\\s]*\\(|confirm[\\s]*\\(|javascript|SELECT[\\s]+|DELETE[\\s]+|ALTER[\\s]+|DROP[\\s]+|TRUNCATE[\\s]+|CREATE[\\s]+|DATABASE[\\s]+|INSERT[\\s]+|UPDATE[\\s]+|EXEC[\\s]+)";
	static Pattern cookieFilterPattern = Pattern.compile(cookieFilter, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 检测 request变量
	 * 
	 * @param request
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkRequest(HttpServletRequest request) {
		Enumeration<?> ParameterNames = request.getParameterNames();
		String parm = null;
		String value = null;
		while (ParameterNames.hasMoreElements()) {
			parm = (String) ParameterNames.nextElement();
			value = request.getParameter(parm);
			if (!checkGetStr(value) || !checkPostStr(value)) {
				logger.error("error parm:" + parm + "=" + value);
				return false;
			}
		}

		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				if (!checkCookieStr(cookies[i].getValue())) {
					logger.error("error parm:" + parm + "=" + value);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 检测get变量
	 * 
	 * @param str
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkGetStr(String str) {
		Matcher m = getFilterPattern.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 检测post变量
	 * 
	 * @param str
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkPostStr(String str) {
		Matcher m = postFilterPattern.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 检测cookie变量
	 * 
	 * @param str
	 * @return true 字符正常 false 有非法字符
	 */
	public static boolean checkCookieStr(String str) {
		Matcher m = cookieFilterPattern.matcher(str);
		if (m.find()) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String str = "or in";

		if (!checkGetStr(str)) {
			System.out.println("error parm");
		}
		System.out.println(checkCookieStr("<script>aaa</script>"));
	}
}
