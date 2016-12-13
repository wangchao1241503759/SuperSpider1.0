/**
 * @Title: DateUtils.java
 * @Package org.zxgs.jweb.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 马超
 * @date 2012-8-10 上午8:59:17
 * @version V1.0
*/
package cn.com.infcn.superspider.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Title: DateUtils.java
 * @Package org.zxgs.jweb.utils
 * @Description: 时间通用类
 * @author WChao
 * @date 2012-8-10 上午8:59:17
 * @version V1.0
 */
public class DateUtils {
	
	/**
	 * 将指定日期格式化为日期对象date;
	 * @param time
	 * @param formateStr
	 * @return
	 */
	public static Date getFormateDate(String time,String formateStr)
	{
		Date date = null;
		SimpleDateFormat ft = new SimpleDateFormat(formateStr);
		try {
			date = ft.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 
	 *@Description: TODO(两个日期的差值)
	 *@author 王超 2012-11-21 下午5:13:41 
	 *@param time1
	 *@param time2
	 *@return
	 */
	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
	
	/**
	 * 
	 *@Description: 取得系统当前时间 String 自定义格式 yyyy年MM月dd日
	 *@author 马超
	 *@date 2012-8-10 上午8:59:52
	 *@param str   yyyy年MM月dd日
	 *@return
	 */
	public static String getCurrentDate(String str) {
		SimpleDateFormat df=new SimpleDateFormat(str); 
		Calendar rightNow = Calendar.getInstance();
		return df.format(rightNow.getTime());
	}
	/**
	 * 
	 *@Description: 获取当前时间 格式为yyyy-MM-dd HH:mm:ss
	 *@author 王超   2012-8-25 上午10:00:53
	 *@return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Calendar rightNow = Calendar.getInstance();
		return df.format(rightNow.getTime());
	}
	public static Date getCurrentDateDate()
	 {

	  Calendar rightNow = Calendar.getInstance();
	  
	  return rightNow.getTime();
	 }
	/**r
	 * date转str
	 * @param date
	 * @param str 格式 "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String  getdatestr(Date date,String str)
	 {
		SimpleDateFormat df=new SimpleDateFormat(str);  
		return df.format(date);
	 }
	/**
	 * 
	 * @author      : caojian  时间：2012-4-25 上午11:30:06
	 * @description :  取得系统当前时间前n个月的相对应的一天
	 * @modify      : 修改人 时间 简单描述 
	 * @param n
	 * @return  String  
	 */
	@SuppressWarnings("static-access")
	public static String getNMonthBeforeCurrentDay(int n,String str) {
		Calendar c = Calendar.getInstance();
		c.add(c.MONTH, -n);
		SimpleDateFormat df=new SimpleDateFormat(str); 
		return df.format(c.getTime());

	}
 
	/**
	 * 
	 * @author      : caojian  时间：2012-4-25 上午11:30:31
	 * @description : 取得系统当前时间后n个月的相对应的一天
	 * @modify      : 修改人 时间 简单描述 
	 * @param n
	 * @return String  
	 */
	@SuppressWarnings("static-access")
	public static String getNMonthAfterCurrentDay(int n,String str) {
		Calendar c = Calendar.getInstance();
		c.add(c.MONTH, n);
		SimpleDateFormat df=new SimpleDateFormat(str); 
		return df.format(c.getTime());

	}
 
	/**
	 * 
	 * @author      : caojian  时间：2012-4-25 上午11:30:58
	 * @description : 取得系统当前时间前n天
	 * @modify      : 修改人 时间 简单描述 
	 * @param n
	 * @return String yyyy-mm-dd
	 */
	@SuppressWarnings("static-access")
	public static String getNDayBeforeCurrentDate(int n,String str) {
		Calendar c = Calendar.getInstance();
		c.add(c.DAY_OF_MONTH, -n);
		SimpleDateFormat df=new SimpleDateFormat(str); 
		return df.format(c.getTime());
	}
 
	/**
	 * 
	 * @author      : caojian  时间：2012-4-25 上午11:31:19
	 * @description : 取得系统当前时间后n天
	 * @modify      : 修改人 时间 简单描述 
	 * @param n
	 * @return String yyyy-MM-dd
	 */
	@SuppressWarnings("static-access")
	public static String getNDayAfterCurrentDate(int n,String str) {
		Calendar c = Calendar.getInstance();
		c.add(c.DAY_OF_MONTH, n);
		SimpleDateFormat df=new SimpleDateFormat(str); 
		return df.format(c.getTime());
	}
	
	/**

	*@param date是为则默认今天日期、可自行设置“2013-06-03”格式的日期

	*@return  返回1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六

	*/

	public static int getDayofweek(String date){
	  Calendar cal = Calendar.getInstance();
	//   cal.setTime(new Date(System.currentTimeMillis()));
	  if (date.equals("")) {
	   cal.setTime(new Date(System.currentTimeMillis()));
	  }else {
	   cal.setTime(new Date(getDateByStr2(date).getTime()));
	  }
	   return cal.get(Calendar.DAY_OF_WEEK);
	 }



	public static Date getDateByStr2(String dd)
	 {

	  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	  Date date;
	  try {
	   date = sd.parse(dd);
	  } catch (ParseException e) {
	   date = null;
	   e.printStackTrace();
	  }
	  return date;
	 }
	public static Date getDateByStr3(String dd)
	 {

	  SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	  Date date;
	  try {
	   date = sd.parse(dd);
	  } catch (ParseException e) {
	   date = null;
	   e.printStackTrace();
	  }
	  return date;
	 }
	/**
	 * 
	 *@Description: TODO(根据日期返回星期几)
	 *@author 马超
	 *@date 2013-10-23 下午3:09:15
	 *@param pTime
	 *@return
	 *@throws Throwable
	 */
	public static int dayForWeek(String pTime) throws Throwable {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
	    Calendar c = Calendar.getInstance();
	    c.setTime(format.parse(pTime));
	    int dayForWeek = 0;
	    if(c.get(Calendar.DAY_OF_WEEK) == 1){
	     dayForWeek = 7;
	    }else{
	     dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
	    }
	    return dayForWeek;
	} 
	/**
	 * 获取当前日期前n天的日期
	 * @param n
	 * @param str
	 * @return
	 */
	public static String getNDayBeforeCurrent(int n,String str) {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  Date inputDate=null;
		try {
			inputDate = dateFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(inputDate);
		  
		  int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		  cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-n );
		  
		  return dateFormat.format(cal.getTime());
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println(DateUtils.getDayofweek("20131005"));
		Calendar cal = Calendar.getInstance();
		//   cal.setTime(new Date(System.currentTimeMillis()));
		 
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		   cal.setTime(new Date(System.currentTimeMillis()));
		   System.out.print(cal.get(Calendar.DAY_OF_WEEK));
		  
	}

}
