package com.pslib.jtool.jftpl.support.springmvc;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import com.pslib.jtool.jftpl.support.TplPreProcessI;
import com.pslib.jtool.jftpl.support.WebEngine;
import com.pslib.jtool.jftpl.support.WebEngine.ServletContextProvider;

public class TemplateViewResolver extends AbstractTemplateViewResolver implements InitializingBean {

	protected WebEngine webEngine;
	public TplPreProcessI tplPreProcess = null;
	private Properties props;

	public TemplateViewResolver() {
		setViewClass(requiredViewClass());
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setTplPreProcess(TplPreProcessI tplPreProcess) {
		this.tplPreProcess = tplPreProcess;
	}

	@Override
	protected Class<?> requiredViewClass() {
		return TemplateView.class;
	}

	public void setConfigPath(String configPath) {

	}

	@Override
	protected TemplateView buildView(String viewName) throws Exception {
		TemplateView view = (TemplateView) super.buildView(viewName);
		view.setResolver(this);
		return view;
	}

	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		View view = super.loadView(viewName, locale);

		return view;
	}

	protected void render(String viewName, Map<String, Object> model, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		model.put("request", request);
		model.put("response", response);
		if (tplPreProcess != null) {
			tplPreProcess.regGlobalVars(webEngine, request, response, model);
			viewName = tplPreProcess.processViewName(viewName, model, request);
		}
		webEngine.renderTemplate(viewName, model, response);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// setSuffix(".html");
		this.webEngine = new WebEngine(props, new ServletContextProvider() {
			public ServletContext getServletContext() {
				return TemplateViewResolver.this.getServletContext();
			}
		});
	}
}
