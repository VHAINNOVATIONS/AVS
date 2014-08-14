package gov.va.med.lom.foundation.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public final class CalendarUtils {
	
	private static Map<Integer, String>  MONTHS = null;
	private static Map<Integer, String>  DAYS   = null;
	private static List<String> YEARS  = null;
	private static List<String> YEARS_WITH_FUTURE  = null;
	private static Map<Integer, String>  HOURS  = null;
	private static Map<Integer, String>  MINUTES= null;
	
	private static final int MAX_FUTURE_YEAR = 2011;
	
	public static String getCurrentMonth() {
		Calendar cal = GregorianCalendar.getInstance();
		
		return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}

	public static String getCurrentDay() {
		Calendar cal = GregorianCalendar.getInstance();
		
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String getCurrentYear() {
		Calendar cal = GregorianCalendar.getInstance();
		
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	public static String getMonth(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}
	
	public static String getDay(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public static String getYear(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		return String.valueOf(cal.get(Calendar.YEAR));
	}
	
	public static String getHours(Timestamp time) {
		if (time == null) {
			return null;
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		// If minutes are 59, we don't want to display time
		int minutes = cal.get(Calendar.MINUTE);
		if (minutes == 59) {
			return null;
		} else {
			return String.valueOf(cal.get(Calendar.HOUR_OF_DAY));	
		}
	}
	
	public static String getMinutes(Timestamp time) {
		if (time == null) {
			return null;
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		// If minutes are 59, we don't want to display time
		int minutes = cal.get(Calendar.MINUTE);
		if (minutes == 59) {
			return null;
		} else {
			return String.valueOf(cal.get(Calendar.MINUTE));	
		}
	}
	
	public static Timestamp getTimestamp(String month, String day, 
			String year, String hours, String minutes) {
		if (StringUtils.isBlank(month) && StringUtils.isBlank(day) 
			&& StringUtils.isBlank(year)) 
		{
			return null;
		}
		
		int iMonth = Integer.parseInt(month) - 1;
		int iDay;
		// Dates fields should be validated by now, so it's safe to default the day
		if (StringUtils.isBlank(day)) {
			iDay = 1;
		} else {
			iDay = Integer.parseInt(day);
		}
		int iYear = Integer.parseInt(year);
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.clear();
		cal.setLenient(false);
		cal.set(Calendar.MONTH, iMonth);
		cal.set(Calendar.DAY_OF_MONTH, iDay);
		cal.set(Calendar.YEAR, iYear);
		if (!StringUtils.isBlank(hours)) {
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hours));
		}
		if (!StringUtils.isBlank(minutes)) {
			cal.set(Calendar.MINUTE, Integer.parseInt(minutes));
		}
		
		if (StringUtils.isBlank(hours) && StringUtils.isBlank(minutes)) {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
		}
		
		return new Timestamp(cal.getTimeInMillis());		
	}
	
	public static Date getDate(String month, String day, String year) {
		if (StringUtils.isBlank(month) && StringUtils.isBlank(day) && StringUtils.isBlank(year)) {
			return null;
		}
		
		int iMonth = Integer.parseInt(month) - 1;
		int iDay;
		// Dates fields should be validated by now, so it's safe to default the day
		if (StringUtils.isBlank(day)) {
			iDay = 1;
		} else {
			iDay = Integer.parseInt(day);
		}
		int iYear = Integer.parseInt(year);
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.clear();
		cal.setLenient(false);
		cal.set(Calendar.MONTH, iMonth);
		cal.set(Calendar.DAY_OF_MONTH, iDay);
		cal.set(Calendar.YEAR, iYear);
		
		return new Date(cal.getTimeInMillis());
	}
	
	public static Date getDate(int month, int day, int year) {
		return getDate(month, day, year, 0, 0, 0, 0);
	}
	public static Date getDate(int month, int day, int year, int hour, 
		int minute) 
	{
		return getDate(month, day, year, hour, minute, 0, 0);
	}
	public static Date getDate(int month, int day, int year, 
		int hour, int minute, int second, int milliSecond) 
	{
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second); 
		calendar.set(Calendar.MILLISECOND, milliSecond);
		return calendar.getTime();
	}

	public static String getDateAsMDY(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}
	
   /**
    * Format date as "Sat 05/06/2006"
    * @param date
    * @return
    */
	public static String getDateAsDayMDY(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("EEE").format(date) + " " + getDateAsMDY(date);
	}
		
	
	public static String getTime(Date date) {
		if (date == null) {
			return null;
		}
		
		// If minutes are 59, we don't want to display date
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		int minutes = cal.get(Calendar.MINUTE);
		if (minutes == 59) {
			return null;
		}
		
		return new SimpleDateFormat("HH:mm").format(date);
	}
	
	public static Map getMonths() {
		if( MONTHS == null ) {
	        MONTHS = new TreeMap<Integer, String>();
	        MONTHS.put(new Integer(1), "Jan");
	        MONTHS.put(new Integer(2), "Feb");
	        MONTHS.put(new Integer(3), "Mar");
	        MONTHS.put(new Integer(4), "Apr");
	        MONTHS.put(new Integer(5), "May");
	        MONTHS.put(new Integer(6), "Jun");
	        MONTHS.put(new Integer(7), "Jul");
	        MONTHS.put(new Integer(8), "Aug");
	        MONTHS.put(new Integer(9), "Sep");
	        MONTHS.put(new Integer(10), "Oct");
	        MONTHS.put(new Integer(11), "Nov");
	        MONTHS.put(new Integer(12), "Dec");
		}
		
        return MONTHS;
	}
	
	public static Map getDays() {
		if( DAYS == null ) {
	        DAYS = new TreeMap<Integer, String>();
	        for (int i = 1; i <= 31; i++) {
	            if (i < 10) {
	                DAYS.put(new Integer(i), "0"+i);
	            } else {
	                DAYS.put(new Integer(i), String.valueOf(i));
	            }
	        }   
		}
		
        return DAYS;
	}
	
	public static List getYears() {
		if( YEARS == null ) {
	        YEARS = new ArrayList<String>();
	        int startYear = Integer.parseInt(CalendarUtils.getCurrentYear());
	        for (int i = startYear; i >= getFirstYear(); i--) {
	            YEARS.add(String.valueOf(i));
	        }   
		}
		
		return YEARS;
	}
	
	public static List getFutureYears() {
		if( YEARS_WITH_FUTURE == null ) {
			YEARS_WITH_FUTURE = new ArrayList<String>();
	        
	        for (int i = MAX_FUTURE_YEAR; i >= getFirstYear(); i--) {
				YEARS_WITH_FUTURE.add(String.valueOf(i));
	        }   
		}
		
		return YEARS_WITH_FUTURE;
	}
	
	public static int getFirstYear() {
		return 1895;
	}
	
	public static Map getHours() {
		if( HOURS == null ) {
	        HOURS = new TreeMap<Integer, String>();
	        for (int i = 0; i <= 23; i++) {
	            if (i < 10) {
	            	HOURS.put(new Integer(i), "0"+i);
	            } else {
	            	HOURS.put(new Integer(i), String.valueOf(i));
	            }
	        } 
		}
		return HOURS;
	}
	
	public static Map getMinutes() {
		if( MINUTES == null ) {
	        MINUTES = new TreeMap<Integer, String>();
	        MINUTES.put(new Integer(0), "00");
	        MINUTES.put(new Integer(10), "10");
	        MINUTES.put(new Integer(20), "20");
	        MINUTES.put(new Integer(30), "30");
	        MINUTES.put(new Integer(40), "40");
	        MINUTES.put(new Integer(50), "50");
		}
		
		return MINUTES;
	}
	
}
