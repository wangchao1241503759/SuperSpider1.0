package cn.com.infcn.ade.system.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 与当前时间的时间差
	 * 
	 * @param createtime
	 * @return
	 */
	public static long getRuntime(String createtime) {

		ParsePosition pos = new ParsePosition(0);
		Date d1 = (Date) sd.parse(createtime, pos);
		long time = new Date().getTime() - d1.getTime();

		return time / 1000;
	}

	public static String formatSecond(String second) {

		String str = "0秒";
		if (second != null) {
			Double s = Double.valueOf(second);
			String format;
			Object[] array;
			Integer hours = (int) (s / (60 * 60));
			Integer minutes = (int) (s / 60 - hours * 60);
			Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
			if (hours > 0) {
				format = "%1$,d时%2$,d分%3$,d秒";
				array = new Object[] { hours, minutes, seconds };
			} else if (minutes > 0) {
				format = "%1$,d分%2$,d秒";
				array = new Object[] { minutes, seconds };
			} else {
				format = "%1$,d秒";
				array = new Object[] { seconds };
			}
			str = String.format(format, array);
		}

		return str;

	}

	/**
	 * 获取几小时以后的时间
	 * 
	 * @param date
	 * @param num
	 */
	public static String getNextHourTime(Date date, int num) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, num);
		return sd.format(cal.getTime());

	}

	/**
	 * 获取几天后的时间
	 * 
	 * @return
	 */
	public static String getNextDay(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, num);
		return sd.format(c.getTime());
	}

	/**
	 * 获取几分钟后的时间
	 * 
	 * @return
	 */
	public static String getNextMinute(Date date, int num) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, num);
		return sd.format(c.getTime());
	}

}