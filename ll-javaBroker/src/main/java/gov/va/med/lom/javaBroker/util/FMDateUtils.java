package gov.va.med.lom.javaBroker.util;

import gov.va.med.lom.javaBroker.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

import gov.va.med.lom.javaBroker.util.DateUtils;

public class FMDateUtils {
	
  public static Date fmDateTimeToDate(String fmDate) {
  	return fmDateTimeToDate(StringUtils.toDouble(fmDate, 0));
  }
  public static Date fmDateTimeToDate(double fmDate) {
   	String datePart = StringUtils.piece(String.valueOf(fmDate), '.', 1);
   	String timePart = StringUtils.piece(String.valueOf(fmDate), '.', 2);
   	if (datePart.length() != 7)
   		return null;
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Integer.valueOf(datePart.substring(0,3)).intValue() + 1700,
    		         Integer.valueOf(datePart.substring(3,5)).intValue()-1,
								 Integer.valueOf(datePart.substring(5,7)).intValue());
    if ((timePart.length() == 2) && timePart.substring(0, 2).equals("24"))
    	timePart = "235959";
    for (int i = timePart.length(); i < 6; i++)
    	timePart += "0";
    if (timePart.length() == 6) {
		  calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timePart.substring(0,2)).intValue());
		  calendar.set(Calendar.MINUTE, Integer.valueOf(timePart.substring(2,4)).intValue());
		  calendar.set(Calendar.SECOND, Integer.valueOf(timePart.substring(4,6)).intValue());
    }
    return calendar.getTime();
  }
  
  public static String fmDateTimeToAnsiDateTime(double fmDate) throws ParseException {
    return DateUtils.toAnsiDateTime(fmDateTimeToDate(fmDate));
  }
  
  public static String fmDateToAnsiDate(double fmDate) throws ParseException {
    return DateUtils.toAnsiDate(fmDateTimeToDate(fmDate));
  }  
  
  public static String fmDateTimeToEnglishDateTime(double fmDate) throws ParseException {
    return DateUtils.toDateTimeStr(fmDateTimeToDate(fmDate), DateUtils.ENGLISH_SHORT_DATE_TIME_FORMAT2);
  }
  
  public static String fmDateToEnglishDate(double fmDate) throws ParseException {
    return DateUtils.toEnglishDate(fmDateTimeToDate(fmDate));
  }    
  
  public static double dateTimeToFMDateTime(Calendar dateTime) {
  	int y = dateTime.get(Calendar.YEAR);
  	int m = dateTime.get(Calendar.MONTH);
  	int d = dateTime.get(Calendar.DAY_OF_MONTH);
  	int h = dateTime.get(Calendar.HOUR_OF_DAY);
		int n = dateTime.get(Calendar.MINUTE);
		int s = dateTime.get(Calendar.SECOND);
		int datePart = ((y-1700)*10000) + ((m+1)*100) + d;
		int timePart = (h*10000) + (n*100) + s;
  	return datePart + (timePart / 1000000.0);
  }
  
  public static double dateTimeToFMDateTime(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return dateTimeToFMDateTime(calendar);
  }  
  
  public static double dateToFMDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH);
    int d = calendar.get(Calendar.DAY_OF_MONTH);
    int datePart = ((y-1700)*10000) + ((m+1)*100) + d;
    return datePart;
  }
  
  public static double ansiDateTimeToFMDateTime(String ansiDateTime) throws ParseException {
    Calendar cal = DateUtils.convertAnsiDateTimeStrToCalendar(ansiDateTime);
    return dateTimeToFMDateTime(cal);
  }
  
  public static double ansiDateToFMDate(String ansiDate) throws ParseException {
    Date dt = DateUtils.convertAnsiDateStr(ansiDate);
    return dateToFMDate(dt);
  }  
}
