package com.pslib.jtool.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * 
 * 
 *         功能说明：日期相关工具函数，与php兼容
 */
public class DateSimpleUtil {
	protected final static Logger logger = LoggerFactory.getLogger(DateSimpleUtil.class);

	/**
	 * 字符型时间变成时间类型
	 * 
	 * @param date
	 *                字符型时间 例如: "2008-08-08"
	 * @param format
	 *                格式化形式 例如: "Y-m-d"
	 * @return 出现异常时返回null
	 */
	public static Date String2Date(String date) {
		return String2Date(date, "Y-m-d");
	}

	public static Date String2Date(String date, String format) {
		format = format2java(format);
		DateFormat df = new SimpleDateFormat(format, Locale.US);
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			logger.error("", e);
		}
		return d;
	}

	/**
	 * 日期字符串转换为另一格式字符串
	 * 
	 */
	public static String date2date(String date, String format) {
		return date(format, time(date));
	}

	/**
	 * 获取当前时间（秒）
	 * 
	 * @return long
	 */
	public static long time() {
		return new Date().getTime() / 1000;
	}

	/**
	 * 获取指定日期时间（秒）
	 * 
	 * @return long
	 */
	public static long time(String date) {
		return time(date, "");
	}

	/**
	 * 得到日期的时间戳(秒) 自动根据长度判断格式 示范： 2010-1-10; 2010-01-10; 2010-1-1; 2010-1-01; 2010-1-10 12:12:10; 2010-1-1 1:1:1; ......
	 * 
	 * @param date
	 *                日期，格式：Y-m-d H:i:s
	 * @return 时间戳
	 */
	public static long time(String date, String format) {
		long time = 0;
		if (date == null || date.trim().length() < 6) {
			return 0;
		}
		date = date.trim();

		if (format.length() == 0) {
			try {
				String arr[] = date.split(" ");
				String arr2[] = arr[0].split("-");
				String arr3[] = null;

				format += StringUtil.strPad("y", arr2[0].length(), "y");
				format += "-";
				format += StringUtil.strPad("M", arr2[1].length(), "M");
				format += "-";
				format += StringUtil.strPad("d", arr2[2].length(), "d");

				if (arr.length == 2) {
					arr3 = arr[1].split(":");
					if (arr3 != null) {
						format += " ";
						format += StringUtil.strPad("H", arr3[0].length(), "H");

						if (arr3.length > 1) {
							format += ":";
							format += StringUtil.strPad("m", arr3[1].length(), "m");
						}
						if (arr3.length > 2) {
							format += ":";
							format += StringUtil.strPad("s", arr3[2].length(), "s");
						}

					}
				}
			} catch (Exception e) {
				logger.error("日期错误：" + date, e);
				return 0;
			}
		} else {
			format = format2java(format);
		}
		// System.out.println(format+" "+date);

		DateFormat df = new SimpleDateFormat(format, Locale.US);
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			logger.error("", e);
		}
		return time / 1000;
	}

	/**
	 * 获取当前日期字符串 Y-m-d H:i:s
	 * 
	 * @return String
	 */
	public static final String date() {
		return date("Y-m-d H:i:s");
	}

	/**
	 * 获取当前日期指定格式字符串
	 * 
	 * @param format
	 * @return String
	 */
	public static final String date(String format) {
		return date(format, new Date());
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串
	 * 
	 * @param format
	 * @param time
	 *                时间（秒）
	 * @return
	 */
	public static final String date(String format, String time) {
		Date d = new Date();
		d.setTime(VarUtil.longval(time) * 1000);
		return date(format, d);
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串
	 * 
	 * @param format
	 * @param time
	 *                时间（秒）
	 * @return
	 */
	public static final String date(String format, long time) {
		Date d = new Date();
		d.setTime(time * 1000);
		return date(format, d);
	}

	/**
	 * 获取指定时间戳、指定格式的时间字符串(与php格式兼容)
	 * 
	 * @param format
	 *                格式 Y=年,m=月,d=日,H=小时,i=分,s=秒
	 * @param time
	 *                日期
	 * @return
	 */
	public static final String date(String format, Date d) {
		if (d != null) {
			SimpleDateFormat df = new SimpleDateFormat(format2java(format, d), Locale.US);
			return df.format(d);
		}
		return "";
	}

	/**
	 * php格式日志转换为java格式
	 * 
	 * @param format
	 * @return
	 */
	public static final String format2java(String format) {
		return format2java(format, null);
	}

	public static final String format2java(String format, Date d) {
		StringBuffer format2 = new StringBuffer();
		char fArr[] = format.toCharArray();
		for (int i = 0; i < fArr.length; i++) {
			if ('Y' == fArr[i]) {// 年=2010
				format2.append("yyyy");
			} else if ('y' == fArr[i]) {// 年=10
				format2.append("yy");
			} else if ('m' == fArr[i]) {// 月= 01
				format2.append("MM");
			} else if ('d' == fArr[i]) {// 日=03
				format2.append("dd");
			} else if ('H' == fArr[i]) {// 小时=02
				format2.append("HH");
			} else if ('i' == fArr[i]) {// 分钟=03
				format2.append("mm");
			} else if ('s' == fArr[i]) {// 秒=02
				format2.append("ss");
			} else if ('s' == fArr[i]) {// 秒=02
				format2.append("ss");
			} else if ('g' == fArr[i]) {// 时 在上午或下午 (1~12)
				format2.append("h");
			} else if ('a' == fArr[i]) {// a 小写的上午和下午值 am 或 pm
				format2.append(DateUtil.getDate(d, "a").equals("AM") ? "am" : "pm");
			} else if ('A' == fArr[i]) {// A 大写的上午和下午值 AM 或 PM
				format2.append(DateUtil.getDate(d, "a").equals("AM") ? "AM" : "PM");
			} else if ('z' == fArr[i]) {// 年份中的第几天 0 到 366
				format2.append("D");
			} else if ('W' == fArr[i]) {// 年份中的第几周
				format2.append("w");
			} else if ('T' == fArr[i]) {// 时区
				format2.append("z");
			} else if ('B' == fArr[i]) {// 毫秒
				format2.append("S");
			} else if ('w' == fArr[i]) {// 星期 0（表示星期天）到 6（表示星期六）
				format2.append(enWeek2numWeek(DateUtil.getDate(d, "E")));
			} else if ('D' == fArr[i]) {// 星期 Mon 到 Sun
				format2.append(DateUtil.getDate(d, "E"));
			} else if ('l' == fArr[i]) {// 星期几，完整的文本格式 Sunday 到
							// Saturday
				format2.append(DateUtil.getDate(d, "EEEE"));
			} else if ('M' == fArr[i]) {// 三个字母缩写表示的月份 Jan 到 Dec
				format2.append("MMM");
			} else if ('F' == fArr[i]) {// 月份，完整的文本格式，例如 January 或者
							// March January 到
							// December
				format2.append("MMMM");
			} else {
				format2.append(fArr[i]);
			}
		}
		return format2.toString();
	}

	/**
	 * 英文周名转换为数字的
	 * 
	 * @param str
	 * @return
	 */
	public static final String enWeek2numWeek(String str) {
		// 0（表示星期天）到 6（表示星期六）
		str = str.toLowerCase();
		if (str.equals("sun"))
			return "0";// 星期天
		if (str.equals("mon"))
			return "1";// 星期一
		if (str.equals("tue"))
			return "2";// 星期二
		if (str.equals("wed"))
			return "3";// 星期三
		if (str.equals("thu"))
			return "4";// 星期四
		if (str.equals("fri"))
			return "5";// 星期五
		if (str.equals("sat"))
			return "6";// 星期六
		return "6";// sat//星期六
	}

	/**
	 * 取日期范围
	 * 
	 * @param type
	 *                =last_week/last_week_2/last_month/curr_month/curr_week /curr_week_2/today/yesterday/7day/3day/1month/2month/3 month/6month
	 * @return
	 */
	public static String[] getDateTimeRange(String type) {
		String[] r = new String[2];
		if (type.equals("last_week")) {// 上周(周1起始)
			String[] week = getLastWeek(1);
			if (week != null) {
				r[0] = week[0] + " 00:00:00";
				r[1] = week[1] + " 23:59:59";
			}
		} else if (type.equals("last_week_2")) {// 上周(周2起始)
			String[] week = getLastWeek(2);
			if (week != null) {
				r[0] = week[0] + " 00:00:00";
				r[1] = week[1] + " 23:59:59";
			}
		} else if (type.equals("last_month")) {// 上月
			r[0] = getLastMonth(1) + " 00:00:00";
			r[1] = getLastMonth(-1) + " 23:59:59";
		} else if (type.equals("curr_month")) {// 本月
			r[0] = date("Y-m") + "-01 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("curr_week")) {// 本周(周1起始)
			r[0] = getCurrWeek(1) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("curr_week_2")) {// 本周(周2起始)
			r[0] = getCurrWeek(2) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("today")) {// 今天
			r[0] = date("Y-m-d") + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("yesterday")) {// 昨天
			r[0] = getDay(-1) + " 00:00:00";
			r[1] = getDay(-1) + " 23:59:59";
		} else if (type.equals("7day")) {// 7天前
			r[0] = getDay(-6) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("3day")) {// 3天前
			r[0] = getDay(-3) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("1month")) {// 1月前
			r[0] = getMonth(-1) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("2month")) {// 2月前
			r[0] = getMonth(-2) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("3month")) {// 3月前
			r[0] = getMonth(-3) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else if (type.equals("6month")) {// 6月前
			r[0] = getMonth(-6) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		} else {// 默认 n天前
			r[0] = getDay(0 - Integer.valueOf(type)) + " 00:00:00";
			r[1] = date("Y-m-d H:i:s");
		}
		return r;
	}

	public static String[] getDateRange(String type) {
		String[] r = new String[2];
		if (type.equals("last_week")) {// 上周(周1起始)
			String[] week = getLastWeek(1);
			if (week != null) {
				r[0] = week[0];
				r[1] = week[1];
			}
		} else if (type.equals("last_week_2")) {// 上周(周2起始)
			String[] week = getLastWeek(2);
			if (week != null) {
				r[0] = week[0];
				r[1] = week[1];
			}
		} else if (type.equals("last_month")) {// 上月
			r[0] = getLastMonth(1);
			r[1] = getLastMonth(-1);
		} else if (type.equals("curr_month")) {// 本月
			r[0] = date("Y-m") + "-01";
			r[1] = date("Y-m-d");
		} else if (type.equals("curr_week")) {// 本周(周1起始)
			r[0] = getCurrWeek(1);
			r[1] = date("Y-m-d");
		} else if (type.equals("curr_week_2")) {// 本周(周2起始)
			r[0] = getCurrWeek(2);
			r[1] = date("Y-m-d");
		} else if (type.equals("today")) {// 今天
			r[0] = date("Y-m-d");
			r[1] = date("Y-m-d");
		} else if (type.equals("yesterday")) {// 昨天
			r[0] = getDay(-1);
			r[1] = getDay(-1);
		} else if (type.equals("7day")) {// 7天前
			r[0] = getDay(-6);
			r[1] = date("Y-m-d");
		} else if (type.equals("3day")) {// 3天前
			r[0] = getDay(-3);
			r[1] = date("Y-m-d");
		} else if (type.equals("1month")) {// 1月前
			r[0] = getMonth(-1);
			r[1] = date("Y-m-d");
		} else if (type.equals("2month")) {// 2月前
			r[0] = getMonth(-2);
			r[1] = date("Y-m-d");
		} else if (type.equals("3month")) {// 3月前
			r[0] = getMonth(-3);
			r[1] = date("Y-m-d");
		} else if (type.equals("6month")) {// 6月前
			r[0] = getMonth(-6);
			r[1] = date("Y-m-d");
		} else {// 默认 n天前
			r[0] = getDay(0 - Integer.valueOf(type));
			r[1] = date("Y-m-d");
		}
		return r;
	}

	/**
	 * 前几天
	 * 
	 * @param day
	 *                =天数,正数为+多少天，负数为-多少天
	 * @return
	 */
	public static String getDay(int day) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String da = df.format(new Date());
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, day);

			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getDay:", e);
			return "";
		}

	}

	/**
	 * 前几月
	 * 
	 * @param n
	 *                =月数,正数为+多少月，负数为-多少月
	 * @return
	 */
	public static String getMonth(int n) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String da = df.format(new Date());
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.MONTH, n);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getCurrWeek:", e);
			return "";
		}

	}

	/**
	 * 获取本周几的日期字符串
	 * 
	 * @param day
	 *                =周几开始
	 * @return
	 */
	public static String getCurrWeek(int day) {
		return getCurrWeek(day, new Date());
	}

	public static String getCurrWeek(int day, Date d) {
		try {
			int w = Integer.valueOf(date("w", d));
			if (w == 0)
				w = 7;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			if (w < day) {
				cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
				cal.add(Calendar.DAY_OF_WEEK, 0 - 7);
			} else {
				cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
			}

			cal.add(Calendar.DAY_OF_WEEK, day - 1);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getCurrWeek:", e);
			return "";
		}

	}

	/**
	 * 上周时间范围
	 * 
	 * @param day
	 *                =一周几天 1-7
	 * @return
	 */
	public static String[] getLastWeek(int day) {
		return getLastWeek(day, new Date());
	}

	/**
	 * 上周时间范围
	 * 
	 * @param day
	 *                =一周几天 1-7
	 * @return
	 */
	public static String[] getLastWeek(int day, Date d) {
		try {
			int w = Integer.valueOf(date("w", d));
			if (w == 0)
				w = 7;
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_WEEK, 0 - w + 1);
			cal.add(Calendar.DAY_OF_WEEK, day - 1);

			cal.add(Calendar.DAY_OF_WEEK, 0 - 7);

			if (w < day) {
				cal.add(Calendar.DAY_OF_WEEK, 0 - 7);
			} else {
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String r[] = new String[2];

			r[0] = df.format(cal.getTime());
			cal.add(Calendar.DATE, 6);
			r[1] = df.format(cal.getTime());

			return r;
		} catch (Exception e) {
			logger.error("getLastWeek:", e);
			return null;
		}
	}

	/**
	 * 上月几号
	 * 
	 * @param day
	 *                =上月几号(-1表示月末)
	 * @return
	 */
	public static String getLastMonth(int day) {
		return getLastMonth(day, new Date());
	}

	public static String getLastMonth(int day, Date d) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String da = df.format(d);
			Calendar cal = Calendar.getInstance();
			Date date = df.parse(da);
			cal.setTime(date);
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			if (day == -1) {
				day = cal.getActualMaximum(Calendar.DATE);
				// System.out.println(day);
			}
			cal.add(Calendar.DAY_OF_MONTH, day - 1);
			String monday = df.format(cal.getTime());
			return monday;
		} catch (Exception e) {
			logger.error("getLastMonth:", e);
			return "";
		}

	}

	/// 0表示星期天）到 6（表示星期六）
	static String[] weeks0 = { "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	static String[] weeks1 = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
	static String[] weeks2 = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
	static String[] weeks3 = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };

	public static String getWeekName(int w, int type) {
		switch (type) {
		case 0:
			return weeks0[w];
		case 1:
			return weeks1[w];
		case 2:
			return weeks2[w];
		case 3:
			return weeks3[w];
		}
		return "";
	}

	public static void main(String[] args) {

		System.out.println(getCurrWeek(2));
		String[] d;

		/*
		 * d = getDateRange("last_week"); System.out.println(d[0]); System.out.println(d[1]);
		 */
		d = getDateRange("last_week_2");
		System.out.println(d[0]);
		System.out.println(d[1]);

		/*
		 * d = getDateRange("curr_week"); System.out.println(d[0]); System.out.println(d[1]);
		 */

		d = getDateRange("curr_week_2");
		System.out.println(d[0]);
		System.out.println(d[1]);

		/*
		 * d = getDateRange("last_month"); System.out.println(d[0]); System.out.println(d[1]);
		 * 
		 * 
		 * d = getDateRange("curr_month"); System.out.println(d[0]); System.out.println(d[1]);
		 */
		/*
		 * 
		 * d = getDateRange("curr_week"); System.out.println(d[0]); System.out.println(d[1]);
		 * 
		 * d = getDateRange("curr_week_2"); System.out.println(d[0]); System.out.println(d[1]);
		 */

		/*
		 * d = getDateRange("today"); System.out.println(d[0]); System.out.println(d[1]);
		 */

		d = getDateRange("12");
		System.out.println(d[0]);
		System.out.println(d[1]);

		/*
		 * //System.out.println(getDate(new Date(), "EEEE")); System.out.println(date("Y-F-d H:i:s", new Date())); System.out.println(time("11-oct-1999", "dd-MMM-yyyy"));
		 * 
		 * String input = "1222-11-11"; SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US); Date t = null; try{ t = formatter.parse(input); System.out.println(t); }catch(ParseException e){
		 * 
		 * System.out.println("unparseable using " + formatter); }
		 */
	}

}
