package com.spuxpu.service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.annotation.SuppressLint;

/**
 * 获取所有的文件系统
 * 
 * @author shengjunhao
 * 
 */
@SuppressLint("SdCardPath")
public class GetAllFiles {

	private static String TAG = "getfile";
	private int filesCount = 0;
	private int curPosition = 0;
	private List<Item> listFile = new ArrayList<Item>();

	public static final int FILE_DOC = 0;
	public static final int FILE_DOCX = 1;
	public static final int FILE_PDF = 2;
	public static final int FILE_PPT = 3;
	public static final int FILE_PPTX = 4;
	public static final int FILE_XLS = 5;
	public static final int FILE_XLSX = 6;
	public static final int FILE_TXT = 7;
	public static final int FILE_FOLDER = -1;
	public static final int FILE_BACK = -2;
	public static final int FILE_UNKNOW = 8;

	/**
	 * 需要过滤的文件夹
	 */
	public static final String[] fileterFile = new String[] { ".doc", ".docx",
			".pdf", ".ppt", ".pptx", ".xls", ".xlsx", ".txt" };

	/**
	 * 异步获取本地数据
	 * 
	 * @param listenser
	 */
	public void getAllOfficeFiles(final GetFileListListenser listenser) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				File currentDir = new File("/sdcard/");
				showDir(currentDir);
				sortListUsDefault();
				listenser.getAllFile(listFile);
			}
		}).start();
	}

	/**
	 * 获取到文档目录
	 */
	public void getFolder(GetFileListListenser listenser) {

		getFolder("/sdcard", listenser);
	}

	public void getFolder(String path, GetFileListListenser listenser) {
		path = path != null ? path : "/sdcard";
		File currentDir = new File(path);
		File[] files = currentDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				Pattern pattern = Pattern.compile("^\\.\\S+$");
				Matcher matcher = pattern.matcher(arg0.getName());
				return !matcher.matches();
			}
		});
		if (!path.equals("/sdcard")) {
			// 在索引中找不到该文档，属于文件夹
			listFile.add(new Item("返回上级目录", 0, 0, currentDir.getParentFile()
					.getPath(), FILE_BACK));
		}
		for (File file : files) {
			if (file.isDirectory()) {
				// 在索引中找不到该文档，属于文件夹
				listFile.add(new Item(file.getName(), file.length(), file
						.lastModified(), file.getPath(), FILE_FOLDER));
			} else {
				getTheWantedFileFolder(file);
			}
		}
		sortListUsDefault();
		listenser.getAllFile(listFile);
	}

	/**
	 * 递归获取到所有的文件
	 * 
	 * @param dir
	 */
	private void showDir(File dir) {
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				Pattern pattern = Pattern.compile("^\\.\\S+$");
				Matcher matcher = pattern.matcher(arg0.getName());
				return !matcher.matches();
			}
		});

		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					showDir(file);
				} else {
					filesCount++;
					getTheWantedFile(file);
					// 在这里开始过滤文件，判断是否是需要的文件
				}
			}
		}
	}

	/**
	 * 获取到想要的文件，
	 * 
	 */
	private void getTheWantedFile(File file) {

		// 文件名后缀在当前的位置
		curPosition = checkEndName(file.getName());
		if (curPosition > 0) {
			// 在索引中能找到该文档
			listFile.add(new Item(file.getName(), file.length(), file
					.lastModified(), file.getPath(), curPosition));
		}
	}

	/**
	 * 在浏览文档目录时候使用的方法
	 * 
	 */
	private void getTheWantedFileFolder(File file) {

		// 文件名后缀在当前的位置
		curPosition = checkEndName(file.getName());
		if (curPosition > 0) {
			// 在索引中能找到该文档
			listFile.add(new Item(file.getName(), file.length(), file
					.lastModified(), file.getPath(), curPosition));
		} else {
			listFile.add(new Item(file.getName(), file.length(), file
					.lastModified(), file.getPath(), FILE_UNKNOW));
		}
	}

	/**
	 * 检测是否含有固定
	 * 
	 * @param name
	 * @return
	 */
	private int checkEndName(String name) {

		for (int i = 0; i < fileterFile.length; i++) {
			if (name.endsWith(fileterFile[i])) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 默认排序
	 */
	private void sortListUsDefault() {
		Collections.sort(listFile, new Comparator<Item>() {
			@SuppressLint("DefaultLocale")
			public int compare(Item o1, Item o2) {
				int comparaResult = o1.getImage() - o2.getImage();
				if (comparaResult != 0) {
					return comparaResult;
				} else {
 
					return toPinYinString(o1.getName().toLowerCase())
							.compareTo(
									toPinYinString(o2.getName()).toLowerCase());
				}
			}
		});
	}

	/**
	 * 将中文转换成为拼音
	 * 
	 * @param str
	 * @return
	 */
	private String toPinYinString(String str) {
		Pattern pattern = Pattern.compile("	^[\u4e00-\u9fa5]\\S+$");
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		String[] arr = null;
		for (int i = 0; i < str.length(); i++) {
			arr = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
			if (arr != null && arr.length > 0) {
				for (String string : arr) {
					sb.append(string);
				}
			}
		}

		return sb.toString();
	}

	public interface GetFileListListenser {
		public void getAllFile(List<Item> listFile);
	}

}
