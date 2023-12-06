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
 *                                                                             *
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
package com.frameworkset.util;

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.util.encoder.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * To change for your class or interface DAO中VOObject String类型与PO数据类型转换工具类.
 * 
 * @author biaoping.yin
 * @version 1.0
 */

public class BaseSimpleStringUtil {
//	private static final SimpleDateFormat format = new SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");

	protected static final Logger logger = LoggerFactory.getLogger(BaseSimpleStringUtil.class);
	// 逗号常量
	public static final String COMMA = ",";

	// 空串常量
	public static final String BLANK = "";
	public static String getPath(String contextPath, String path) {

		if(contextPath == null || contextPath.equals("") )
			return path;
		if(path == null || path.equals("")){
			return contextPath;
		}
		StringBuilder builder = new StringBuilder();

		builder.append(contextPath);
        boolean contextPathEndsWith = contextPath.endsWith("/");
        boolean pathEndsWith = path.endsWith("/");
		if(contextPathEndsWith || pathEndsWith){
			if(contextPathEndsWith && pathEndsWith){
				builder.append(path.substring(1));
			}
			else {
				builder.append(path);
			}
		}
		else{
			builder.append("/").append(path);
		}
		return builder.toString();

	}

	/**
	 * A constant passed to the {@link # split()}methods indicating that
	 * all occurrences of a pattern should be used to split a string.
	 */
	public static final int SPLIT_ALL = 0;

	/**
	 * 以下的变量：dontNeedEncoding，dfltEncName，caseDiff 是从jdk 1.4 java.net.URLEncoder
	 * 下移植过来
	 */

	private static BitSet dontNeedEncoding;

	private static String dfltEncName = null;
	private static String getDfltEncName(){
		if(dfltEncName == null) {
			synchronized (BaseSimpleStringUtil.class) {
				if(dfltEncName == null) {
					dfltEncName = System.getProperty("file.encoding");
				}
			}
		}
		return dfltEncName;
	}


	static final int caseDiff = ('a' - 'A');

	private static BitSet initDontNeedEncoding(){

		/*
		 * The list of characters that are not encoded has been determined as
		 * follows:
		 * 
		 * RFC 2396 states: ----- Data characters that are allowed in a URI but
		 * do not have a reserved purpose are called unreserved. These include
		 * upper and lower case letters, decimal digits, and a limited set of
		 * punctuation marks and symbols.
		 * 
		 * unreserved = alphanum | mark
		 * 
		 * mark = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
		 * 
		 * Unreserved characters can be escaped without changing the semantics
		 * of the URI, but this should not be done unless the URI is being used
		 * in a context that does not allow the unescaped character to appear.
		 * -----
		 * 
		 * It appears that both Netscape and Internet Explorer escape all
		 * special characters from this list with the exception of "-", "_",
		 * ".", "*". While it is not clear why they are escaping the other
		 * characters, perhaps it is safest to assume that there might be
		 * contexts in which the others are unsafe if not escaped. Therefore, we
		 * will use the same list. It is also noteworthy that this is consistent
		 * with O'Reilly's "HTML: The Definitive Guide" (page 164).
		 * 
		 * As a last note, Intenet Explorer does not encode the "@" character
		 * which is clearly not unreserved according to the RFC. We are being
		 * consistent with the RFC in this matter, as is Netscape.
		 */

		if(dontNeedEncoding == null) {
			synchronized (BaseSimpleStringUtil.class) {
				if(dontNeedEncoding == null) {
					dontNeedEncoding = new BitSet(256);
					int i;
					for (i = 'a'; i <= 'z'; i++) {
						dontNeedEncoding.set(i);
					}
					for (i = 'A'; i <= 'Z'; i++) {
						dontNeedEncoding.set(i);
					}
					for (i = '0'; i <= '9'; i++) {
						dontNeedEncoding.set(i);
					}
					dontNeedEncoding.set(' '); /*
					 * encoding a space to a + is done in the
					 * encode() method
					 */
					dontNeedEncoding.set('-');
					dontNeedEncoding.set('_');
					dontNeedEncoding.set('.');
					dontNeedEncoding.set('*');
				}
			}
		}


		return dontNeedEncoding;
	}
	private static String ipHost;
	private static String ip;
	private static String hostName;
	private static void init(){
		if(ip == null) {
			try {
				InetAddress addr = InetAddress.getLocalHost();
				String ip_ = addr.getHostAddress();//获得本机IP
				String address = addr.getHostName();//获得本机名称
				ipHost = ipHost + "-" + address;
				hostName = address;
				ip = ip_;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}
	public static String getIp()
	{
		if(ip != null)
			return ip;
		synchronized (BaseSimpleStringUtil.class) {
			init();
		}
		return ip;
	}
	/**
	 * 获取服务器IP和名称
	 * @return
	 */
	public static String getHostIP()
	  {
		  if(ipHost != null)
			  return ipHost;
		  synchronized (BaseSimpleStringUtil.class) {
			  init();
		  }
		  return ipHost;
	  }

	  public static String getHostName(){
		  if(hostName != null)
			  return hostName;
		  synchronized (BaseSimpleStringUtil.class) {
			  init();
		  }
		  return hostName;
	  }



	/**
	 * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
	 * @param timeZoneString the time zone {@code String}, following {@link TimeZone#getTimeZone(String)}
	 * but throwing {@link IllegalArgumentException} in case of an invalid time zone specification
	 * @return a corresponding {@link TimeZone} instance
	 * @throws IllegalArgumentException in case of an invalid time zone specification
	 */
	public static TimeZone parseTimeZoneString(String timeZoneString) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
		if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
			// We don't want that GMT fallback...
			throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
		}
		return timeZone;
	}

	
	
	public static String getRealPath(String contextPath, String path) {
		
		
		return getRealPath(contextPath, path,false); 

	}

	public static void getRealPath(StringBuilder builder,String contextPath, String path) {


		getRealPath(builder,contextPath, path,false);

	}
	
