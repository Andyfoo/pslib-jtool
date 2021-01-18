package com.pslib.jtool.jftpl.support.springmvc;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractTemplateView;

public final class TemplateView extends AbstractTemplateView {

	private TemplateViewResolver resolver;

	public void setResolver(TemplateViewResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.resolver.render(getUrl(), model, request, response);
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		// return JetWebEngine.getEngine().checkTemplate(getUrl());
		// System.out.println(getUrl());
		return true;
	}
}
