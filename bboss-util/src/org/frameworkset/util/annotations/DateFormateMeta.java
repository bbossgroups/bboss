package org.frameworkset.util.annotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
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
			return null;
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
	public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale,String timeZone){
		if(dataformat == null )
			return null;
		DateFormateMeta dateFormateMeta = new DateFormateMeta();
		Locale locale = null;
		if(_locale != null && !_locale.equals(""))
		{
			try
			{
				locale = new Locale(_locale);
			}
			catch(Exception e)
			{

			}
			dateFormateMeta.setLocale(locale);
			dateFormateMeta.setLocale_str(_locale);

		}
		if(timeZone != null && !timeZone.equals(""))
		{
			try
			{
				dateFormateMeta.setTimeZone(TimeZone.getTimeZone(timeZone));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			dateFormateMeta.setTimeZone_str(timeZone);

		}
		dateFormateMeta.setDateformat(dataformat);
		dateFormateMeta.toDateFormat(false);
		return dateFormateMeta;
	}

}
