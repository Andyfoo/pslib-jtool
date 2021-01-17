package com.pslib.jtool.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.json.JSONArray;


/**
 * @author ******
 * 
 * 
 *         功能说明：请补充
 */
public class MathUtil {

	// 取随机数
	/*
	 * 生成 min-max之间的数字， 包括min,max
	 * 
	 */
	public static int getRand(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	// 生成随机数的数组，
	public static int[] getUniqueRandArray(int num, int min, int max) {
		if(max - min + 1 < num) {
			return null;
		}
		List<Integer> arr = new ArrayList<Integer>();
		int n;
		int[] r = new int[num];
		for (int i = 0; i < num; i++) {
			do {
				n = MathUtil.getRand(min, max);
			}while(arr.contains(n));
			arr.add(n);
			r[i] = n;
		}
		arr.clear();
		return r;
	}

	// 生成随机数的数组，并排序
	public static int[] getUniqueRandArray(int num, int min, int max, String sort) {
		int[] sel = getUniqueRandArray(num, min, max);
		if (sort.equals("ASC")) {
			Arrays.sort(sel);
			int[] sel2 = new int[sel.length];
			for (int i = sel.length; i > 0; i--) {
				sel2[sel.length - i] = sel[i - 1];
			}
			return sel;
		} else {
			Arrays.sort(sel);
			return sel;
		}
	}

	// 生成随机数字符串的数组，
	public static String[] getUniqueStringRandArray(int num, int len, int min, int max) {
		int[] sel = getUniqueRandArray(num, min, max);
		String[] result = new String[sel.length];
		for (int i = 0; i < sel.length; i++) {
			result[i] = StringUtil.strPad(Integer.toString(sel[i]), len, "0");
		}
		return result;
	}

	// 生成随机数字符串的数组，并排序
	public static String[] getUniqueStringRandArray(int num, int len, int min, int max, String sort) {
		int[] sel = getUniqueRandArray(num, min, max, sort);
		String[] result = new String[sel.length];
		for (int i = 0; i < sel.length; i++) {
			result[i] = StringUtil.strPad(Integer.toString(sel[i]), len, "0");
		}
		return result;
	}

	/**
	 * 排列组合 (java多个一维数组进行组合排序 笛卡尔 http://blog.csdn.net/wuyongde_0922/article/details/56511362)
	 * 
	 * @param source
	 *                "a,b,c|2,3|A,B,C"
	 * @return
	 */
	public static JSONArray getCombJSONArray(String source) {
		String[] list = source.split("\\|");
		JSONArray strs = new JSONArray();
		for (int i = 0; i < list.length; i++) {
			strs.add(Arrays.asList(list[i].split(",")));
		}
		return getCombJSONArray(strs);
	}

	/**
	 * 排列组合
	 * 
	 * @param strs
	 *                [["a","b","c"],["2","3"]]
	 * @return
	 */
	public static JSONArray getCombJSONArray(JSONArray strs) {
		int total = 1;
		for (int i = 0; i < strs.size(); i++) {
			total *= strs.getJSONArray(i).size();
		}
		JSONArray mysesult = new JSONArray();
		for (int i = 0; i < total; i++) {
			mysesult.add(new JSONArray());
		}
		int now = 1;
		// 每个元素每次循环打印个数
		int itemLoopNum = 1;
		// 每个元素循环的总次数
		int loopPerItem = 1;
		for (int i = 0; i < strs.size(); i++) {
			JSONArray temp = strs.getJSONArray(i);
			now = now * temp.size();
			// 目标数组的索引值
			int index = 0;
			int currentSize = temp.size();
			itemLoopNum = total / now;
			loopPerItem = total / (itemLoopNum * currentSize);
			int myindex = 0;
			for (int j = 0; j < temp.size(); j++) {

				// 每个元素循环的总次数
				for (int k = 0; k < loopPerItem; k++) {
					if (myindex == temp.size())
						myindex = 0;
					// 每个元素每次循环打印个数
					for (int m = 0; m < itemLoopNum; m++) {
						JSONArray rows = mysesult.getJSONArray(index);
						rows.add(temp.get(myindex));
						mysesult.set(index, rows);
						index++;
					}
					myindex++;

				}
			}
		}
		return mysesult;
	}

	/**
	 * 排列组合
	 * 
	 * @param source
	 *                "a,b,c|2,3|A,B,C"
	 * @return
	 */
	public static List<List<String>> getCombList(String source) {
		String[] list = source.split("\\|");
		List<List<String>> strs = new ArrayList<List<String>>();
		for (int i = 0; i < list.length; i++) {
			strs.add(Arrays.asList(list[i].split(",")));
		}
		return getCombList(strs);
	}

	/**
	 * 排列组合
	 * 
	 * @param strs
	 *                [["a","b","c"],["2","3"]]
	 * @return
	 */
	public static List<List<String>> getCombList(List<List<String>> strs) {
		int total = 1;
		for (int i = 0; i < strs.size(); i++) {
			total *= strs.get(i).size();
		}
		List<List<String>> mysesult = new ArrayList<List<String>>();
		for (int i = 0; i < total; i++) {
			mysesult.add(new ArrayList<String>());
		}
		int now = 1;
		// 每个元素每次循环打印个数
		int itemLoopNum = 1;
		// 每个元素循环的总次数
		int loopPerItem = 1;
		for (int i = 0; i < strs.size(); i++) {
			List<String> temp = strs.get(i);
			now = now * temp.size();
			// 目标数组的索引值
			int index = 0;
			int currentSize = temp.size();
			itemLoopNum = total / now;
			loopPerItem = total / (itemLoopNum * currentSize);
			int myindex = 0;
			for (int j = 0; j < temp.size(); j++) {

				// 每个元素循环的总次数
				for (int k = 0; k < loopPerItem; k++) {
					if (myindex == temp.size())
						myindex = 0;
					// 每个元素每次循环打印个数
					for (int m = 0; m < itemLoopNum; m++) {
						List<String> rows = mysesult.get(index);
						rows.add(temp.get(myindex));
						mysesult.set(index, rows);
						index++;
					}
					myindex++;

				}
			}
		}
		return mysesult;
	}

	public static void main(String[] args) {
		System.out.println(getRand(3, 999));
		System.out.println(getUniqueRandArray(3, 0, 3));
	}

}
