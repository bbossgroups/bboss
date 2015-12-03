package org.frameworkset.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

/**
 * 时间格式、数字格式化工具，request请求级别安全，单线程安全，多线程不安全
 * @author yinbp
 *
 */
public class DataFormatUtil {
	public static String DataFormatUtilKey = "org.bboss.dataformat"; 
	private Map<String,SimpleDateFormat> dateformat = new HashMap<String,SimpleDateFormat>();
	private Map<String,DecimalFormat> dataformat = new HashMap<String,DecimalFormat>();
 
	
	
	public DataFormatUtil() {
		// TODO Auto-generated constructor stub
	}
	public DecimalFormat _getDecimalFormat(String decimalFormat)
	{
		DecimalFormat format = this.dataformat.get(decimalFormat);
		if(format != null)
			return format;
		format = new DecimalFormat(decimalFormat);
		dataformat.put(decimalFormat, format);
		return format;
	}
	
	public SimpleDateFormat _getSimpleDateFormat(String dateFormat)
	{
		SimpleDateFormat format = this.dateformat.get(dateFormat);
		if(format != null)
			return format;
		format = new SimpleDateFormat(dateFormat);
		dateformat.put(dateFormat, format);
		return format;
	}
	
	public SimpleDateFormat _getSimpleDateFormat(String dateFormat,Locale locale,TimeZone tz)
	{
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:dateFormat+"_"+tz.toString();
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
			String key = tz == null?dateFormat+"_"+locale.toString():dateFormat+"_"+locale.toString()+"_"+tz.toString();
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
	
	public SimpleDateFormat _getSimpleDateFormat(String dateFormat,Locale locale,String tz)
	{
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:dateFormat+"_"+tz ;
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
			String key = tz == null?dateFormat+"_"+locale.toString():dateFormat+"_"+locale.toString()+"_"+tz ;
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
	
	public SimpleDateFormat _getSimpleDateFormat(String dateFormat,String locale,String tz)
	{
		SimpleDateFormat format = null;
		if(locale == null)
		{
			String key = tz == null?dateFormat:dateFormat+"_"+tz ;
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
			String key = tz == null?dateFormat+"_"+locale.toString():dateFormat+"_"+locale.toString()+"_"+tz;
			format = this.dateformat.get(key);
			if(format != null)
				return format;
//			Locale locale_ = new Locale(locale);
			
			format = new SimpleDateFormat(dateFormat);
			if(tz != null)
				format.setTimeZone(TimeZone.getTimeZone(tz));
			dateformat.put(key, format);
		}
		return format;
	}
	
	public static void main(String[] args)
	{
		 
		        //假如这个是你已知的时间类型
		        Calendar cal = Calendar.getInstance();
		        cal.getTimeInMillis();
		       
		        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String dd = Locale.JAPANESE.toString();
//		        fmt.setTimeZone(TimeZone.getTimeZone("Africa/Algiers"));
		         String beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println(beijingFormatStr);
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        fmt.setTimeZone(TimeZone.getTimeZone("GMT+9"));
		          beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println("jpane:"+beijingFormatStr);      
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        dd = Locale.JAPAN.getVariant();
		        dd = Locale.JAPAN.getCountry();
		        fmt.setTimeZone(TimeZone.getTimeZone(dd ));
		          beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println("jpane1:"+beijingFormatStr);      
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        dd = Locale.JAPANESE.toString();
		        fmt.setTimeZone(TimeZone.getTimeZone("Africa/Algiers"));
		         beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println(beijingFormatStr);
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        dd = Locale.CHINA.toString();
		        fmt.setTimeZone(TimeZone.getTimeZone("America/Dawson"));
		         beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println(beijingFormatStr);
		        
		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        dd = Locale.JAPANESE.toString();
		        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		         beijingFormatStr = fmt.format(cal.getTime());
		        System.out.println(beijingFormatStr);
	}
	
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat)
	{
		DataFormatUtil dataFormatUtil = (DataFormatUtil)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
		if(dataFormatUtil == null)
		{
			dataFormatUtil = new DataFormatUtil();
			request.setAttribute(DataFormatUtilKey, dataFormatUtil);
		}
		SimpleDateFormat temp = dataFormatUtil._getSimpleDateFormat(dateFormat);
		return temp;
	}
	
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,Locale locale,TimeZone timeZone)
	{
		DataFormatUtil dataFormatUtil = (DataFormatUtil)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
		if(dataFormatUtil == null)
		{
			dataFormatUtil = new DataFormatUtil();
			request.setAttribute(DataFormatUtilKey, dataFormatUtil);
		}
		SimpleDateFormat temp = dataFormatUtil._getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,Locale locale,String timeZone)
	{
		DataFormatUtil dataFormatUtil = (DataFormatUtil)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
		if(dataFormatUtil == null)
		{
			dataFormatUtil = new DataFormatUtil();
			request.setAttribute(DataFormatUtilKey, dataFormatUtil);
		}
		SimpleDateFormat temp = dataFormatUtil._getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,String locale,String timeZone)
	{
		DataFormatUtil dataFormatUtil = (DataFormatUtil)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
		if(dataFormatUtil == null)
		{
			dataFormatUtil = new DataFormatUtil();
			request.setAttribute(DataFormatUtilKey, dataFormatUtil);
		}
		SimpleDateFormat temp = dataFormatUtil._getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	
	public static DecimalFormat getDecimalFormat(HttpServletRequest request,String decimalFormat)
	{
		DataFormatUtil dataFormatUtil = (DataFormatUtil)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
		if(dataFormatUtil == null)
		{
			dataFormatUtil = new DataFormatUtil();
			request.setAttribute(DataFormatUtilKey, dataFormatUtil);
		}
		DecimalFormat temp = dataFormatUtil._getDecimalFormat(decimalFormat);
		return temp;
	}

}
