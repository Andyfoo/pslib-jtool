package com.pslib.jtool.jftpl.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.febit.wit.InternalContext;
import org.febit.wit.global.GlobalManager;
import org.febit.wit.global.GlobalRegister;
import org.febit.wit.lang.Bag;
import org.febit.wit.lang.MethodDeclare;
import com.pslib.jtool.jftpl.util.TplArrayUtil;
import com.pslib.jtool.jftpl.util.TplDateSimpleUtil;
import com.pslib.jtool.jftpl.util.TplDateUtil;
import com.pslib.jtool.jftpl.util.TplHtmlFormUtil;
import com.pslib.jtool.jftpl.util.TplStringUtil;
import com.pslib.jtool.jftpl.util.TplVarUtil;

public class TplGlobalRegister implements GlobalRegister {

	@Override
	public void regist(GlobalManager manager) {
		// 全局变量
		// SimpleBag globalBag = manager.getGlobalBag();

		// 全局常量
		Bag constBag = manager.getConstBag();

		// 全局Native 函数
		// constBag.set("new_list",
		// this.nativeFactory.createNativeConstructorDeclare(ArrayList.class,
		// null));

		// 全局自定函数

		// 字符串替换
		// str_replace(String text, String searchString, String
		// replacement)
		constBag.set("str_replace", new MethodDeclare() {
			@Override
			public Object invoke(InternalContext context, Object[] args) {
				// String text, String searchString, String
				// replacement
				if (args[0] == null)
					return "";
				return TplStringUtil.replace(String.valueOf(args[0]), String.valueOf(args[1]),
						String.valueOf(args[2]));
			}
		});

		// 字符串截取
		// cutStr(String str, int toCount, String more)
		// cutStr(String str, int toCount)
		constBag.set("cutStr", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// cutStr(String str, int toCount, String more)
				if (args[0] == null)
					return "";
				if (args.length == 3) {
					return TplStringUtil.cutStr(String.valueOf(args[0]),
							Integer.valueOf(String.valueOf(args[1])),
							String.valueOf(args[2]));
				} else {
					return TplStringUtil.cutStr(String.valueOf(args[0]),
							Integer.valueOf(String.valueOf(args[1])), "");
				}

			}
		});
		// 字符串截取2
		// substring(String str, int begin, int end)
		constBag.set("substring", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args[0] == null)
					return "";
				String str = (String) args[0];
				if (args.length == 3) {
					return str.substring(Integer.valueOf(String.valueOf(args[1])),
							Integer.valueOf(String.valueOf(args[2])));
				} else {
					return str.substring(Integer.valueOf(String.valueOf(args[1])));
				}

			}
		});
		// 格式化金额:将分格式化为元
		// formatFenMoney(String money, String format)
		// formatFenMoney(String money)
		constBag.set("formatFenMoney", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// formatFenMoney(String money, String format)
				if (args.length == 2) {
					return TplStringUtil.formatFenMoney(String.valueOf(args[0]),
							String.valueOf(args[1]));
				} else {
					return TplStringUtil.formatFenMoney(String.valueOf(args[0]));
				}

			}
		});
		// 格式化金额
		// formatMoney(String money, String format)
		// formatMoney(String money)
		constBag.set("formatMoney", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// formatMoney(String money, String format)
				if (args[0] == null)
					return "";
				if (args.length == 2) {
					return TplStringUtil.formatMoney(String.valueOf(args[0]),
							String.valueOf(args[1]));
				} else {
					return TplStringUtil.formatMoney(String.valueOf(args[0]));
				}

			}
		});
		// 格式化比率
		// formatRate(String sp, String format)
		// formatRate(String sp)
		constBag.set("formatRate", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// formatRate(String sp, String format)
				if (args[0] == null)
					return "";
				if (args.length == 2) {
					return TplStringUtil.formatRate(String.valueOf(args[0]),
							String.valueOf(args[1]));
				} else {
					return TplStringUtil.formatRate(String.valueOf(args[0]));
				}

			}
		});
		// 字符串补齐
		// strPad(String str, int fillLen, String fillStr)
		// strPad(String str, int fillLen)
		constBag.set("strPad", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// strPad(String str, int fillLen, String
				// fillStr)
				if (args[0] == null)
					return "";
				if (args.length == 3) {
					return TplStringUtil.strPad(String.valueOf(args[0]),
							Integer.valueOf(String.valueOf(args[1])),
							String.valueOf(args[2]));
				} else {
					return TplStringUtil.strPad(String.valueOf(args[0]),
							Integer.valueOf(String.valueOf(args[1])), "0");
				}
			}
		});
		// 日期格式化
		// time是10位的时间戳, format:Y-m-d H:i:s
		// date(String format, String time)
		// date(String format)
		// date()
		constBag.set("date", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// date(String format, String time)
				if (args.length == 2) {
					if (args[0] == null)
						return "";
					if (args[1] == null)
						return "";
					if (args[1] instanceof Date) {
						return TplDateSimpleUtil.date(String.valueOf(args[0]), (Date) args[1]);
					} else {
						return TplDateSimpleUtil.date(String.valueOf(args[0]),
								String.valueOf(args[1]));
					}
				} else if (args.length == 1) {
					if (args[0] == null)
						return "";
					return TplDateSimpleUtil.date(String.valueOf(args[0]));
				} else {
					return TplDateSimpleUtil.date();
				}
			}
		});
		
		// 日期 Y-m-d H:i:s
		// datetime_str(Date time)
		// datetime_str()
		constBag.set("datetime_str", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// date(String format, String time)
				if (args.length == 1) {
					if (args[0] == null)
						return "";
					if (args[0] instanceof Date) {
						return TplDateSimpleUtil.date("Y-m-d H:i:s", (Date) args[0]);
					} else {
						return TplDateSimpleUtil.date("Y-m-d H:i:s", 
								String.valueOf(args[0]));
					}
				} else {
					return TplDateSimpleUtil.date();
				}
			}
		});
		// 日期 Y-m-d
		// date_str(Date time)
		// date_str()
		constBag.set("date_str", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 1) {
					if (args[0] == null)
						return "";
					if (args[0] instanceof Date) {
						return TplDateSimpleUtil.date("Y-m-d", (Date) args[0]);
					} else {
						return TplDateSimpleUtil.date("Y-m-d", 
								String.valueOf(args[0]));
					}
				} else {
					return TplDateSimpleUtil.date("Y-m-d");
				}
			}
		});
		// 时间 H:i:s
		// time_str(Date time)
		// time_str()
		constBag.set("time_str", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 1) {
					if (args[0] == null)
						return "";
					if (args[0] instanceof Date) {
						return TplDateSimpleUtil.date("H:i:s", (Date) args[0]);
					} else {
						return TplDateSimpleUtil.date("H:i:s", 
								String.valueOf(args[0]));
					}
				} else {
					return TplDateSimpleUtil.date("H:i:s");
				}
			}
		});
		
		// 10位时间戳
		// time(String date, String format)
		// time(String date)
		// time()
		constBag.set("time", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// time(String date, String format)
				if (args.length == 2) {
					if (args[0] == null)
						return "";
					return TplDateSimpleUtil.time(String.valueOf(args[0]), String.valueOf(args[1]));
				} else if (args.length == 1) {
					if (args[0] == null)
						return "";
					return TplDateSimpleUtil.time(String.valueOf(args[0]));
				} else {
					return TplDateSimpleUtil.time();
				}
			}
		});

		// 日期格式化2--java格式
		// time是13位的时间戳, format:yyyy-MM-dd HH:mm:ss
		// getDate(String time, String format)
		// getDate(String format)
		// getDate()
		constBag.set("getDate", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// getDate(String format, String time)
				if (args.length == 2) {
					if (args[0] == null)
						return "";
					if (args[0] instanceof Date) {
						return TplDateUtil.getDate((Date) args[0], String.valueOf(args[1]));
					} else {
						return TplDateUtil.getDate(String.valueOf(args[0]),
								String.valueOf(args[1]));
					}
				} else if (args.length == 1) {
					if (args[0] == null)
						return "";
					return TplDateUtil.getDate(String.valueOf(args[0]));
				} else {
					return TplDateUtil.getDate();
				}
			}
		});
		// 13位时间戳--java格式
		// getTime(String date, String format)
		// getTime(String date)
		// getTime()
		constBag.set("getTime", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// getTime(String date, String format)
				if (args.length == 2) {
					if (args[0] == null)
						return "";
					return TplDateUtil.getTime(String.valueOf(args[0]), String.valueOf(args[1]));
				} else if (args.length == 1) {
					if (args[0] == null)
						return "";
					return TplDateUtil.getTime(String.valueOf(args[0]));
				} else {
					return TplDateUtil.getTime();
				}
			}
		});

		// 日期转换格式--简化format
		// date是日期字符串, format:Y-m-d H:i:s
		// date2date(String date, String format)
		constBag.set("date2date", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// date2date(String date, String format)
				if (args[0] == null)
					return "";
				return TplDateSimpleUtil.date2date(String.valueOf(args[0]), String.valueOf(args[1]));
			}
		});

		// 日期转换格式--java format
		// date是日期字符串, format:yyyy-MM-dd HH:mm:ss
		// dateToDate(String date, String format)
		constBag.set("dateToDate", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// dateToDate(String date, String format)
				if (args[0] == null)
					return "";
				return TplDateUtil.dateToDate(String.valueOf(args[0]), String.valueOf(args[1]));
			}
		});

		// 字符串转换为 int
		// intval(String str, int def)
		// intval(String str)
		constBag.set("intval", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// intval(String str, int def)
				if (args.length == 2) {
					return TplVarUtil.intval(String.valueOf(args[0]),
							Integer.valueOf(String.valueOf(args[1])));
				} else {
					return TplVarUtil.intval(String.valueOf(args[0]));
				}
			}
		});

		// 字符串转换为 long
		// longval(String str, long def)
		// longval(String str)
		constBag.set("longval", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// longval(String str, long def)
				if (args.length == 2) {
					return TplVarUtil.longval(String.valueOf(args[0]),
							Long.valueOf(String.valueOf(args[1])));
				} else {
					return TplVarUtil.longval(String.valueOf(args[0]));
				}
			}
		});

		// 字符串转换为 float
		// floatval(String str, float def)
		// floatval(String str)
		constBag.set("floatval", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// floatval(String str, float def)
				if (args.length == 2) {
					return TplVarUtil.floatval(String.valueOf(args[0]),
							Float.valueOf(String.valueOf(args[1])));
				} else {
					return TplVarUtil.floatval(String.valueOf(args[0]));
				}
			}
		});

		// 字符串转换为 double
		// doubleval(String str, float def)
		// doubleval(String str)
		constBag.set("doubleval", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// doubleval(String str, double def)
				if (args.length == 2) {
					return TplVarUtil.doubleval(String.valueOf(args[0]),
							Double.valueOf(String.valueOf(args[1])));
				} else {
					return TplVarUtil.doubleval(String.valueOf(args[0]));
				}
			}
		});

		// 判断字符串是否在数组中
		// strInArray(String str)
		constBag.set("strInArray", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// strInArray(String str, String[] arr)
				return TplArrayUtil.inArray(String.valueOf(args[0]), (String[]) args[1]);
			}
		});
		// 判断数字是否在数组中
		// intInArray(String str)
		constBag.set("intInArray", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				// intInArray(int str, int[] arr)
				return TplArrayUtil.inArray(Integer.valueOf(String.valueOf(args[0])), (int[]) args[1]);
			}
		});

		// option生成--map
		// gen_option_list(Map arr , String sel)
		// public static final Map<Integer, String> arr;
		// static{
		// arr = new HashMap<Integer, String>();
		// arr.put(1, "成功");
		// arr.put(2, "失败");
		//
		// }
		constBag.set("gen_option_map", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				if (args.length == 1) {
					return TplHtmlFormUtil.gen_option_map((Map<?, ?>) args[0], null);
				} else if (args.length == 2) {
					return TplHtmlFormUtil.gen_option_map((Map<?, ?>) args[0], args[1]);
				}
				return "";
			}
		});

		// option生成--list
		// gen_option_list(List<Object[]> arr , String sel)
		// ArrayList<Object[]> arr = new
		// ArrayList<Object[]>();arr.add(new Object[]{"1","aa"});
		constBag.set("gen_option_list", new MethodDeclare() {
			@SuppressWarnings("unchecked")
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				if (args.length == 1) {
					return TplHtmlFormUtil.gen_option_list((List<Object[]>) args[0], null);
				} else if (args.length == 2) {
					return TplHtmlFormUtil.gen_option_list((List<Object[]>) args[0], args[1]);
				}
				return "";
			}
		});
		// option生成--array
		// gen_option_arr(List<Object[]> arr , String sel)
		// new Object[]{"1","aa"}
		constBag.set("gen_option_arr", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				if (args.length == 1) {
					return TplHtmlFormUtil.gen_option_arr((Object[]) args[0], null);
				} else if (args.length == 2) {
					return TplHtmlFormUtil.gen_option_arr((Object[]) args[0], args[1]);
				}
				return "";
			}
		});

		// option索引标题
		// get_option_title(List<Object[]> arr , String sel)
		// ArrayList<Object[]> arr = new
		// ArrayList<Object[]>();arr.add(new Object[]{"1","aa"});
		constBag.set("get_option_title", new MethodDeclare() {
			@SuppressWarnings("unchecked")
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_option_title((List<Object[]>) args[0],
						String.valueOf(args[1]));
			}
		});
		constBag.set("get_map_option_title", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_map_option_title((Map<?, ?>)args[0],
						String.valueOf(args[1]));
			}
		});

		// get_checked
		// get_checked(boolean checked)
		constBag.set("get_checked", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_checked(Boolean.valueOf(String.valueOf(args[0])));
			}
		});
		// get_selected
		// get_selected(boolean selected)
		constBag.set("get_selected", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length == 0 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_selected(Boolean.valueOf(String.valueOf(args[0])));
			}
		});

		// get_bool_str
		// get_bool_str(boolean s, String trueStr, String falseStr)
		constBag.set("get_bool_str", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length != 3 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_bool_str(Boolean.valueOf(String.valueOf(args[0])),
						String.valueOf(args[1]), String.valueOf(args[2]));
			}
		});

		// get_expr_str
		// get_expr_str(int s, String greaterStr, String equalStr,
		// String lessStr)
		constBag.set("get_expr_str", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				if (args.length != 4 || args[0] == null)
					return "";
				return TplHtmlFormUtil.get_expr_str(Integer.valueOf(String.valueOf(args[0])),
						String.valueOf(args[1]), String.valueOf(args[2]),
						String.valueOf(args[3]));
			}
		});

		// is_empty
		// is_empty(String str)
		constBag.set("is_empty", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				return TplStringUtil.isEmpty(String.valueOf(args[0]));
			}
		});

		// is_not_empty
		// is_not_empty(String str)
		constBag.set("is_not_empty", new MethodDeclare() {
			public Object invoke(InternalContext context, Object[] args) {
				return TplStringUtil.isNotEmpty(String.valueOf(args[0]));
			}
		});

	}

}
