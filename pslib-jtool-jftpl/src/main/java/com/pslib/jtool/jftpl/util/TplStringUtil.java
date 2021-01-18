package com.pslib.jtool.jftpl.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * 
 * 
 */
public class TplStringUtil {

	/**
	 * 取文件尺寸，带单位
	 * 
	 * @param size
	 *                数字
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		// if (kiloByte < 1) {
		// return size + "Byte(s)";
		// }
		if (kiloByte < 1) {
			return size + "B";
		}
		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 *                生成字符串的长度
	 * @param randCase
	 *                是否随机大小写
	 * @return
	 */
	public static String getRandomString(int length, boolean randCase) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		if (randCase) {
			base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		}
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomString(int length) {
		return getRandomString(length, false);
	}

	/**
	 * 显示隐藏后的字符
	 * 
	 * @param str
	 * @param start
	 * @param num
	 * @return
	 */
	public static String getHideStr(String str, int start, int num) {
		StringBuffer sb = new StringBuffer();
		byte[] b = str.getBytes();
		int end = start + num;
		if (num < 0) {
			end = b.length + end - 1;
		}

		for (int i = 0; i < b.length; i++) {
			if (i >= start && i <= end) {
				sb.append("*");
			} else {
				sb.append((char) b[i]);
			}
		}

		return sb.toString();
	}

	/**
	 * 生成option列表
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToOptions(Map<Integer, String> map) {
		StringBuffer sb = new StringBuffer();

		Iterator<?> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = (Integer) iter.next();
			String val = map.get(key);

			sb.append("<option value=\"");
			sb.append(key);
			sb.append("\">");
			sb.append(val);
			sb.append("</option>\r\n");
		}

		return sb.toString();
	}

	/**
	 * 字符串填充
	 * 
	 * @param str
	 * @param fillLen
	 * @param fillStr
	 * @return
	 */
	public static String strPad(String str, int fillLen, String fillStr) {
		if (fillLen == 0)
			return str;
		int len = str.length();
		for (int i = len; i < fillLen; i++) {
			str = fillStr + str;
		}
		return str;
	}

