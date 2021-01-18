package com.pslib.jtool.jftpl.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TplUtil {
	protected transient final static Logger logger = LoggerFactory.getLogger(TplUtil.class);

	/**
	 * 清除多余路径
	 * 
	 * @param str
	 * @return
	 */
	private static Pattern p_clearPath = Pattern.compile("[\\\\/]+");

	public static String clearPath(String str) {
		if (str == null)
			return str;
		return p_clearPath.matcher(str).replaceAll("/");
	}

	/**
	 * 读取文件
	 * 
	 * @param filename
	 * @param encode
	 * @return
	 */
	public static String fileRead(String filename, String encode) {
		return fileRead(new File(filename), encode);
	}

	public static String fileRead(File _file, String encode) {
		FileInputStream file = null;
		BufferedReader fw = null;
		try {
			file = new FileInputStream(_file);
			int ch;
			StringBuffer strb = new StringBuffer();
			fw = new BufferedReader(new InputStreamReader(file, encode));
			while ((ch = fw.read()) > -1) {
				strb.append((char) ch);
			}
			fw.close();
			file.close();
			return strb.toString();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

	}

	/**
	 * 返回字符串的文件名，过滤目录字符
	 * 
	 * @param str
	 *                字符串
	 * 
	 * @return 字符串
	 */
	public static String basename(String str) {
		str = clearPath(str);
		// str = str.replaceAll("\\\\", "/");
		// String arr[] = str.split("/");

		// return arr.length > 0 ? arr[arr.length-1] : "";
		int pos = str.lastIndexOf('/');
		int pos2 = str.lastIndexOf('\\');
		if (str.length() == 0)
			return "";
		if (pos > pos2) {
			return str.substring(pos + 1);
		} else {
			return str.substring(pos2 + 1);
		}
	}

	/**
	 * 返回字符串的目录，过滤文件名字符
	 * 
	 * @param str
	 *                字符串
	 * 
	 * @return 字符串
	 */
	public static String dirname(String str) {
		str = clearPath(str);
		int pos = str.lastIndexOf('/');
		int pos2 = str.lastIndexOf('\\');
		if (str.length() == 0)
			return "";
		if (pos > pos2) {
			return str.substring(0, pos + 1);
		} else {
			return str.substring(0, pos2 + 1);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
