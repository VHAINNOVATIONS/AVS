package gov.va.med.lom.javaBroker.util;

import java.util.*;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtils {
  
  public static String ANSI_DATE_FORMAT = "yyyy-MM-dd";
  public static String ANSI_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static String PRECISE_ANSI_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
  public static String ANSI_DATE_FORMAT2 = "yyyy/MM/dd";
  public static String ANSI_DATE_TIME_FORMAT2 = "yyyy/MM/dd@HH:mm:ss";
  public static String ANSI_SHORT_DATE_TIME_FORMAT2 = "yyyy/MM/dd@HH:mm";
  public static String ENGLISH_DATE_FORMAT = "MM/dd/yyyy";
  public static String ENGLISH_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
  public static String ENGLISH_DATE_TIME_FORMAT2 = "MM/dd/yyyy@HH:mm:ss";
  public static String ENGLISH_SHORT_DATE_TIME_FORMAT = "MM/dd/yyyy HH:mm";
  public static String ENGLISH_SHORT_DATE_TIME_FORMAT2 = "MM/dd/yyyy@HH:mm";
  public static String ENGLISH_SHORT_DATE_TIME_FORMAT3 = "MM/dd/yyyy hh:mm a";
  public static String ENGLISH_SHORT_DATE_TIME_FORMAT4 = "MM/dd/yyyy hh:mm z";
  public static String MEDIUM_ENGLISH_DATE_FORMAT = "MMMM dd, yyyy";
  public static String MEDIUM_ENGLISH_DATE_TIME_FORMAT = "MMMM dd yyyy HH:mm";
  public static String MEDIUM_ENGLISH_DATE_TIME_FORMAT2 = "MMMM dd yyyy hh:mm z";
  public static String LONG_ENGLISH_DATE_FORMAT = "EEEE, MMMM dd, yyyy";
  public static String LONG_ENGLISH_DATE_TIME_FORMAT = "EEEE, MMMM dd yyyy HH:mm";
  public static String LONG_ENGLISH_DATE_TIME_FORMAT2 = "EEEE, MMMM dd yyyy hh:mm z";
  public static String PRECISE_TIME_FORMAT = "HH:mm:ss:SS";
  public static String SHORT_TIME_FORMAT = "HH:mm:ss";
  public static String SHORTEST_TIME_FORMAT = "HH:mm";
  public static String HL7_TIME_STAMP_FORMAT = "yyyyMMddHHmmss";
  public static String HL7_TIME_STAMP_FORMAT2 = "yyyyMMddHHmm";
  public static String HL7_DATE_STAMP_FORMAT = "yyyyMMdd";
  

  private static long elapsedTime; // msec
  private static int DAYS_IN_MONTH[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  public static boolean isLeapYear(int year) {
    if (year < 1600)
      return false;
    if (year % 4 != 0)
      return false;
    if (year % 100 == 0 && year % 400 != 0)
      return false;
    return true;
  }

  public static boolean isValidDate(String d) {
    // check for null string and invalid length
    if (d == null)
      return false;
    if (d.length() != 10)
      return false;
    // check for valid characters
    for (int i = 0; i < d.length(); i++) {
      char c = d.charAt(i);
      if (i == 2 || i == 5) {
        if (c != '/')
          return false;
      } else {
        if (c < '0' || c > '9')
          return false;
      }
    }
    // extract field values
    int month = Integer.parseInt(d.substring(0, 2));
    int day = Integer.parseInt(d.substring(3, 5));
    int year = Integer.parseInt(d.substring(6, 10));
    // validate field values
    if (month < 1 || month > 12)
      return false;
    if (day < 1 || day > 31)
      return false;
    if (year < 1900)
      return false;
    // handle leap years
    int dim = DAYS_IN_MONTH[month - 1];
    if (month == 2 && isLeapYear(year))
      dim++;
    if (day > dim)
      return false;
    return true;
  }

  public static String getCurrentDate() {
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static Date getCurrentUTCDate() {
    try {
        String localTimeStr = toAnsiDateTime(new Date());
        DateFormat localDateFormat = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
        Date localTime = localDateFormat.parse(localTimeStr);
        DateFormat gmtDateFormat = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return convertAnsiDateTimeStr(gmtDateFormat.format(localTime));
    } catch (ParseException e) {
        return null;
    }
  }
  
  public static String getCurrentDateTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static String getCurrentDateMinusDays(int numDays) {
    return getCurrentDatePlusDays(-numDays);
  }
  
  public static String getCurrentDatePlusDays(int numDays) {
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date today = new Date();
    today = addDaysToDate(today, numDays);
    return formatter.format(today);
  }

  public static String getCurrentShortestTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(SHORTEST_TIME_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static String getCurrentTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(SHORT_TIME_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static String getCurrentPreciseTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(PRECISE_TIME_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }
  
  public static String getHL7DateStamp() {
    SimpleDateFormat formatter = new SimpleDateFormat(HL7_DATE_STAMP_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }    
  
  public static String getHL7TimeStamp() {
    SimpleDateFormat formatter = new SimpleDateFormat(HL7_TIME_STAMP_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }  

  public static String shortTimeToShortestTime(String shortTime) throws ParseException {
    SimpleDateFormat shortTimeFormatter = new SimpleDateFormat(SHORT_TIME_FORMAT);
    Date dt = shortTimeFormatter.parse(shortTime);
    SimpleDateFormat shortestTimeFormatter = new SimpleDateFormat(SHORTEST_TIME_FORMAT);
    return shortestTimeFormatter.format(dt);
  }

  public static void resetElapsedTime() {
    elapsedTime = new Date().getTime();
  }

  public static long getElapsedTime() {
    return new Date().getTime() - elapsedTime;
  }

  public static void printElapsedTime(String text) {
    System.out.println(text + ": " + getElapsedTime());
  }

  public static String getEnglishDate() {
    SimpleDateFormat formatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static String getEnglishDateTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    Date today = new Date();
    return formatter.format(today) + "@" + getCurrentTime();
  }

  public static String getLongEnglishDate() {
    SimpleDateFormat formatter = new SimpleDateFormat(LONG_ENGLISH_DATE_FORMAT);
    Date today = new Date();
    return formatter.format(today);
  }

  public static String getLongEnglishDateTime() {
    SimpleDateFormat formatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    Date today = new Date();
    return formatter.format(today) + " " + getCurrentTime();
  }
  
  public static Date convertDateStr(String dateStr, String format) throws ParseException {
    if (dateStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
      return dateFormatter.parse(dateStr);
    }
    return null;
  }  
  
  public static GregorianCalendar convertDateStrToCalendar(String dateStr, String format) throws ParseException {
    if (dateStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
      Date date = dateFormatter.parse(dateStr);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return cal;
    }
    return null;
  }    
  
  public static Date convertAnsiDateStr(String ansiDateStr) throws ParseException {
    if (ansiDateStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      return dateFormatter.parse(ansiDateStr);
    }
    return null;
  }
  
  public static Date convertAnsiDateTimeStr(String ansiDateTimeStr) throws ParseException {
    if (ansiDateTimeStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
      return dateFormatter.parse(ansiDateTimeStr);
    }
    return null;
  }  
  
  public static GregorianCalendar convertAnsiDateStrToCalendar(String ansiDateStr) throws ParseException {
    if (ansiDateStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      Date date = dateFormatter.parse(ansiDateStr);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return cal;
    }
    return null;
  }  
  
  public static GregorianCalendar convertUTCAnsiDateStrToCalendar(String ansiDateStr) throws ParseException {
    if (ansiDateStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      Date date = dateFormatter.parse(ansiDateStr);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return cal;
    }
    return null;
  }   
  
  public static GregorianCalendar convertAnsiDateTimeStrToCalendar(String ansiDateTimeStr) throws ParseException {
    if (ansiDateTimeStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
      Date date = dateFormatter.parse(ansiDateTimeStr);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return cal;
    }
    return null;
  }    

  public static GregorianCalendar convertPreciseAnsiDateTimeStrToCalendar(String ansiDateTimeStr) throws ParseException {
    if (ansiDateTimeStr != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(PRECISE_ANSI_DATE_TIME_FORMAT);
      Date date = dateFormatter.parse(ansiDateTimeStr);
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      return cal;
    }
    return null;
  }    
  
  public static Date convertEnglishDateStr(String englishDate) throws ParseException {
    if (englishDate != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
      return dateFormatter.parse(englishDate);
    }
    return null;
  }
  
  public static Date convertEnglishDatetimeStr(String englishDatetime) throws ParseException {
    if (englishDatetime != null) {
      SimpleDateFormat dateFormatter = new SimpleDateFormat(ENGLISH_DATE_TIME_FORMAT);
      return dateFormatter.parse(englishDatetime);
    }
    return null;
  }

  public static Date addDaysToDate(Date date, int numDays) {
    if (date != null) {
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      cal.add(Calendar.DATE, numDays);
      return cal.getTime();
    }
    return null;
  }

  public static Date subtractDaysFromDate(Date date, int numDays) {
    return addDaysToDate(date, -numDays);
  }

  public static String toDateTimeStr(Date date, String format) throws ParseException {
    if (date != null) {
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(format);
      return ansiFormatter.format(date);
    } else
      return "";
  }  
  
  public static Date toDate(String dt, String format) throws ParseException {
    if (dt != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(format);
      Date date = englishFormatter.parse(dt);
      return date;
    }
    return null;    
  }  
  
  public static String toUTCDateTimeStr(Date date, String format) throws ParseException {
    if (date != null) {
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(format);
      ansiFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      return ansiFormatter.format(date);
    } else
      return "";
  }    
  
  public static String toAnsiDate(String dateStr) throws ParseException {
    if (dateStr != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
      Date date = englishFormatter.parse(dateStr);
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      return ansiFormatter.format(date);
    }
    return "";
  }
  
  public static String toAnsiDatetime(String datetimeStr) throws ParseException {
    if (datetimeStr != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_TIME_FORMAT);
      Date date = englishFormatter.parse(datetimeStr);
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
      return ansiFormatter.format(date);
    }
    return "";
  }
  
  public static String toAnsiDate(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      return ansiFormatter.format(date);
    } else
      return "";
  }

  public static String toAnsiDateTime(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
      return ansiFormatter.format(date);
    } else
      return "";
  }
  
  public static String toUTCAnsiDateTime(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
      ansiFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      return ansiFormatter.format(date);
    } else
      return "";
  }  
  
  public static Date convertLocalTimeToGMT(Date date, String zone) throws ParseException {
    return convertDateToTimeZone(date, zone, "GMT");
  }
  
  public static Date convertGMTToLocalTime(Date date, String zone) throws ParseException {
    return convertDateToTimeZone(date, "GMT", zone);
  }
  
  public static Date convertDateToTimeZone(Date date, String fromZone, String toZone) throws ParseException {
    String localTimeStr = toAnsiDateTime(date);
    DateFormat localDateFormat = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
    localDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
    Date localTime = localDateFormat.parse(localTimeStr);
    DateFormat gmtDateFormat = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
    gmtDateFormat.setTimeZone(TimeZone.getTimeZone(toZone));
    return convertAnsiDateTimeStr(gmtDateFormat.format(localTime));
  }
  
  public static String toAnsiDate(long msec) throws ParseException {
    SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    return ansiFormatter.format(new Date(msec));
  }

  public static String toAnsiDateTime(long msec) throws ParseException {
    SimpleDateFormat ansiFormatter = new SimpleDateFormat(ANSI_DATE_TIME_FORMAT);
    return ansiFormatter.format(new Date(msec));
  }

  public static String toEnglishDate(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
      return englishFormatter.format(date);
    } else
      return "";
  }

  public static String toEnglishDateTime(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_TIME_FORMAT);
      return englishFormatter.format(date);
    } else
      return "";
  }
  
  public static String toEnglishShortDateTime(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_SHORT_DATE_TIME_FORMAT);
      return englishFormatter.format(date);
    } else
      return "";
  }  
  
  public static String toEnglishDate(long msec) throws ParseException {
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    return englishFormatter.format(new Date(msec));
  }

  public static String toEnglishDateTime(long msec) throws ParseException {
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_TIME_FORMAT);
    return englishFormatter.format(new Date(msec));
  }

  public static String toEnglishDate(String ansiDate) throws ParseException {
    Date date = convertAnsiDateStr(ansiDate);
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    return englishFormatter.format(date);
  }

  public static String toEnglishDateTime(String ansiDateTime) throws ParseException {
    Date date = convertAnsiDateStr(ansiDateTime);
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    String dt = englishFormatter.format(date);
    int index = ansiDateTime.indexOf(' ');
    if (index == -1)
      return dt;
    else
      return dt + ' ' + ansiDateTime.substring(index+1, index + 6);
  }
  
  public static String toEnglishDate(String dateStr, String format) throws ParseException {
    Date date = convertDateStr(dateStr, format);
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    return englishFormatter.format(date);
  }

  public static String toEnglishDateTime(String dateStr, String format) throws ParseException {
    Date date = convertDateStr(dateStr, format);
    SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATE_FORMAT);
    String dt = englishFormatter.format(date);
    int index = dateStr.indexOf('@');
    if (index == -1)
      return dt;
    else
      return dt + ' ' + dateStr.substring(index+1, index + 6);
  }  

  public static String formatDate(String dateStr) throws ParseException {
    if (dateStr != null) {
      SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
      Date date = formatter.parse(dateStr);
      return formatter.format(date);
    } else
      return "";
  }

  public static String formatDate(Date date, String format) {
      if (date != null) {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      return formatter.format(date);
      } else
          return "";
  }

  public static String formatTime(String timeStr) {
    int len = timeStr.trim().length();
    if (len > 0) {
      int numColons = 0;
      int index = -1;
      while ((index = timeStr.indexOf(":", index+1)) >= 0) {
        numColons++;
      }
      if (numColons == 0)
        return timeStr + ":00:00";
      else if (numColons == 1)
        return timeStr + ":00";
      else
        return timeStr;
    } else
      return "00:00:00";
  }

  public static long toMsec(String dateStr) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date date = formatter.parse(dateStr);
    return date.getTime();
  }
 
  public static double getNumDays(String dateStr1, String dateStr2, String dateFormat) throws ParseException {
    /* calculates time difference in msec divided by
       the number of msec in 1 day to return the
       difference between the dates in days.
       Assumes date2 is later than date1.
    */
    if ((dateStr1 != null) && (dateStr2 != null)) {
      SimpleDateFormat englishFormatter = new SimpleDateFormat(dateFormat);
      Date date1 = englishFormatter.parse(dateStr1);
      Date date2 = englishFormatter.parse(dateStr2);
      return getNumDays(date1, date2);
    }
    return 0;
  }

  public static double getNumDays(long msec1, long msec2) {
    return getNumDays(new Date(msec1), new Date(msec2));
  }

  public static double getNumDays(Date date1, Date date2) {
    if ((date1 != null) && (date2 != null))
      return (date2.getTime() - date1.getTime()) / (24.0 * 3600.0 * 1000.0);
    else
      return 0;
  }

  public static double msecToDays(long msec) {
    return msec / (24.0 * 3600.0 * 1000.0);
  }

  public static double msecToDays(double msec) {
    return msec / (24.0 * 3600.0 * 1000.0);
  }

  public static boolean after(String dateStr1, String dateStr2) throws ParseException {
    if (dateStr1.equals("0000-00-00") || dateStr1.trim().equals(""))
      return true;
    if (dateStr2.equals("0000-00-00") || dateStr2.trim().equals(""))
      return false;
    // tests if the date represented by dateStr1 is after the date represented by dateStr2
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date date1 = formatter.parse(dateStr1);
    Date date2 = formatter.parse(dateStr2);
    return date1.after(date2);
  }

  public static boolean afterToday(String dateStr) throws ParseException {
    if (dateStr.equals("0000-00-00") || dateStr.trim().equals(""))
      return true;
    // tests if the date represented by dateStr is after the current date (today)
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date date1 = formatter.parse(dateStr);
    Date date2 = formatter.parse(getCurrentDate());
    return date1.after(date2);
  }

  public static boolean before(String dateStr1, String dateStr2) throws ParseException {
    if (dateStr1.equals("0000-00-00") || dateStr1.trim().equals(""))
      return false;
    if (dateStr2.equals("0000-00-00") || dateStr2.trim().equals(""))
      return true;
    // tests if the date represented by dateStr1 is after the date represented by dateStr2
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date date1 = formatter.parse(dateStr1);
    Date date2 = formatter.parse(dateStr2);
    return date1.before(date2);
  }

  public static boolean beforeToday(String dateStr) throws ParseException {
    if (dateStr.equals("0000-00-00") || dateStr.trim().equals(""))
      return false;
    // tests if the date represented by dateStr is before the current date (today)
    SimpleDateFormat formatter = new SimpleDateFormat(ANSI_DATE_FORMAT);
    Date date1 = formatter.parse(dateStr);
    Date date2 = formatter.parse(getCurrentDate());
    return date1.before(date2);
  }
  
  public static int calcAge(Date birthdate) {
    return calcAge(new Date(), birthdate);
  }
  
  public static int calcAge(Date aDate, Date birthdate) {
    if (birthdate != null) {
      GregorianCalendar aDateCal = new GregorianCalendar();
      aDateCal.setTime(aDate);
      GregorianCalendar bDateCal = new GregorianCalendar();
      bDateCal.setTime(birthdate);
      int aMonth = aDateCal.get(Calendar.MONTH);
      int aDay = aDateCal.get(Calendar.DAY_OF_MONTH);
      int aYear = aDateCal.get(Calendar.YEAR);
      int bMonth = bDateCal.get(Calendar.MONTH);
      int bDay = bDateCal.get(Calendar.DAY_OF_MONTH);
      int bYear = bDateCal.get(Calendar.YEAR);  
      int age = (aYear - bYear) - 1;
      if (aMonth > bMonth)
        age++;
      else if (aMonth == bMonth) {
        if (aDay >= bDay)
          age++;
      }
      return age;
    } else
      return 0;
  }
  
  public static GregorianCalendar fmDateTimeToDateTime(double fmDateTime) {
    String dtString = String.valueOf(fmDateTime);
    String datePart = dtString.substring(0, dtString.indexOf('.'));
    String timePart = dtString.substring(dtString.indexOf('.')+1, dtString.length()) + "000000";
    if (datePart.startsWith("24"))
      timePart = "23595959";
    GregorianCalendar gc = new GregorianCalendar();
    gc.set(Integer.parseInt(datePart.substring(0, 3)) + 1700, 
           Integer.parseInt(datePart.substring(3, 5)) - 1, 
           Integer.parseInt(datePart.substring(5, 7)),
           Integer.parseInt(timePart.substring(0, 2)), 
           Integer.parseInt(timePart.substring(2, 4)), 
           Integer.parseInt(timePart.substring(4, 6)));
    return gc;
  }

  public static String fmDateTimeToAnsiDateTime(double fmDateTime) throws ParseException {
    GregorianCalendar gc = fmDateTimeToDateTime(fmDateTime);
    return toAnsiDateTime(gc.getTime());
  }
  
  public static double dateTimeToFMDateTime(GregorianCalendar gc) {
    int datePart = ((gc.get(GregorianCalendar.YEAR) - 1700) * 10000) +
                   ((gc.get(GregorianCalendar.MONTH) + 1) * 100) + 
                   gc.get(GregorianCalendar.DAY_OF_MONTH);
    int timePart = (gc.get(GregorianCalendar.HOUR_OF_DAY) * 10000) + 
                   (gc.get(GregorianCalendar.MINUTE) * 100) + 
                   gc.get(GregorianCalendar.SECOND);
    return datePart + (timePart / 1000000.00);
  }
  
  private static SimpleDateFormat getHl7Formatter(String ts) {
    if ((ts.length() == HL7_TIME_STAMP_FORMAT.length()) || 
        (ts.length() == HL7_TIME_STAMP_FORMAT.length() + 5))
      return new SimpleDateFormat(HL7_TIME_STAMP_FORMAT);
    else if (ts.length() == HL7_DATE_STAMP_FORMAT.length())
      return new SimpleDateFormat(HL7_DATE_STAMP_FORMAT);
    else
      return new SimpleDateFormat(HL7_TIME_STAMP_FORMAT2);
  }
  
  public static Date hl7TimestampToDate(String ts) throws ParseException {
    if (ts != null) {
      SimpleDateFormat sdf = getHl7Formatter(ts);
      Date date = sdf.parse(ts);
      return date;
    }
    return null;    
  }
  
  public static GregorianCalendar hl7TimestampToDatetime(String ts) throws ParseException {
    if (ts != null) {
      SimpleDateFormat sdf = getHl7Formatter(ts);
      Date date = sdf.parse(ts);
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTime(date);
      return gc;
    }
    return null;    
  }  
  
  public static Date hl7DatestampToDate(String ds) throws ParseException {
    SimpleDateFormat sdf;
    if (ds != null) {
      sdf = getHl7Formatter(ds);
      Date date = sdf.parse(ds);
      return date;
    }
    return null;    
  }  
  
  public static Date hl7TimestampToUTC(String ts) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(ENGLISH_SHORT_DATE_TIME_FORMAT);
    int len = ts.length();
    String tz = ts.substring(len - 4);
    String op = ts.substring(len - 5, len - 4);
    // in java the operator is opposite of what it appears
    if(op.equals("-")) 
        op = "+";
    else
        op = "-";
    tz = tz.replace('0', ' ').trim();
    tz = "Etc/GMT" + op + tz;
    Date d = DateUtils.hl7TimestampToDate(ts);
    return DateUtils.convertDateToTimeZone(d, tz, "UTC");          
  }  
  
  public static Date hl7DatetimeToDatetime(String ds) throws ParseException {
    // try parsing a time stamp first
    try {
      return hl7TimestampToDate(ds);
    } catch(ParseException pe) {
      return hl7DatestampToDate(ds);
    }
  }   
  
  public static String dateToHL7Timestamp(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat formatter = new SimpleDateFormat(HL7_TIME_STAMP_FORMAT);
      return formatter.format(date);
    } else
      return "";
  }
  
  public static String dateToHL7Datestamp(Date date) throws ParseException {
    if (date != null) {
      SimpleDateFormat formatter = new SimpleDateFormat(HL7_DATE_STAMP_FORMAT);
      return formatter.format(date);
    } else
      return "";
  }  
  
  public static String datetimeToHL7Datetime(GregorianCalendar gc) throws ParseException {
    Date date = gc.getTime();
    return dateToHL7Datetime(date);
  }    
  
  public static String dateToHL7Datetime(Date date) throws ParseException {
    // try parsing a time stamp first
    try {
      return dateToHL7Timestamp(date);
    } catch(ParseException pe) {
      return dateToHL7Timestamp(date);
    }
  }    
  
  public static void main(String[] args) throws ParseException {
    Calendar cal = DateUtils.hl7TimestampToDatetime("20071030");
    System.out.println(cal.getTime());
  }
}

