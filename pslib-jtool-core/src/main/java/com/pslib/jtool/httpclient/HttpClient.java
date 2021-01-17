package com.pslib.jtool.httpclient;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class HttpClient {
	protected final static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	/**
	 * default client
	 */
	public static HttpClient DF;
	static {
		DF = new HttpClient();
	}

	protected CloseableHttpClient httpClient;

	private String ua = "httpclient";
	private HttpHost proxy = null;
	private Charset charset = Consts.UTF_8;

	private int maxTotal = 500;
	private int defaultMaxPerRoute = 100;

	private int socketTimeout = 10000;
	private int connectTimeout = 10000;
	private int connectionRequestTimeout = 20000;

	private String toolInfo = "httpclient";

	/**
	 * 是否打印日志
	 */
	private boolean printData = false;

	public HttpClient() {
		init();
	}

	public void init() {
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(charset).build();
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true).build();
		httpClient = HttpClientBuilder.create().setDefaultConnectionConfig(connectionConfig).setDefaultSocketConfig(socketConfig).setSSLSocketFactory(new SSLConnectionSocketFactory(sslcontext)).build();
	}

	public String getUa() {
		return ua;
	}

	/**
	 * 设置UA
	 * 
	 * @param maxTotal
	 * @return
	 */
	public void setUa(String ua) {
		this.ua = ua;
	}

	public HttpHost getProxy() {
		return proxy;
	}

	/**
	 * 设置代理
	 * 
	 * @param maxTotal
	 * @return
	 */
	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	public boolean isPrintData() {
		return printData;
	}

	/**
	 * 是否打印日志
	 * 
	 * @param printData
	 */
	public void setPrintData(boolean printData) {
		this.printData = printData;
	}

	public Charset getCharset() {
		return charset;
	}

	/**
	 * 设置字符集
	 * 
	 * @param maxTotal
	 * @return
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	/**
	 * 设置总最大连接数
	 * 
	 * @param maxTotal
	 * @return
	 */
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	/**
	 * 设置路由最大连接数
	 * 
	 * @param defaultMaxPerRoute
	 * @return
	 */
	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * 设置socket连接超时时间(毫秒)
	 * 
	 * @param socketTimeout
	 * @return
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置连接超时时间(毫秒)
	 * 
	 * @param connectTimeout
	 * @return
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	/**
	 * 设置请求超时时间(毫秒)
	 * 
	 * @param connectionRequestTimeout
	 * @return
	 */
	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void setConfig(RequestBuilder requestBuilder) {
		RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).setExpectContinueEnabled(false)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST)).setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
		org.apache.http.client.config.RequestConfig.Builder requestConfigBuilder = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout);
		if (proxy != null) {
			// new HttpHost("myotherproxy", 8080)
			requestConfigBuilder.setProxy(proxy);
		}
		RequestConfig requestConfig = requestConfigBuilder.build();
		requestBuilder.setConfig(requestConfig);
	}

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param vars
	 * @return
	 */
	public String get(String url, List<NameValuePair> vars) {
		try {
			logger.info("get start:" + url);
			// HttpGet httpget = new HttpGet(url);
			// HttpContext context = new BasicHttpContext();
			// CloseableHttpResponse response =
			// httpClient.execute(httpget, context);
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
			if (vars != null && vars.size() > 0) {
				requestBuilder.addParameters(vars.toArray(new NameValuePair[vars.size()]));
			}
			requestBuilder.addHeader("user-agent", ua);
			requestBuilder.addHeader("tool", toolInfo);
			setConfig(requestBuilder);
			HttpUriRequest req = requestBuilder.build();
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(req);
			} catch (Exception e) {
				logger.error("get:" + url, e);
				closeResponse(response);
				closeRequest(req);
				return null;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// byte[] bytes =
					// EntityUtils.toByteArray(entity);
					String data = EntityUtils.toString(entity, charset);
					logger.info("get finish:" + url + (printData ? "\ndata=" + data : ""));
					// logger.info("data:" + data);
					return data;
				}
			}
			logger.error("get error: statusCode=" + statusCode);
			closeResponse(response);
			closeRequest(req);
			return null;
		} catch (Exception e) {
			logger.error("get:" + url, e);
			return null;
		}
	}

	public String get(String url) {
		return get(url, null);
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 * @param vars
	 * @return
	 */
	public String post(String url, List<NameValuePair> vars) {
		try {
			logger.info("post start:" + url);
			// HttpGet httpget = new HttpGet(url);
			// HttpContext context = new BasicHttpContext();
			// CloseableHttpResponse response =
			// httpClient.execute(httpget, context);
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
			if (vars != null && vars.size() > 0) {
				requestBuilder.addParameters(vars.toArray(new NameValuePair[vars.size()]));
			}
			requestBuilder.addHeader("user-agent", ua);
			requestBuilder.addHeader("tool", toolInfo);
			setConfig(requestBuilder);
			HttpUriRequest req = requestBuilder.build();
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(req);
			} catch (Exception e) {
				logger.error("post:" + url, e);
				closeResponse(response);
				closeRequest(req);
				return null;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// byte[] bytes =
					// EntityUtils.toByteArray(entity);
					String data = EntityUtils.toString(entity, charset);
					logger.info("post finish:" + url + (printData ? "\ndata=" + data : ""));
					// logger.info("data:" + data);
					return data;
				}
			}
			logger.error("post error: statusCode=" + statusCode);
			closeResponse(response);
			closeRequest(req);
			return null;
		} catch (Exception e) {
			logger.error("post:" + url, e);
			return null;
		}
	}

	/**
	 * postStream 请求
	 * 
	 * @param url
	 * @param dataStr
	 * @return
	 */
	public String postStream(String url, String dataStr) {
		try {
			logger.info("postStream start:" + url);
			RequestBuilder requestBuilder = RequestBuilder.post().setUri(new URI(url));
			if (dataStr != null) {
				StringEntity entityData = new StringEntity(dataStr);
				entityData.setContentEncoding(charset.displayName());
				// entity.setContentType("application/json");// 设置为 json数据
				requestBuilder.setEntity(entityData);
			}
			requestBuilder.addHeader("user-agent", ua);
			requestBuilder.addHeader("tool", toolInfo);
			setConfig(requestBuilder);
			HttpUriRequest req = requestBuilder.build();
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(req);
			} catch (Exception e) {
				logger.error("postStream:" + url, e);
				closeResponse(response);
				closeRequest(req);
				return null;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// byte[] bytes =
					// EntityUtils.toByteArray(entity);
					String data = EntityUtils.toString(entity, charset);
					logger.info("postStream finish:" + url + (printData ? "\ndata=" + data : ""));
					// logger.info("data:" + data);
					return data;
				}
			}
			logger.error("postStream error: statusCode=" + statusCode);
			closeResponse(response);
			closeRequest(req);
			return null;
		} catch (Exception e) {
			logger.error("postStream:" + url, e);
			return null;
		}
	}

	public boolean ping(String host) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.create("CONNECT").setUri(new URI(host));
			requestBuilder.addHeader("tool", toolInfo);
			HttpUriRequest req = requestBuilder.build();
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(req);
			} catch (Exception e) {
				logger.error("", e);
				closeResponse(response);
				closeRequest(req);
				return false;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			// System.out.println("ping " + host + " StatusCode=" + statusCode);
			// logger.info("ping " + host + " StatusCode=" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				closeResponseStream(response);
			} else {
				closeResponse(response);
			}
			closeRequest(req);
			return statusCode == HttpStatus.SC_OK;
		} catch (Exception e) {
			logger.error("ping:" + host, e);
			return false;
		}

	}

	public boolean ping(String url, String containStr) {
		try {
			RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
			requestBuilder.addHeader("user-agent", ua);
			requestBuilder.addHeader("tool", toolInfo);
			setConfig(requestBuilder);
			HttpUriRequest req = requestBuilder.build();
			CloseableHttpResponse response = null;
			try {
				response = httpClient.execute(req);
			} catch (Exception e) {
				logger.error("ping:" + url, e);
				closeResponse(response);
				closeRequest(req);
				return false;
			}
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				// System.out.println("ping " + url + " StatusCode=" + statusCode);
				if (containStr == null) {
					// logger.info("ping " + url + " StatusCode=" + statusCode);
					closeResponseStream(response);
					return true;
				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String data = EntityUtils.toString(entity, charset);
					if (data.contains(containStr)) {
						// logger.info("ping " + url + " StatusCode=" + statusCode);
						return true;
					} else {
						logger.error("ping error: no contain=" + containStr + ", data=" + data);
						return false;
					}
				}
			}
			logger.error("ping:" + url + " statusCode=" + statusCode);
			closeResponse(response);
			closeRequest(req);
			return false;
		} catch (Exception e) {
			logger.error("ping:" + url, e);
			return false;
		}
	}

	/**
	 * 关闭request
	 * 
	 * @param req
	 */
	public void closeRequest(HttpUriRequest req) {
		if (req == null)
			return;
		try {
			req.abort();
		} catch (Exception e) {
			logger.error("closeRequest:", e);
		}
	}

	/**
	 * 关闭response Stream
	 * 
	 * @param response
	 */
	public void closeResponseStream(HttpResponse resp) {
		if (resp == null)
			return;
		EntityUtils.consumeQuietly(resp.getEntity());
	}

	/**
	 * 关闭response
	 * 
	 * @param response
	 */
	public void closeResponse(CloseableHttpResponse resp) {
		if (resp == null)
			return;
		try {
			resp.close();
		} catch (Exception e) {
			logger.error("closeResponse:", e);
		}
	}

	public static void main(String[] args) {
		HttpClient httpClient = new HttpClient();
		List<NameValuePair> vars = new ArrayList<NameValuePair>();
		vars.add(new BasicNameValuePair("username", "test"));
		vars.add(new BasicNameValuePair("password", "1111中文abc"));
		httpClient.printData = true;
		String out = httpClient.get("http://localhost/print_headers.php", vars);
		System.out.println(out);

		out = HttpClient.DF.get("http://localhost/print_headers.php", vars);
		System.out.println(out);
	}

}
