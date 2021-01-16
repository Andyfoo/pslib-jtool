package com.pslib.jtool.util.verify;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 */
public class VerifyUtil {

	public static boolean isEmail(String email) {
		if (email == null)
			return false;
		String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		return m.find();
	}

	public static boolean isMobile(String mobile) {
		return isMobile(mobile, 11);
	}

	public static boolean isMobile(String mobile, int num) {
		if (mobile == null) {
			return false;
		}
		String regex = "^(1)(\\d{" + (num - 1) + "})";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		return m.find();
	}

	public static boolean isIP(String s) {
		if (s == null) {
			return false;
		}
		String regex0 = "(2[0-4]\\d)|(25[0-5])";
		String regex1 = "1\\d{2}";
		String regex2 = "[1-9]\\d";
		String regex3 = "\\d";
		String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
		regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
		Pattern p = Pattern.compile(regex);
		return p.matcher(s).matches();
	}

	public static boolean checkStringLen(String str, int min, int max) {
		boolean flag = false;
		if ((str != null) && (min > -1) && (max >= min) && (str.getBytes().length >= min) && (str.getBytes().length <= max)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isEmpty(String str) {
		boolean flag = false;
		if ((str == null) || (str.equals(""))) {
			flag = true;
		}
		return flag;
	}

	public static boolean isAmount(String amount, float maxAmount) {
		boolean flag = false;
		if ((isFloat(amount, 2)) && (Float.parseFloat(amount) > 0.0F) && (Float.parseFloat(amount) < maxAmount)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isFloat(String num, int dd) {
		boolean flag = false;
		if (num != null) {
			String regex = "[-]?\\d+";
			if (dd > 0)
				regex = regex + "\\.?\\d{0," + dd + "}";
			else if (dd <= 0) {
				regex = regex + "\\.?\\d+";
			}
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(num);
			if (m.matches()) {
				flag = true;
			}
		}
		return flag;
	}

	public static boolean isFloat(BigDecimal num, int dd) {
		boolean flag = false;
		String regex = "[-]?\\d+";
		if (dd > 0)
			regex = regex + "\\.?\\d{0," + dd + "}";
		else if (dd <= 0) {
			regex = regex + "\\.?\\d+";
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(String.valueOf(num));
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}

	public static Pattern pattern_isFloat = Pattern.compile("^(\\-*(\\d+\\.{1}\\d+|\\d+))$");

	public static boolean isFloat(String str) {
		return str != null && pattern_isFloat.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 *                字符串
	 * @return true/false
	 */
	public static Pattern pattern_isInt = Pattern.compile("^(\\-*\\d+)$");

	public static boolean isInt(String str) {
		return str != null && pattern_isInt.matcher(str).matches();
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 *                字符串
	 * @return true/false
	 */
	public static Pattern pattern_isNum = Pattern.compile("^(\\d+)$");

	public static boolean isNum(String str) {
		return str != null && pattern_isNum.matcher(str).matches();
	}

	public static boolean isDate(String date) {
		boolean flag = true;
		String dateStr = "";
		if (date.length() == 8) {
			dateStr = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			df.format(df.parse(dateStr));
		} catch (ParseException e) {
			// e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 检测用户名 (4-20个英文、数字、字符)(- _ . @ 即：减号、下划线、点、圈a)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkUserName(String str) {

		String regex = "^[\\w\\d-_.@]{4,20}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 检测昵称4-20个汉字、英文、数字、字符- _ . @
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkNickName(String str) {

		String regex = "^[\\u4e00-\\u9fa5\\w\\d-_.@]{1,20}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return (str.getBytes().length < 4 || str.getBytes().length > 20) && m.find();
	}

	/**
	 * 检测密码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkPassWord(String str) {
		// if(str.length() < 6 || str.length() > 20)return false;
		String regex = "^[\\w\\d-_.@]{6,20}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);

		return m.find();
	}

	/**
	 * 检测姓名
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkRealName(String str) {
		if (str.length() < 2 || str.length() > 15)
			return false;
		String regex;
		Pattern p;
		Matcher m;

		regex = "^[\\u0391-\\uFFE5]{2,15}$";
		p = Pattern.compile(regex);
		m = p.matcher(str);

		return m.find();
	}

	/**
	 * 检测支付密码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkPayPassWord(String str) {
		String regex = "^[\\d]{6}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 
	 * 验证证件类型(身份证、护照、军官证、其他)
	 * 
	 * @param type
	 *                -证件类型
	 * @param str
	 *                - 证件号码
	 * @return
	 */
	public static boolean checkIdcard(String type, String str) {
		if ("1".equals(type)) {// 身份证
			if (str.length() == 18) {
				String regex = "^[0-9]{17}[0-9]{1}|x{1}|X{1}$";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(str);
				return m.find();
			} else {
				return false;
			}
		} else if ("2".equals(type)) {// 护照
			String regex = "(^P[\\d]{7})|(^G[\\d]{8})";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.find();

		} else if ("3".equals(type)) {// 军官证
			String regex = "^[\\d]{7}$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.find();
		} else if ("4".equals(type)) {// 其他

			String regex = "^[\\d]+$";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.find();
		}
		return false;
	}

	/**
	 * 校验银行卡卡号
	 * 
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
		return cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0) {// ||
												// !nonCheckCodeCardId.matches("\\+"))
												// {
			throw new IllegalArgumentException("Bank card code must be number!");
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	public static void main(String[] args) {
		System.out.println(isNum("123456"));
		// System.out.println(checkNumber("aa"));
		// System.out.println(checkPassWord("12312012345678912345"));
		// System.out.println(checkIdcard("4","12345dd"));
		// System.out.println(checkBankCard("6226220108051458"));
		// System.out.println(checkNickName("顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶"));
	}
}
