package com.pslib.jtool.jftpl.test;

import java.util.HashMap;
import java.util.Map;

import org.febit.wit.Engine;
import com.pslib.jtool.jftpl.util.TplSkinInfo;

public class Test1 {

	public static void main(String[] args) throws Exception {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put("skinFileLoader.@class", "com.pslib.jtool.jftpl.loaders.FileLoader");
		String root = "/workspace/jee/lib_src/web_tpl/jftpl/jftpl-wit/jftpl/temp/";
		settings.put("skinFileLoader.root", root);

		settings.put("routeLoader.defaultLoader", "skinFileLoader");
		settings.put("engine.looseVar", "true");
		settings.put("skinFileLoader.encoding", "UTF-8");
		settings.put("global.registers",
				"\n org.febit.wit.global.impl.GlobalMapRegister\n "
						+ "org.febit.wit.global.impl.ContextLocalRegister\n "
						+ "com.pslib.jtool.jftpl.config.TypeGlobalRegister\n "
						+ "com.pslib.jtool.jftpl.config.TplGlobalRegister\n ");
		
		Engine engine = Engine.create((String) null, settings);
		
		TplSkinInfo skinInfo = new TplSkinInfo();
		skinInfo.setRoot("temp/");
		skinInfo.setWebRoot("temp/");
		skinInfo.setDefaultSkin("default");
		skinInfo.setSkin("default");
		skinInfo.setImgDomain("http://aaa.com/");
		skinInfo.setStaticResVersion("***");
		String orgName = "test/a.html";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("headimgurl", "aaaa.jpg?aaa");
		
		engine.getTemplate(orgName.concat("|").concat(skinInfo.toString())).merge(parameters,System.out);
		engine.getTemplate(orgName.concat("|").concat(skinInfo.toString())).merge(parameters,System.out);


	}

}
/*


asdf
<link rel="stylesheet" type="text/css" href="images/css/global.css" />
<link rel="stylesheet" type="text/css" href="images/css/aaa.css?aadd=222" />
<link rel="stylesheet" type="text/css" href="${staticResUrl}${CONTEXT_PATH}/js/jquery/plugins/layer/mobile/need/layer.css" />
<script language="JavaScript" type="text/javascript" src="images/js/base.min.js" merge="1"></script>

<link rel="stylesheet" type="text/css" href="${headimgurl}" />
asdfas
<link rel="stylesheet" type="text/css" href="${get_bool_str(is_empty(headimgurl), "images/game/page/headerimg.jpg", headimgurl)}" />


*/