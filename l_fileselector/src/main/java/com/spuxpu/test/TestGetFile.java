package com.spuxpu.test;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.spuxpu.service.Item;

/**
 * 由于测试已经完成，故核心测试方法被删除，
 * 
 * @author shengjunhao
 * 
 */
public class TestGetFile {

	private static String TAG = "getfile";
	private int filesCount = 0;
	private int curPosition = 0;
	private List<Item> listFile = new ArrayList<Item>();

	/**
	 * 测试执行时间及其执行结果
	 */
	public void getAllOfficeFiles() {
		long time1 = System.currentTimeMillis();

		long time2 = System.currentTimeMillis();
		long time = time2 - time1;
		Log.i(TAG + "count", filesCount + "**allfilecount" + time);
		showResult();
	}

	/**
	 * 展示数据结果
	 */
	private void showResult() {

		for (Item item : listFile) {
			Log.i(TAG, item.getName());
		}
	}
}