	public static boolean isHttpUrl(String path)
	{
		if(path == null)
			return false;
		return path.startsWith("http://") 
				||  path.startsWith("https://") 
				||  path.startsWith("ftp://")   
				||  path.startsWith("tps://")  ;
	}
	public static String getUUID()
	{
		return UUID.randomUUID().toString();
	}
	public static String getRealPath(String contextPath, String path,boolean usebase) {
		
		if(isHttpUrl(path))
			return path;
		if (contextPath == null || contextPath.equals("") || contextPath.equals("/")) {
//			System.out.println("SimpleStringUtil.getRealPath() contextPath:"
//					+ contextPath);
			if(usebase)//如果需要检测上下文路径为空串，那么如果path第一个字符不是/，那么需要补充字符/到第一个位置
			{
				if(isEmpty(path))
					return "/";
				else
				{
					return path.startsWith("/")?path:new StringBuilder().append("/").append(path).toString();
				}
			}
			else
			{
				return path == null?"":path;
			}
				
		}
		if (path == null || path.equals("")) {
			
			return contextPath;
		}
		
		contextPath = contextPath.replace('\\', '/');
		path = path.replace('\\', '/');
		if (path.startsWith("/") ) {
			
			if (!contextPath.endsWith("/"))
				return new StringBuilder().append(contextPath).append( path).toString();
			else {
				return new StringBuilder().append(contextPath.substring(0,contextPath.length() - 1)).append( path).toString();
			}

		} else {
			if (!contextPath.endsWith("/"))
				return new StringBuilder().append(contextPath).append("/" ).append(path).toString();
			else {
				return new StringBuilder().append(contextPath).append( path).toString();
			}
		}

	}

	public static void getRealPath(StringBuilder builder,String contextPath, String path,boolean usebase) {

		if(isHttpUrl(path)) {
			builder.append(path);
			return;
		}
		if (contextPath == null || contextPath.equals("") || contextPath.equals("/")) {
//			System.out.println("SimpleStringUtil.getRealPath() contextPath:"
//					+ contextPath);
			if(usebase)//如果需要检测上下文路径为空串，那么如果path第一个字符不是/，那么需要补充字符/到第一个位置
			{
				if(isEmpty(path)) {
					builder.append("/");
					return;
				}
				else
				{
					if(path.startsWith("/"))
						builder.append(path);
					else
						builder.append("/").append(path);
					return;
				}
			}
			else
			{
				if(path != null)
					builder.append(path);
				return;
			}

		}
		if (path == null || path.equals("")) {

			builder.append( contextPath);
			return;
		}

		contextPath = contextPath.replace('\\', '/');
		path = path.replace('\\', '/');
		if (path.startsWith("/") ) {

			if (!contextPath.endsWith("/")) {
				builder.append(contextPath).append(path);
				return;
			}
			else {
				builder.append(contextPath.substring(0,contextPath.length() - 1)).append( path);
				return;
			}

		} else {
			if (!contextPath.endsWith("/")) {
				builder.append(contextPath).append("/").append(path);
			}
			else {
				builder.append(contextPath).append( path);
				return;
			}
		}

	}

	public static boolean containKey(String[] values, String key) {
		if (values == null || key == null) {
			return false;
		}
		boolean contain = false;
		for (int i = 0; i < values.length; i++) {

			// System.out.println("values[" + i + "]:" + values[i]);
			// System.out.println("key:" + key);
			if (values[i].equals(key)) {
				contain = true;
				break;
			}
		}
		return contain;
	}
	
	public static String getNormalPath(String parent,String file)
	{
//		if(parent.equals("") )
//			return file;
//		if(file.equals("")){
//			return parent;
//		}
//		if(parent.endsWith("/") && !file.startsWith("/"))
//			return parent + file;
//		else if(!parent.endsWith("/") && file.startsWith("/"))
//			return parent + file;
//		else if(!parent.endsWith("/") && !file.startsWith("/"))
//			return parent + "/"+file;
//		else //if(parent.endsWith("/") && file.startsWith("/"))
//			return parent + file.substring(1);
		return getPath(parent, file);

	}

	public static String getFormatDate(Date date, String formate) {
		SimpleDateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(formate);
		if (date == null) {
			return null;
		}
		return dateFormat.format(date);

	}

