package com.pslib.jtool.captcha.util;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CaptchaFileUtil {
	
	/**
	 * 设置 ttf字体大小
	 * @param fontFileName
	 * @param fontSize
	 * @return
	 */
	public static Font loadFont(Font font, float fontSize) {
		try {
			return font.deriveFont(fontSize);
		} catch (Exception e){
			e.printStackTrace();
			return new java.awt.Font("宋体", Font.PLAIN, 14);
		}
	}
	public static Font loadFont(Font font,int style, float fontSize) {
		try {
			return font.deriveFont(style, fontSize);
		} catch (Exception e){
			e.printStackTrace();
			return new java.awt.Font("宋体", Font.PLAIN, 14);
		}
	}
	/**
	 * 从class目录或jar包中加载ttf字体
	 * @param fontFileName
	 * @param fontSize
	 * @return
	 */
	public static Font loadFont(String fontFileName) {
		try {
			InputStream file = readResource(fontFileName);
			Font font = Font.createFont(Font.TRUETYPE_FONT, file);
			file.close();
			return font;
		} catch (Exception e){
			e.printStackTrace();
			return new java.awt.Font("宋体", Font.PLAIN, 14);
		}
	}
	/**
	 * Get a file resource and return it as an InputStream. Intended
	 * primarily to read in binary files which are contained in a jar.
	 * 
	 * @param filename
	 * @return An @{link InputStream} to the file
	 */
	/**
	 * 从class目录或jar包中加载文件
	 * @param filename
	 * @return
	 */
	public static final InputStream readResource(String filename) {
		InputStream jarIs = CaptchaFileUtil.class.getResourceAsStream(filename);
		if (jarIs == null) {
			throw new RuntimeException(new FileNotFoundException("File '" + filename + "' not found."));
		}

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		byte[] data = new byte[16384];
		int nRead;

		try {
			while ((nRead = jarIs.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			jarIs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(buffer.toByteArray());
	}


	public static void main(String[] args) {
		
	}
}
