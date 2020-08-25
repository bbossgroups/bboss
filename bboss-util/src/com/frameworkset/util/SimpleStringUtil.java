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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.oro.text.regex.*;
import org.frameworkset.json.DefaultJsonTypeReference;
import org.frameworkset.json.JacksonObjectMapperWrapper;
import org.frameworkset.json.JsonTypeReference;
import org.frameworkset.soa.BBossStringWriter;
import org.frameworkset.util.ObjectUtils;
import org.frameworkset.util.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.*;

/**
 * To change for your class or interface DAO中VOObject String类型与PO数据类型转换工具类.
 * 
 * @author biaoping.yin
 * @version 1.0
 */

public class SimpleStringUtil  extends BaseSimpleStringUtil{
//	private static final SimpleDateFormat format = new SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");

	protected static final Logger logger = LoggerFactory.getLogger(SimpleStringUtil.class);
	
	private static JacksonObjectMapperWrapper objectMapper = null;
	/**
	 * Capitalize a {@code String}, changing the first letter to
	 * upper case as per {@link Character#toUpperCase(char)}.
	 * No other letters are changed.
	 * @param str the {@code String} to capitalize
	 * @return the capitalized {@code String}
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}
	/**
	 * Concatenate the given {@code String} arrays into one,
	 * with overlapping array elements included twice.
	 * <p>The order of elements in the original arrays is preserved.
	 * @param array1 the first array (can be {@code null})
	 * @param array2 the second array (can be {@code null})
	 * @return the new array ({@code null} if both given arrays were {@code null})
	 */
	@Nullable
	public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}

		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (!hasLength(str)) {
			return str;
		}

		char baseChar = str.charAt(0);
		char updatedChar;
		if (capitalize) {
			updatedChar = Character.toUpperCase(baseChar);
		}
		else {
			updatedChar = Character.toLowerCase(baseChar);
		}
		if (baseChar == updatedChar) {
			return str;
		}

		char[] chars = str.toCharArray();
		chars[0] = updatedChar;
		return new String(chars, 0, chars.length);
	}
	private static void initJacksonObjectMapperWrapper(){
		if(objectMapper == null) {
			synchronized (SimpleStringUtil.class) {
				if(objectMapper == null) {
					objectMapper = new JacksonObjectMapperWrapper();
					objectMapper.init();
				}
			}
		}
	}
	public static JacksonObjectMapperWrapper getJacksonObjectMapper(){
		initJacksonObjectMapperWrapper();
		return objectMapper;
	}



	/**
	 * 将一个字符串根据逗号分拆
	 */
	public static String[] split(String s) {
		return split(s, COMMA);
	}
	

	/**
	 * 将字符串根据给定分隔符分拆
	 */
	public static String[] split(String s, String delimiter) {
		return split(s, delimiter, true);

		// if (s == null || delimiter == null) {
		// return new String[0];
		// }
		//
		// s = s.trim();
		//
		// if (!s.endsWith(delimiter)) {
		// s += delimiter;
		// }
		//
		// if (s.equals(delimiter)) {
		// return new String[0];
		// }
		//
		// List nodeValues = new ArrayList();
		//
		// if (delimiter.equals("\n") || delimiter.equals("\r")) {
		// try {
		// BufferedReader br = new BufferedReader(new StringReader(s));
		//
		// String line = null;
		//
		// while ((line = br.readLine()) != null) {
		// nodeValues.add(line);
		// }
		//
		// br.close();
		// }
		// catch (IOException ioe) {
		// ioe.printStackTrace();
		// }
		// }
		// else {
		// int offset = 0;
		// int pos = s.indexOf(delimiter, offset);
		//
		// while (pos != -1) {
		// nodeValues.add(s.substring(offset, pos));
		//
		// offset = pos + delimiter.length();
		// pos = s.indexOf(delimiter, offset);
		// }
		// }
		//
		// return (String[])nodeValues.toArray(new String[0]);
	}

	

	/**
	 * 字符串替换函数
	 * 
	 * @param val
	 *            String
	 * @param str1
	 *            String
	 * @param str2
	 *            String
	 * @return String
	 */
	public static String replaceAll(String val, String str1, String str2) {
		return replaceAll(val, str1, str2, true);
	}

	public static String replaceFirst(String val, String str1, String str2) {
		return replaceFirst(val, str1, str2, true);
	}

	public static String replaceFirst(String val, String str1, String str2,
			boolean CASE_INSENSITIVE) {
		String patternStr = str1;

		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;

		try {
			if (CASE_INSENSITIVE) {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.DEFAULT_MASK);
			} else {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.CASE_INSENSITIVE_MASK);
			}
			PatternMatcher matcher = new Perl5Matcher();
			return org.apache.oro.text.regex.Util.substitute(matcher, pattern,
					new StringSubstitution(str2), val);

		} catch (MalformedPatternException e) {
			e.printStackTrace();

			return val;
		}

	}

	public static String replaceAll(String val, String str1, String str2,
			boolean CASE_INSENSITIVE) {
		String patternStr = str1;

		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;

		try {
			if (CASE_INSENSITIVE) {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.DEFAULT_MASK);
			} else {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.CASE_INSENSITIVE_MASK);
			}
			PatternMatcher matcher = new Perl5Matcher();
			return org.apache.oro.text.regex.Util.substitute(matcher, pattern,
					new StringSubstitution(str2), val,
					org.apache.oro.text.regex.Util.SUBSTITUTE_ALL);

		} catch (MalformedPatternException e) {
			e.printStackTrace();

			return val;
		}
	}

	public static String replaceAll(String val, String str1, String str2,
			int mask) {
		String patternStr = str1;

		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;

		try {
			
			pattern = compiler.compile(patternStr,
					mask);
			
			PatternMatcher matcher = new Perl5Matcher();
			return org.apache.oro.text.regex.Util.substitute(matcher, pattern,
					new StringSubstitution(str2), val,
					org.apache.oro.text.regex.Util.SUBSTITUTE_ALL);

		} catch (MalformedPatternException e) {
			e.printStackTrace();

			return val;
		}
	}

	/**
	 * 分割字符串为数组函数
	 * 
	 * @param val
	 *            String
	 * @param token
	 *            String
	 * @param CASE_INSENSITIVE
	 *            boolean
	 * @return String[]
	 */
	public static String[] split(String val, String token,
			boolean CASE_INSENSITIVE) {
		String patternStr = token;
		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;

		try {
			if (CASE_INSENSITIVE) {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.DEFAULT_MASK);
			} else {
				pattern = compiler.compile(patternStr,
						Perl5Compiler.CASE_INSENSITIVE_MASK);
			}

			PatternMatcher matcher = new Perl5Matcher();
			List list = new ArrayList();
			split(list, matcher, pattern, val, SPLIT_ALL);
			String[] rets = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				rets[i] = (String) list.get(i);

			}
			return rets;

		} catch (MalformedPatternException e) {
			e.printStackTrace();

			return new String[] { val };
		}

	}
	
	/**
	 * 分割字符串为数组函数
	 * 
	 * @param val
	 *            String
	 * @param token
	 *            String
	 *            boolean
	 * @return String[]
	 */
	public static String[] split(String val, String token,
			int mask) {
		String patternStr = token;
		/**
		 * 编译正则表达式patternStr，并用该表达式与传入的sql语句进行模式匹配,
		 * 如果匹配正确，则从匹配对象中提取出以上定义好的6部分，存放到数组中并返回 该数组
		 */

		PatternCompiler compiler = new Perl5Compiler();
		Pattern pattern = null;

		try {
			
				pattern = compiler.compile(patternStr,
						mask);
			

			PatternMatcher matcher = new Perl5Matcher();
			List list = new ArrayList();
			split(list, matcher, pattern, val, SPLIT_ALL);
			String[] rets = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				rets[i] = (String) list.get(i);

			}
			return rets;

		} catch (MalformedPatternException e) {
			e.printStackTrace();

			return new String[] { val };
		}

	}

	private static void split(Collection results, PatternMatcher matcher,
			Pattern pattern, String input, int limit) {
		int beginOffset;
		MatchResult currentResult;
		PatternMatcherInput pinput;

		pinput = new PatternMatcherInput(input);
		beginOffset = 0;

		while (--limit != 0 && matcher.contains(pinput, pattern)) {
			currentResult = matcher.getMatch();
			results.add(input.substring(beginOffset, currentResult
					.beginOffset(0)));
			beginOffset = currentResult.endOffset(0);
		}

		results.add(input.substring(beginOffset, input.length()));
	}


	

	public static void main(String args[]) {
//		String str = "中文,'bb,cc,'dd";
//		try {
//			str = new String(str.getBytes(), "utf-8");
//		} catch (UnsupportedEncodingException ex) {
//		}
//		System.out.println(str.getBytes()[0]);
//		System.out.println(str.getBytes()[1]);
//		System.out.println(str.getBytes()[2]);
//		System.out.println(str.getBytes()[3]);
//
//		System.out.println("?".getBytes()[0]);
		int maxlength = 16;
		String replace  ="...";
		String outStr = "2010年02月04日12时许，何金瑶（女、1987年06月18日生、身份证：430981198706184686、湖南省沅江市沅江市南大膳镇康宁村十二村民组24号）报警：其经营的益阳市电信对面的晴天服装店被盗了。接警后我所民警立即赶至现场了解系，今日中午12时许何金瑶与母亲黄志元在店内做生意，有两男子进入店内，其中一男子以搬店内的试衣镜出去吸引注意力。另一男子就进行盗窃，盗取了其店内收银台抽屉内700元人民币";
		
		System.out.println(SimpleStringUtil.getHandleString(maxlength,replace,false,false,outStr));
		
outStr = "2010年02月07日11时许，周灵颖报警：在2路公交车上被扒窃，并抓获一名嫌疑人。民警出警后，经调查，周灵颖于当日10时40分许坐2路车到桥南，途中被二名男子扒窃现金3100元。一名被当场抓获，另一名已逃走。 ";
		
		System.out.println(SimpleStringUtil.getHandleString(maxlength,replace,false,false,outStr));
	}



	/**
	 * 将html中标记语言字符转换为转义符
	 * 
	 * @param text
	 * @return
	 */
	public static String HTMLEncode(String text) {
		if(SimpleStringUtil.isEmpty(text ))
			return text;
		text = SimpleStringUtil.replaceAll(text, "&", "&amp;");
		text = SimpleStringUtil.replaceAll(text, "\"", "&quot;");
		text = SimpleStringUtil.replaceAll(text, "<", "&lt;");
		text = SimpleStringUtil.replaceAll(text, ">", "&gt;");
		text = SimpleStringUtil.replaceAll(text, "'", "&#146;");
		text = SimpleStringUtil.replaceAll(text, "\\ ", "&nbsp;");
		text = SimpleStringUtil.replaceAll(text, "\n", "<br>");
		text = SimpleStringUtil.replaceAll(text, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		return text;
	}
	
	
	/**
	 * 将html中标记语言字符转换为转义符
	 * 
	 * @param text
	 * @return
	 */
	public static String HTMLNoBREncode(String text) {
		if(SimpleStringUtil.isEmpty(text ))
			return text;
		text = SimpleStringUtil.replaceAll(text, "&", "&amp;");
		text = SimpleStringUtil.replaceAll(text, "\"", "&quot;");
		text = SimpleStringUtil.replaceAll(text, "<", "&lt;");
		text = SimpleStringUtil.replaceAll(text, ">", "&gt;");
		text = SimpleStringUtil.replaceAll(text, "'", "&#146;");
		text = SimpleStringUtil.replaceAll(text, "\\ ", "&nbsp;");
//		text = SimpleStringUtil.replaceAll(text, "\n", "<br>");
//		text = SimpleStringUtil.replaceAll(text, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		return text;
	}

	/**
	 * 将转义的字符串还原
	 * 
	 * @param text
	 * @return
	 */
	public static String HTMLEncodej(String text) {
		if(SimpleStringUtil.isEmpty(text ))
			return text;
		text = SimpleStringUtil.replaceAll(text, "&amp;", "&");
		text = SimpleStringUtil.replaceAll(text, "&quot;", "\"");
		text = SimpleStringUtil.replaceAll(text, "&lt;", "<");
		text = SimpleStringUtil.replaceAll(text, "&gt;", ">");
		text = SimpleStringUtil.replaceAll(text, "&#146;", "'");
		text = SimpleStringUtil.replaceAll(text, "&nbsp;", "\\ ");
		text = SimpleStringUtil.replaceAll(text, "<br>", "\n");
		text = SimpleStringUtil.replaceAll(text, "&nbsp;&nbsp;&nbsp;&nbsp;", "\t");
		return text;
	}
	/**
	 * 将转义的字符串还原
	 * 
	 * @param text
	 * @return
	 */
	public static String HTMLNoBREncodej(String text) {
		if(SimpleStringUtil.isEmpty(text ))
			return text;
		text = SimpleStringUtil.replaceAll(text, "&amp;", "&");
		text = SimpleStringUtil.replaceAll(text, "&quot;", "\"");
		text = SimpleStringUtil.replaceAll(text, "&lt;", "<");
		text = SimpleStringUtil.replaceAll(text, "&gt;", ">");
		text = SimpleStringUtil.replaceAll(text, "&#146;", "'");
		text = SimpleStringUtil.replaceAll(text, "&nbsp;", "\\ ");
//		text = SimpleStringUtil.replaceAll(text, "<br>", "\n");
//		text = SimpleStringUtil.replaceAll(text, "&nbsp;&nbsp;&nbsp;&nbsp;", "\t");
		return text;
	}

	public static String getHandleString(int maxlength, String replace,
			boolean htmlencode, boolean htmldecode, String outStr) {
		if (maxlength > 0 && outStr != null && outStr.length() > maxlength) {
			outStr = outStr.substring(0, maxlength);
			if (replace != null)
				outStr += replace;
		}
		if (htmlencode) {
			return SimpleStringUtil.HTMLNoBREncode(outStr);
		} else if (htmldecode) {
			return SimpleStringUtil.HTMLNoBREncodej(outStr);
		} else {
			return outStr;
		}

	}


    
    public static <T> T json2Object(String jsonString,Class<T> toclass,boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, toclass);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
    	return getJacksonObjectMapper().json2Object(jsonString,toclass,ALLOW_SINGLE_QUOTES);
		
		
	
	}
    public static <T> T json2ObjectWithType(String jsonString,JsonTypeReference<T> ref) {
		return json2ObjectWithType(jsonString,ref,true);
		
	
	}
	public static <T> List<T> json2ListObject(String jsonString,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaType(List.class,beanType));
		return (List<T>)json2ObjectWithType(jsonString,ref,true);


	}
	public static <T> Set<T> json2LSetObject(String jsonString,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaType(Set.class,beanType));
		return (Set<T>)json2ObjectWithType(jsonString,ref,true);


	}

	public static <T> T[] json2LArrayObject(String jsonString, final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getObjectMapper().getTypeFactory().constructArrayType(beanType));
		return (T[])json2ObjectWithType(jsonString,ref,true);


	}
	public static ObjectMapper getObjectMapper(){
		return getJacksonObjectMapper().getObjectMapper();
	}
	public static <K,T> Map<K,T> json2LHashObject(String jsonString,final Class<K> keyType,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaMapType(Map.class,keyType,beanType));
		return (Map<K,T>)json2ObjectWithType(jsonString,ref,true);


	}

	public static <T> List<T> json2ListObject(InputStream jsonString,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaType(List.class,beanType)) ;
		return (List<T>)json2ObjectWithType(jsonString,ref,true);


	}

	public static <D,T> D json2TypeObject(InputStream jsonString,final Class<D> containType,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaType(containType,beanType)) ;
		return (D)json2ObjectWithType(jsonString,ref,true);
	}
	public static <T> Set<T> json2LSetObject(InputStream jsonString,final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaType(Set.class,beanType)) ;
		return (Set<T>)json2ObjectWithType(jsonString,ref,true);


	}
	public static <K,T> Map<K,T> json2LHashObject(InputStream jsonString, final Class<K> keyType, final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getJavaMapType(Map.class,keyType,beanType)) ;

		return (Map<K,T>)json2ObjectWithType(jsonString,ref,true);


	}
	public static <T> T[] json2LArrayObject(InputStream jsonString, final Class<T> beanType) {
		JsonTypeReference ref = new DefaultJsonTypeReference(getJacksonObjectMapper().getObjectMapper().getTypeFactory().constructArrayType(beanType));
		return (T[])json2ObjectWithType(jsonString,ref,true);


	}
    public static <T> T json2ObjectWithType(InputStream json,JsonTypeReference<T> ref) {
		return json2ObjectWithType(json,ref,true);
		
	
	}
    public static <T> T  json2ObjectWithType(String jsonString,JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, ref);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
    	return getJacksonObjectMapper().json2ObjectWithType(jsonString, ref, ALLOW_SINGLE_QUOTES);
		
	
	}
    
    public static <T> T  json2ObjectWithType(InputStream json,JsonTypeReference<T> ref,boolean ALLOW_SINGLE_QUOTES) {
		// TODO Auto-generated method stub

//		String jsonString = "[{'from_date':'2001-09-21','to_date':'2011-04-02','company':'人寿保险','department':'xxx','position':'主管' },{'from_date':'0002-12-01','to_date':'2011-04-02', 'company':'人寿保险','department':'xxx','position':'主管' }]";
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			T value = mapper.readValue(jsonString, ref);
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException(jsonString,e);
//		}
    	return getJacksonObjectMapper().json2ObjectWithType(json, ref, ALLOW_SINGLE_QUOTES);
		
	
	}
    
    public static <T> T json2Object(String jsonString,Class<T> toclass) {
		// TODO Auto-generated method stub
		return getJacksonObjectMapper().json2Object(jsonString,toclass,true);
		
	
	}
    public static <T> T json2Object(InputStream jsonString,Class<T> toclass) {
		// TODO Auto-generated method stub
		return getJacksonObjectMapper().json2Object(jsonString,toclass,true);
		
	
	}
    
    public static String object2json(Object object,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			String value = mapper.writeValueAsString(object);
//			
//			return value;
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
    	return getJacksonObjectMapper().object2json(  object,  ALLOW_SINGLE_QUOTES);
		
	
	}
    
    public static String object2json(Object object) {
    	return object2json(object,true) ;
		
		
	
	}
    
    public static void object2json(Object object,Writer writer,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
		
    	getJacksonObjectMapper().object2json(object,writer,ALLOW_SINGLE_QUOTES);
	
	}
    
    public static void object2json(Object object,Writer writer) {
    	getJacksonObjectMapper().object2json(object,writer,true) ;
	}
	public static void object2json(Object object,StringBuilder builder) {
    	BBossStringWriter writer = new BBossStringWriter(builder);
		getJacksonObjectMapper().object2json(object,writer,true) ;
	}
    
    public static void object2json(Object object,OutputStream writer,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
    	getJacksonObjectMapper().object2json(object,writer,ALLOW_SINGLE_QUOTES);
		
	
	}
    
    public static void object2json(Object object,OutputStream writer) {
    	getJacksonObjectMapper().object2json(object,writer,true) ;
	}
    
    public static void object2json(Object object,File writer,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			mapper.writeValue(writer,object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
    	  getJacksonObjectMapper().object2json(object,writer,ALLOW_SINGLE_QUOTES);
		
	
	}
    
    public static void object2json(Object object,File writer) {
    	getJacksonObjectMapper().object2json(object,writer,true) ;
	}
    
    public static byte[] object2jsonAsbyte(Object object,boolean ALLOW_SINGLE_QUOTES) {
//    	ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(Feature.ALLOW_SINGLE_QUOTES, ALLOW_SINGLE_QUOTES); 
//		try {
//			return mapper.writeValueAsBytes(object);
//			
//			
//			
//			
//		} catch (Exception e) {
//			throw new IllegalArgumentException("错误的json序列化操作",e);
//		}
		
    	return getJacksonObjectMapper().object2jsonAsbyte(  object,  ALLOW_SINGLE_QUOTES);
	
	}
    
    public static byte[] object2jsonAsbyte(Object object) {
    	return getJacksonObjectMapper().object2jsonAsbyte(object,true) ;
	}
    
    



}
