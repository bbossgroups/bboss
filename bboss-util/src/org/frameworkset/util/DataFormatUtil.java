package org.frameworkset.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

/**
 * 时间格式、数字格式化工具，request请求级别安全，单线程安全，多线程不安全
 * @author yinbp
 *
 */
public class DataFormatUtil {
	public static String DataFormatUtilKey = "org.bboss.dataformat"; 
	 
	private final static ThreadLocal<DataFormat> dateformatThreadLocal = new ThreadLocal<DataFormat>(); 

	
	public static DataFormat initDateformatThreadLocal()
	{
		DataFormat dataFormat = dateformatThreadLocal.get();
		if(dataFormat == null)
		{
			dataFormat = new DataFormat();
			dateformatThreadLocal.set(dataFormat);
		}
		else
		{
			dataFormat.increament();
		}
		return dataFormat;
	}
	
	public static DataFormat getDateformatThreadLocal()
	{
		return dateformatThreadLocal.get();
		
		 
	}
	public static void releaseDateformatThreadLocal()
	{
		DataFormat dataFormat = dateformatThreadLocal.get();
		if(dataFormat == null)
			return;
		if(dataFormat.reachroot())
			dateformatThreadLocal.set(null);
		dataFormat.decreament();
	}
	
//	public static void main(String[] args)
//	{
//		 
//		        //假如这个是你已知的时间类型
//		        Calendar cal = Calendar.getInstance();
//		        cal.getTimeInMillis();
//		       
//		        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        String dd = Locale.JAPANESE.toString();
////		        fmt.setTimeZone(TimeZone.getTimeZone("Africa/Algiers"));
//		         String beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println(beijingFormatStr);
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        fmt.setTimeZone(TimeZone.getTimeZone("GMT+9"));
//		          beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println("jpane:"+beijingFormatStr);      
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        dd = Locale.JAPAN.getVariant();
//		        dd = Locale.JAPAN.getCountry();
//		        fmt.setTimeZone(TimeZone.getTimeZone(dd ));
//		          beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println("jpane1:"+beijingFormatStr);      
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        dd = Locale.JAPANESE.toString();
//		        fmt.setTimeZone(TimeZone.getTimeZone("Africa/Algiers"));
//		         beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println(beijingFormatStr);
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        dd = Locale.CHINA.toString();
//		        fmt.setTimeZone(TimeZone.getTimeZone("America/Dawson"));
//		         beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println(beijingFormatStr);
//		        
//		        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		        dd = Locale.JAPANESE.toString();
//		        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
//		         beijingFormatStr = fmt.format(cal.getTime());
//		        System.out.println(beijingFormatStr);
//	}
	
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			if(request == null)
				return new SimpleDateFormat(dateFormat);
			dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
			if(dataFormatUtil == null)
			{
				dataFormatUtil = new DataFormat();
				request.setAttribute(DataFormatUtilKey, dataFormatUtil);
			}
			
		}
		SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat);
		return temp;
	}
	public static Date getDate(HttpServletRequest request,String dateFormat,String date) throws ParseException
	{
		SimpleDateFormat temp = null;

		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
		
			if(request == null)
			{
				temp = new SimpleDateFormat(dateFormat);
			}
			else
			{
				 dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
				if(dataFormatUtil == null)
				{
					dataFormatUtil = new DataFormat();
					request.setAttribute(DataFormatUtilKey, dataFormatUtil);
				}
				temp = dataFormatUtil.getSimpleDateFormat(dateFormat);
			}
		}
		else
		{
			temp = dataFormatUtil.getSimpleDateFormat(dateFormat);
		}
		
		return temp.parse(date);
	}
	
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,Locale locale,TimeZone timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			if(request == null)
			{
				
				SimpleDateFormat format = locale == null ?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat, locale);
				if(timeZone != null)
					format.setTimeZone( timeZone);
				return format;
			}
			dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
			if(dataFormatUtil == null)
			{
				dataFormatUtil = new DataFormat();
				request.setAttribute(DataFormatUtilKey, dataFormatUtil);
			}
		}
		SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,Locale locale,String timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			if(request == null)
			{
				
				SimpleDateFormat format = locale == null ?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat, locale);
				if(timeZone != null)
					format.setTimeZone(TimeZone.getTimeZone(timeZone));
				return format;
			}
			dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
			if(dataFormatUtil == null)
			{
				dataFormatUtil = new DataFormat();
				request.setAttribute(DataFormatUtilKey, dataFormatUtil);
			}
		}
		SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	public static SimpleDateFormat getSimpleDateFormat(HttpServletRequest request,String dateFormat,String locale,String timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			if(request == null)
			{
				
				SimpleDateFormat format = locale == null || locale.equals("")?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat,new Locale(locale));
				if(timeZone != null)
					format.setTimeZone(TimeZone.getTimeZone(timeZone));
				return format;
			}
			dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
			if(dataFormatUtil == null)
			{
				dataFormatUtil = new DataFormat();
				request.setAttribute(DataFormatUtilKey, dataFormatUtil);
			}
		}
		SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
		return temp;
	}
	
	
	
	public static DecimalFormat getDecimalFormat(HttpServletRequest request,String decimalFormat)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			if(request == null)
				return new DecimalFormat(decimalFormat);
			dataFormatUtil = (DataFormat)request.getAttribute(DataFormatUtil.DataFormatUtilKey);
			if(dataFormatUtil == null)
			{
				dataFormatUtil = new DataFormat();
				request.setAttribute(DataFormatUtilKey, dataFormatUtil);
			}
		}
		DecimalFormat temp = dataFormatUtil.getDecimalFormat(decimalFormat);
		return temp;
	}
	
	/**-----------------------------*/
	
	public static SimpleDateFormat getSimpleDateFormat( String dateFormat)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
			return new SimpleDateFormat(dateFormat);
		
		SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat);
		return temp;
	}
	public static Date getDate( String dateFormat,String date) throws ParseException
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		SimpleDateFormat temp = null;
		if(dataFormatUtil != null)
		{
			temp = dataFormatUtil.getSimpleDateFormat(dateFormat);
		}
		else
		{
			temp = new SimpleDateFormat(dateFormat);
		}
		return temp.parse(date);
	}
	
	public static SimpleDateFormat getSimpleDateFormat( String dateFormat,Locale locale,TimeZone timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			
			SimpleDateFormat format = locale == null ?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat, locale);
			if(timeZone != null)
				format.setTimeZone( timeZone);
			return format;
		}
		else
		{
			SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
			return temp;
		}
	}
	public static SimpleDateFormat getSimpleDateFormat( String dateFormat,Locale locale,String timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			
			SimpleDateFormat format = locale == null ?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat, locale);
			if(timeZone != null)
				format.setTimeZone(TimeZone.getTimeZone(timeZone));
			return format;
		}
		else
		{
	 
			SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
			return temp;
		}
	}
	public static SimpleDateFormat getSimpleDateFormat( String dateFormat,String locale,String timeZone)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
		{
			
			SimpleDateFormat format = locale == null || locale.equals("")?new SimpleDateFormat(dateFormat):new SimpleDateFormat(dateFormat,new Locale(locale));
			if(timeZone != null)
				format.setTimeZone(TimeZone.getTimeZone(timeZone));
			return format;
		}
		else
		{
		
			SimpleDateFormat temp = dataFormatUtil.getSimpleDateFormat(dateFormat,locale,timeZone);
			return temp;
		}
	}
	
	
	
	public static DecimalFormat getDecimalFormat( String decimalFormat)
	{
		DataFormat dataFormatUtil = getDateformatThreadLocal();
		if(dataFormatUtil == null)
			return new DecimalFormat(decimalFormat);
		 
		DecimalFormat temp = dataFormatUtil.getDecimalFormat(decimalFormat);
		return temp;
	}

}
