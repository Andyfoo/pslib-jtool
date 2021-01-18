package com.pslib.jtool.jftpl.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TplPreProcessI {
	public void regGlobalVars(WebEngine webEngine, HttpServletRequest request, HttpServletResponse response,
			Map<String, Object> model);

	public String processViewName(String viewName, Map<String, Object> model, final HttpServletRequest request);

}
