package com.pslib.jtool.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import com.pslib.jtool.security.base64.Base64;
import com.pslib.jtool.security.base64.NewBase64;

public class EncodeUtil {
	public static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 字节数组转换为字节字符串
	 * 
	 * @param ba
	 * @return
	 */
	public static String bin2hex(byte[] ba) {
		int length = ba.length;
		char[] buf = new char[length * 2];
		for (int i = 0, j = 0, k; i < length;) {
			k = ba[i++];
			buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
			buf[j++] = HEX_DIGITS[k & 0x0F];
		}
		return new String(buf);
	}

	/**
	 * 字符串转换为字节字符串
	 * 
	 * @param ba
	 * @return
	 */
	public static String str2hex(String ba, String encode) {
		try {
			return bin2hex(ba.getBytes(encode));
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			return "";
		}
	}

	public static String str2hex(String ba) {
		return str2hex(ba, "UTF-8");
	}

	/**
	 * 字节字符串转换为字节数组
	 * 
	 * @param ba
	 * @return
	 */
	public static byte[] hex2bin(String hex) {
		int len = hex.length();
		byte[] buf = new byte[((len + 1) / 2)];

		int i = 0, j = 0;
		if ((len % 2) == 1)
			buf[j++] = (byte) hexDigit(hex.charAt(i++));

		while (i < len) {
			buf[j++] = (byte) ((hexDigit(hex.charAt(i++)) << 4) | hexDigit(hex.charAt(i++)));
		}
		return buf;
	}

	/**
	 * 字节字符串还原为字符串
	 * 
	 * @param ba
	 * @return
	 */
	public static String hex2str(String hex, String encode) {
		try {
			return new String(hex2bin(hex), encode);
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			return "";
		}
	}

	public static String hex2str(String hex) {
		return hex2str(hex, "UTF-8");
	}

	public static int hexDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 10;
		if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 10;

		return (0); // any other char is treated as 0
	}

	/**
	 * 把字符串以MD5的方式加密
	 * 
	 * @param origin
	 *                需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String MD5(String origin) {
		return MD5(origin, "UTF-8");
	}

	public static String MD5(String origin, String encode) {
		String resultString = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = bin2hex(md.digest(origin.getBytes(encode))).toLowerCase();
		} catch (Exception ex) {
		}
		return resultString;
	}

	public final static String Sha1(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("sha-1");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			return bin2hex(mdInst.digest()).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * base64编码
	 * 
	 * @param s
	 * @return
	 */
	public static String base64Encode(String s, String charset) {
		return Base64.encode(s, charset);
	}
	public static String base64Encode(String s) {
		return Base64.encode(s);
	}

	/**
	 * base64解码
	 * 
	 * @param s
	 * @return
	 */
	public static String base64Decode(String s, String charset) {
		return Base64.decode(s, charset);
	}
	public static String base64Decode(String s) {
		return Base64.decode(s);
	}

	/**
	 * base64编码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] base64Encode(byte[] s) {
		return Base64.encode(s);
	}

	/**
	 * base64解码
	 * 
	 * @param s
	 * @return
	 */
	public static String base64EncodeStr(byte[] s) {
		return new String(Base64.encode(s));
	}

	/**
	 * base64解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] base64Decode(byte[] s) {
		return Base64.decode(s);
	}

	public static byte[] base64DecodeBin(String s) {
		return Base64.decode(s.getBytes());
	}

	/**
	 * base64编码(重定义字典可url传输)
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String newBase64Encode(String str, String charset) {
		return NewBase64.encode(str, charset);
	}
	public static String newBase64Encode(String str) {
		return NewBase64.encode(str);
	}

	/**
	 * base64解码(重定义字典可url传输)
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String newBase64Decode(String str, String charset) {
		return NewBase64.decode(str, charset);
	}
	public static String newBase64Decode(String str) {
		return NewBase64.decode(str);
	}

	/**
	 * base64编码(重定义字典可url传输)
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] newBase64Encode(byte[] s) {
		return NewBase64.encode(s);
	}

	/**
	 * base64解码(重定义字典可url传输)
	 * 
	 * @param s
	 * @return
	 */
	public static String newBase64EncodeStr(byte[] s) {
		return new String(NewBase64.encode(s));
	}

	/**
	 * base64解码(重定义字典可url传输)
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] newBase64Decode(byte[] s) {
		return NewBase64.decode(s);
	}
	public static byte[] newBase64DecodeBin(String s) {
		return NewBase64.decode(s.getBytes());
	}

	/**
	 * 重定义字典base64串转标准串
	 * @param str
	 * @return
	 */
	public static String newBase64ToBase64(String str) {
		return str.replace(".", "+").replace("_", "/").replace("-", "=");
	}
	/**
	 * 标准串转重定义字典base64串
	 * @param str
	 * @return
	 */
	public static String base64TonewBase64(String str) {
		return str.replace("+", ".").replace("/", "_").replace("=", "-");
	}

	/**
	 * url加密
	 * 
	 * @param s
	 * @return
	 */
	public static String urlEncode(String s) {
		return urlEncode(s, "UTF-8");
	}

	public static String urlEncode(String s, String encode) {
		try {
			s = java.net.URLEncoder.encode(s, encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * url解密
	 * 
	 * @param s
	 * @return
	 */
	public static String urlDecode(String s) {
		return urlDecode(s, "UTF-8");
	}

	public static String urlDecode(String s, String encode) {
		try {
			s = java.net.URLDecoder.decode(s, encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 转换为js escape编码
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		if (src == null) {
			return "";
		}
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j) || isUnConv(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16).toUpperCase());

			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16).toUpperCase());
			}
		}
		return tmp.toString();
	}

	/**
	 * 这里的字符不转换，为了和js统一
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isUnConv(char ch) {
		return (ch == '-' || ch == '_' || ch == '@' || ch == '.' || ch == '*');
	}

	/**
	 * 转换js escape编码为正常字符
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	public static void main(String[] args) {
		

	}

}
