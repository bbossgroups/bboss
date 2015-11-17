package org.frameworkset.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
