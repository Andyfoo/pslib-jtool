package com.pslib.jtool.jftpl.test;

import java.util.HashMap;
import java.util.Map;

import org.febit.wit.Engine;
import org.febit.wit.Template;
import org.febit.wit.exceptions.ResourceNotFoundException;

import cn.hutool.json.JSONObject;

public class Test3 {

	public static void main(String[] args) throws ResourceNotFoundException {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		//String root = "/workspace/jee/lib_src/web_tpl/jftpl/jftpl-wit/jftpl/temp/";
		//settings.put("skinFileLoader.root", root);

		settings.put("engine.looseVar", "true");
		settings.put("skinFileLoader.encoding", "UTF-8");
		
		Engine engine = Engine.create((String) null, settings);
		Template template = engine.getTemplate("/jftpl/test/tpl/a.wit");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("headimgurl", "aaaa.jpg?aaa");
		params.put("aaaa", "aaaa.jpg?aaa");
		params.put("ccc", new JSONObject());
		
		template.merge(params, System.out);

	}

}