	/**
	 * 重复字符串
	 * 
	 * @param str
	 * @param count
	 * @return
	 */
	public static String strRepeat(String str, int count) {
		if (count == 0 || str == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static String formatRate(String sp) {
		sp = sp.replace(",", "");
		return formatRate(Double.valueOf(sp), "##0.00");
	}

	public static String formatRate(String sp, String format) {
		sp = sp.replace(",", "");
		return formatRate(Double.valueOf(sp), format);
	}

	public static String formatRate(double sp) {
		return formatRate(sp, "##0.00");
	}

	public static String formatRate(double sp, String format) {
		DecimalFormat myformat3 = new DecimalFormat();
		myformat3.applyPattern(format);
		return myformat3.format(sp);
	}

	public static String formatMoney(String money) {
		return formatMoney(money, ",##0.00");
	}

	public static String formatMoney(Double money) {
		return formatMoney(money, ",##0.00");
	}

	public static String formatMoney(String money, String format) {
		money = (money == null || money.length()==0) ? "0" : money.replace(",", "");
		return formatMoney(Double.valueOf(money), format);
	}

	public static String formatMoney(double money, String format) {
		String formatMoney = "0.00";
		try {
			DecimalFormat myformat3 = new DecimalFormat();
			myformat3.applyPattern(format);
			formatMoney = myformat3.format(money);
		} catch (Exception ex) {
		}
		return formatMoney;
	}

	/**
	 * 转换 以分为单位的 金额格式
	 * 
	 * @param money
	 * @param format
	 * @return
	 */
	public static String formatFenMoney(long money, String format) {
		BigDecimal b = new BigDecimal(money + "");
		b = b.divide(new BigDecimal("100")).setScale(2);
		String formatMoney = "0.00";
		if (format != null && format.length() > 0) {
			try {
				DecimalFormat myformat3 = new DecimalFormat();
				myformat3.applyPattern(format);
				formatMoney = myformat3.format(b.doubleValue());
			} catch (Exception ex) {
			}
			return formatMoney;
		} else {
			return b.toString();
		}
	}

	public static BigDecimal getYuanFormFen(long fen) {
		BigDecimal b = new BigDecimal(fen + "");
		b = b.divide(new BigDecimal("100")).setScale(2);
		return b;
	}

	public static String formatFenMoney(long money) {
		return formatFenMoney(money, ",##0.00");
	}

	public static String formatFenMoney(String money) {
		return formatFenMoney(money, ",##0.00");
	}

	public static String formatFenMoney(String money, String format) {
		money = (money == null || money.length()==0) ? "0" : money.replace(",", "");
		return formatFenMoney(Long.parseLong(money), format);
	}

	/**
	 * 将html转换为字符串
	 * 
	 * @param str
	 * @param str
	 * @return
	 */
	public static String html2js(String str) {
		str = str.replaceAll("\r\n", " ");
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\\\"", "\\\\\"");
		return str;
	}

	/**
	 * 字符串截取
	 * @param str
	 * @param toCount
	 * @param more
<pre>
http://blog.csdn.net/wxqian25/article/details/7997641
汉字区 包括
GBK/2：0xB0A1-F7FE, 收录 GB2312 汉字 6763 个，按原序排列；
GBK/3：0x8140-A0FE，收录 CJK 汉字 6080 个；
GBK/4：0xAA40-FEA0，收录 CJK 汉字和增补的汉字 8160 个。
图形符号区 包括
GBK/1：0xA1A1-A9FE，除 GB2312 的符号外，还增补了其它符号
GBK/5：0xA840-A9A0，扩除非汉字区。
用户自定义区
即 GBK 区域中的空白区，用户可以自己定义字符。
編碼
GBK 亦采用双字节表示，总体编码范围为 8140-FEFE 之间，首字节在 81-FE 之间，尾字节在 40-FE 之间，剔除 XX7F 一条线。
</pre>
	 * @return
	 */
	public static String cutStr(String str, int toCount, String more) {
		try {
			String reStr = "";
			if (str == null)
				return "";
			byte[] bte = str.getBytes("GBK");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bte.length && i < toCount; i++) {
				if ((bte[i] & 0xFF) > 0x7F) {
					sb.append(new String(new byte[] { bte[i], bte[i + 1] }, "GBK"));
					i++;
				} else {
					sb.append((char) bte[i]);
				}

			}
			reStr = sb.toString();
			if (reStr.getBytes().length < str.getBytes().length)
				reStr += more;
			return reStr;
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 连接字符串
	 * 
	 * @param s1
	 * @param s2
	 * @param s3
	 * @return
	 */
	public static String concat(String s1, String s2, String s3) {
		return s1.concat(s2).concat(s3);
	}

	/**
	 * 连接字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static String concat(String... strings) {
		int len = 0;
		int i;
		final int size;
		i = size = strings.length;
		while (i != 0) {
			--i;
			len += strings[i].length();
		}
		StringBuilder sb = new StringBuilder(len);
		for (i = 0; i < size; i++) {
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isEmpty(CharSequence cs) {
		return (cs == null) || (cs.length() == 0);
	}

	/**
	 * 字符串是否不为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * 替换字符串
	 * 
	 * @param buf
	 * @param searchString
	 * @param replacement
	 */
	public static void replace(StringBuilder buf, String searchString, String replacement) {
		int start = 0;
		int end = buf.indexOf(searchString, start);
		if (end == -1) {
			return;
		}
		int replLength = searchString.length();
		while (end != -1) {
			buf.replace(end, end + replLength, replacement);
			start = end + replLength;
			end = buf.indexOf(searchString, start);
		}
	}

	/**
	 * 替换字符串
	 * 
	 * @param text
	 * @param searchString
	 * @param replacement
	 * @return
	 */
	public static String replace(String text, String searchString, String replacement) {
		if ((isEmpty(text)) || (isEmpty(searchString)) || (replacement == null)) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= 64;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(TplStringUtil.formatFenMoney("2000"));
	}

}
