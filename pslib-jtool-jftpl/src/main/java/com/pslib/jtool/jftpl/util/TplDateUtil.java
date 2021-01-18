package com.pslib.jtool.jftpl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 *         功能说明：日期相关工具函数
 */
public class TplDateUtil {
	protected static final Logger logger = LoggerFactory.getLogger(TplDateUtil.class);

	// private static String defaultDatePattern = "yyyyMMdd HH:mm:ss";
	// private static String timePattern = "HH:mm";

	/**
	 * 字符型时间变成时间类型
	 * 
	 * @param date
	 *                字符型时间 例如: "2008-08-08"
	 * @param format
	 *                格式化形式 例如: "yyyy-MM-dd"
	 * @return 出现异常时返回null
	 */
	public static Date String2Date(String date, String format) {
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
	public static String dateToDate(String date, String format) {
		return getDate(getTime(date), format);
	}

	/**
	 * 日期的指定域加减
	 * 
	 * @param time
	 *                时间戳(长整形字符串)
	 * @param field
	 *                加减的域,如date表示对天进行操作,month表示对月进行操作,其它表示对年进行操作
	 * @param num
	 *                加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static long addDate(long time, String field, int num) {
		int fieldNum = Calendar.YEAR;
		if (field.equals("month")) {
			fieldNum = Calendar.MONTH;
		}
		if (field.equals("date")) {
			fieldNum = Calendar.DATE;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.add(fieldNum, num);
		return cal.getTimeInMillis();
	}

	/**
	 * 日期的指定域加减(对天操作)
	 * 
	 * @param time
	 *                时间戳(长整形字符串)
	 * @param num
	 *                加减的数值
	 * @return 操作后的时间戳(长整形字符串)
	 */
	public static long addDate(long time, int num) {
		return addDate(time, "date", num);
	}

	/**
	 * 获取当前时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime() {
		return new Date().getTime();
	}

	/**
	 * 获取指定日期时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime(String date) {
		return getTime(date, "");
	}

	/**
	 * 获取指定日期时间（毫秒）
	 * 
	 * @return long
	 */
	public static long getTime(String date, String format) {
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

				format += TplStringUtil.strPad("y", arr2[0].length(), "y");
				format += "-";
				format += TplStringUtil.strPad("M", arr2[1].length(), "M");
				format += "-";
				format += TplStringUtil.strPad("d", arr2[2].length(), "d");

				if (arr.length == 2) {
					arr3 = arr[1].split(":");
					if (arr3 != null) {
						format += " ";
						format += TplStringUtil.strPad("H", arr3[0].length(), "H");

						if (arr3.length > 1) {
							format += ":";
							format += TplStringUtil.strPad("m", arr3[1].length(), "m");
						}
						if (arr3.length > 2) {
							format += ":";
							format += TplStringUtil.strPad("s", arr3[2].length(), "s");
						}

					}
				}
			} catch (Exception e) {
				logger.error("日期错误：" + date, e);
				return 0;
			}
		}
		// System.out.println(format+" "+date);

		DateFormat df = new SimpleDateFormat(format, Locale.US);
		try {
			time = df.parse(date).getTime();
		} catch (ParseException e) {
			logger.error("", e);
		}
		return time;
	}

	/**
	 * 获取指定日期，指定格式的字符串时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static final String getDate(Date date, String format) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (date != null) {
			df = new SimpleDateFormat(format, Locale.US);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取指定时间戳（毫秒），指定格式的字符串时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static final String getDate(long time, String format) {
		Date d = new Date();
		d.setTime(time);

		return getDate(d, format);
	}

	/**
	 * 获取指定时间戳（毫秒），指定格式的字符串时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static final String getDate(String time, String format) {
		return getDate(TplVarUtil.longval(time), format);
	}

	/**
	 * 获取当前日期，指定格式的字符串时间
	 * 
	 * @param format
	 * @return
	 */
	public static final String getDate(String format) {
		SimpleDateFormat df = null;
		String returnValue = "";
		Date date = new Date();
		if (date != null) {
			df = new SimpleDateFormat(format, Locale.US);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取当前日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param format
	 * @return
	 */
	public static final String getDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 时间间隔计算
	 * 
	 */
	public static String getDaysBeforeNow(Date date) {
		long sysTime = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		long ymdhms = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date));
		String strYear = "年前";
		String strMonth = "月前";
		String strDay = "天前";
		String strHour = "小时前";
		String strMinute = "分钟前";
		try {
			if (ymdhms == 0) {
				return "";
			}
			long between = (sysTime / 10000000000L) - (ymdhms / 10000000000L);
			if (between > 0) {
				return between + strYear;
			}
			between = (sysTime / 100000000L) - (ymdhms / 100000000L);
			if (between > 0) {
				return between + strMonth;
			}
			between = (sysTime / 1000000L) - (ymdhms / 1000000L);
			if (between > 0) {
				return between + strDay;
			}
			between = (sysTime / 10000) - (ymdhms / 10000);
			if (between > 0) {
				return between + strHour;
			}
			between = (sysTime / 100) - (ymdhms / 100);
			if (between > 0) {
				return between + strMinute;
			}
			return "1" + strMinute;
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) {

		System.out.println(getDate("a"));

	}

}
