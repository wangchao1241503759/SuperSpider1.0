package cn.com.infcn.superspider.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {

  public static String dateFormat(Date date, String format) {
    try {
      if (date != null) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Date dateValidation(String dateStr) {

    if (StringUtils.isEmpty(dateStr)) {
      return null;
    } else {
      try {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateStr, new ParsePosition(0));
        return date;
      } catch (Exception e) {
        return null;
      }
    }

  }

  public static Date dateValidation(String dateStr, String format) {

    try {
      if (StringUtils.isEmpty(dateStr)) {
        return null;
      } else {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(dateStr, new ParsePosition(0));
        return date;
      }
    } catch (Exception e) {
      return null;
    }

  }

  /**
   * 计算两个日期之间相差的天数
   * 
   * @param smdate
   *          较小的时间
   * @param bdate
   *          较大的时间
   * @return 相差天数
   * @throws ParseException
   */
  public static int daysBetween(Date smdate, Date bdate) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      smdate = sdf.parse(sdf.format(smdate));
      bdate = sdf.parse(sdf.format(bdate));
      Calendar cal = Calendar.getInstance();
      cal.setTime(smdate);
      long time1 = cal.getTimeInMillis();
      cal.setTime(bdate);
      long time2 = cal.getTimeInMillis();
      long between_days = (time2 - time1) / (1000 * 3600 * 24);
      return Integer.parseInt(String.valueOf(between_days));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return -1;
  }

  /**
   * 字符串的日期格式的计算
   */
  public static int daysBetween(String smdate, String bdate) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Calendar cal = Calendar.getInstance();
      cal.setTime(sdf.parse(smdate));
      long time1 = cal.getTimeInMillis();
      cal.setTime(sdf.parse(bdate));
      long time2 = cal.getTimeInMillis();
      long between_days = (time2 - time1) / (1000 * 3600 * 24);

      return Integer.parseInt(String.valueOf(between_days));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return -1;
  }

  /**
   * 获取指定日期所在月的天数
   * 
   * @param sDate
   * @return
   */
  public static int getLastDayOfMonth(Date sDate) {
    Calendar cDay = Calendar.getInstance();
    cDay.setTime(sDate);
    final int lastDay = cDay.getActualMaximum(Calendar.DAY_OF_MONTH);
    return lastDay;
  }

  /**
   * 获取本周的第一天日期
   * 
   * @return
   */
  public static Date getWeekFirstDay() {
    Calendar calendar = Calendar.getInstance();
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
      calendar.add(Calendar.DATE, -1);
    }
    return calendar.getTime();
  }

  /**
   * 获取本月的第一天日期
   * 
   * @return
   */
  public static Date getMonthFirstDay() {
    Calendar calendar = Calendar.getInstance();// 获取当前日期
    calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号,当前日期既为本月第一天
    return calendar.getTime();
  }

  /**
   * 获取前12个月的第一天，比如：今天2014-12-12日，那么返回2014-01-01
   * 
   * @return
   */
  public static Date getLastYearMonthFirstDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, -1);
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    return calendar.getTime();
  }

  public static String getMonthName(int i) {
    if (i == 1) {
      return "一月";
    } else if (i == 2) {
      return "二月";
    } else if (i == 3) {
      return "三月";
    } else if (i == 4) {
      return "四月";
    } else if (i == 5) {
      return "五月";
    } else if (i == 6) {
      return "六月";
    } else if (i == 7) {
      return "七月";
    } else if (i == 8) {
      return "八月";
    } else if (i == 9) {
      return "九月";
    } else if (i == 10) {
      return "十月";
    } else if (i == 11) {
      return "十一月";
    } else if (i == 12) {
      return "十二月";
    } else {
      return "未知";
    }
  }

  /**
   * 把字符串的日期按参数格式转换为日期类型的数据
   * 
   * @param strDate
   * @param param
   * @return
   */
  public static Date formatDate(String strDate, String param) {
    String pattern = "yyyy-MM-dd";
    if (param != null)
      pattern = param;
    try {
      if (strDate == null || strDate.equals(""))
        return null;
      SimpleDateFormat dd = new SimpleDateFormat(pattern);
      return dd.parse(strDate);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * 获取格式化后的日期字符串
   * 
   * @param date
   * @return
   */
  public static String getStrDate(Date date) {
    if (date == null)
      return null;
    try {
      SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
      return dd.format(date);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
  
	/**
	 * 两个日期相差多少秒
	 * 
	 * @author lihf
	 * @date 2016年9月22日 下午1:19:08
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getTimeDelta(Date date1, Date date2)
	{
		long timeDelta = (date1.getTime() - date2.getTime()) / 1000;// 单位是秒
		int secondsDelta = timeDelta > 0 ? (int) timeDelta : (int) Math.abs(timeDelta);
		return secondsDelta;
	} 
	
    /**
     * 两个日期相差多少秒
     * @author lihf
     * @date 2016年9月22日	下午1:21:47
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static int getTimeDelta(String dateStr1,String dateStr2){  
    	String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";  
        Date date1=parseDateByPattern(dateStr1, yyyyMMddHHmmss);  
        Date date2=parseDateByPattern(dateStr2, yyyyMMddHHmmss);  
        return getTimeDelta(date1, date2);  
    } 
    
    public static Date parseDateByPattern(String dateStr,String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        try {  
            return sdf.parse(dateStr);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  

  /**
   * @param args
   * @throws ParseException
   */

  public static void main(String[] args) throws ParseException { // TODO
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Date d1 = sdf.parse("2012-09-08 10:10:10");
    // Date d2 = sdf.parse("2012-09-09 23:59:59");
    // System.out.println(daysBetween(d1, d2));
    //
    // System.out.println(daysBetween("2012-09-08 23:59:59", "2012-09-09
    // 00:00:00"));
    String hour = dateFormat(new Date(), "yyyyMMddHHmmss");
    System.out.println(hour);
    System.out.println(dateValidation("2016-12"));
  }

}