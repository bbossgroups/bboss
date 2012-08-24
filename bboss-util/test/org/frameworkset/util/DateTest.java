package org.frameworkset.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTest {
	public static void main(String[] args)
	{
		Calendar gmtlocal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));
		  gmtlocal.set(Calendar.YEAR, 2007);
		  gmtlocal.set(Calendar.MONTH, 0);
		  gmtlocal.set(Calendar.DAY_OF_MONTH, 1);
		  gmtlocal.set(Calendar.HOUR_OF_DAY, 0);
		  gmtlocal.set(Calendar.MINUTE, 0);
		   
		  Calendar gmt0 = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
		  gmt0.setTimeInMillis(gmtlocal.getTimeInMillis());
		  System.out.println(gmt0.get(Calendar.YEAR));
		  
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  sf.setTimeZone(TimeZone.getTimeZone("GMT+12"));
		  System.out.println(sf.format(gmtlocal.getTime()));
		   
		  sf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		  System.out.println(sf.format(gmtlocal.getTime()));
		   
		  sf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		  System.out.println(sf.format(gmtlocal.getTime()));  

	}

}
