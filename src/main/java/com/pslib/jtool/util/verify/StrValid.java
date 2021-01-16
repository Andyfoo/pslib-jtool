package com.pslib.jtool.util.verify;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.pslib.jtool.util.VarUtil;

import cn.hutool.json.JSONUtil;

/**
 * 验证字符串
 * 
 * @author ******
 *
 */
public class StrValid {
	public static Map<String, Pattern> rules;
	static {
		rules = new HashMap<String, Pattern>();
		rules.put("email", Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"));
		rules.put("number", Pattern.compile("^\\d+$"));
		rules.put("url", Pattern.compile("^(http[s]?://.+)$"));
		rules.put("tel", Pattern.compile("^((\\(\\d{2,3}\\))|(\\d{3}[\\-]{0,1}))?(\\(0\\d{2,3}\\)|0\\d{2,3}[\\-]{0,1})?[1-9]\\d{6,7}([\\-]{0,1}\\d{1,4})?$"));
		rules.put("mobile", Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}$"));
		rules.put("domain", Pattern.compile("^([\\w]+\\.[\\w]+[\\w.]*)$"));

		rules.put("money", Pattern.compile("^\\d+(\\.\\d+)?$"));
		rules.put("zip", Pattern.compile("^[0-9]{5,6}$"));
		rules.put("oicq", Pattern.compile("^[0-9]{4,12}$"));
		rules.put("int", Pattern.compile("^[-\\+]?\\d+$"));
		rules.put("double", Pattern.compile("^[-\\+]?\\d+(\\.\\d+)?$"));
		rules.put("english", Pattern.compile("^[\\w\\. ]+$"));
		rules.put("letter", Pattern.compile("^[A-Za-z]+$"));
		rules.put("upper_letter", Pattern.compile("^[A-Z]+$"));
		rules.put("lower_letter", Pattern.compile("^[a-z]+$"));

		rules.put("chinese", Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$"));
		rules.put("username", Pattern.compile("^[a-z]\\w{2,32}$"));
		rules.put("username2", Pattern.compile("^[\\w]{2,32}$"));
		rules.put("nickname", Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{2,40}$"));
		rules.put("password", Pattern.compile("^[\\w]+$"));
		rules.put("password2", Pattern.compile("^[\\w\\.;,'~!#@$%^&*()<>\\-=:\\?+|]+$"));
		rules.put("char", Pattern.compile("^[\\w\\-]+$"));
		rules.put("text_cn_en", Pattern.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]*$"));

		rules.put("idcard", Pattern.compile("^\\d{15}(\\d{2}[\\w])?$"));
		rules.put("bankcard", Pattern.compile("^\\d+$"));

		rules.put("ip", Pattern.compile("^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$"));

	}

	private String rule = null;
	private Integer lenMax = null;
	private Integer lenMin = null;
	private Integer blenMax = null;
	private Integer blenMin = null;
	private Integer rangeMax = null;
	private Integer rangeMin = null;

	public StrValid() {

	}

	public StrValid(String rule) {
		this.rule = rule;
	}

	public StrValid(String rule, int lenMin) {
		this.rule = rule;
		this.lenMin = lenMin;
	}

	public StrValid(String rule, int lenMin, int lenMax) {
		this.rule = rule;
		this.lenMin = lenMin;
		this.lenMax = lenMax;
	}

	/**
	 * 获取验证实例
	 * 
	 * @return
	 */
	public static StrValid get() {
		return new StrValid();
	}

	public static StrValid get(String rule) {
		return new StrValid(rule);
	}

	public static StrValid get(String rule, int lenMin) {
		return new StrValid(rule, lenMin);
	}

	public static StrValid get(String rule, int lenMin, int lenMax) {
		return new StrValid(rule, lenMin, lenMax);
	}

	/**
	 * 规则名称，也可直接写正则字符串
	 * 
	 * @param rule
	 * @return
	 */
	public StrValid rule(String rule) {
		this.rule = rule;
		return this;
	}

	/**
	 * 最大长度
	 * 
	 * @param max
	 * @return
	 */
	public StrValid lenMax(Integer lenMax) {
		this.lenMax = lenMax;
		return this;
	}

	/**
	 * 最小长度
	 * 
	 * @param max
	 * @return
	 */
	public StrValid lenMin(Integer lenMin) {
		this.lenMin = lenMin;
		return this;
	}

	/**
	 * 字节最大长度
	 * 
	 * @param max
	 * @return
	 */
	public StrValid blenMax(Integer blenMax) {
		this.blenMax = blenMax;
		return this;
	}

	/**
	 * 字节最小长度
	 * 
	 * @param max
	 * @return
	 */
	public StrValid blenMin(Integer blenMin) {
		this.blenMin = blenMin;
		return this;
	}

	/**
	 * 数字最大值
	 * 
	 * @param max
	 * @return
	 */
	public StrValid rangeMax(Integer rangeMax) {
		this.rangeMax = rangeMax;
		return this;
	}

	/**
	 * 数字最小值
	 * 
	 * @param max
	 * @return
	 */
	public StrValid rangeMin(Integer rangeMin) {
		this.rangeMin = rangeMin;
		return this;
	}

	/**
	 * 验证str是否有效
	 * 
	 * @param str
	 * @return
	 */
	public boolean valid(String str) {
		Pattern pattern = rules.get(rule);
		if (pattern == null && rule.length() > 0) {
			pattern = Pattern.compile(rule, Pattern.CASE_INSENSITIVE);
		}
		if (pattern != null && !pattern.matcher(str).find())
			return false;
		if (lenMax != null && lenMax > str.length())
			return false;
		if (lenMin != null && lenMin < str.length())
			return false;
		try {
			if (blenMax != null && blenMax > str.getBytes("GBK").length)
				return false;
			if (blenMin != null && blenMin < str.getBytes("GBK").length)
				return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		if (rangeMax != null && Double.valueOf(rangeMax) > VarUtil.doubleval(str))
			return false;
		if (rangeMin != null && Double.valueOf(rangeMin) < VarUtil.doubleval(str))
			return false;

		return true;
	}

	/**
	 * 显示所有正则
	 */
	public static void printRules() {
		System.out.println(JSONUtil.toJsonPrettyStr(rules));
	}

	public static void main(String[] args) throws Exception {
		System.out.println(get().rule("mobile").valid("13811111111"));
		// printRules();
		// System.out.println("正".getBytes("UTF-8").length);
	}
}
