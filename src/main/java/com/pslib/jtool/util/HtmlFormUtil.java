package com.pslib.jtool.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class HtmlFormUtil {
	protected final static Logger logger = LoggerFactory.getLogger(HtmlFormUtil.class);

	// public class AdminRoleConst {
	//
	// public static final List<Object[]> statusList = new ArrayList<Object[]>();
	//
	// static {
	// statusList.add(new Object[] { 0, "禁用" });
	// statusList.add(new Object[] { 1, "启用" });
	//
	//
	// }
	//
	// }

	/**
	 * 根据数组生成option列表字符串
	 * 
	 * @param list
	 * @param sel
	 * @return
	 */
	public static String gen_option_list(List<Object[]> list, Object sel) {
		StringBuilder sb = new StringBuilder();
		String k, v, select;
		for (int i = 0; i < list.size(); i++) {
			Object[] row = (Object[]) list.get(i);
			k = v = select = "";
			if (row.length == 1) {
				k = String.valueOf(row[0]);
				v = k;
			} else if (row.length == 2) {
				k = String.valueOf(row[0]);
				v = String.valueOf(row[1]);
			} else {
				continue;
			}
			if (sel != null) {
				select = String.valueOf(sel).equals(k) ? " selected" : "";
			}
			sb.append("<option value=\"");
			sb.append(k);
			sb.append("\"");
			sb.append(select);
			sb.append(">");
			sb.append(v);
			sb.append("</option>\r\n");
		}
		return sb.toString();
	}

	public static String gen_option_list(List<Object[]> list) {
		return gen_option_list(list, null);
	}

	public static String gen_option_arr(Object[] list, Object sel) {
		StringBuilder sb = new StringBuilder();
		String k, v, select;
		for (int i = 0; i < list.length; i++) {
			k = String.valueOf(list[i]);
			v = k;
			select = "";
			if (sel != null) {
				select = String.valueOf(sel).equals(k) ? " selected" : "";
			}
			sb.append("<option value=\"");
			sb.append(k);
			sb.append("\"");
			sb.append(select);
			sb.append(">");
			sb.append(v);
			sb.append("</option>\r\n");
		}
		return sb.toString();
	}

	public static String gen_option_arr(Object[] list) {
		return gen_option_arr(list, null);
	}

	/**
	 * 返回option列表中的索引标题
	 * 
	 * @param list
	 * @param index
	 * @return
	 */
	public static String get_option_title(List<Object[]> list, int index) {
		for (Object[] row : list) {
			if (Integer.valueOf(String.valueOf(row[0])) == index)
				return String.valueOf(row[1]);
		}
		return null;
	}

	/**
	 * 返回option列表中的索引标题
	 * 
	 * @param list
	 * @param index
	 * @return
	 */
	public static String get_option_title(List<Object[]> list, String index) {
		for (Object[] row : list) {
			if (index.equals(String.valueOf(row[0])))
				return String.valueOf(row[1]);
		}
		return null;
	}

	/**
	 * 返回 checkbox/radio 是否选中的字符串
	 * 
	 * @return
	 */
	public static String get_checked(boolean checked) {
		return checked ? " checked " : "";
	}

	/**
	 * 返回 option 是否选中的字符串
	 * 
	 * @return
	 */
	public static String get_selected(boolean checked) {
		return checked ? " selected " : "";
	}

	/**
	 * 根据布尔值返回2种字符串 s=true 返回 trueStr s=false 返回falseStr
	 * 
	 * @return
	 */
	public static String get_bool_str(boolean s, String trueStr, String falseStr) {
		return s ? trueStr : falseStr;
	}

	/**
	 * 根据数值返回3种字符串 s>0 返回 greaterStr s=0 返回 equalStr s<0 返回 lessStr
	 * 
	 * @return
	 */
	public static String get_expr_str(int s, String greaterStr, String equalStr, String lessStr) {
		if (s > 0)
			return greaterStr;
		if (s < 0)
			return lessStr;
		return lessStr;
	}

	public static void main(String[] argc) {
		ArrayList<Object[]> arr = new ArrayList<Object[]>();
		arr.add(new Object[] { "1", "aa" });
		arr.add(new Object[] { "2", "bb" });
		arr.add(new Object[] { "3", "cc" });
		System.out.println(gen_option_list(arr, 2));
	}

}
