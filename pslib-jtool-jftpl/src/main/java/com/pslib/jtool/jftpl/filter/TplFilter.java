package com.pslib.jtool.jftpl.filter;

import java.io.File;
import java.util.Vector;
import java.util.regex.Pattern;

import com.pslib.jtool.jftpl.util.TplSkinInfo;
import com.pslib.jtool.jftpl.util.TplUtil;

public class TplFilter {
	private static Vector<Object[]> syntaxPatterns = new Vector<Object[]>();
	//private static Pattern staticResPatterns1 = Pattern.compile("(<(img|video|audio|script|link).*?)(src|href)([\\s]*=[\\s]*[\"']{1})([^\"']+\\?[^\"']*)([\"']{1})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	//private static Pattern staticResPatterns2 = Pattern.compile("(<(img|video|audio|script|link).*?)(src|href)([\\s]*=[\\s]*[\"']{1})([^\"'\\?]+)([\"']{1})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	//js|css|png|jpg|jpeg|gif|json|html|jsp|do|mp3|mov|mp4|m4a|ogg|flv|swf
	private static Pattern staticResPatterns1 = Pattern.compile("(<(img|video|audio|script|link).*?)(src|href)([\\s]*=[\\s]*[\"']{1})([^\"']+\\.[\\w]+\\?[^\"']*)([\"']{1})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static Pattern staticResPatterns2 = Pattern.compile("(<(img|video|audio|script|link).*?)(src|href)([\\s]*=[\\s]*[\"']{1})([^\"'\\?]+\\.[\\w]+)([\"']{1})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	static {
		/*
		 * DelimiterStatementStart = "<%" DelimiterStatementEnd = "%>" int length = yylength()-2;
		 */
		Object[] pt;

		// <!-- #include file="/test2.header.html" --> to <% include
		// "/test2.header.html"; %>
		pt = new Object[] { Pattern.compile("<!--\\s*#include\\s*file\\s*=\\s*\"([^\"]+)\"\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% include \"$1\"; %>" };
		syntaxPatterns.add(pt);

		// <!-- for(book : books) --> to <% for(book : books){ %>
		pt = new Object[] { Pattern.compile("<!--\\s*for\\s*([\\(](.+?)[\\)])\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% for $1{ %>" };
		syntaxPatterns.add(pt);

		// <!-- if( ... ) --> to <% if( ... ){ %>
		pt = new Object[] { Pattern.compile("<!--\\s*if\\s*([\\(](.+?)[\\)])\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% if $1{ %>" };
		syntaxPatterns.add(pt);

		// <!-- else if( ... ) --> to <% }else if( ... ){ %>
		// "<!--\\s*(else\\s*(if|)(\\s*[\\(](.+?)[\\)]|))\\s*-->"
		pt = new Object[] { Pattern.compile("<!--\\s*else\\s*if\\s*([\\(](.+?)[\\)])\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% }else if $1{ %>" };
		syntaxPatterns.add(pt);

		// <!-- else --> to <% }else{ %>
		pt = new Object[] { Pattern.compile("<!--\\s*(else)\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% }else{ %>" };
		syntaxPatterns.add(pt);

		// <!-- end --> to <% } %>
		pt = new Object[] { Pattern.compile("<!--\\s*end\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% } %>" };
		syntaxPatterns.add(pt);

		// <!-- {echo 32;} --> to <% echo 32; %>
		pt = new Object[] { Pattern.compile("<!--\\s*\\{(.+?)\\}\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "<% $1 %>" };
		syntaxPatterns.add(pt);

		// ${data['index01']} to ${data["index01"]}
		pt = new Object[] { Pattern.compile("\\$\\{([\\w\\d_\\-]+)\\[[']*([\\w\\d_\\-]+)[']*]}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "\\${$1[\"$2\"]}" };
		syntaxPatterns.add(pt);
		
		
		//remove all <!-- -->
		//pt = new Object[] { Pattern.compile("<!--.*?-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "" };
		//syntaxPatterns.add(pt);
		
		//remove all <!--[ ]-->
		pt = new Object[] { Pattern.compile("<!--\\s*\\[.*?\\]\\s*-->", Pattern.CASE_INSENSITIVE | Pattern.DOTALL), "" };
		syntaxPatterns.add(pt);
	}

	/**
	 * 替换语法
	 * 
	 * @param str
	 * @return
	 */
	public static String syntaxReplace(String str) {
		if (str == null)
			return str;

		// 替换语法
		for (Object[] pt : syntaxPatterns) {
			str = ((Pattern) pt[0]).matcher(str).replaceAll(((String) pt[1]));
		}

		return str;
	}

	/**
	 * 语法替换，模板规则统一
	 * 
	 * @param fname
	 * @param str
	 * @return
	 */
	public static String filter(TplSkinInfo skinInfo, String filename, File file, String str) {
		if (str == null)
			return str;

		// 语法替换
		str = syntaxReplace(str);
		// 图片路径 替换
		str = replacePath(skinInfo, filename, file, str);
		// 静态资源动态增加版本号
		if (skinInfo.getStaticResVersion() != null && skinInfo.getDefaultSkin().length() > 0) {
			str = replaceStaticRes(skinInfo, filename, file, str);
		}

		return str;
	}

	/**
	 * 替换图片路径
	 * 
	 * @param absRoot
	 * @param root
	 * @param file
	 */
	public static String replacePath(TplSkinInfo skinInfo, String filename, File file, String str) {

		Pattern p_replacePath;
		String newPath;
		String imagesDomain = "";
		if (skinInfo.getImgPath() != null) {
			p_replacePath = Pattern.compile(skinInfo.getImgPath());
			newPath = skinInfo.getImgPath();
		} else {
			p_replacePath = Pattern.compile("images/");
			newPath = "images/";
		}
		if (skinInfo.getImgDomain() != null) {
			imagesDomain = skinInfo.getImgDomain();
		}
		String root = TplUtil.clearPath(skinInfo.getRoot());
		if (root.endsWith("/"))
			root = root.substring(0, root.length() - 1);
		String webRoot = TplUtil.clearPath(skinInfo.getWebRoot());
		if (webRoot.endsWith("/"))
			webRoot = webRoot.substring(0, webRoot.length() - 1);

		// System.out.println(root);
		String fullpathFile = TplUtil.dirname(file.getAbsolutePath());
		String fullpathFileImage = TplUtil.clearPath(fullpathFile + "/" + newPath);
		String srcTplPathImage = TplUtil.clearPath(fullpathFile.substring(webRoot.length()));
		File imgFile = new File(fullpathFileImage);
		while (!imgFile.isDirectory() && srcTplPathImage.length() > 0) {
			int pos = fullpathFile.lastIndexOf("/");
			if(pos == -1) {
				break;
			}
			fullpathFile = fullpathFile.substring(0, fullpathFile.lastIndexOf("/"));
			srcTplPathImage = TplUtil.clearPath(fullpathFile.substring(webRoot.length()));

			fullpathFileImage = TplUtil.clearPath(fullpathFile + "/" + newPath);
			imgFile = new File(fullpathFileImage);
			// System.out.println(fullpathFile);
			// System.out.println(srcTplPath);
		}

		if (imgFile.isDirectory()) {
			str = p_replacePath.matcher(str).replaceAll(imagesDomain.concat(TplUtil.clearPath(skinInfo.getContextPath() + srcTplPathImage + "/" + newPath)));
		}

		return str;

	}

	/**
	 * 静态资源动态增加版本号
	 * 
	 * @param skinInfo
	 * @param filename
	 * @param file
	 * @param str
	 * @return
	 */
	public static String replaceStaticRes(TplSkinInfo skinInfo, String filename, File file, String str) {
		str = staticResPatterns1.matcher(str).replaceAll("$1$3$4$5&_t=" + skinInfo.getStaticResVersion() + "$6");
		str = staticResPatterns2.matcher(str).replaceAll("$1$3$4$5?_t=" + skinInfo.getStaticResVersion() + "$6");
		return str;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String str4 = "<img src=\"images/game/page/finger.png\">\n"
//				+ "<audio id=\"backgroundMusic\" src=\"aaa.mp3\" loop preload=\"preload\"></audio>\n"
//				+ "<audio id=\"backgroundMusic\" src=\"aaa.mp3?aa=bb\" loop preload=\"preload\"></audio>\n"
//				+ "<video id=\"backgroundMusic\" src=\"aaa.mp3\" loop preload=\"preload\"></video>\n"
//				+ "<script type=\"text/javascript\" src=\"images/js/game.min.js\"></script>\n"
//				+ "<script type=\"text/javascript\" src=\"${CONTEXT_PATH}/js/jquery/plugins/kxbdmarquee/jquery.kxbdmarquee.mobile.js\"></script>\n"
//				+ "<link rel=\"stylesheet\" href=\"images/css/game.css\" />\n"
//				+ "<a href=\"a.do\">asdfas</a>\n";
//		str4 = staticResPatterns1.matcher(str4).replaceAll("$1$3$4$5&_t=" + "***********" + "$6");
//		str4 = staticResPatterns2.matcher(str4).replaceAll("$1$3$4$5?_t=" + "#########" + "$6");
//		System.out.println(str4);
//		System.exit(0);
		String str = "<link rel=\"stylesheet\" type=\"text/css\" href=\"images/css/ziliao.css{?e\" />";

//		str = staticResPatterns1.matcher(str).replaceAll("$1$2$3&_t=1234$4");
//		str = staticResPatterns2.matcher(str).replaceAll("$1$2$3&_t=1234$4");
//		System.out.println(str);
		TplSkinInfo skinInfo = new TplSkinInfo();
		skinInfo.setRoot("./");
		skinInfo.setWebRoot("./");
		skinInfo.setImgDomain("http://aaa.com");
		String filename = "temp/test/a.html";
		File file = new File(filename);
		String str2 = replacePath(skinInfo, filename, file, str);
		System.out.println(str2);
	}

}
