package com.pslib.jtool.util;

public class ClassPathUtil {
	public static String classPath(Class<?> cls) {
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
		String classPackage = cls.getPackage().getName();
		classPackage = classPackage.replaceAll("\\.", "[\\\\/]+") + "[\\/]+";
		path = path.replaceAll(classPackage, "");
		if (path.contains(".jar")) {
			path = path.replaceAll("/[^/]+\\.jar[!\\/]*", "/");
		}
		if (path.contains(":") && path.substring(0, 1).equals("/")) {
			path = path.substring(1);
		}
		path = path.replaceAll("[\\\\/]+", "/");
		return path;
	}

	/**
	 * 获取class根目录
	 * 
	 * @return
	 */
	public static String basePath() {
		return basePath(jarPath());
	}
	public static String basePath(String path) {
		return path.replaceAll("/WEB-INF/lib/", "/WEB-INF/classes/").replaceAll("[\\\\/]+", "/");
	}

	/**
	 * 获取web项目根目录
	 * 
	 * @return
	 */
	public static String webPath() {
		return webPath(jarPath());
	}
	public static String webPath(String path) {
		return path.replaceAll("(/lib/|/classes/|/WEB-INF/|/WEB-INF/lib/|/WEB-INF/classes/)$", "/").replaceAll("[\\\\/]+", "/");
	}


	public static String jarPath() {
		return classPath(ClassPathUtil.class);
	}

	public static void main(String[] args) {
		System.out.println("jarPath=" + jarPath());
		System.out.println("webPath=" + webPath());
		System.out.println("basePath=" + basePath());
		System.out.println("classPath=" + classPath(ClassPathUtil.class));

	}
}
