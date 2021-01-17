package com.pslib.jtool.httpclient.test;

import com.pslib.jtool.httpclient.HttpClient;

public class HttpClientTest {
	public static void main(String[] args) {
		String body = "asdf";
		// System.out.println(HttpClient.DF.postStream("http://localhost:2082/api/abc/index/home?name=a", body));
		// System.out.println(HttpClient.DF.postStream("http://localhost:2082/api/aaa/areaana/home?name=a", body));

		// System.out.println(HttpClient.DF.postStream("http://localhost:8080/hlwwebpro/api/aaa/areaana/home?name=a", body));
		// System.out.println(HttpClient.DF.postStream("http://localhost:8080/hlwwebpro/api/aaa/areaana/home?name=a", body));

		System.out.println(HttpClient.DF.postStream("http://localhost:2082/trade/api/sid/getcdconfig/home.do", body));

	}
}
