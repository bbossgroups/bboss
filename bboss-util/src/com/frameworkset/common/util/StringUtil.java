/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.DataFormatUtil;



/**
 * To change for your class or interface
 * DAO中VOObject String类型与PO数据类型转换工具类.
 * @author wangyixing
 * @version 1.0
 */

public class StringUtil {
	//逗号常量
	public static final String COMMA = ",";
	//空串常量
	public static final String BLANK = "";
	
	/**
	 * 将一个字符串根据逗号分拆
	 * */
	public static String[] split(String s) {
		return split(s, COMMA);
	}

	/**
	 * 将字符串根据给定分隔符分拆
	 * */
	public static String[] split(String s, String delimiter) {		
		return s.split(delimiter);
	    
//		if (s == null || delimiter == null) {
//			return new String[0];
//		}
//
//		s = s.trim();
//
//		if (!s.endsWith(delimiter)) {
//			s += delimiter;
//		}
//
//		if (s.equals(delimiter)) {
//			return new String[0];
//		}
//
//		List nodeValues = new ArrayList();
//
//		if (delimiter.equals("\n") || delimiter.equals("\r")) {
//			try {
//				BufferedReader br = new BufferedReader(new StringReader(s));
//
//				String line = null;
//
//				while ((line = br.readLine()) != null) {
//					nodeValues.add(line);
//				}
//
//				br.close();
//			}
//			catch (IOException ioe) {
//				ioe.printStackTrace();
//			}
//		}
//		else {
//			int offset = 0;
//			int pos = s.indexOf(delimiter, offset);
//
//			while (pos != -1) {
//				nodeValues.add(s.substring(offset, pos));
//
//				offset = pos + delimiter.length();
//				pos = s.indexOf(delimiter, offset);
//			}
//		}
//
//		return (String[])nodeValues.toArray(new String[0]);
	}	

	
	public static String getRealPath(HttpServletRequest request,String path)	
	{
		String contextPath = request.getContextPath();
		if(path == null)
			return null;
		if(path.startsWith("/") && !path.startsWith(contextPath + "/"))
			return contextPath + path;
		else
			return path;
		
	}
	
	public static boolean containKey(String[] values,String key)		
	{
		if(values == null || key == null)
			return false;
		boolean contain = false;
		for(int i = 0; i < values.length; i ++)
		{
		    
//			    System.out.println("values[" + i + "]:" + values[i]);
//			    System.out.println("key:" + key);
			if(values[i].equals(key))
			{
				contain = true;
				break;
			}
		}
		return contain;
	}
	
	public static String getFormatDate(Date date,String formate)
	{
	    SimpleDateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(formate);			
		if (date == null)
			return null;
		return dateFormat.format(date);

	}
	
	public static Date stringToDate(String date)
	{	   
	    if(date == null || date.trim().equals(""))
	        return null;
	        
	    date = date.replace('-','/');
	    SimpleDateFormat format  = new SimpleDateFormat();
	    try {
            return format.parse(date);
        } catch (ParseException e) {            
    	    return new Date(date);
        }
	}
	
	/**
	 * 获取文件名称
	 * @param path 文件路经
	 * @return String
	 */
	public static String getFileName(String path)
	{	
	    int index = path.lastIndexOf('/');
	    String fileName = "";
	    if(index == -1)
	        index = path.lastIndexOf('\\');	    
	    
    	fileName = path.substring(index + 1);    	
    	return fileName ;
	}	
	
	public static String getFileName(String prefix, String extension) throws UnsupportedEncodingException {
        //prefix = MessageUtility.getValidFileName(prefix);
        //UTF8 URL encoding only works in IE, not Mozilla
        String fileName = URLEncoder.encode(prefix, "UTF-8");
        //Bug of IE (http://support.microsoft.com/?kbid=816868)
        //Cannot be more than 150(I don't know the exact number)
        int limit = 150 - extension.length();
        if (fileName.length() > limit) {
            //because the UTF-8 encoding scheme uses 9 bytes to represent a single CJK character
            fileName = URLEncoder.encode(prefix.substring(0, Math.min(prefix.length(), limit / 9)), "UTF-8");
        }
        return fileName + extension;
    }

}
