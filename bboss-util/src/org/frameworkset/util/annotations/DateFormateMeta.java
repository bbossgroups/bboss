package org.frameworkset.util.annotations;

import java.util.Locale;
import java.util.TimeZone;

public class DateFormateMeta {
	private String dateformat;
	private Locale locale;
	private String locale_str;
	private String timeZone_str;
	private TimeZone timeZone;
	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getLocale_str() {
		return locale_str;
	}
	public void setLocale_str(String locale_str) {
		this.locale_str = locale_str;
	}
	public String getTimeZone_str() {
		return timeZone_str;
	}
	public void setTimeZone_str(String timeZone_str) {
		this.timeZone_str = timeZone_str;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append("dateformat=").append(dateformat).append(",").append("locale=").append(locale_str).append(",").append("timeZone=").append(timeZone_str);
		return ret.toString();
	}

	public static DateFormateMeta buildDateFormateMeta(String dataformat,String _locale){
		if(dataformat == null || _locale.equals(""))
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
		dateFormateMeta.setDateformat(dataformat);
		return dateFormateMeta;
	}

}
