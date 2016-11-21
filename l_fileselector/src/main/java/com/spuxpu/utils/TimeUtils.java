package com.spuxpu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.ParseException;

public class TimeUtils {

	public TimeUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 时间数据类型的转换
	 * 
	 * @param millSec
	 * @return
	 */
	public static String getTimebyLong(long millSec) {
		String dateFormat = " HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	/**
	 * 时间数据类型的转换
	 * 
	 * @param millSec
	 * @return
	 */
	public static String getTimebyLong(long millSec, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	/**
	 * 时间数据类型的转换
	 * 
	 * @param millSec
	 * @return
	 */
	public static String getTimeDetailbyLong(long millSec) {
		String dateFormat = "yy/MM/dd HH:mm ";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);
	}

	/**
	 * 得到小时
	 * 
	 * @param millSec
	 * @return
	 */
	public static long getHourByLong(long millSec) {
		long hour = millSec / 3600000;
		return hour;
	}

	/**
	 * 得到分钟
	 * 
	 * @param millSec
	 * @return
	 */
	public static long getMinByLong(long millSec) {
		long hour = millSec / 3600000;
		long min = (millSec - hour * 3600000) / 60000;
		return min;
	}

	/**
	 * 得到秒
	 * 
	 * @param millSec
	 * @return
	 */
	public static long getSecondByLong(long millSec) {
		long hour = millSec / 3600000;
		long min = (millSec - hour * 3600000) / 60000;
		long second = (millSec - hour * 3600000 - min * 60000) / 1000;
		return min;
	}

	/**
	 * 对时间长度的处理
	 * 
	 * @param time
	 * @return
	 */
	public static String setTime(long time) {
		String stringtime = String.valueOf(time);
		if (stringtime.length() == 1) {
			stringtime = "0" + stringtime;
		}
		return stringtime;
	}

	public static int getWeekByTime(long time) {

		Date date = new Date(time);
		int weekday = getWeekOfDate(date);
		return weekday;
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfDate(Date date) {

		int[] weekDaysCode = { 0, 1, 2, 3, 4, 5, 6 };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysCode[intWeek];
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringWeekOfDate(long time) {
		Date date = new Date(time);
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		// String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
		// int[] weekDaysCode = { 1, "1", "2", "3", "4", "5", "6" };
		int[] weekDaysCode = { 0, 1, 2, 3, 4, 5, 6 };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	/**
	 * 通过传递进来一个时间得到星期的第一天
	 * 
	 * 在这个方法种出现的时间是那个早上8：00的时间，如果想要更换其他的时间需要进行判断
	 * 
	 * @param timeNow
	 *            随便一个long类型的时间
	 * @return 传递进来时间所在的星期的第一天
	 */
	public static long getTimeBeginOfWeek(long timeNow) {

		if (getHourOfTime(timeNow) < 8) {
			int timeCode = getWeekByTime(timeNow);
			long timeClock = timeNow % (24 * 3600 * 1000);
			long timePlus = timeCode * 24 * 3600 * 1000 + timeClock;
			long timeBegin = timeNow - timePlus + 16 * 3600000;
			return timeBegin;
		} else {
			int timeCode = getWeekByTime(timeNow);
			long timeClock = timeNow % (24 * 3600 * 1000);
			long timePlus = timeCode * 24 * 3600 * 1000 + timeClock;
			long timeBegin = timeNow - timePlus;
			return timeBegin - 8 * 3600000;
		}
	}

	/**
	 * 通过传递进来一个时间得到今天
	 * 
	 * 这个时间是从早上8点开始的,所有在后边出现了减去8个小时的情况,,不要对时间进行判定
	 * 
	 * @param timeNow
	 *            随便一个long类型的时间
	 * @return 传递进来时间所在那天的开始时间
	 */
	public static long getTimeBeginOfDay(long timeNow) {

		if (getHourOfTime(timeNow) < 8) {
			long timeBegin = timeNow - timeNow % (24 * 3600 * 1000) + 16
					* 3600000;
			return timeBegin;
		} else {
			long timeBegin = timeNow - timeNow % (24 * 3600 * 1000) - 8
					* 3600000;
			return timeBegin;
		}
	}

	private static int getHourOfTime(long timeNow) {
		String dateFormat = "HH";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(timeNow);
		String time = sdf.format(date);
		return Integer.parseInt(time);
	}

	/**
	 * 得到倒计时
	 * 
	 * @param millSec
	 * @return
	 */
	public static String getCountTime(long millSec) {

		long hour = millSec / 3600000;
		long min = (millSec - hour * 3600000) / 60000;
		long second = (millSec - hour * 3600000 - min * 60000) / 1000;
		return getDoubleTime(hour) + ":" + getDoubleTime(min) + ":"
				+ getDoubleTime(second);
	}

	/**
	 * 得到倒计时
	 * 
	 * 没有秒
	 * 
	 * @param millSec
	 * @return
	 */
	public static String getCountTimeNoSecond(long millSec) {

		long hour = millSec / 3600000;
		long min = (millSec - hour * 3600000) / 60000 + 1;
		return getDoubleTime(hour) + ":" + getDoubleTime(min);
	}

	public static String getDoubleTime(long time) {
		if (String.valueOf(time).length() == 1) {
			return "0" + String.valueOf(time);
		} else {
			return String.valueOf(time);
		}
	}

	public static int getDays(long time) {
		return (int) (time / (24 * 3600000));
	}

	/**
	 * 将字符串截取，完后拼接成+1的字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeOfCloud(String time) {
		// 字符串总长度
		int length = time.length();
		// 获取到秒的值
		int second = Integer.parseInt(time.substring(length - 1, length));
		// 返回减一秒的数据
		return time.substring(0, length - 1) + (second - 1);
	}

	public static long getLongFromStrinig(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(TimeUtils.getTimeOfCloud(time));

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
}
