package com.android.lib.map.osm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public final static String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static Calendar stringToCalendar(String date) {		
		Date myNewDate = null;
		Calendar calendar = Calendar.getInstance();
		
		if (date == null || date.length() == 0)
			return null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_DATE_FORMAT, Locale.US);
		try {
			myNewDate = dateFormat.parse(date);
			if (myNewDate != null) {
				calendar.setTime(myNewDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return calendar;
	}
	
	public static String longToSqlDateFormat(long date) {
		Date d = new Date(date);
		SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_DATE_FORMAT, Locale.US);
		return dateFormat.format(d);
	}
	
	public static Calendar calendarFromDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}
	
	public static Calendar longToCalendar(Long time) {
		Calendar c = null;
		if (time != null) {
			c = Calendar.getInstance();
			c.setTimeInMillis(time);
		}
		return c;
	}

	public static Long calendarToLong(Calendar c) {
		if (c != null)
			return c.getTimeInMillis();
		return null;
	}
	
	public static String getFormattedDate(Calendar c, String format) {
		try {
			Date d = new Date(c.getTimeInMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
			return dateFormat.format(d);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static int compare(Calendar d1, Calendar d2) {
		int result = 0;
		
		if (d1 == null && d2 == null)
			return result;
		if (d1 == null)
			return -1;
		if (d2 == null)
			return 1;
			
		if (d1.after(d2))
			result = 1;
		else if (d1.before(d2))
			result = -1;
		else
			result = 0;
		
		return result;
	}
}
