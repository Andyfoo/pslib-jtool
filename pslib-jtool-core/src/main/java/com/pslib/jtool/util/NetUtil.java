package com.pslib.jtool.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络操作
 */
public class NetUtil {
	protected final static Logger logger = LoggerFactory.getLogger(NetUtil.class);

	/**
	 * 获取所有cookie
	 * 
	 * @param response
	 */
	public static Map<String, String> getCookieMap(HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return cookieMap;
		for (int i = 0; i < cookies.length; i++) {
			cookieMap.put(cookies[i].getName(), cookies[i].getValue());
		}
		return cookieMap;
	}

	/**
	 * 获取cookie
	 * 
	 * @param response
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookieMap(request).get(name);
	}

	/**
	 * 增加cookie
	 * 
	 * @param response
	 * @param time
	 *                -1=关闭浏览器时清除 0=立即清除 秒数=指定秒清除
	 */
	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String val, int time) {
		Cookie cookies1 = new Cookie(name, val);
		cookies1.setPath("/");
		cookies1.setSecure(request.getScheme().equals("https"));
		cookies1.setMaxAge(time);
		// cookies1.setHttpOnly(true);
		// cookies1.setHttpOnly(false);
		response.addCookie(cookies1);
	}

	/**
	 * 获取当前域名下的全局cookie域名
	 * 
	 * @return Str
	 */
	public static String getCookieDomain(HttpServletRequest request, String defaultDomain) {
		String host = getCurrDomain(request);

		if (host.endsWith(".com.cn") || host.endsWith(".net.cn") || host.endsWith(".org.cn")
				|| host.endsWith(".gov.cn") || host.endsWith(".js.cn")) {
			String arr[] = host.split("\\.");
			host = "." + arr[arr.length - 3] + "." + arr[arr.length - 2] + "." + arr[arr.length - 1];
		} else if (host.endsWith(".com") || host.endsWith(".net") || host.endsWith(".org") 
				|| host.endsWith(".gov") || host.endsWith(".cn")) {
			String arr[] = host.split("\\.");
			host = "." + arr[arr.length - 2] + "." + arr[arr.length - 1];
		} else {
			host = defaultDomain;
		}
		return host;
	}

	/**
	 * 获取当前域名
	 * 
	 * @return Str
	 */
	public static String getCurrDomain(HttpServletRequest request) {
		String host = request.getHeader("X-Forwarded-Host");
		if (null == host || host.equals("")) {
			host = request.getHeader("host");
		}
		try {
			String[] urllist = host.split(",");
			if (urllist.length > 0)
				host = urllist[0];
		} catch (Exception e) {
			logger.error("获取用户访问url错误：" + e);
		}
		if (host.indexOf(":") > 0) {
			String[] arr = host.split(":");
			host = arr[0];
		}
		return host;
	}

	/**
	 * 获取客户ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// logger.info("ip="+ip);
		if (ip != null && ip.indexOf(",") > 0) {
			String[] ipList = ip.split(",");
			if (ipList.length > 0) {
				ip = ipList[0];
			}
		}
		return ip;
	}

	/**
	 * IP转换成10位数字
	 * 
	 * @param ip
	 *                IP
	 * @return 10位数字
	 */
	public static long ip2num(String ip) {
		long ipNum = 0;
		try {
			if (ip != null) {
				if (ip.indexOf(".") != -1) {
					String ips[] = ip.split("\\.");
					for (int i = 0; i < ips.length; i++) {
						int k = Integer.parseInt(ips[i]);
						ipNum = ipNum + k * (1L << ((3 - i) * 8));
					}
				} else {
					ipNum = Integer.parseInt(ip);
				}
			}
		} catch (Exception e) {
		}
		return ipNum;
	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String num2ip(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 判断当前请求是否是ajax方式
	 * 
	 * @param request
	 * @return
	 */
	public static boolean IsJsonRequest(HttpServletRequest request) {
		String xReqWith = request.getHeader("X-Requested-With");
		// String accept = request.getHeader("Accept");
		// if(xReqWith!=null&&accept!=null&& xReqWith.equals("XMLHttpRequest")&&accept.indexOf("application/json")>-1){
		if (xReqWith != null && xReqWith.equals("XMLHttpRequest")) {
			return true;
		}
		return false;
	}

	/**
	 * 测试端口是否可以连接
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 * @return
	 */
	public static boolean connHostPort(String host, int port, int timeout) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(host, port), timeout);
			return true;
		} catch (Exception e) {
			// logger.error("", e);
			return false;
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				// logger.error("", e);
			}
		}
	}

	/**
	 * 测试端口是否可以连接
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean connHostPort(String host, int port) {
		return connHostPort(host, port, 2000);
	}

	/**
	 * 测试IP是否ping通
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static boolean pingHost(String host) {
		try {
			return InetAddress.getByName(host).isReachable(2000);
		} catch (Exception e) {
			// logger.error("", e);
		}
		return false;
	}
	

	/**
	 * 组成url字符串 : a=1&b=2&c=3
	 */
	public static String buildQuery(final Map<String, Object> queryMap, final String encoding) {
		if (queryMap.isEmpty()) {
			return "";
		}
		StringBuffer query = new StringBuffer();
		Set<String> keySet = queryMap.keySet();
		int count = 0;
		for (String key : keySet) {
			key = EncodeUtil.urlEncode(key, encoding);
			Object value = queryMap.get(key);

			if (value == null) {
				if (count != 0) {
					query.append('&');
				}
				query.append(key);
				count++;
			} else {
				if (count != 0) {
					query.append('&');
				}

				query.append(key);
				count++;
				query.append('=');

				String valueString = EncodeUtil.urlEncode(value.toString(), encoding);
				query.append(valueString);
			}
		}

		return query.toString();
	}
	public static String buildQuery(final Map<String, Object> queryMap) {
		return buildQuery(queryMap, "UTF-8");
	}

	/**
	 * 解码URL字符为Map（a=1&b=2&c=3）
	 */
	public static Map<String, Object> parseQuery(final String query, final boolean decode, final String encoding) {
		Map<String, Object> queryMap = new LinkedHashMap<String, Object>();
		int ndx, ndx2 = 0;
		while (true) {
			ndx = query.indexOf('=', ndx2);
			if (ndx == -1) {
				if (ndx2 < query.length()) {
					queryMap.put(query.substring(ndx2), null);
				}
				break;
			}
			String name = query.substring(ndx2, ndx);
			if (decode) {
				name = EncodeUtil.urlDecode(name);
			}

			ndx2 = ndx + 1;

			ndx = query.indexOf('&', ndx2);

			if (ndx == -1) {
				ndx = query.length();
			}

			String value = query.substring(ndx2, ndx);

			if (decode) {
				value = EncodeUtil.urlDecode(value);
			}

			queryMap.put(name, value);

			ndx2 = ndx + 1;
		}

		return queryMap;
	}
	public static Map<String, Object> parseQuery(final String query) {
		return parseQuery(query, false, "");
	}
	public static Map<String, Object> parseQuery(final String query, final String encoding) {
		return parseQuery(query, true, encoding);
	}
	public static Map<String, Object> parseQuery(final String query, final boolean decode) {
		return parseQuery(query, decode, "UTF-8");
	}


	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(buildQuery(parseQuery("d=1&b=2&c", true, "UTF-8"), "UTF-8"));
	}
}
