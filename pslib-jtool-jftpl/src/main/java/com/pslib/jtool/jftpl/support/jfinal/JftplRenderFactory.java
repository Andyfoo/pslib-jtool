package com.pslib.jtool.jftpl.support.jfinal;

import javax.servlet.ServletContext;

import com.jfinal.config.Constants;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import com.jfinal.template.Engine;

import org.febit.wit.util.Props;
import org.febit.wit.util.PropsUtil;
import com.pslib.jtool.jftpl.support.TplPreProcessI;
import com.pslib.jtool.jftpl.support.WebEngine;

public class JftplRenderFactory extends RenderFactory {
	protected WebEngine webEngine;
	protected String suffix = null;
	public TplPreProcessI tplPreProcess = null;
	public JftplRenderFactory(TplPreProcessI tplPreProcess) {
		this.tplPreProcess = tplPreProcess;
	}
	public void init(Engine engine, Constants constants, ServletContext servletContext) {
		super.init(engine, constants, servletContext);
		config(constants);
	}
	
	public Render getRender(String view) {
		return new JftplRender(webEngine,tplPreProcess, view+suffix);
	}

	public void config(Constants constants) {
		Props props = PropsUtil.loadFromClasspath(new Props(), "jftpl.properties");
		suffix = props.get("suffix");
		//constants.setViewExtension(".xxx");
		webEngine = new WebEngine(props, servletContext);
	}
}
