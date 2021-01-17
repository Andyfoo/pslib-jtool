package com.pslib.jtool.captcha.util;

public class CaptchaStringUtil {
	public static String join(String arr[]) {
		return join("", arr);
	}
	public static String join(String split, String arr[]) {
		StringBuffer sb = new StringBuffer();
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			if(i < len - 1)sb.append(split);
		}
		return sb.toString();
	}
	
	

	public static void main(String[] args) {
		String[] arr = new String[]{"1","2","3"};
		System.out.println(join(",", arr));
	}
}
