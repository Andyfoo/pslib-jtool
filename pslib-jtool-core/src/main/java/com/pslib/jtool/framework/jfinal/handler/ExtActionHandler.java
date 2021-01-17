package com.pslib.jtool.framework.jfinal.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;
import com.jfinal.kit.StrKit;

public class ExtActionHandler extends Handler {
	private String viewPostfix;
	
	public ExtActionHandler() {
		viewPostfix = ".do";
	}
	
	public ExtActionHandler(String viewPostfix) {
		if (StrKit.isBlank(viewPostfix)) {
			throw new IllegalArgumentException("viewPostfix can not be blank.");
		}
		this.viewPostfix = viewPostfix;
	}
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		if ("/".equals(target)) {
			next.handle(target, request, response, isHandled);
			return;
		}
		
		if (!target.endsWith(viewPostfix)) {
			HandlerKit.renderError404(request, response, isHandled);
			return ;
		}
		target = target.substring(0, target.lastIndexOf(viewPostfix));
		next.handle(target, request, response, isHandled);
	}
}