	public static Date stringToDate(String date) {
		if (date == null || date.trim().equals("")) {
			return null;
		}
		SimpleDateFormat format = DataFormatUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// date = date.replace('-', '/');
		// SimpleDateFormat format = new SimpleDateFormat();
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return new Date(date);
		}
	}

	public static Date stringToDate(String date, String format_) {
		if (date == null || date.trim().equals("")) {
			return null;
		}

		// date = date.replace('-', '/');
		SimpleDateFormat format = DataFormatUtil.getSimpleDateFormat(format_);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return new Date(date);
		}
	}

	/**
	 * 获取文件名称
	 * 
	 * @param path
	 *            文件路经
	 * @return String
	 */
	public static String getFileName(String path) {
		int index = path.lastIndexOf('/');
		String fileName = "";
		if (index == -1) {
			index = path.lastIndexOf('\\');
		}

		fileName = path.substring(index + 1);
		return fileName;
	}

	public static String getFileName(String prefix, String extension)
			throws UnsupportedEncodingException {
		// prefix = MessageUtility.getValidFileName(prefix);
		// UTF8 URL encoding only works in IE, not Mozilla
		String fileName = URLEncoder.encode(prefix);
		// Bug of IE (http://support.microsoft.com/?kbid=816868)
		// Cannot be more than 150(I don't know the exact number)
		int limit = 150 - extension.length();
		if (fileName.length() > limit) {
			// because the UTF-8 encoding scheme uses 9 bytes to represent a
			// single CJK character
			fileName = URLEncoder.encode(prefix.substring(0, Math.min(prefix
					.length(), limit / 9)));
		}
		return fileName + extension;
	}

	/**
	 * 将日期数组转换成字符串数组
	 * 
	 * @param dates
	 *            Date[] 日期数组
	 * @return String[] 字符串数组
	 */
	public static String[] dateArrayTOStringArray(Date[] dates) {
		if (dates == null) {
			return null;
		}
		SimpleDateFormat format = DataFormatUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] dates_s = new String[dates.length];
		for (int i = 0; i < dates.length; i++) {
			dates_s[i] = format.format(dates[i]);
		}
		return dates_s;
	}

	/**
	 * 将字符串数组转换成日期数组
	 * 
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static Date[] stringArrayTODateArray(String[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		Date[] dates = new Date[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {
			if(dateformat != null)
			{
				try {
					dates[i] = dateformat.parse((dates_s[i]));
				} catch (ParseException e) {
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new Date(dl);
				}
				
			}
			else
			{
				try
				{
					dates[i] = new Date(dates_s[i]);
				}
				catch (Exception e)
				{
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new Date(dl);
				}
			}
			
		}
		return dates;
	}
	
	/**
	 * 将字符串数组转换成日期数组
	 * 
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static Date[] longArrayTODateArray(long[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		Date[] dates = new Date[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {
			
			{
				dates[i] = new Date(dates_s[i]);
			}
			
		}
		return dates;
	}

	/**
	 * 将字符串数组转换成日期数组
	 * 
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static java.sql.Date[] stringArrayTOSQLDateArray(String[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		java.sql.Date[] dates = new java.sql.Date[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {
			if(dateformat != null)
			{
				try {
					dates[i] = new java.sql.Date(dateformat.parse((dates_s[i]))
					.getTime());
				} catch (ParseException e) {
//					dates[i] = new java.sql.Date(new java.util.Date(dates_s[i])
//					.getTime());
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new java.sql.Date(dl);
				}
				
			}
			else
			{
				try
				{
					dates[i] = new java.sql.Date(new Date(dates_s[i])
					.getTime());
				}
				catch (Exception e)
				{
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new java.sql.Date(dl);
				}
			}

		}
		return dates;
	}

	/**
	 * 将字符串数组转换成日期数组
	 *
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static java.sql.Date[] longArrayTOSQLDateArray(long[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		java.sql.Date[] dates = new java.sql.Date[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {

			{
				dates[i] = new java.sql.Date(dates_s[i]);
			}

		}
		return dates;
	}

	/**
	 * 将字符串数组转换成日期数组
	 *
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static java.sql.Timestamp[] stringArrayTOTimestampArray(String[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		java.sql.Timestamp[] dates = new java.sql.Timestamp[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {
			if(dateformat != null)
			{
				try {
					dates[i] = new java.sql.Timestamp(dateformat.parse((dates_s[i]))
					.getTime());
				} catch (ParseException e) {
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new java.sql.Timestamp(dl);
//					dates[i] = new java.sql.Timestamp(new java.util.Date(dates_s[i])
//					.getTime());
				}

			}
			else
			{
				try
				{
					dates[i] = new java.sql.Timestamp(new Date(dates_s[i])
					.getTime());
				}
				catch (Exception e)
				{
					long dl = Long.parseLong(dates_s[i]);
					dates[i] = new java.sql.Timestamp(dl);
				}
			}
		}
		return dates;
	}

	/**
	 * 将字符串数组转换成日期数组
	 *
	 *            Date[] 字符串数组
	 * @return String[] 日期数组
	 */
	public static java.sql.Timestamp[] longArrayTOTimestampArray(long[] dates_s,DateFormat dateformat) {
		if (dates_s == null) {
			return null;
		}
		java.sql.Timestamp[] dates = new java.sql.Timestamp[dates_s.length];
		for (int i = 0; i < dates_s.length; i++) {

			{
				dates[i] = new java.sql.Timestamp(dates_s[i]);
			}
		}
		return dates;
	}

	/**
     * <p>Replaces all occurrences of a String within another String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int max)
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }
    /**
     * Represents a failed index search.
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;
    /**
     * <p>Replaces a String with another String inside a larger String,
     * for the first {@code max} values of the search String.</p>
     *
     * <p>A {@code null} reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *, *)         = null
     * StringUtils.replace("", *, *, *)           = ""
     * StringUtils.replace("any", null, *, *)     = "any"
     * StringUtils.replace("any", *, null, *)     = "any"
     * StringUtils.replace("any", "", *, *)       = "any"
     * StringUtils.replace("any", *, *, 0)        = "any"
     * StringUtils.replace("abaa", "a", null, -1) = "abaa"
     * StringUtils.replace("abaa", "a", "", -1)   = "b"
     * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
     * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
     * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
     * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
	/**
     * The empty String {@code ""}.
     * @since 2.0
     */
    public static final String EMPTY = "";

	/**
	 *
	 * @param str
	 * @param searchChars
	 * @param replaceChars
	 * @return
	 */
	public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (isEmpty(str) || isEmpty(searchChars)) {
            return str;
        }
        if (replaceChars == null) {
            replaceChars = EMPTY;
        }
        boolean modified = false;
        int replaceCharsLength = replaceChars.length();
        int strLength = str.length();
        StringBuilder buf = new StringBuilder(strLength);
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            int index = searchChars.indexOf(ch);
            if (index >= 0) {
                modified = true;
                if (index < replaceCharsLength) {
                    buf.append(replaceChars.charAt(index));
                }
            } else {
                buf.append(ch);
            }
        }
        if (modified) {
            return buf.toString();
        }
        return str;
    }



	public static String replaceNull(String resource) {
		return resource == null ? "" : resource;
	}



	/**
	 * Translates a string into <code>application/x-www-form-urlencoded</code>
	 * format using a specific encoding scheme. This method uses the supplied
	 * encoding scheme to obtain the bytes for unsafe characters.
	 * <p>
	 * <em><strong>Note:</strong> The <a href=
	 * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that
	 * UTF-8 should be used. Not doing so may introduce
	 * incompatibilites.</em>
	 *
	 * @param s
	 *            <code>String</code> to be translated.
	 * @param enc
	 *            The name of a supported <a
	 *            href="../lang/package-summary.html#charenc">character encoding
	 *            </a>.
	 * @return the translated <code>String</code>.
	 * @exception UnsupportedEncodingException
	 *                If the named encoding is not supported
	 * @see URLDecoder#decode(String, String)
	 * @since 1.4
	 */
	public static String encode(String s, String enc) {
		if (enc == null || enc.trim().equals("")) {
			enc = getDfltEncName();
		}
		boolean needToChange = false;
		boolean wroteUnencodedChar = false;
		int maxBytesPerChar = 10; // rather arbitrary limit, but safe for now
		StringBuilder out = new StringBuilder(s.length());
		ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);

		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(buf, enc);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < s.length(); i++) {
			int c = (int) s.charAt(i);
			// System.out.println("Examining character: " + c);
			if (initDontNeedEncoding().get(c)) {
				if (c == ' ') {
					c = '+';
					needToChange = true;
				}
				// System.out.println("Storing: " + c);
				out.append((char) c);
				wroteUnencodedChar = true;
			} else {
				// convert to external encoding before hex conversion
				try {
					if (wroteUnencodedChar) { // Fix for 4407610
						writer = new OutputStreamWriter(buf, enc);
						wroteUnencodedChar = false;
					}
					writer.write(c);
					/*
					 * If this character represents the start of a Unicode
					 * surrogate pair, then pass in two characters. It's not
					 * clear what should be done if a bytes reserved in the
					 * surrogate pairs range occurs outside of a legal surrogate
					 * pair. For now, just treat it as if it were any other
					 * character.
					 */
					if (c >= 0xD800 && c <= 0xDBFF) {
						/*
						 * System.out.println(Integer.toHexString(c) + " is high
						 * surrogate");
						 */
						if ((i + 1) < s.length()) {
							int d = (int) s.charAt(i + 1);
							/*
							 * System.out.println("\tExamining " +
							 * Integer.toHexString(d));
							 */
							if (d >= 0xDC00 && d <= 0xDFFF) {
								/*
								 * System.out.println("\t" +
								 * Integer.toHexString(d) + " is low
								 * surrogate");
								 */
								writer.write(d);
								i++;
							}
						}
					}
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}
				byte[] ba = buf.toByteArray();
				for (int j = 0; j < ba.length; j++) {
					out.append('%');
					char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
					// converting to use uppercase letter as part of
					// the hex value if ch is a letter.
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
					ch = Character.forDigit(ba[j] & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
				}
				buf.reset();
				needToChange = true;
			}
		}

		return (needToChange ? out.toString() : s);
	}

	/**
	 * Translates a string into <code>application/x-www-form-urlencoded</code>
	 * format using a specific encoding scheme. This method uses the supplied
	 * encoding scheme to obtain the bytes for unsafe characters.
	 * <p>
	 * <em><strong>Note:</strong> The <a href=
	 * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that
	 * UTF-8 should be used. Not doing so may introduce
	 * incompatibilites.</em>
	 *
	 * @param s
	 *            <code>String</code> to be translated.
	 *            The name of a supported <a
	 *            href="../lang/package-summary.html#charenc">character encoding
	 *            </a>.
	 * @return the translated <code>String</code>.
	 * @exception UnsupportedEncodingException
	 *                If the named encoding is not supported
	 * @see URLDecoder#decode(String, String)
	 * @since 1.4
	 */
	public static String encode(String s) {
		return encode(s, getDfltEncName());
	}

	public static String toUTF(String inpara) {

		char temchr;

		int ascchr;

		int i;

		String rtstr = new String("");

		if (inpara == null) {

			inpara = "";

		}

		for (i = 0; i < inpara.length(); i++) {
			temchr = inpara.charAt(i);
			ascchr = temchr + 0;
			// System.out.println(ascchr);

			// System.out.println(Integer.toBinaryString(ascchr));

			rtstr = rtstr + "&#x" + Integer.toHexString(ascchr) + ";";

		}

		return rtstr;

	}

	public static String toGB2312(String inpara) {

		// System.out.println("原来的字符串为：" + inpara);

		if (inpara == null) {

			inpara = "";

		}

		try {

			char[] temp = inpara.toCharArray();

			byte[] b = new byte[temp.length];

			// System.out.println("分成char[]后字符串的为：" + temp);

			// System.out.println("传递字符串的长度为：" + temp.length);

			int tempint;

			for (int i = 0; i < temp.length; i++) {

				b[i] = (byte) temp[i];

				tempint = (int) b[i];

				// System.out.println("第" + i + "个字符的编码为：" + tempint +
				// "\t二进制码为：" +
				// Integer.toBinaryString(tempint));
			}

			String deststring = new String(b, "gb2312");

			// System.out.println(deststring);

			return deststring;

		} catch (UnsupportedEncodingException e) {
			return "不支持的字符编码";
		}
	}

	public List splitString(String src, int size) {
		if (src == null)
			return null;
		List segs = new ArrayList();
		StringBuilder seg = new StringBuilder();
		while (src.length() > size) {

		}
		if (src.length() <= size) {
			segs.add(src);
			return segs;
		}

		// int length = msg.length();
		// int splitNum = (int) (length / step) + 1;
		// boolean flag = true;
		// if (splitNum > 10) {
		// splitNum = 10;
		// flag = false;
		// }
		// int len = 0;
		// for (int i = 0; i < splitNum; i++) {
		// if (i == 0) {
		// String spMsg = msg.substring(0, step) +
		// getFirstEnd(splitNum);
		// v.addElement(spMsg);
		// spMsg = null;
		// len = step;
		// continue;
		// }
		// if (i == splitNum - 1) {
		// String spMsg = null;
		// if (flag == false)
		// spMsg = getSecondFirst(i, splitNum) +
		// msg.substring(len, len + step);
		// v.addElement(spMsg);
		// spMsg = null;
		// continue;
		// }
		// String spMsg = null;
		// spMsg = getSecondFirst(i, splitNum) +
		// msg.substring(len, len + step) +
		// getSecondEnd(i, splitNum);
		// v.addElement(spMsg);
		// spMsg = null;
		// }
		return null;

	}

	public String splitString(String src, int offset, int size) {
		if (src == null || src.equals("")) {
			return "";
		}
		if (offset < src.length()) {
			return src.substring(offset, size);
		} else {
			int newoffset = src.length() % size;

		}
		return (String) splitString(src, size).get(offset);
	}

	public static String replaceNull(String value, String nullReplace) {
		return value == null ? nullReplace : value;
	}

	public static boolean getBoolean(String value, boolean nullReplace) {
		boolean ret = false;
		if (value == null)
			ret = nullReplace;
		else if (value.trim().equalsIgnoreCase("true"))
			ret = true;
		else
			ret = false;
		return ret;

	}

	public static int getInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 判断链接是否是javascript函数
	 *
	 * @param nodeLink
	 * @return
	 */
	public static boolean isJavascript(String nodeLink) {
		return nodeLink != null
				&& nodeLink.toLowerCase().startsWith("javascript:");

	}


	/**
	 * 构建消息日志
	 *
	 * @param messages
	 * @return
	 */
	public static String buildStringMessage(List messages) {
		if (messages == null || messages.size() == 0)
			return null;
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < messages.size(); i++) {
			if (i == 0)
				str.append(messages.get(i));
			else
				str.append("\\n").append(messages.get(i));

		}
		return str.toString();
	}

	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}
	/**
	 * Check whether the given CharSequence has actual text.
	 * More specifically, returns <code>true</code> if the string not <code>null</code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not <code>null</code>,
	 * its length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Check that the given CharSequence is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
	 * <p><pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	/**
	 * Test if the given String starts with the specified prefix,
	 * ignoring upper/lower case.
	 * @param str the String to check
	 * @param prefix the prefix to look for
	 * @see String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Compare two paths after normalization of them.
	 * @param path1 first path for comparison
	 * @param path2 second path for comparison
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}
	/**
	 * Normalize the path by suppressing sequences like "path/.." and
	 * inner simple dots.
	 * <p>The result is convenient for path comparison. For other uses,
	 * notice that Windows separators ("\") are replaced by simple slashes.
	 * @param path the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			}
			else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			}
			else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				}
				else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of potential
	 * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of potential
	 * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single delimiter,
	 * rather than a bunch individual delimiter characters)
	 * @param charsToDelete a set of characters to delete. Useful for deleting unwanted
	 * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] {str};
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static Integer[] toIntArray(Collection<Integer> collection) {
		if (collection == null) {
			return null;
		}
		return (Integer[]) collection.toArray(new Integer[collection.size()]);
	}



	/**
	 * Copy the given Enumeration into a String array.
	 * The Enumeration must contain String elements only.
	 * @param enumeration the Enumeration to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Enumeration was <code>null</code>)
	 */
	public static String[] toStringArray(Enumeration enumeration) {
		if (enumeration == null) {
			return null;
		}
		List list = Collections.list(enumeration);
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Trim the elements of the given String array,
	 * calling <code>String.trim()</code> on each of them.
	 * @param array the original String array
	 * @return the resulting array (of the same size) with trimmed elements
	 */
	public static String[] trimArrayElements(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String element = array[i];
			result[i] = (element != null ? element.trim() : null);
		}
		return result;
	}

	/**
	 * Remove duplicate Strings from the given array.
	 * Also sorts the array, as it uses a TreeSet.
	 * @param array the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toStringArray(set);
	}

	/**
	 * Delete any character in a given String.
	 * @param inString the original String
	 * @param charsToDelete a set of characters to delete.
	 * E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @param prefix the String to start each element with
	 * @param suffix the String to end each element with
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}
	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display
	 * @param delim the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a CSV String.
	 * E.g. useful for <code>toString()</code> implementations.
	 * @param coll the Collection to display
	 * @return the delimited String
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String
	 * was <code>null</code>)
	 * @see StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}
	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	/**
	 * Apply the given relative path to the given path,
	 * assuming standard Java folder separation (i.e. "/" separators);
	 * @param path the path to start from (usually a full file path)
	 * @param relativePath the relative path to apply
	 * (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		}
		else {
			return relativePath;
		}
	}




	/**
	 * Extract the filename from the given path,
	 * e.g. "mypath/myfile.txt" -> "myfile.txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * Convenience method to return a String array as a CSV String.
	 * E.g. useful for <code>toString()</code> implementations.
	 * @param arr the array to display
	 * @return the delimited String
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for <code>toString()</code> implementations.
	 * @param arr the array to display
	 * @param delim the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Trim trailing whitespace from the given String.
	 * @param str the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied leading character from the given String.
	 * @param str the String to check
	 * @param leadingCharacter the leading character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied trailing character from the given String.
	 * @param str the String to check
	 * @param trailingCharacter the trailing character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim leading whitespace from the given String.
	 * @param str the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder buf = new StringBuilder(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Count the occurrences of the substring in string s.
	 * @param str string to search in. Return 0 if this is null.
	 * @param sub string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Strip the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "mypath/myfile".
	 * @param path the file path (may be <code>null</code>)
	 * @return the path with stripped filename extension,
	 * or <code>null</code> if none
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * @param str the input String
	 * @return an array of Strings, or the empty array in case of empty input
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Append the given String to the given String array, returning a new array
	 * consisting of the input array contents plus the given String.
	 * @param array the array to append to (can be <code>null</code>)
	 * @param str the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] {str};
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * Trim <i>all</i> whitespace from the given String:
	 * leading, trailing, and inbetween characters.
	 * @param str the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuilder buf = new StringBuilder(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			}
			else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * Parse the given <code>localeString</code> into a {@link Locale}.
	 * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
	 * @param localeString the locale string, following <code>Locale's</code>
	 * <code>toString()</code> format ("en", "en_UK", etc);
	 * also accepts spaces as separators, as an alternative to underscores
	 * @return a corresponding <code>Locale</code> instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = "";
		if (parts.length >= 2) {
			// There is definitely a variant, and it is everything after the country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}

	/**
	 * Determine the RFC 3066 compliant language tag,
	 * as used for the HTTP "Accept-Language" header.
	 * @param locale the Locale to transform to a language tag
	 * @return the RFC 3066 compliant language tag as String
	 */
	public static String toLanguageTag(Locale locale) {
		return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
	}


	/**
	 * Extract the filename extension from the given path,
	 * e.g. "mypath/myfile.txt" -> "txt".
	 * @param path the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}



 	public static final String AOP_PROPERTIES_PATH = "/aop.properties";
	public static InputStream getInputStream(String resourcefile,Class clazz) throws IOException {
		InputStream is = null;

		is = clazz.getResourceAsStream(resourcefile);
		if (is == null) {
			throw new FileNotFoundException(resourcefile
					+ " cannot be opened because it does not exist");
		}
		return is;
	}

	public static Properties getProperties(String resourcefile,Class clazz) throws IOException {
		InputStream is = getInputStream(resourcefile,clazz);
		try {
			Properties props = new Properties();
			props.load(is);
			return props;

		} finally {
			is.close();
		}
	}

	public static boolean isEmpty(String value)
	{
		return value == null || "".equals(value);
	}

	public static boolean isNotEmpty(String value)
	{
		return value != null && !"".equals(value);
	}


	public static boolean isEmpty(Collection cl)
	{
		return cl == null || cl.size() == 0;
	}

	public static boolean isNotEmpty(Collection cl)
	{
		return !isEmpty( cl);
	}

	public static boolean isEmpty(Object obj)
	{
		if(obj == null)
			return true;
		if(obj instanceof String)
		{
			return isEmpty((String)obj);
		}
		if(obj instanceof Collection)
		{
			return isEmpty((Collection)obj);
		}
		else if(!obj.getClass().isArray())
		{
			return false;
		}
		else
		{
			return Array.getLength(obj) == 0;
		}

	}

	public static boolean isNotEmpty(Object obj)
	{
		return !isEmpty( obj);
	}

	public static String formatException(Throwable exception)
	{
		StringWriter out = new StringWriter();
		exception.printStackTrace(new PrintWriter(out));
		String errorMessage = out.toString();
		errorMessage = errorMessage.replaceAll("\\n",
				"\\\\n");
		errorMessage = errorMessage.replaceAll("\\r",
				"\\\\r");
		return errorMessage;
	}

	public static String formatBRException(Throwable exception)
	{
		StringWriter out = new StringWriter();
		exception.printStackTrace(new PrintWriter(out));
		String errorMessage = out.toString();
		errorMessage = errorMessage.replaceAll("\\n",
				"<br/>");
		errorMessage = errorMessage.replaceAll("\\r",
				"<br/>");
		return errorMessage;
	}

	public static String exceptionToString(Throwable exception)
	{
		StringWriter out = new StringWriter();
		try {
			exception.printStackTrace(new PrintWriter(out));
			String errorMessage = out.toString();
			return errorMessage;
		}
		finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}

//		errorMessage = errorMessage.replaceAll("\\n",
//				"\\\\n");
//		errorMessage = errorMessage.replaceAll("\\r",
//				"\\\\r");

	}
	
//	/**
//	 * 对value采用charset进行URLEncode编码，编码的次数根据encoudtimes指定
//	 * @param value
//	 * @param charset
//	 * @param encoudtimes
//	 * @return
//	 */
//	public static String urlencode(String value,String charset,int encodecount)
//	{
//		if(value == null)
//			return value;
//		else if(encodecount <= 1)
//			try {
//				value = URLEncoder.encode(value,"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else if(encodecount == 2)
//			try {
//				value = URLEncoder.encode(URLEncoder.encode(value,"UTF-8"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else if(encodecount == 3)
//			try {
//				value = URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(value,"UTF-8"),"UTF-8"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else
//			try {
//				for(int i = 0; i < encodecount; i ++)
//				{
//					value = URLEncoder.encode(value,"UTF-8");
//				}
//				return value;
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		return value;
//		
//	}
	
	/**
	 * 对value采用charset进行URLEncode编码，编码的次数根据encoudtimes指定
	 * @param value
	 * @param charset
	 * @param encodecount
	 * @return
	 */
	public static String urlencode(String value,String charset,int encodecount)
	{
 		if(value == null)
			return value;
		else if(encodecount <= 1)
			try {
				value = URLEncoder.encode(value,charset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(encodecount == 2)
			try {
				value = URLEncoder.encode(URLEncoder.encode(value,charset),charset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else if(encodecount == 3)
			try {
				value = URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(value,charset),charset),charset);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			try {
				for(int i = 0; i < encodecount; i ++)
				{
					value = URLEncoder.encode(value,charset);
				}
				return value;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return value;
		
	}
	
	/**
	 * 对value采用charset进行URLEncode编码，编码的次数为1
	 * @param value
	 * @param charset
	 * @return
	 */
	public static String urlencode(String value,String charset)
	{
		return urlencode(value,charset,1);
	}
	
	
	private static Map<String,Locale> localesIndexByString;
	public static Locale getLocale(String locale)
	{
		return getAllLocales().get(locale);
	}
	
	  /**
     * 
    
   
     * @return
     */
    public static Map<String,Locale> getAllLocales()
	{
    	if(localesIndexByString != null)
    	{
    		return localesIndexByString;
    	}
    	synchronized(BaseSimpleStringUtil.class)
    	{
    		if(localesIndexByString != null)
        	{
        		return localesIndexByString;
        	}
	    	Map<String,Locale> lm = new HashMap<String,Locale>();
	    	
	    	{
	//    		if(locale.equals(Locale.SIMPLIFIED_CHINESE))
	    		{
	    			lm.put(String.valueOf(Locale.SIMPLIFIED_CHINESE), Locale.SIMPLIFIED_CHINESE);
	    		}
	//    		else if(locale.equals(Locale.ENGLISH))
	    		{
	    			lm.put(String.valueOf(Locale.ENGLISH), Locale.ENGLISH);
	    		}
	//    		else if(locale.equals(Locale.US))
	    		{
	    			lm.put(String.valueOf(Locale.US), Locale.US);
	    		}
	//    		else if(locale.equals(Locale.JAPANESE))
	    		{
	    			lm.put(String.valueOf(Locale.JAPANESE), Locale.JAPANESE);
	    		}
	//    		else if(locale.equals(Locale.FRENCH))
	    		{
	    			lm.put(String.valueOf(Locale.FRENCH), Locale.FRENCH);
	    		}
	//    		else if(locale.equals(Locale.CANADA_FRENCH))
	    		{
	    			lm.put(String.valueOf(Locale.CANADA_FRENCH), Locale.CANADA_FRENCH);
	    		}
	//    		else if(locale.equals(Locale.CANADA))
	    		{
	    			lm.put(String.valueOf(Locale.CANADA), Locale.CANADA);
	    		}
	//    		else if(locale.equals(Locale.UK))
	    		{
	    			lm.put(String.valueOf(Locale.UK), Locale.UK);
	    		}
	//    		else if(locale.equals(Locale.TAIWAN))
	    		{
	    			lm.put(String.valueOf(Locale.TAIWAN), Locale.TAIWAN);
	    		}
	//    		else if(locale.equals(Locale.PRC))
	    		{
	    			lm.put(String.valueOf(Locale.PRC), Locale.PRC);
	    		}
	//    		else if(locale.equals(Locale.KOREA))
	    		{
	    			lm.put(String.valueOf(Locale.KOREA), Locale.KOREA);
	    		}
	//    		else if(locale.equals(Locale.JAPAN))
	    		{
	    			lm.put(String.valueOf(Locale.JAPAN), Locale.JAPAN);
	    		}
	//    		else if(locale.equals(Locale.ITALY))
	    		{
	    			lm.put(String.valueOf(Locale.ITALY), Locale.ITALY);
	    		}
	//    		else if(locale.equals(Locale.GERMANY))
	    		{
	    			lm.put(String.valueOf(Locale.GERMANY), Locale.GERMANY);
	    		}
	//    		else if(locale.equals(Locale.FRANCE))
	    		{
	    			lm.put(String.valueOf(Locale.FRANCE), Locale.FRANCE);
	    		}
	//    		else if(locale.equals(Locale.TRADITIONAL_CHINESE))
	    		{
	    			lm.put(String.valueOf(Locale.TRADITIONAL_CHINESE), Locale.TRADITIONAL_CHINESE);
	    		}
	//    		else if(locale.equals(Locale.CHINESE))
	    		{
	    			lm.put(String.valueOf(Locale.CHINESE), Locale.CHINESE);
	    		}
	//    		else if(locale.equals(Locale.KOREAN))
	    		{
	    			lm.put(String.valueOf(Locale.KOREAN), Locale.KOREAN);
	    		}
	//    		else if(locale.equals(Locale.ITALIAN))
	    		{
	    			lm.put(String.valueOf(Locale.ITALIAN), Locale.ITALIAN);
	    		}
	//    		else if(locale.equals(Locale.GERMAN))
	    		{
	    			lm.put(String.valueOf(Locale.GERMAN), Locale.GERMAN);
	    		}
	    		lm.put("ROOT", Locale.ROOT);   			
	//    		else
	//    		{
	//    			log.debug("不正确的语言代码:"+ locale);
	//    		}    		
	    	}
	    	localesIndexByString = lm;
	    	
    	}
    	return localesIndexByString;
	}
    /**
     * 
    
   
     * @param locales
     * @return
     */
    public static  Map<String,Locale> converLocales(String locales)
	{
    	if(locales == null || locales.trim().equals(""))
    		return null;
    	String[] locales_ = locales.split("\\,");
    	Map<String,Locale> lm = new HashMap<String,Locale>();
    	for(String locale:locales_)
    	{
    		if(locale.equals(Locale.SIMPLIFIED_CHINESE.toString()))
    		{
    			lm.put(locale, Locale.SIMPLIFIED_CHINESE);
    		}
    		else if(locale.equals(Locale.ENGLISH.toString()))
    		{
    			lm.put(locale, Locale.ENGLISH);
    		}
    		else if(locale.equals(Locale.US.toString()))
    		{
    			lm.put(locale, Locale.US);
    		}
    		else if(locale.equals(Locale.JAPANESE.toString()))
    		{
    			lm.put(locale, Locale.JAPANESE);
    		}
    		else if(locale.equals(Locale.FRENCH.toString()))
    		{
    			lm.put(locale, Locale.FRENCH);
    		}
    		else if(locale.equals(Locale.CANADA_FRENCH.toString()))
    		{
    			lm.put(locale, Locale.CANADA_FRENCH);
    		}
    		else if(locale.equals(Locale.CANADA.toString()))
    		{
    			lm.put(locale, Locale.CANADA);
    		}
    		else if(locale.equals(Locale.UK.toString()))
    		{
    			lm.put(locale, Locale.UK);
    		}
    		else if(locale.equals(Locale.TAIWAN.toString()))
    		{
    			lm.put(locale, Locale.TAIWAN);
    		}
    		else if(locale.equals(Locale.PRC.toString()))
    		{
    			lm.put(locale, Locale.PRC);
    		}
    		else if(locale.equals(Locale.KOREA.toString()))
    		{
    			lm.put(locale, Locale.KOREA);
    		}
    		else if(locale.equals(Locale.JAPAN.toString()))
    		{
    			lm.put(locale, Locale.JAPAN);
    		}
    		else if(locale.equals(Locale.ITALY.toString()))
    		{
    			lm.put(locale, Locale.ITALY);
    		}
    		else if(locale.equals(Locale.GERMANY.toString()))
    		{
    			lm.put(locale, Locale.GERMANY);
    		}
    		else if(locale.equals(Locale.FRANCE.toString()))
    		{
    			lm.put(locale, Locale.FRANCE);
    		}
    		else if(locale.equals(Locale.TRADITIONAL_CHINESE.toString()))
    		{
    			lm.put(locale, Locale.TRADITIONAL_CHINESE);
    		}
    		else if(locale.equals(Locale.CHINESE.toString()))
    		{
    			lm.put(locale, Locale.CHINESE);
    		}
    		else if(locale.equals(Locale.KOREAN.toString()))
    		{
    			lm.put(locale, Locale.KOREAN);
    		}
    		else if(locale.equals(Locale.ITALIAN.toString()))
    		{
    			lm.put(locale, Locale.ITALIAN);
    		}
    		else if(locale.equals(Locale.GERMAN.toString()))
    		{
    			lm.put(locale, Locale.GERMAN);
    		}
    		else
    		{
    			logger.debug("不正确的语言代码:"+ locale + ",build new Locale for " + locale + "." );
    			lm.put(locale, new Locale(locale));   			
    		}    		
    	}
    	return lm;
	}
    

    
    public static String tostring(Object data)
    {
    	StringBuilder ret = new StringBuilder();
    	tostring(ret ,data);
    	return ret.toString();
    }
    
    public static void tostring(StringBuilder ret ,Object data)
	{
		if(data == null )
		{
			return ;
		}
		else if(!data.getClass().isArray())
		{
			ret.append(data.toString());
		}
		else
		{
			int size = Array.getLength(data);
			ret.append("{");
			for(int i = 0; i < size; i ++)
			{
				if(i == 0)
				{
					
					
				}
				else
				{
					ret.append(",");
				}
				tostring(ret ,Array.get(data, i));
			}
			ret.append("}");
		}
		
	}
    
    public static String formatTimeToString(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;

		StringBuilder sb = new StringBuilder();
		if (days != 0) {
			sb.append(days + "天");
		}
		if (hours != 0) {
			sb.append(hours + "小时");
		}
		if (minutes != 0) {
			sb.append(minutes + "分钟");
		}
		if (seconds != 0) {
			sb.append(seconds + "秒");
		}

		return sb.toString();
	}
    
    public static Enumeration arryToenum(final Object[] values)
    {
		
		return new  Enumeration()
		{
			int length = values!= null ?values.length:0;
			int count = 0;
			@Override
			public boolean hasMoreElements() {
				
				return count < length;
			}

			@Override
			public Object nextElement() {
				Object element = values[count];
				count ++ ;
				return element;
			}
			
		};
    }
    
    public static String native2ascii( String s )
	   {
	      StringBuilder res = new StringBuilder();
	      for ( int i = 0; i < s.length(); i++ ){
	         char ch = s.charAt( i );
	         int val = (int) ch;
	         if ( ch == '\r' ) continue;
	         if ( val >= 0 && val < 128 && ch != '\n' && ch != '\\' ) res.append( ch );
	         else{
	            res.append( "\\u" );
	            String hex = Integer.toHexString( val );
	            for( int j = 0; j < 4 - hex.length(); j++ ) res.append( "0" );
	            res.append( hex );
	         }
	      }
	      return res.toString();
	   }
	public static String ascii2native( String s )
	   {
	      StringBuilder res = new StringBuilder(s.length());
	      for ( int i = 0; i < s.length(); i++ ){
	         char ch = s.charAt( i );
	         if (ch == '\\' && i+1>=s.length()){
	            res.append(ch);
	            break;
	         }
	         if ( ch != '\\') res.append( ch );
	         else {
	             switch (s.charAt(i+1)) {
	             case 'u': 
	                 res.append( (char) Integer.parseInt( s.substring( i+2, i+6 ), 16 ) );
	                 i += 5;
	                 break;
	             case 'n': res.append('\n'); i++; break;
	             case 't': res.append('\t'); i++; break;
	             case 'r': res.append('\r'); i++; break;
	             default: break;
	             }
	         }
	      }
	      return res.toString();
	   }
	 /**
		 * determine the OS name
		 * 
		 * @return The name of the OS
		 */
		public static final String getOS() {
			return System.getProperty("os.name");
		}

		/**
		 * @return True if the OS is a Windows derivate.
		 */
		public static final boolean isWindows() {
			return getOS().startsWith("Windows");
		}

		/**
		 * @return True if the OS is a Linux derivate.
		 */
		public static final boolean isLinux() {
			return getOS().startsWith("Linux");
		}

		/**
		 * @return True if the OS is an OSX derivate.
		 */
		public static final boolean isOSX() {
			return getOS().toUpperCase().contains("OS X");
		}
		
		/**
	     * Constructs a new <code>String</code> by decoding the specified array of bytes using the UTF-8 charset.
	     *
	     * @param bytes
	     *            The bytes to be decoded into characters
	     * @return A new <code>String</code> decoded from the specified array of bytes using the UTF-8 charset,
	     *         or <code>null</code> if the input byte array was <code>null</code>.
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     */
	    public static String newStringUtf8(final byte[] bytes) {
	        return newString(bytes, Charsets.UTF_8);
	    }
	    /**
	     * Constructs a new <code>String</code> by decoding the specified array of bytes using the given charset.
	     *
	     * @param bytes
	     *            The bytes to be decoded into characters
	     * @param charset
	     *            The {@link Charset} to encode the <code>String</code>
	     * @return A new <code>String</code> decoded from the specified array of bytes using the given charset,
	     *         or <code>null</code> if the input byte array was <code>null</code>.
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     */
	    private static String newString(final byte[] bytes, final Charset charset) {
	        return bytes == null ? null : new String(bytes, charset);
	    }
	    

	    /**
	     * Encodes the given string into a sequence of bytes using the US-ASCII charset, storing the result into a new byte
	     * array.
	     *
	     * @param string
	     *            the String to encode, may be <code>null</code>
	     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#US_ASCII} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	     */
	    public static byte[] getBytesUsAscii(final String string) {
	        return getBytes(string, Charsets.US_ASCII);
	    }

	    /**
	     * Encodes the given string into a sequence of bytes using the UTF-16 charset, storing the result into a new byte
	     * array.
	     *
	     * @param string
	     *            the String to encode, may be <code>null</code>
	     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_16} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	     */
	    public static byte[] getBytesUtf16(final String string) {
	        return getBytes(string, Charsets.UTF_16);
	    }

	    /**
	     * Encodes the given string into a sequence of bytes using the UTF-16BE charset, storing the result into a new byte
	     * array.
	     *
	     * @param string
	     *            the String to encode, may be <code>null</code>
	     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_16BE} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	     */
	    public static byte[] getBytesUtf16Be(final String string) {
	        return getBytes(string, Charsets.UTF_16BE);
	    }

	    /**
	     * Encodes the given string into a sequence of bytes using the UTF-16LE charset, storing the result into a new byte
	     * array.
	     *
	     * @param string
	     *            the String to encode, may be <code>null</code>
	     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_16LE} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	     */
	    public static byte[] getBytesUtf16Le(final String string) {
	        return getBytes(string, Charsets.UTF_16LE);
	    }

	    /**
	     * Encodes the given string into a sequence of bytes using the UTF-8 charset, storing the result into a new byte
	     * array.
	     *
	     * @param string
	     *            the String to encode, may be <code>null</code>
	     * @return encoded bytes, or <code>null</code> if the input string was <code>null</code>
	     * @throws NullPointerException
	     *             Thrown if {@link Charsets#UTF_8} is not initialized, which should never happen since it is
	     *             required by the Java platform specification.
	     * @since As of 1.7, throws {@link NullPointerException} instead of UnsupportedEncodingException
	     * @see <a href="http://download.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
	     */
	    public static byte[] getBytesUtf8(final String string) {
	        return getBytes(string, Charsets.UTF_8);
	    }
	    

	    /**
	     * Calls {@link String#getBytes(Charset)}
	     *
	     * @param string
	     *            The string to encode (if null, return null).
	     * @param charset
	     *            The {@link Charset} to encode the <code>String</code>
	     * @return the encoded bytes
	     */
	    private static byte[] getBytes(final String string, final Charset charset) {
	        if (string == null) {
	            return null;
	        }
	        return string.getBytes(charset);
	    }

}
