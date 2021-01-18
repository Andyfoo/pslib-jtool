package com.pslib.jtool.jftpl.test;

import cn.hutool.json.JSONUtil;
import com.pslib.jtool.jftpl.util.TplEncodeUtil;

public class Test2 {
	private String webRoot = null;// web目录
	private String root = null;// 模板路径
	private String defaultSkin = null;// 默认模板
	private String skin = null;// 要使用的模板，文件不存在则使用默认
	private String imgPath = null;// 图片路径，默认为images
	private String imgDomain = null;// 图片动态增加的域名
	private String staticResVersion = null;// 静态资源版本号

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getDefaultSkin() {
		return defaultSkin;
	}

	public void setDefaultSkin(String defaultSkin) {
		this.defaultSkin = defaultSkin;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgDomain() {
		return imgDomain;
	}

	public void setImgDomain(String imgDomain) {
		this.imgDomain = imgDomain;
	}

	public String getStaticResVersion() {
		return staticResVersion;
	}

	public void setStaticResVersion(String staticResVersion) {
		this.staticResVersion = staticResVersion;
	}

	public static void main(String[] args) {
		Test2 ttt = new Test2();
		long t1;
		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(TplEncodeUtil.urlDecode(TplEncodeUtil.urlEncode(JSONUtil.toJsonStr(ttt))),Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);
		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(TplEncodeUtil.urlDecode(TplEncodeUtil.urlEncode(JSONUtil.toJsonStr(ttt))),Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);

		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(TplEncodeUtil.hex2str(TplEncodeUtil.str2hex(JSONUtil.toJsonStr(ttt))),Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);
		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(TplEncodeUtil.hex2str(TplEncodeUtil.str2hex(JSONUtil.toJsonStr(ttt))),Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);

		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(JSONUtil.toJsonStr(ttt), Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);
		t1 = System.currentTimeMillis();
		for(int i = 0; i < 100000; i ++) {
			JSONUtil.toBean(JSONUtil.toJsonStr(ttt), Test2.class);
		}
		System.out.println(System.currentTimeMillis() - t1);

	}

}
