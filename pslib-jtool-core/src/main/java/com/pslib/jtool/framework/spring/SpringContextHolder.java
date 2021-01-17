package com.pslib.jtool.framework.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 通过字符串或类获取bean，包含web mvc的bean
 * @author FH
 *
 */
public class SpringContextHolder implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return applicationContext;
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return (T) applicationContext.getBean(clazz);
	}
	public static <T> T getBean(String name, Class<T> clas) {
		checkApplicationContext();
		return (T) applicationContext.getBean(name, clas);
	}
	
	
	/**
	 * 从request中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getWebBean(HttpServletRequest request, String name) {
		return (T) RequestContextUtils.findWebApplicationContext(request).getBean(name);
	}

	/**
	 * 从request中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getWebBean(HttpServletRequest request, Class<T> clazz) {
		return (T) RequestContextUtils.findWebApplicationContext(request).getBeansOfType(clazz);
	}

	/**
	 * 从request中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getWebBean(HttpServletRequest request, String name, Class<T> clas) {
		return (T) RequestContextUtils.findWebApplicationContext(request).getBean(name, clas);
	}
	
	/**
	 * 清除applicationContext静态变量.
	 */
	public static void cleanApplicationContext() {
		applicationContext = null;
	}

	private static void checkApplicationContext() {
		if (applicationContext == null) {
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}
	
	/**
	 * 在main函数中初始化用
	 */
	public static void init(){
		//ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		//ApplicationContext context = new FileSystemXmlApplicationContext(new String[]{"/WebContent/WEB-INF/spring-context.xml","/WebContent/WEB-INF/spring-servlet.xml"});
		applicationContext = new FileSystemXmlApplicationContext(new String[]{"/WebContent/WEB-INF/spring-context.xml"});
		//(new AnnotationConfigApplicationContext());
		//WebApplicationContextUtils.getWebApplicationContext(context.g);
		//System.out.println(context.getClassLoader());
	}
	public static void initForClassPath(){
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
	}
}
