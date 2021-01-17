package com.pslib.jtool.framework.spring;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.json.JSONObject;


/**
 * 显示输出工具类
 * @author FH
 *
 */
public class ViewTool {
	protected static final Logger logger = LoggerFactory.getLogger(ViewTool.class);

			
	public static void forward(HttpServletRequest request,  HttpServletResponse response, String url){
		try{
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	public static void redirect(HttpServletResponse response, String url){
		try{
			response.sendRedirect(url);
		}catch(Exception e){
			logger.error("", e);
		}
	}
	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	public void download(HttpServletResponse response, String filename, String content) {
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);

		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	
	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	public static void result(HttpServletResponse response, String out_str) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(out_str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	public static void result(HttpServletResponse response, JSONObject json) {
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 通用返回
	 * 
	 * @param pageStr
	 * @return
	 */
	public void resultGbk(HttpServletResponse response, String out_str) {
		response.setContentType("text/html;charset=gb2312");
		try {
			response.getWriter().write(out_str);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	public void resultGbk(HttpServletResponse response, JSONObject json) {
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * 为url追加参数
	 * 
	 * @param url
	 * @param parm
	 * @return
	 */
	public String addUrlParm(String url, String parm) {
		return url.indexOf("?") > -1 ? url + "&" + parm : url + "?" + parm;
	}
	
	public static JSONObject getAjaxMessage(int result, String message) {
		return getAjaxMessage(result, message, null);
	}

	public static JSONObject getAjaxMessage(int result, String message, JSONObject data) {
		JSONObject json = new JSONObject();
		json.set("result", result);
		json.set("message", message);

		if (data != null) {
			json.putAll(data);
		}

		return json;
	}
	
	
	


}
