package com.pslib.jtool.jftpl.support.jfinal;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.render.Render;

import com.pslib.jtool.jftpl.support.TplPreProcessI;
import com.pslib.jtool.jftpl.support.WebEngine;

public class JftplRender extends Render {
	protected static final Logger logger = LoggerFactory.getLogger(JftplRender.class);
	protected WebEngine webEngine;
	protected TplPreProcessI tplPreProcess;

	public JftplRender(WebEngine webEngine,TplPreProcessI tplPreProcess , String view) {
		super.setView(view);
		this.webEngine = webEngine;
		this.tplPreProcess = tplPreProcess;
	}

	public void render(){
		this.response.setContentType("text/html; charset=" + getEncoding());
		Map<String, Object> model = new HashMap<String, Object>();
		final Enumeration<String> enumeration = request.getAttributeNames();
		String key;
		while (enumeration.hasMoreElements()) {
			model.put(key = enumeration.nextElement(), request.getAttribute(key));
		}
		this.tplPreProcess.regGlobalVars(webEngine, request, response, model);

		String viewName = this.tplPreProcess.processViewName(view, model, request);
		try {
			webEngine.renderTemplate(viewName, model, response);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
