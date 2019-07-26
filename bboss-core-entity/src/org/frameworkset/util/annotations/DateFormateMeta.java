package org.frameworkset.util.annotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * use static method to construction DateFormateMeta Object
 * public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale)
 * public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale,String timeZone)
 */
public class DateFormateMeta {
	private String dateformat;
	private Locale locale;
	private String locale_str;
	private String timeZone_str;
	private TimeZone timeZone;
	private SimpleDateFormat simpleDateFormat = null;
	private DateFormateMeta(){
		
	}
	public String getDateformat() {
		return dateformat;
	}
	public SimpleDateFormat getOriginDateFormat(){
		return simpleDateFormat;
	}
	public DateFormat toDateFormat(){
		return  toDateFormat(true);
	}

	/**
	 *
	 * @param clone
	 * @return
	 */
	private DateFormat toDateFormat(boolean clone){
		if(simpleDateFormat == null) {
			SimpleDateFormat f = null;
			if (getLocale() == null)
				f = new SimpleDateFormat(dateformat);
			else
				f = new SimpleDateFormat(dateformat, getLocale());
			if (getTimeZone() != null)
				f.setTimeZone(getTimeZone());
			this.simpleDateFormat = f;
		}
		if(clone) {
			return (DateFormat) simpleDateFormat.clone();
		}
		else
			return simpleDateFormat;
	}

	  void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	public Locale getLocale() {
		return locale;
	}
	  void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getLocale_str() {
		return locale_str;
	}
	  void setLocale_str(String locale_str) {
		this.locale_str = locale_str;
	}
	public String getTimeZone_str() {
		return timeZone_str;
	}
	  void setTimeZone_str(String timeZone_str) {
		this.timeZone_str = timeZone_str;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	  void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("dateformat=").append(dateformat).append(",").append("locale=").append(locale_str).append(",").append("timeZone=").append(timeZone_str);
		return ret.toString();
	}
	public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale){
		return buildDateFormateMeta(dataformat,_locale,null);
	}
	private static Map<String,DateFormateMeta> baseDataformatMap = new HashMap<String,DateFormateMeta>(20);
	private static Map<String,DateFormateMeta> localDataformatMap = new HashMap<String,DateFormateMeta>(20);
	private static Map<String,DateFormateMeta> timeZoneDataformatMap = new HashMap<String,DateFormateMeta>(20);
	private static Map<String,DateFormateMeta> localTimeZoneDataformatMap = new HashMap<String,DateFormateMeta>(20);
	public static DateFormateMeta buildDateFormateMeta(String dataformat){
		return buildDateFormateMeta(dataformat,null,null);
	}
	public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale,String timeZone){
		if(dataformat == null )
			return null;
		if(_locale == null && timeZone == null){
			DateFormateMeta dateFormateMeta = baseDataformatMap.get(dataformat);
			if(dateFormateMeta != null){
				return dateFormateMeta;
			}
			synchronized(baseDataformatMap){
				dateFormateMeta = baseDataformatMap.get(dataformat);
				if(dateFormateMeta != null){
					return dateFormateMeta;
				}
				dateFormateMeta = new DateFormateMeta();
				dateFormateMeta.setDateformat(dataformat);
				dateFormateMeta.toDateFormat(false);
				baseDataformatMap.put(dataformat, dateFormateMeta);
				return dateFormateMeta;
				
			}
			
		}
		else if(_locale != null && timeZone != null){
			String key = dataformat + "_" + _locale + "_"+timeZone;
			DateFormateMeta dateFormateMeta = localTimeZoneDataformatMap.get(key);
			if(dateFormateMeta != null){
				return dateFormateMeta;
			}
			synchronized(localTimeZoneDataformatMap){
				dateFormateMeta = localTimeZoneDataformatMap.get(key);
				if(dateFormateMeta != null){
					return dateFormateMeta;
				}
				dateFormateMeta = new DateFormateMeta();
				dateFormateMeta.setDateformat(dataformat);
				Locale locale = null;
				try
				{
					locale = new Locale(_locale);
					dateFormateMeta.setLocale(locale);
				}
				catch(Exception e)
				{
					throw new RuntimeException("buildDateFormateMeta failed:",e);
				}
				dateFormateMeta.setLocale_str(_locale);
				try
				{
					dateFormateMeta.setTimeZone(TimeZone.getTimeZone(timeZone));
				}
				catch(Exception e)
				{
					throw new RuntimeException("buildDateFormateMeta failed:",e);
				}
				dateFormateMeta.setTimeZone_str(timeZone);
				dateFormateMeta.toDateFormat(false);
				localTimeZoneDataformatMap.put(key, dateFormateMeta);
				return dateFormateMeta;
			}
		}
		else if(_locale != null ){
			String key = dataformat + "_" + _locale ;
			DateFormateMeta dateFormateMeta = localDataformatMap.get(key);
			if(dateFormateMeta != null){
				return dateFormateMeta;
			}
			synchronized(localDataformatMap){
				dateFormateMeta = localDataformatMap.get(key);
				if(dateFormateMeta != null){
					return dateFormateMeta;
				}
				dateFormateMeta = new DateFormateMeta();
				dateFormateMeta.setDateformat(dataformat);
				Locale locale = null;
				try
				{
					locale = new Locale(_locale);
					dateFormateMeta.setLocale(locale);
				}
				catch(Exception e)
				{
					throw new RuntimeException("buildDateFormateMeta failed:",e);
				}
				dateFormateMeta.setLocale_str(_locale);

				dateFormateMeta.toDateFormat(false);
				localDataformatMap.put(key, dateFormateMeta);
				return dateFormateMeta;
			}
		}
		else {
			String key = dataformat + "_" + timeZone ;
			DateFormateMeta dateFormateMeta = timeZoneDataformatMap.get(key);
			if(dateFormateMeta != null){
				return dateFormateMeta;
			}
			synchronized(timeZoneDataformatMap){
				dateFormateMeta = timeZoneDataformatMap.get(key);
				if(dateFormateMeta != null){
					return dateFormateMeta;
				}
				dateFormateMeta = new DateFormateMeta();
				dateFormateMeta.setDateformat(dataformat);
				try
				{
					dateFormateMeta.setTimeZone(TimeZone.getTimeZone(timeZone));
				}
				catch(Exception e)
				{
					throw new RuntimeException("buildDateFormateMeta failed:",e);
				}
				dateFormateMeta.setTimeZone_str(timeZone);
				dateFormateMeta.toDateFormat(false);
				timeZoneDataformatMap.put(key, dateFormateMeta);
				return dateFormateMeta;
			}
		}

	}

}
