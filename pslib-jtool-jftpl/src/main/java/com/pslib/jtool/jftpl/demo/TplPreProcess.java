package com.pslib.jtool.jftpl.demo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pslib.jtool.jftpl.support.TplPreProcessI;
import com.pslib.jtool.jftpl.support.WebEngine;
import com.pslib.jtool.jftpl.util.TplSkinInfo;

public class TplPreProcess implements TplPreProcessI {

	@Override
	public void regGlobalVars(WebEngine webEngine, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model) {

	}

	@Override
	public String processViewName(String viewName, Map<String, Object> model, HttpServletRequest request) {
		TplSkinInfo skinInfo = new TplSkinInfo();
		skinInfo.setDefaultSkin("default");// 默认模板
		skinInfo.setSkin("10000");// 商户模板，文件不存在则使用默认
		// skinInfo.setImgPath("img");//图片路径，默认为images
		// skinInfo.setImgDomain("http://aaa.com/aa");//图片动态增加的域名

		skinInfo.setStaticResVersion("10000");

		return viewName.concat("|").concat(skinInfo.toString());
	}

}
