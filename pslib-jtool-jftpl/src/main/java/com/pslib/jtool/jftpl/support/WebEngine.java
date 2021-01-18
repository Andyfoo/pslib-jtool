package com.pslib.jtool.jftpl.support;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.febit.wit.Engine;
import org.febit.wit.Vars;
import org.febit.wit.util.Props;
import com.pslib.jtool.jftpl.util.TplSkinInfo;

public class WebEngine {
	private Engine engine;
	private final ServletContext servletContext;
	private Props props;
	private String webRoot;
	private String contextPath;
	
	public WebEngine(Properties properties, ServletContextProvider servletContextAware) {
		Props props = new Props();
		Enumeration<?> propsNames = properties.propertyNames();
		while(propsNames.hasMoreElements()) {
			String key = (String)propsNames.nextElement();
			props.append(key, properties.getProperty(key));
		}
		this.props = props;
		//this.servletContextProvider = servletContextAware;
		servletContext = servletContextAware.getServletContext();
	}
	public WebEngine(Props props, ServletContextProvider servletContextAware) {
		this.props = new Props();
		servletContext = servletContextAware.getServletContext();
	}
	public WebEngine(Props props, ServletContext servletContext) {
		this.props = props;
		this.servletContext = servletContext;
	}
	public void resetEngine() {
		this.engine = null;
	}

	public Engine getEngine() {
		if (this.engine != null) {
			return this.engine;
		}
		webRoot = servletContext.getRealPath("/");
		contextPath = servletContext.getContextPath();

		HashMap<String, Object> settings = new HashMap<String, Object>();

		settings.put("skinFileLoader.@class", "com.pslib.jtool.jftpl.loaders.FileLoader");
		String root = props.get("root");
		if (root == null) {
			throw new IllegalArgumentException("参数必须设置[root]");
		}
		settings.put("skinFileLoader.root", webRoot + root);// default/

		String encoding = props.get("encoding");
		if (encoding == null || encoding.length() == 0) {
			encoding = "UTF-8";
		}
		settings.put("skinFileLoader.encoding", encoding);
		settings.put("routeLoader.defaultLoader", "skinFileLoader");
		settings.put("engine.looseVar", "true");

		String registers = props.get("registers");
		if (registers != null) {
			registers = registers.replaceAll(",", "\n");
		}
		settings.put("global.registers",
				"\n org.febit.wit.global.impl.GlobalMapRegister\n "
						+ "org.febit.wit.global.impl.ContextLocalRegister\n "
						+ "com.pslib.jtool.jftpl.config.TypeGlobalRegister\n "
						+ "com.pslib.jtool.jftpl.config.TplGlobalRegister\n " + registers);

		return this.engine = Engine.create((String) null, settings);

	}

	public void renderTemplate(final String name, final Map<String, Object> parameters,
			final HttpServletResponse response) throws IOException {
		Engine engine = getEngine();
		TplSkinInfo skinInfo;
		String[] nameArr = name.split("\\|");
		String orgName;
		if (nameArr.length == 2) {
			orgName = nameArr[0];
			skinInfo = TplSkinInfo.parse(nameArr[1]);
		} else {
			orgName = name;
			skinInfo = new TplSkinInfo();
		}
		skinInfo.setWebRoot(webRoot);
		skinInfo.setContextPath(contextPath);

		engine.getTemplate(orgName.concat("|").concat(skinInfo.toString())).merge(parameters,
				response.getOutputStream());
	}
	public void renderTemplate(final String name, final Vars parameters, final HttpServletResponse response)
			throws IOException {
		Engine engine = getEngine();
		engine.getTemplate(name).merge(parameters, response.getOutputStream());
	}
	public static interface ServletContextProvider {

		ServletContext getServletContext();
	}
}
