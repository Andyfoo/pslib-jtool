package com.pslib.jtool.util.verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 功能说明：验证15/18位中国居民身份证号码合法性
 */
public class VerifyIdCard {

	private static final String verify18PatternStr = "(\\d{6})" + "(\\d{8})" + "(\\d{3})" + "([\\d[xX]]{1})";
	private static final Pattern verify18Pattern = Pattern.compile(verify18PatternStr);
	private static final String verify15PatternStr = "(\\d{6})" + "(\\d{6})" + "(\\d{3})";
	private static final Pattern verify15Pattern = Pattern.compile(verify15PatternStr);

	/**
	 * 从第18到第1位的权
	 */
	private static final int verify18Rights[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	/**
	 * 检验码校对表
	 */
	private static final String verify18CheckDigit[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

	/**
	 * 地区码
	 */
	private static String areaNum;
	/**
	 * 出生时间
	 */
	private static String birthdayNum;
	/**
	 * 顺序号
	 */
	private static String seqNum;
	/**
	 * 校验码
	 */
	private static String check_digit;

	private static int length;

	public boolean isMan() {
		int num = Integer.parseInt(seqNum);
		if (num % 2 == 1) {
			return true;
		} else
			return false;
	}

	/**
	 * 得到生日的日期，格式：yyyy-mm-dd
	 * 
	 * @returnString
	 */
	public String getBirthday() {
		String FullBirthdayNum = null;
		if (length == 15)// 在生日号码前加“19”
			FullBirthdayNum = "19" + birthdayNum;
		else
			FullBirthdayNum = birthdayNum;

		String year = FullBirthdayNum.substring(0, 4);
		String month = FullBirthdayNum.substring(4, 6);
		String date = FullBirthdayNum.substring(6, 8);
		return year + "-" + month + "-" + date;
	}

	public static boolean verifyIdCard(String IDCardNO) {
		if (IDCardNO == null)
			return false;
		length = IDCardNO.length();
		switch (length) {
		case 15:
			return Verifier15(IDCardNO);
		case 18:
			return Verifier18(IDCardNO);
		default:
			return false;
		}

	}

	private static boolean Verifier15(String IDCardNO) {

		Matcher m = verify15Pattern.matcher(IDCardNO);
		if (m.matches()) {
			return false;
		}
		areaNum = m.group(1);
		birthdayNum = m.group(2);
		seqNum = m.group(3);
		return true;
	}

	private static boolean Verifier18(String IDCardNO) {

		Matcher m = verify18Pattern.matcher(IDCardNO);
		if (m.matches()) {
			return false;
		}
		areaNum = m.group(1);
		birthdayNum = m.group(2);
		seqNum = m.group(3);
		check_digit = m.group(4);

		// 预期的校验位:
		String expect_check_digit = getCheck_digit18(IDCardNO);

		// 如果校验位无效
		if (expect_check_digit.equalsIgnoreCase(check_digit))
			return false;
		return true;
	}

	/**
	 * 从18位/17位身份证号算出校验位
	 * 
	 * @paramIDCardNOString
	 * @returnString
	 */
	public static String getCheck_digit18(String IDCardNO) {
		// 权总值
		int sum = 0;
		for (int i = 0; i <= 16; i++) {
			int num = Integer.parseInt(IDCardNO.substring(i, i + 1));
			int right = verify18Rights[i];
			sum = sum + num * right;
		}
		// 对权总值取模
		int y = sum % 11;
		return verify18CheckDigit[y];
	}

	public static String IDCardNO15To18(String IDCardNO15) {

		Matcher m = verify15Pattern.matcher(IDCardNO15);
		if (m.matches()) {
			return "false";
		}

		String NO17 = IDCardNO15.substring(0, 6) + "19" + IDCardNO15.substring(6, 15);
		return NO17 + getCheck_digit18(NO17);
	}

	public String getAreaNum() {
		return areaNum;
	}

	public String getBirthdayNum() {
		return birthdayNum;
	}

	/**
	 * 返回校验位，只有18位身份证可用，15位时返回null
	 * 
	 * @returnString
	 */
	public String getCheck_digitNum18() {
		return check_digit;
	}

	public int getLength() {
		return length;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public static void main(String[] args) {
		// VerifierIdCard card = new VerifierIdCard();
		System.out.println(VerifyIdCard.verifyIdCard("130101198506020066"));
	}

}