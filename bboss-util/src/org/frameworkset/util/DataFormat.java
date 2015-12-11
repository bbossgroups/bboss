package org.frameworkset.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DataFormat {
	private int count;
	public DataFormat() {
		// TODO Auto-generated constructor stub
	}
	
	public void increament()
	{
		count ++;
	}
	public void decreament()
	{
		count --;
		
	}
	
	public boolean reachroot()
	{
		return count == 0;
	}
	
	
	
	 
	private Map<String,SimpleDateFormat> dateformat = null;
	private Map<String,DecimalFormat> dataformat = null;
	private void initdateformat()
	{
		if(dateformat == null)
			dateformat = new HashMap<String,SimpleDateFormat>();
		
	}
	
	
	private void initdataformat()
	{
		if(dataformat == null)
			dataformat = new HashMap<String,DecimalFormat>();
	}
	 
	public DecimalFormat getDecimalFormat(String decimalFormat)
	{
		initdataformat();
		DecimalFormat format = this.dataformat.get(decimalFormat);
		if(format != null)
			return format;
		format = new DecimalFormat(decimalFormat);
		dataformat.put(decimalFormat, format);
		return format;
	}
	
	public SimpleDateFormat getSimpleDateFormat(String dateFormat)
	{
		initdateformat();
		SimpleDateFormat format = this.dateformat.get(dateFormat);
		if(format != null)
			return format;
		format = new SimpleDateFormat(dateFormat);
		dateformat.put(dateFormat, format);
		return format;
	}
	
	public SimpleDateFormat getSimpleDateFormat(String dateFormat,Locale locale,TimeZone tz)
	{
		initdateformat();
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:new StringBuilder().append(dateFormat).append("_").append(tz.toString()).toString();
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			format = new SimpleDateFormat(dateFormat);
			if(tz != null)
				format.setTimeZone(tz);
			dateformat.put(key, format);
		}
		else
		{
			String key = tz == null?new StringBuilder().append(dateFormat).append("_").append(locale.toString()).toString():
				new StringBuilder().append(dateFormat).append("_").append(locale.toString()).append("_").append(tz.toString()).toString();
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			format = new SimpleDateFormat(dateFormat,locale);
			if(tz != null)
				format.setTimeZone(tz);
			dateformat.put(key, format);
		}
		return format;
	}
	
	public SimpleDateFormat getSimpleDateFormat(String dateFormat,Locale locale,String tz)
	{
		initdateformat();
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:new StringBuilder().append(dateFormat).append("_").append(tz).toString() ;
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			format = new SimpleDateFormat(dateFormat);
			if(tz != null)
				format.setTimeZone(TimeZone.getTimeZone(tz));
			dateformat.put(key, format);
		}
		else
		{
			String key = tz == null?new StringBuilder().append(dateFormat).append("_").append(locale.toString()).toString():
				new StringBuilder().append(dateFormat).append("_").append(locale.toString()).append("_").append(tz).toString() ;
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			format = new SimpleDateFormat(dateFormat,locale);
			if(tz != null)
				format.setTimeZone(TimeZone.getTimeZone(tz));
			dateformat.put(key, format);
		}
		return format;
	}
	
	public SimpleDateFormat getSimpleDateFormat(String dateFormat,String locale,String tz)
	{
		initdateformat();
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:new StringBuilder().append(dateFormat).append("_").append(tz).toString() ;
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			format = new SimpleDateFormat(dateFormat);
			if(tz != null)
				format.setTimeZone(TimeZone.getTimeZone(tz));
			dateformat.put(dateFormat, format);
		}
		else
		{
			String key = tz == null?new StringBuilder().append(dateFormat).append("_").append(locale.toString()).toString():
				new StringBuilder().append(dateFormat).append("_").append(locale.toString()).append("_").append(tz).toString();
			format = this.dateformat.get(key);
			if(format != null)
				return format;
			Locale locale_ = new Locale(locale);
			
			format = new SimpleDateFormat(dateFormat,locale_);
			if(tz != null)
				format.setTimeZone(TimeZone.getTimeZone(tz));
			dateformat.put(key, format);
		}
		return format;
	}
	
	 
	
	
	
	 

}
