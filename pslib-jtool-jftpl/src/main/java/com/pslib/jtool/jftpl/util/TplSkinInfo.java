package com.pslib.jtool.jftpl.util;

import cn.hutool.json.JSONUtil;

/**
 * 模板皮肤信息
 * 
 * @author FH
 *
 */
public class TplSkinInfo {
	private String contextPath = null;// servletContextPath
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

	/**
	 * web目录
	 * 
	 * @param webRoot
	 */
	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public String getRoot() {
		return root;
	}

	/**
	 * 模板路径
	 * 
	 * @param root
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	public String getDefaultSkin() {
		return defaultSkin;
	}

	/**
	 * 默认模板
	 * 
	 * @param defaultSkin
	 */
	public void setDefaultSkin(String defaultSkin) {
		this.defaultSkin = defaultSkin;
	}

	public String getSkin() {
		return skin;
	}

	/**
	 * 要使用的模板，文件不存在则使用默认
	 * 
	 * @param skin
	 */
	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getImgPath() {
		return imgPath;
	}

	/**
	 * 图片路径，默认为images
	 * 
	 * @return
	 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgDomain() {
		return imgDomain;
	}

	/**
	 * 图片动态增加的域名
	 * 
	 */
	public void setImgDomain(String imgDomain) {
		this.imgDomain = imgDomain;
	}

	public String getStaticResVersion() {
		return staticResVersion;
	}

	/**
	 * 静态资源动态增加版本号
	 * 
	 * @return
	 */
	public void setStaticResVersion(String staticResVersion) {
		this.staticResVersion = staticResVersion;
	}

	/**
	 * json字符串转为TplSkinInfo
	 * 
	 * @return
	 */
	public static TplSkinInfo parse(String str) {
		//return JSON.parseObject(TplEncodeUtil.urlDecode(str),TplSkinInfo.class);
		return JSONUtil.toBean(TplEncodeUtil.hex2str(str),TplSkinInfo.class);
		// return JSON.parseObject(str, TplSkinInfo.class);
	}

	/**
	 * TplSkinInfo转为json字符串
	 */
	public String toString() {
		//return TplEncodeUtil.urlEncode(JSON.toJSONString(this));
		return TplEncodeUtil.str2hex(JSONUtil.toJsonStr(this));
		// return JSON.toJSONString(this);
	}

	public String getContextPath() {
		return contextPath == null ? "" : contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
}
