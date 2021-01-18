package com.pslib.jtool.jftpl.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 */
public class TplArrayUtil {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("44");
		list.add("33");
		list.add("11");
		list.add("22");

		System.out.println(join(",", list));

		String[] param = new String[] { "cc", "aa", "bb" };

		System.out.println(join(",", param));

		int[] param2 = new int[] { 11, 22 };

		System.out.println(join(",", param2));

		System.out.println(joinSqlIn(param));

		System.out.println(inArray("aa2", param));

		String[] param3 = sortAsc(param);
		String[] param4 = sortDesc(param3);

		System.out.println(join(",", param3));

		System.out.println(join(",", param4));

		// 顺序
		java.util.Collections.sort(list);
		System.out.println(list);
		// 倒序
		java.util.Collections.reverse(list);
		System.out.println(list);

	}

	/**
	 * 连接字符串 List
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, List<?> arr) {
		int len = arr.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr.get(i));
			sb.append(separator);
		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, Object[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接long
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, long[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接int
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, int[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接double
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, double[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接float
	 * 
	 * @param separator
	 * @param arr
	 * @return
	 */
	public static String join(String separator, float[] arr) {
		int len = arr.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串，sql in 条件中使用
	 * 
	 * @param arr
	 * @return
	 */
	public static String joinSqlIn(String[] arr) {
		int len = arr.length;
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append("'");
			sb.append(arr[i]);
			sb.append("'");
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串，sql in 条件中使用
	 * 
	 * @param arr
	 * @return
	 */
	public static String joinSqlIn(int[] arr) {
		int len = arr.length;
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串，sql in 条件中使用
	 * 
	 * @param arr
	 * @return
	 */
	public static String joinSqlIn(long[] arr) {
		int len = arr.length;
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串，sql in 条件中使用
	 * 
	 * @param arr
	 * @return
	 */
	public static String joinSqlIn(double[] arr) {
		int len = arr.length;
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 连接字符串，sql in 条件中使用
	 * 
	 * @param arr
	 * @return
	 */
	public static String joinSqlIn(float[] arr) {
		int len = arr.length;
		String separator = ",";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(arr[i]);
			sb.append(separator);

		}
		len = sb.length();
		if (len > 0)
			sb.delete(len - separator.length(), len);
		return sb.toString();
	}

	/**
	 * 判断一个数字是否在数组中
	 * 
	 * @param n
	 * @param arr
	 * @return
	 */
	public static boolean inArray(int n, int[] arr) {
		if (arr == null)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (n == arr[i])
				return true;
		}
		return false;
	}

	/**
	 * 判断一个数字是否在数组中
	 * 
	 * @param n
	 * @param arr
	 * @return
	 */
	public static boolean inArray(long n, long[] arr) {
		if (arr == null)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (n == arr[i])
				return true;
		}
		return false;
	}

	/**
	 * 判断一个数字是否在数组中
	 * 
	 * @param n
	 * @param arr
	 * @return
	 */
	public static boolean inArray(float n, float[] arr) {
		if (arr == null)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (n == arr[i])
				return true;
		}
		return false;
	}

	/**
	 * 判断一个数字是否在数组中
	 * 
	 * @param n
	 * @param arr
	 * @return
	 */
	public static boolean inArray(double n, double[] arr) {
		if (arr == null)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (n == arr[i])
				return true;
		}
		return false;
	}

	/**
	 * 判断一个字符串是否在数组中
	 * 
	 * @param n
	 * @param arr
	 * @return
	 */
	public static boolean inArray(Object n, Object[] arr) {
		if (arr == null)
			return false;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(n))
				return true;
		}
		return false;
	}

	/**
	 * 顺序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static int[] sortAsc(int[] arr) {
		if (arr == null) {
			return arr;
		}
		int[] arr2 = new int[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		return arr2;
	}

	/**
	 * 顺序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static long[] sortAsc(long[] arr) {
		if (arr == null) {
			return arr;
		}
		long[] arr2 = new long[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		return arr2;
	}

	/**
	 * 顺序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static float[] sortAsc(float[] arr) {
		if (arr == null) {
			return arr;
		}
		float[] arr2 = new float[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		return arr2;
	}

	/**
	 * 顺序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static double[] sortAsc(double[] arr) {
		if (arr == null) {
			return arr;
		}
		double[] arr2 = new double[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		return arr2;
	}

	/**
	 * 顺序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static String[] sortAsc(String[] arr) {
		if (arr == null) {
			return arr;
		}
		String[] arr2 = new String[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		return arr2;
	}

	/**
	 * 倒序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static int[] sortDesc(int[] arr) {
		if (arr == null) {
			return arr;
		}
		int[] arr2 = new int[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		int[] arr3 = new int[arr.length];
		for (int i = 0; i < arr2.length; i++) {
			arr3[i] = arr2[arr2.length - i - 1];
		}
		return arr3;
	}

	/**
	 * 倒序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static long[] sortDesc(long[] arr) {
		if (arr == null) {
			return arr;
		}
		long[] arr2 = new long[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		long[] arr3 = new long[arr.length];
		for (int i = 0; i < arr2.length; i++) {
			arr3[i] = arr2[arr2.length - i - 1];
		}
		return arr3;
	}

	/**
	 * 倒序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static float[] sortDesc(float[] arr) {
		if (arr == null) {
			return arr;
		}
		float[] arr2 = new float[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		float[] arr3 = new float[arr.length];
		for (int i = 0; i < arr2.length; i++) {
			arr3[i] = arr2[arr2.length - i - 1];
		}
		return arr3;
	}

	/**
	 * 倒序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static double[] sortDesc(double[] arr) {
		if (arr == null) {
			return arr;
		}
		double[] arr2 = new double[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		double[] arr3 = new double[arr.length];
		for (int i = 0; i < arr2.length; i++) {
			arr3[i] = arr2[arr2.length - i - 1];
		}
		return arr3;
	}

	/**
	 * 倒序排序
	 * 
	 * @param arr
	 * @return
	 */
	public static String[] sortDesc(String[] arr) {
		if (arr == null) {
			return arr;
		}
		String[] arr2 = new String[arr.length];
		System.arraycopy(arr, 0, arr2, 0, arr.length);
		Arrays.sort(arr2);
		String[] arr3 = new String[arr.length];
		for (int i = 0; i < arr2.length; i++) {
			arr3[i] = arr2[arr2.length - i - 1];
		}
		return arr3;
	}
	/*
	 * // 排序 public static int[] sortAcs(int[] array) { if (array == null) {
	 * return null; } int[] srcdata = (int[]) array.clone(); int size =
	 * array.length; for (int i = 0; i < size; i++) for (int j = i; j <
	 * size; j++) { if (srcdata[i] > srcdata[j]) { swap(srcdata, i, j); } }
	 * return srcdata; }
	 * 
	 * public static String[] sortAcs(String[] array) { int array2[] = new
	 * int[array.length]; for (int i = 0; i < array.length; i++) { array2[i]
	 * = Integer.valueOf(array[i]); } array2 = ArrayUtil.sortAcs(array2);
	 * for (int i = 0; i < array.length; i++) { array[i] =
	 * String.valueOf(array2[i]); } return array; }
	 * 
	 * public static void swap(int[] srcdata, int src, int dest) { int temp
	 * = srcdata[src]; srcdata[src] = srcdata[dest]; srcdata[dest] = temp; }
	 */

	/**
	 * Resizes an array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] resize(T[] buffer, int newSize) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		T[] temp = (T[]) Array.newInstance(componentType, newSize);
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>String</code> array.
	 */
	public static String[] resize(String[] buffer, int newSize) {
		String[] temp = new String[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>byte</code> array.
	 */
	public static byte[] resize(byte[] buffer, int newSize) {
		byte[] temp = new byte[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>char</code> array.
	 */
	public static char[] resize(char[] buffer, int newSize) {
		char[] temp = new char[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>short</code> array.
	 */
	public static short[] resize(short[] buffer, int newSize) {
		short[] temp = new short[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>int</code> array.
	 */
	public static int[] resize(int[] buffer, int newSize) {
		int[] temp = new int[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>long</code> array.
	 */
	public static long[] resize(long[] buffer, int newSize) {
		long[] temp = new long[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>float</code> array.
	 */
	public static float[] resize(float[] buffer, int newSize) {
		float[] temp = new float[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>double</code> array.
	 */
	public static double[] resize(double[] buffer, int newSize) {
		double[] temp = new double[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	/**
	 * Resizes a <code>boolean</code> array.
	 */
	public static boolean[] resize(boolean[] buffer, int newSize) {
		boolean[] temp = new boolean[newSize];
		System.arraycopy(buffer, 0, temp, 0, buffer.length >= newSize ? newSize : buffer.length);
		return temp;
	}

	// ----------------------------------------------------------------
	// append

	/**
	 * Appends an element to array.
	 */
	public static <T> T[] append(T[] buffer, T newElement) {
		T[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>String</code> array.
	 */
	public static String[] append(String[] buffer, String newElement) {
		String[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>byte</code> array.
	 */
	public static byte[] append(byte[] buffer, byte newElement) {
		byte[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>char</code> array.
	 */
	public static char[] append(char[] buffer, char newElement) {
		char[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>short</code> array.
	 */
	public static short[] append(short[] buffer, short newElement) {
		short[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>int</code> array.
	 */
	public static int[] append(int[] buffer, int newElement) {
		int[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>long</code> array.
	 */
	public static long[] append(long[] buffer, long newElement) {
		long[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>float</code> array.
	 */
	public static float[] append(float[] buffer, float newElement) {
		float[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>double</code> array.
	 */
	public static double[] append(double[] buffer, double newElement) {
		double[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	/**
	 * Appends an element to <code>boolean</code> array.
	 */
	public static boolean[] append(boolean[] buffer, boolean newElement) {
		boolean[] t = resize(buffer, buffer.length + 1);
		t[buffer.length] = newElement;
		return t;
	}

	// ----------------------------------------------------------------
	// remove

	/**
	 * Removes sub-array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(T[] buffer, int offset, int length) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		return remove(buffer, offset, length, componentType);
	}

	/**
	 * Removes sub-array.
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T[] remove(T[] buffer, int offset, int length, Class<T> componentType) {
		int len2 = buffer.length - length;
		T[] temp = (T[]) Array.newInstance(componentType, len2);
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>String</code> array.
	 */
	public static String[] remove(String[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		String[] temp = new String[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>byte</code> array.
	 */
	public static byte[] remove(byte[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		byte[] temp = new byte[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>char</code> array.
	 */
	public static char[] remove(char[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		char[] temp = new char[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>short</code> array.
	 */
	public static short[] remove(short[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		short[] temp = new short[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>int</code> array.
	 */
	public static int[] remove(int[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		int[] temp = new int[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>long</code> array.
	 */
	public static long[] remove(long[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		long[] temp = new long[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>float</code> array.
	 */
	public static float[] remove(float[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		float[] temp = new float[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>double</code> array.
	 */
	public static double[] remove(double[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		double[] temp = new double[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	/**
	 * Removes sub-array from <code>boolean</code> array.
	 */
	public static boolean[] remove(boolean[] buffer, int offset, int length) {
		int len2 = buffer.length - length;
		boolean[] temp = new boolean[len2];
		System.arraycopy(buffer, 0, temp, 0, offset);
		System.arraycopy(buffer, offset + length, temp, offset, len2 - offset);
		return temp;
	}

	// ----------------------------------------------------------------
	// subarray

	/**
	 * Returns subarray.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] subarray(T[] buffer, int offset, int length) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		return subarray(buffer, offset, length, componentType);
	}

	/**
	 * Returns subarray.
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T[] subarray(T[] buffer, int offset, int length, Class<T> componentType) {
		T[] temp = (T[]) Array.newInstance(componentType, length);
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static String[] subarray(String[] buffer, int offset, int length) {
		String[] temp = new String[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static byte[] subarray(byte[] buffer, int offset, int length) {
		byte[] temp = new byte[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static char[] subarray(char[] buffer, int offset, int length) {
		char[] temp = new char[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static short[] subarray(short[] buffer, int offset, int length) {
		short[] temp = new short[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static int[] subarray(int[] buffer, int offset, int length) {
		int[] temp = new int[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static long[] subarray(long[] buffer, int offset, int length) {
		long[] temp = new long[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static float[] subarray(float[] buffer, int offset, int length) {
		float[] temp = new float[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static double[] subarray(double[] buffer, int offset, int length) {
		double[] temp = new double[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	/**
	 * Returns subarray.
	 */
	public static boolean[] subarray(boolean[] buffer, int offset, int length) {
		boolean[] temp = new boolean[length];
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

	// ----------------------------------------------------------------
	// insert

	/**
	 * Inserts one array into another array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] dest, T[] src, int offset) {
		Class<T> componentType = (Class<T>) dest.getClass().getComponentType();
		return insert(dest, src, offset, componentType);
	}

	/**
	 * Inserts one element into an array.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(T[] dest, T src, int offset) {
		Class<T> componentType = (Class<T>) dest.getClass().getComponentType();
		return insert(dest, src, offset, componentType);
	}

	/**
	 * Inserts one array into another array.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T[] insert(T[] dest, T[] src, int offset, Class componentType) {
		T[] temp = (T[]) Array.newInstance(componentType, dest.length + src.length);
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another array.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T[] insert(T[] dest, T src, int offset, Class componentType) {
		T[] temp = (T[]) Array.newInstance(componentType, dest.length + 1);
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>String</code> array.
	 */
	public static String[] insert(String[] dest, String[] src, int offset) {
		String[] temp = new String[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>String</code> array.
	 */
	public static String[] insert(String[] dest, String src, int offset) {
		String[] temp = new String[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>byte</code> array.
	 */
	public static byte[] insert(byte[] dest, byte[] src, int offset) {
		byte[] temp = new byte[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>byte</code> array.
	 */
	public static byte[] insert(byte[] dest, byte src, int offset) {
		byte[] temp = new byte[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>char</code> array.
	 */
	public static char[] insert(char[] dest, char[] src, int offset) {
		char[] temp = new char[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>char</code> array.
	 */
	public static char[] insert(char[] dest, char src, int offset) {
		char[] temp = new char[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>short</code> array.
	 */
	public static short[] insert(short[] dest, short[] src, int offset) {
		short[] temp = new short[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>short</code> array.
	 */
	public static short[] insert(short[] dest, short src, int offset) {
		short[] temp = new short[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>int</code> array.
	 */
	public static int[] insert(int[] dest, int[] src, int offset) {
		int[] temp = new int[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>int</code> array.
	 */
	public static int[] insert(int[] dest, int src, int offset) {
		int[] temp = new int[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>long</code> array.
	 */
	public static long[] insert(long[] dest, long[] src, int offset) {
		long[] temp = new long[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>long</code> array.
	 */
	public static long[] insert(long[] dest, long src, int offset) {
		long[] temp = new long[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>float</code> array.
	 */
	public static float[] insert(float[] dest, float[] src, int offset) {
		float[] temp = new float[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>float</code> array.
	 */
	public static float[] insert(float[] dest, float src, int offset) {
		float[] temp = new float[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>double</code> array.
	 */
	public static double[] insert(double[] dest, double[] src, int offset) {
		double[] temp = new double[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>double</code> array.
	 */
	public static double[] insert(double[] dest, double src, int offset) {
		double[] temp = new double[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one array into another <code>boolean</code> array.
	 */
	public static boolean[] insert(boolean[] dest, boolean[] src, int offset) {
		boolean[] temp = new boolean[dest.length + src.length];
		System.arraycopy(dest, 0, temp, 0, offset);
		System.arraycopy(src, 0, temp, offset, src.length);
		System.arraycopy(dest, offset, temp, src.length + offset, dest.length - offset);
		return temp;
	}

	/**
	 * Inserts one element into another <code>boolean</code> array.
	 */
	public static boolean[] insert(boolean[] dest, boolean src, int offset) {
		boolean[] temp = new boolean[dest.length + 1];
		System.arraycopy(dest, 0, temp, 0, offset);
		temp[offset] = src;
		System.arraycopy(dest, offset, temp, offset + 1, dest.length - offset);
		return temp;
	}

}
