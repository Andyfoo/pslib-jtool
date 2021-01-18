package com.pslib.jtool.jftpl.test;

import java.util.Date;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;

public class Test4 {
	public static void t1() {
		JSONConfig jsonConfig = JSONConfig.create();
		JSONObject json = new JSONObject(jsonConfig);
		json.set("date", new Date());
		json.set("bbb", "222");
		json.set("aaa", "123");
		System.out.println(json);
	}
	public static void t2() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonConfig.setOrder(true);
		JSONObject json = new JSONObject(jsonConfig);
		json.set("date", new Date());
		json.set("bbb", "222");
		json.set("aaa", "123");
		System.out.println(json);
	}
	public static void t3() {
		JSONConfig jsonConfig = JSONConfig.create();
		jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonConfig.setOrder(true);
		JSONObject json = new JSONObject();
		json.set("date", new Date());
		json.set("bbb", "222");
		json.set("aaa", "123");
		System.out.println(json);

		JSONObject json2 = new JSONObject(json, jsonConfig);
		System.out.println(json2);
	}

	public static void main(String[] args) {
		//t1();
		//t2();
		t3();
	}

}
