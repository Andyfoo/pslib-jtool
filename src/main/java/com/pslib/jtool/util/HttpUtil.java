package com.pslib.jtool.util;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;


/**
 * 使用hutool.http访问http页面
 */
public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public String default_charset = "UTF-8";
	public int _time_out = 60000;// 超时时间

	public String _ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36";
	public String _accept = "*/*";
	public String _accept_encoding = "gzip,deflate";
	public String _accept_language = "zh-CN,zh;q=0.9,en;q=0.8";
	public String _stream_media_type = "text/html";

	public Map<String, String> _headers = new HashMap<String, String>();

	/**
	 * 静态实例
	 */
	public static HttpUtil http = newInstance();

	public static HttpUtil newInstance() {
		return new HttpUtil();
	}

	public HttpRequest addHeader(HttpRequest req) {
		if (_headers.size() > 0) {
			Set<String> keyset = _headers.keySet();
			for (String key : keyset) {
				req.header(key, _headers.get(key));
			}
		}
		return req;
	}

	/**
	 * 读取网页
	 * 
	 * @param url
	 *                = url地址
	 * @param charset
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public String get(String url) {
		return get(url, default_charset);
	}

	public String get(String url, String charset) {
		return get(url, charset, _ua);
	}

	public String get(String url, String charset, String ua) {
		return get(url, charset, ua, null);
	}

	public String get(String url, String charset, String ua, String refer) {
		logger.debug("get=" + url);

		try {
			HttpResponse response = addHeader(HttpRequest.get(url)
					.charset(charset)
					.header("Accept",_accept)
					.header("Accept-Encoding",_accept_encoding)
					.header("User-Agent", ua)
					.header("Referer", refer != null ? refer : url)
					.header("Accept-Language", _accept_language)
					.timeout(_time_out)).execute();

			if (response.isOk()) {
				return response.body();
			} else {
				logger.error(url + ", failed: " + response.getStatus());
				return null;
			}
		} catch (Exception e) {
			logger.error("get Error(" + url + "):", e);
			return null;
		} finally {
		}
	}

	/**
	 * 读取网页
	 * 
	 * @param url
	 *                = url地址
	 * @param charset
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public String post(String url) {
		return post(url, null, default_charset);
	}

	public String post(String url, Map<String, Object> list) {
		return post(url, list, default_charset, _ua);
	}

	public String post(String url, Map<String, Object> list, String charset) {
		return post(url, list, charset, _ua);
	}

	public String post(String url, Map<String, Object> list, String charset, String ua) {
		return post(url, list, charset, _ua, null);
	}

	public String post(String url, Map<String, Object> list, String charset, String ua, String refer) {
		logger.debug("post=" + url);
		try {
			HttpResponse response = addHeader(HttpRequest.post(url)
					.form(list == null ? new HashMap<String, Object>() : list)
					.charset(charset)
					.header("Accept",_accept)
					.header("Accept-Encoding",_accept_encoding)
					.header("User-Agent", ua)
					.header("Referer", refer != null ? refer : url)
					.header("Accept-Language", _accept_language)
					.timeout(_time_out)).execute();

			if (response.isOk()) {
				return response.body();
			} else {
				logger.error(url + ", failed: " + response.getStatus());
				return null;
			}
		} catch (Exception e) {
			logger.error("post Error(" + url + "):", e);
			return null;
		} finally {
		}
	}

	/**
	 * post 流数据
	 * 
	 * @param url
	 *                = url地址
	 * @param encode
	 *                = 编码
	 * 
	 * @return 网页内容
	 */
	public String postStream(String url) {
		return postStream(url, null, default_charset);
	}

	public String postStream(String url, String data) {
		return postStream(url, data, default_charset, _ua);
	}

	public String postStream(String url, String data, String charset) {
		return postStream(url, data, charset, _ua);
	}

	public String postStream(String url, String data, String charset, String ua) {
		return postStream(url, data, charset, _ua, null);
	}

	public String postStream(String url, String data, String charset, String ua, String refer) {
		logger.debug("postStream=" + url);
		try {
			HttpResponse response = addHeader(HttpRequest.post(url)
					.body(data,ContentType.build(_stream_media_type, Charset.forName(charset)))
					.charset(charset)
					.header("Accept",_accept)
					.header("Accept-Encoding",_accept_encoding)
					.header("User-Agent", ua)
					.header("Referer", refer != null ? refer : url)
					.header("Accept-Language", _accept_language)
					.timeout(_time_out)).execute();

			if (response.isOk()) {
				return response.body();
			} else {
				logger.error(url + ", failed: " + response.getStatus());
				return null;
			}
			
		} catch (Exception e) {
			logger.error("postStream Error(" + url + "):", e);
			return null;
		} finally {
		}

	}

	/**
	 * 读取2进制数据
	 * 
	 * @param url
	 *                = url地址
	 * @return 网页内容
	 */
	public byte[] getBytes(String url) {
		return getBytes(url, _ua);
	}

	public byte[] getBytes(String url, String ua) {
		return getBytes(url, ua, null);
	}

	public byte[] getBytes(String url, String ua, String refer) {
		logger.debug("getBytes=" + url);

		try {
			HttpResponse response = addHeader(HttpRequest.get(url)
					.header("Accept",_accept)
					.header("Accept-Encoding",_accept_encoding)
					.header("User-Agent", ua)
					.header("Referer", refer != null ? refer : url)
					.header("Accept-Language", _accept_language)
					.timeout(_time_out)).execute();

			if (response.isOk()) {
				return response.bodyBytes();
			} else {
				logger.error(url + ", failed: " + response.getStatus());
				return null;
			}
			
		} catch (Exception e) {
			logger.error("getBytes Error(" + url + "):", e);
			return null;
		} finally {
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://www.test.com";
		url = "http://localhost/print_r.php";
		url = "http://localhost/print_r_utf8.php";
		url = "https://getman.cn/echo";

		// System.out.println(inst.get(url));
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("aaaa", "111中文");
		System.out.println(http.post(url, data));

		byte[] bytes = http.getBytes("https://www.oschina.net/img/newindex-03.svg?t=1451961935000");
		FileUtil.fileWriteBin("e:\\a.svg", bytes);
		// System.out.println(inst.postStream(url, "asdsasa中文sfdasd"));

	}

}
