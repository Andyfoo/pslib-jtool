package com.pslib.jtool.httpclient.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.pslib.jtool.httpclient.PoolingHttpClient;

public class PoolingHttpClientTest {

	public static void main(String[] args) {
		PoolingHttpClient httpClient = new PoolingHttpClient();
		httpClient.init();
		List<NameValuePair> vars = new ArrayList<NameValuePair>();
		vars.add(new BasicNameValuePair("username", "test"));
		vars.add(new BasicNameValuePair("password", "1111中文abc"));
		String out = httpClient.postStream("http://localhost/print_headers.php", "asdfasfsd");
		System.out.println(out);
	}

}
