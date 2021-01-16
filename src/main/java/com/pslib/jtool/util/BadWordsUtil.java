package com.pslib.jtool.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 敏感词判断和过滤
 * 
 *
 */
public class BadWordsUtil {
	protected final static Logger logger = LoggerFactory.getLogger(BadWordsUtil.class);
	/**
	 * 敏感词文件，如果不指定，则读取类根目录下的badwords.txt
	 */
	public static String filename = null;
	/**
	 * 文件大小限制
	 */
	public static long filesizeLimit = 20 * 1024 * 1024;

	private static long lastModified = 0;
	private static File file;
	private static Pattern badwords = null;
	private static Pattern replace_badwords = null;
	private static String filter1_rule = "\"`~!@#$%^&*()=':;,\\\\\\[\\]\\.<>/?~￥%…（）—+|{}【】‘”“’。，、？《》 \t";
	private static Pattern filter1 = Pattern.compile("[" + filter1_rule + "]+");

	/**
	 * 初始化
	 */
	public static void init(String _filename) {
		filename = _filename;
		file = getFile();
		reload(true);
		fileChecker();
	}

	public static boolean fileCheckerShutdown = false;

	public static void fileChecker() {
		Thread daemon = new Thread("BADWORDS-FILE-CHECK") {
			@Override
			public void run() {
				synchronized (this) {
					while (!fileCheckerShutdown) {
						try {
							wait(30 * 1000);
							/// logger.info("file checker");
							reload(false);
						} catch (Exception ex) {
							logger.error("", ex);
						}
					}
				}
			}
		};
		daemon.setDaemon(true);
		daemon.start();
	}

	public static File getFile() {
		return new File(filename);
	}

	/**
	 * 加载数据
	 * 
	 * @param force
	 */
	public static void reload(boolean force) {
		if (force || lastModified != file.lastModified()) {
			try {
				logger.info("加载敏感词数据:" + file.getAbsolutePath());
				logger.info("文件大小:" + StringUtil.getFormatSize(file.length()));
				if (!file.exists()) {
					logger.error("文件不存在 ");
					return;
				}
				if (!file.canRead()) {
					logger.error("文件不可读 ");
					return;
				}

				if (file.length() > filesizeLimit) {
					logger.error("文件大小超过限制:limit(" + StringUtil.getFormatSize(file.length()) + ")>file(" + StringUtil.getFormatSize(filesizeLimit) + ")");
					return;
				}
				String words = FileUtil.fileRead(file);

				words = filterStr(words);

				String[] wordarr = words.split("\n");
				int len;
				StringBuffer tmp = new StringBuffer("(");
				StringBuffer tmp2 = new StringBuffer("(");
				for (int idx = 0; idx < wordarr.length; idx++) {
					wordarr[idx] = wordarr[idx].trim();
					len = wordarr[idx].length();
					if (len == 0) {
						continue;
					}
					tmp.append(wordarr[idx]).append("|");
					if (len > 1) {
						for (int i = 0; i < len; i++) {
							tmp2.append(wordarr[idx].substring(i, i + 1));
							tmp2.append("[" + filter1_rule + "]*");
						}
						tmp2.append("|");
					}

				}
				tmp.setLength(tmp.length() - 1);
				tmp.append(")");
				badwords = Pattern.compile(tmp.toString());

				tmp2.setLength(tmp2.length() - 1);
				tmp2.append(")");
				replace_badwords = Pattern.compile(tmp2.toString());

				lastModified = file.lastModified();

				logger.info("敏感词数据加载完成");
			} catch (Exception ex) {
				logger.error("", ex);
			}
		}
	}

	/**
	 * 过滤字符中的标点符号
	 * 
	 * @param str
	 * @return
	 */
	public static String filterStr(String str) {
		str = filter1.matcher(str).replaceAll("");
		return str;
	}

	/**
	 * 判断是否包含敏感词
	 * 
	 * @param str
	 * @return
	 */
	public static boolean check(String str) {
		if (badwords == null || str == null || str.length() == 0) {
			return true;
		}
		str = filterStr(str);
		return badwords.matcher(str).find();
	}

	/**
	 * 替换敏感词为指定字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String replace(String str, String replace) {
		if (badwords == null || str == null || str.length() == 0) {
			return str;
		}
		str = badwords.matcher(str).replaceAll(replace);
		str = replace_badwords.matcher(str).replaceAll(replace);
		return str;
	}

	/**
	 * 替换敏感词为**
	 * 
	 * @param str
	 * @return
	 */
	public static String replace(String str) {
		return replace(str, "**");
	}

	/**
	 * 重复词过滤
	 * 
	 * @param str
	 * @return
	 */
	public static void filterWordsFile(String filename) {
		File file = new File(filename);
		String words = FileUtil.fileRead(file);

		words = filterStr(words);

		String[] wordarr = words.split("\n");
		List<String> wordList = new ArrayList<String>();
		for (int i = 0; i < wordarr.length; i++) {
			wordarr[i] = wordarr[i].trim();
			if (wordarr[i].length() == 0) {
				continue;
			}
			if (!wordList.contains(wordarr[i])) {
				wordList.add(wordarr[i]);
			}
		}
		// System.out.println(ArrayUtil.join("\r\n", wordList));
		FileUtil.fileWrite(file, ArrayUtil.join("\r\n", wordList));
	}

	public static void main(String[] args) {

	}

}
