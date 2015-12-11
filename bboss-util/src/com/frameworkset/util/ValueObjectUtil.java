/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
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
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.frameworkset.util.BigFile;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.util.MethodParameter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import bsh.Interpreter;

import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.spi.assemble.CurrentlyInCreationException;

/**
 * @author biaoping.yin 改类充分使用java.lang.reflection中提供的功能，提供以下工具： 从对象中获取对应属性的值
 */
public class ValueObjectUtil {

	/**
	 * 基本数据类型，bboss定位如下：
	 * java定义的基本数据类型
	 * BigFile 大附件类型
	 * 枚举类型
	 * 日期类型
	 * 字符串类型
	 * 这些类型主要应用于mvc控制器方法参数的绑定过程中
	 */
	public static final Class[] baseTypes = {String.class,
		int.class ,Integer.class,
		long.class,Long.class,
		java.sql.Timestamp.class,java.sql.Date.class,java.util.Date.class,
		boolean.class ,Boolean.class,
		BigFile.class,
		float.class ,Float.class,
		short.class ,Short.class,
		double.class,Double.class,
		char.class ,Character.class,
		
		byte.class ,Byte.class,
		BigDecimal.class,BigInteger.class};
	/**
	 * 用于序列化机制识别基础数据类型   
	 */
	public static final Class[] basePrimaryTypes = {String.class,
		int.class ,
		long.class,		
		boolean.class ,
//		BigFile.class,
		float.class ,
		short.class ,
		double.class,
		char.class ,
		byte.class ,
		Class.class,BigInteger.class,BigDecimal.class,
		java.sql.Timestamp.class,java.sql.Date.class,java.util.Date.class
		};
	private static final Logger log = Logger.getLogger(ValueObjectUtil.class);

//	private static final SimpleDateFormat format = new SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");
	/**
	 * 缓存所有的日期格式
	 */
//	private static final Map<String,SimpleDateFormat> dataformats = new HashMap<String,SimpleDateFormat>();

//	/**
//	 * Description:获取对象obj的property属性值
//	 * 
//	 * @param obj
//	 * @param property
//	 * @return Object
//	 */
//	public static Object getValue(Object obj, String property) {
//		return getValue(obj, property);
//	}

	/**
	 * Description:获取对象obj的property属性值,params为参数数组
	 * 
	 * @param obj
	 * @param property
	 * @param params
	 *            获取属性方法值的参数
	 * @return Object
	 */
	public static Object getValue(Object obj, String property) {
		if (obj == null || property == null || property.trim().length() == 0)
			return null;

		try {

			PropertieDescription pd = ClassUtil.getPropertyDescriptor(obj.getClass(), property);
			return pd.getValue(obj);
				
			
		} catch (Exception e) {
			log.debug("没有为属性[" + property + "]定义get或者返回布尔值的is方法.");
		}
		
		return null;
	}
	/**
	 * Description:获取对象obj的property属性值,params为参数数组
	 * 
	 * @param obj
	 * @param property
	 * @param params
	 *            获取属性方法值的参数
	 * @return Object
	 */
	public static Object getValueOrSize(Object obj, String property) {
		boolean issize = false;
		 
			
			if(property.startsWith("size:"))
			{
				issize = true;
				property = property.substring(5); 
			}
			Object value =  getValue(  obj,   property);
			if(issize)
			{
				return length(value);
			}
			else
			{
				return value;
			}
		 
	}
	
	public static int length(Object _actualValue)
	{
		if(_actualValue == null)
			return 0;
		else
		{
			if(_actualValue instanceof Collection)
			{
				return ((Collection)_actualValue).size();
			}
			else if(_actualValue instanceof Map)
			{
				return ((Map)_actualValue).size();
			}
			else if(_actualValue.getClass().isArray())
			{
				return Array.getLength(_actualValue);
			}
			else if(_actualValue instanceof String)
			{
				return ((String)_actualValue).length();
			}
			else if(_actualValue instanceof ListInfo) 
			{
				return ((ListInfo)_actualValue).getSize();
			}
			else //评估对象长度
			{
				throw new IllegalArgumentException("无法计算类型为"+_actualValue.getClass().getName()+"的对象长度length。");
			}
				
		}
	}
	
	public static Object getValue(Object obj, String property,Object[] params) {
		if (obj == null || property == null || property.trim().length() == 0)
			return null;

		try {
			if(params == null || params.length == 0)
			{
				PropertieDescription pd = ClassUtil.getPropertyDescriptor(obj.getClass(), property);
				return pd.getValue(obj);
				
			}
//			else
//			{
//				Method method = getMethodByPropertyName(obj, property);
//				return getValueByMethod(obj, method, params);
//			}
		} catch (Exception e) {
			log.debug("没有为属性[" + property + "]定义get或者返回布尔值的is方法.");
		}
		// Object ret = getValueByMethodName(obj, getMethodName(property),
		// params);
		// if (ret == null)
		// {
		// // log.debug("Try to get Boolean attribute for property[" + property
		// // + "].");
		// ret = getValueByMethodName(obj, getBooleanMethodName(property),
		// params);
		// if (ret != null)
		// log.debug("Get Boolean property[" + property + "=" + ret + "].");
		// }

		// return ret;
		return null;
	}

	/**
	 * Description:根据方法名称获取， 在对象obj上调用改方法并且返回调用的返回值
	 * 
	 * @param obj
	 * @param methodName
	 *            方法名称
	 * @param params
	 *            方法的参数
	 * @return Object
	 * @deprecated
	 */
	public static Object getValueByMethodName(Object obj, String methodName,
			Object[] params) {
		if (obj == null || methodName == null
				|| methodName.trim().length() == 0)
			return null;
		return getValueByMethodName(obj, methodName, params, null);
	}

//	/*
//	 *
//	 */
//	@Deprecated
//	public static Method getMethodByPropertyName(Object obj,
//			String propertyName, Class[] paramsTtype) throws Exception {
//		String name = getMethodName(propertyName);
//		Method method = null;
//		try {
//			method = obj.getClass().getMethod(name, paramsTtype);
//		} catch (SecurityException e) {
//			name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, paramsTtype);
//		} catch (NoSuchMethodException e) {
//			name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, paramsTtype);
//		} catch (Exception e) {
//			name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, paramsTtype);
//		}
//		return method;
//
//	}
	
	
	public static Method getMethodByPropertyName(Object obj,
			String propertyName) throws Exception {
//		String name = getMethodName(propertyName);
		Method method = null;
		try {
//			method = obj.getClass().getMethod(name, paramsTtype);
			PropertieDescription pd = ClassUtil.getPropertyDescriptor(obj.getClass(), propertyName);
			if(pd != null)
				method = pd.getReadMethod();
			
		} catch (SecurityException e) {
//			String name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, null);
		}  catch (Exception e) {
//			String name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, null);
		}
		if(method == null)
		{
			String name = getBooleanMethodName(propertyName);
//			method = obj.getClass().getMethod(name, null);
			method = ClassUtil.getDeclaredMethod(obj.getClass(),name);
			if(method == null)
				 throw new NoSuchMethodException(obj.getClass().getName() + "." + name );
		}
		return method;

	}

	/**
	 * Description:根据方法名称获取， 在对象obj上调用改方法并且返回调用的返回值
	 * 
	 * @param obj
	 * @param methodName
	 *            方法名称
	 * @param params
	 *            方法的参数
	 * @param paramsTtype
	 *            方法的参数类型
	 * @deprecated           
	 * @return Object
	 */
	public static Object getValueByMethodName(Object obj, String methodName,
			Object[] params, Class[] paramsTtype) {
		if (obj == null || methodName == null
				|| methodName.trim().length() == 0)
			return null;
		try {
			if(paramsTtype == null || paramsTtype.length == 0)
			{
				Method method = ClassUtil.getDeclaredMethod(obj.getClass(),methodName);
				if (method != null)
					return method.invoke(obj, params);
			}
			else
			{
				Method method = obj.getClass().getMethod(methodName, paramsTtype);
				if (method != null)
					return method.invoke(obj, params);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			log.info("NoSuchMethodException:" + e.getMessage());
		}
		return null;

	}

	/**
	 * Description:根据方法名称获取， 在对象obj上调用改方法并且返回调用的返回值
	 * 
	 * @param obj
	 * @param methodName
	 *            方法名称
	 * @param params
	 *            方法的参数
	 * @param paramsTtype
	 *            方法的参数类型
	 * @return Object
	 */
	public static Object getValueByMethod(Object obj, Method method,
			Object[] params) {
		if (obj == null || method == null)
			return null;
		try {
			// Method method = obj.getClass().getMethod(methodName,
			// paramsTtype);
			// if (method != null)
			return method.invoke(obj, params);
		} catch (Exception e) {
			// e.printStackTrace();
			log.info("Invoker method[" + method.getName() + "] on "
					+ obj.getClass().getCanonicalName() + " failed:"
					+ e.getMessage());
		}
		return null;

	}

	/**
	 * Description:实现在对象调用method并为该方法传入参数数组params
	 * 
	 * @param obj
	 *            对象
	 * @param method
	 *            待调用的方法
	 * @param params
	 *            参数数组
	 * @return Object
	 * @throws Exception
	 *             Object
	 */
	public static Object invoke(Object obj, Method method, Object[] params)
			throws Exception {
		return method.invoke(obj, params);
	}
	/**
	 * Description:实现在对象调用method并为该方法传入参数数组params
	 * 
	 * @param obj
	 *            对象
	 * @param method
	 *            待调用的方法
	 * @param params
	 *            参数数组
	 * @return Object
	 * @throws Exception
	 *             Object
	 */
	public static Object invoke(Object obj, String method, Object[] params)
			throws Exception {
		if(obj == null)
		{
			return null;
		}
		Class clazz = obj.getClass();
		Method _m = ClassUtil.getDeclaredMethod(clazz, method);
		return _m.invoke(obj, params);
	}

	/**
	 * 获取fieldName的getter方法名称
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String getMethodName(String fieldName) {
		String ret = null;
		if (fieldName == null)
			return null;
		String letter = String.valueOf(fieldName.charAt(0));
		letter = letter.toUpperCase();
		ret = "get" + letter + fieldName.substring(1);
		// System.out.println("method name:" + ret);
		return ret;

	}

	public static String getBooleanMethodName(String fieldName) {
		String ret = null;
		if (fieldName == null)
			return null;
		String letter = String.valueOf(fieldName.charAt(0));
		letter = letter.toUpperCase();
		ret = "is" + letter + fieldName.substring(1);
		// System.out.println("method name:" + ret);
		return ret;

	}

	/**
	 * 获取fieldName的setter方法
	 * 
	 * @param fieldName
	 * @return String
	 * @deprecated
	 */
	public static String getSetterMethodName(String fieldName) {
		String ret = null;
		if (fieldName == null)
			return null;
		String letter = String.valueOf(fieldName.charAt(0));
		letter = letter.toUpperCase();
		ret = "set" + letter + fieldName.substring(1);
		// System.out.println("method name:" + ret);
		return ret;

	}

	// public final static boolean isSameType(Class type, Class toType)
	// {
	// if(toType == Object.class)
	// return true;
	//		
	// else if(type == toType)
	// return true;
	// else if(toType.isAssignableFrom(type))//检查toType是不是type的父类或者是type所实现的接口
	// {
	// return true;
	// }
	// else
	// if(type.isAssignableFrom(toType))//检查type是不是toType的父类或者是拖油瓶type所实现的接口
	// {
	// if(type.getName().equals(toType.getName()))
	// return true;
	// }
	//		
	// else if((type == int.class && toType == Integer.class) ||
	// type == Integer.class && toType == int.class)
	// {
	// return true;
	// }
	// else if((type == short.class && toType == Short.class) ||
	// type == Short.class && toType == short.class)
	// {
	// return true;
	// }
	// else if((type == long.class && toType == Long.class) ||
	// type == Long.class && toType == long.class)
	// {
	// return true;
	// }
	// else if((type == double.class && toType == Double.class) ||
	// type == Double.class && toType == double.class)
	// {
	// return true;
	// }
	// else if((type == float.class && toType == Float.class) ||
	// type == Float.class && toType == float.class)
	// {
	// return true;
	// }
	// else if((type == char.class && toType == Character.class) ||
	// type == Character.class && toType == char.class)
	// {
	// return true;
	// }
	// return false;
	//		
	//		
	// }

	/**
	 * 
	 * @param types
	 *            构造函数的参数类型
	 * @param params
	 *            外部传入的形式参数类型
	 * @return
	 */
	public static boolean isSameTypes(Class[] types, Class[] params,
			Object[] paramArgs) {

		if (types.length != params.length)
			return false;
		for (int i = 0; i < params.length; i++) {

			// if(!ValueObjectUtil.isSameType(type, toType))
			if (!isSameType(params[i], types[i], paramArgs[i])) {
				return false;
			}

		}
		return true;
	}

	public final static boolean isSameType(Class type, Class toType,
			Object value) {
		if (toType == Object.class)
			return true;

		else if (type == toType)
			return true;
		else if (toType.isAssignableFrom(type))// 检查toType是不是type的父类或者是type所实现的接口
		{
			return true;
		} else if (type.isAssignableFrom(toType))// 检查type是不是toType的父类或者是拖油瓶toType所实现的接口
		{
			try {
				toType.cast(value);
				return true;
			} catch (Exception e) {
				return false;
			}
			// if(type.getName().equals(toType.getName()))
			// return true;
		}
	 else if ((type == boolean.class && toType == Boolean.class)
				|| type == Boolean.class && toType == boolean.class) {
			return true;
		}
		else if ((type == int.class && toType == Integer.class)
				|| type == Integer.class && toType == int.class) {
			return true;
		} else if ((type == short.class && toType == Short.class)
				|| type == Short.class && toType == short.class) {
			return true;
		} else if ((type == long.class && toType == Long.class)
				|| type == Long.class && toType == long.class) {
			return true;
		} else if ((type == double.class && toType == Double.class)
				|| type == Double.class && toType == double.class) {
			return true;
		} else if ((type == float.class && toType == Float.class)
				|| type == Float.class && toType == float.class) {
			return true;
		} else if ((type == char.class && toType == Character.class)
				|| type == Character.class && toType == char.class) {
			return true;
		}
		return false;

	}

	/**
	 * 通过属性编辑器来转换属性值
	 * 
	 * @param obj
	 * @param editor
	 * @return
	 * @throws NoSupportTypeCastException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 */
	public final static Object typeCast(Object obj, EditorInf editor)
			throws NoSupportTypeCastException, NumberFormatException,
			IllegalArgumentException {

		return editor.getValueFromObject(obj);
	}

	/**
	 * 将obj对象从类型type转换到类型toType 支持字符串向其他基本类行转换: 支持的类型:
	 * int,char,short,double,float,long,boolean,byte
	 * java.sql.Date,java.util.Date, Integer Long Float Short Double Character
	 * Boolean Byte
	 * 
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws ClassCastException
	 *             ,NumberFormatException,IllegalArgumentException
	 */
	public final static Object typeCast(Object obj, Class toType,String dateformat)
			throws NoSupportTypeCastException, NumberFormatException,
			IllegalArgumentException {
		if(toType == null)
			return obj;
		if (obj == null)
			return null;
		return typeCast(obj, obj.getClass(), toType,dateformat);
	}
	
	public final static Object typeCastWithDateformat(Object obj, Class toType,SimpleDateFormat dateformat)
			throws NoSupportTypeCastException, NumberFormatException,
			IllegalArgumentException {
		if (obj == null)
			return null;
		return typeCastWithDateformat(obj, obj.getClass(), toType,dateformat);
	}
	
	public final static Object typeCast(Object obj, Class toType)
	throws NoSupportTypeCastException, NumberFormatException,
	IllegalArgumentException{
		return typeCast( obj, toType,(String )null);
	}
	public final static Object typeCast(Object obj, Class type, Class toType)
	throws NoSupportTypeCastException, NumberFormatException,
	IllegalArgumentException
	{
		return typeCast(obj, type, toType,null);
	}
	
	public static byte[] getByteArrayFromBlob(Blob blob) throws SQLException
	{
		if(blob == null)
			return null;
		ByteArrayOutputStream out = null;
		InputStream in = null;
		
		try
		{
			out = new java.io.ByteArrayOutputStream();
			in =	blob.getBinaryStream();
			
			
			byte[] buf = new byte[1024];
			int i =0;
			
			while((i = in.read(buf)) > 0)
			{
				
//				System.out.println("i=" + i);
				
				out.write(buf,0,i);
							
			}
		
			return out.toByteArray();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				if(in != null)
				{
					in.close();
					in = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}


	}
	
	public static String getStringFromBlob(Blob blob) throws SQLException
	{		
		if(blob == null)
			return null;
		OutputStream out = null;
		InputStream in = null;
		
		try
		{
			out = new ByteArrayOutputStream();
			in =	blob.getBinaryStream();
			
			
			byte[] buf = new byte[1024];
			int i =0;
			
			while((i = in.read(buf)) > 0)
			{
				
//				System.out.println("i=" + i);
				
				out.write(buf,0,i);
							
			}
		
			return out.toString();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				if(in != null)
				{
					in.close();
					in = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}


	}
	
	public static byte[] getByteArrayFromClob(Clob clob) throws SQLException
	{
		if(clob == null)
			return null;
		StringWriter w = null;
		
		Reader out = null;
		try
		{
			w = new StringWriter();
			out =	clob.getCharacterStream();
			char[] buf = new char[1024];
			int i =0;
			
			while((i = out.read(buf)) > 0)
			{
				w.write(buf,0,i);
							
			}
			String temp = w.toString();
			return temp.getBytes();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				if(w != null)
				{
					w.close();
					w = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	
	public static String getStringFromClob(Clob clob) throws SQLException
	{
		if(clob == null)
			return null;
		StringWriter w = null;
		
		Reader out = null;
		try
		{
			w = new StringWriter();
			out =	clob.getCharacterStream();
			char[] buf = new char[1024];
			int i =0;
			
			while((i = out.read(buf)) > 0)
			{
				w.write(buf,0,i);
							
			}
			return w.toString();
			
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				if(w != null)
				{
					w.close();
					w = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	
	public static String getByteStringFromBlob(Blob blob) throws SQLException
	{
		if(blob == null)
			return null;
		ByteArrayOutputStream out = null;
		InputStream in = null;
		
		try
		{
			out = new java.io.ByteArrayOutputStream();
			in =	blob.getBinaryStream();
			
			
			byte[] buf = new byte[1024];
			int i =0;
			
			while((i = in.read(buf)) > 0)
			{
				
//				System.out.println("i=" + i);
				
				out.write(buf,0,i);
							
			}
			BASE64Encoder en = new BASE64Encoder();
			return en.encode(out.toByteArray());
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(out != null)
				{
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			
			try
			{
				if(in != null)
				{
					in.close();
					in = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}


	}
	/**
	 * 父类型向子类型转换
	 * @param obj
	 * @param toType
	 * @param type
	 * @return
	 */
	public static Object cast(Object obj,Class toType)
	{
//		if (!java.util.Date.class.isAssignableFrom(type))
		{
			if(!toType.isArray())
				return toType.cast(obj);
			else
			{
				int size = Array.getLength(obj);
				Class ctype = toType.getComponentType();
				Object ret = Array.newInstance(ctype, size);
				for(int i = 0; i < size; i ++)
				{
					Array.set(ret, i,ctype.cast(Array.get(obj, i)));
				}
				return ret;
			}
		}
		/**
		 * 日期类型处理比较特殊
		 */
//		return null;
			
	}
	/**
	 * 将obj对象从类型type转换到类型toType 支持字符串向其他基本类行转换: 支持的类型:
	 * int,char,short,double,float,long,boolean,byte
	 * java.sql.Date,java.util.Date, Integer Long Float Short Double Character
	 * Boolean Byte
	 * 
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws ClassCastException
	 *             ,NumberFormatException,IllegalArgumentException
	 */
	public final static Object typeCast(Object obj, Class type, Class toType,String dateformat)
			throws NoSupportTypeCastException, NumberFormatException,
			IllegalArgumentException {
		if (obj == null)
			return null;
		if (isSameType(type, toType, obj))
			return obj;

		if (type.isAssignableFrom(toType)) // type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
			// return shell(toType,obj);
			if (!java.util.Date.class.isAssignableFrom(type))
//				return toType.cast(obj);
				return cast(obj,toType);
		}

		if (type == byte[].class && toType == String.class) {
			return new String((byte[]) obj);
		} else if (type == String.class && toType == byte[].class) {
			return ((String) obj).getBytes();
		}
		else if (java.sql.Blob.class.isAssignableFrom(type) ) {
			
			try
			{
				if( File.class.isAssignableFrom(toType))
				{
					File tmp = File.createTempFile(java.util.UUID.randomUUID().toString(),".tmp");
					getFileFromBlob((Blob)obj,tmp);
					return tmp;
				}
				else if( byte[].class.isAssignableFrom(toType))
				{
					return ValueObjectUtil.getByteArrayFromBlob((Blob)obj);
				}
				else
					return ValueObjectUtil.getStringFromBlob((Blob)obj);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向[")
				.append(toType.getName()).append("]转换").append(",value is ").append(obj).toString());
			}
			
			
		}
		else if (java.sql.Clob.class.isAssignableFrom(type) ) {
			
			try
			{
				if( File.class.isAssignableFrom(toType))
				{
					File tmp = File.createTempFile(java.util.UUID.randomUUID().toString(),".tmp");
					getFileFromClob((Clob)obj,tmp);
					return tmp;
				}
				else if( byte[].class.isAssignableFrom(toType))
				{
					return ValueObjectUtil.getByteArrayFromClob((Clob)obj);
				}
				else
					return ValueObjectUtil.getStringFromClob((Clob)obj);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向[")
				.append(toType.getName()).append("]转换").append(",value is").append(obj).toString());
			}
			
			
		}

		else if (type == byte[].class && File.class.isAssignableFrom(toType)) {
			java.io.ByteArrayInputStream byteIn = null;
			java.io.FileOutputStream fileOut = null;
			if(!(obj instanceof byte[]))
			{
				Object[] object = (Object[]) obj;
				
				try {
					byteIn = new java.io.ByteArrayInputStream((byte[]) object[0]);
					fileOut = new java.io.FileOutputStream((File) object[1]);
					byte v[] = new byte[1024];
	
					int i = 0;
	
					while ((i = byteIn.read(v)) > 0) {
						fileOut.write(v, 0, i);
	
					}
					fileOut.flush();
					return object[1];
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (byteIn != null)
							byteIn.close();
					} catch (Exception e) {
	
					}
					try {
						if (fileOut != null)
							fileOut.close();
					} catch (Exception e) {
	
					}
				}
			}
			else
			{
				try {
					byteIn = new java.io.ByteArrayInputStream((byte[]) obj);
					File f = File.createTempFile(java.util.UUID.randomUUID().toString(), ".soa");
					fileOut = new java.io.FileOutputStream(f);
					byte v[] = new byte[1024];
	
					int i = 0;
	
					while ((i = byteIn.read(v)) > 0) {
						fileOut.write(v, 0, i);
	
					}
					fileOut.flush();
					return f;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (byteIn != null)
							byteIn.close();
					} catch (Exception e) {
	
					}
					try {
						if (fileOut != null)
							fileOut.close();
					} catch (Exception e) {
	
					}
				}
			}
		} else if (List.class.isAssignableFrom(toType)) {

			if (!type.isArray()) {
				List valueto = new java.util.ArrayList();
				valueto.add(obj);
				return valueto;
			} else {
				if (type == String[].class) {
					List valueto = new java.util.ArrayList();
					for (String data : (String[]) obj)
						valueto.add(data);
					return valueto;
				}
			}

		}

		else if (Set.class.isAssignableFrom(toType)) {

			if (!type.isArray()) {
				Set valueto = new java.util.TreeSet();
				valueto.add(obj);
				return valueto;
			} else {
				if (type == String[].class) {
					Set valueto = new java.util.TreeSet();
					for (String data : (String[]) obj)
						valueto.add(data);
					return valueto;
				}

			}

		} else if (File.class.isAssignableFrom(toType)
				&& toType == byte[].class) {
			java.io.FileInputStream in = null;
			java.io.ByteArrayOutputStream out = null;
			try {
				int i = 0;
				in = new FileInputStream((File) obj);
				out = new ByteArrayOutputStream();
				byte v[] = new byte[1024];
				while ((i = in.read(v)) > 0) {
					out.write(v, 0, i);
				}
				return out.toByteArray();
			} catch (Exception e) {

			} finally {
				try {
					if (in != null)
						in.close();
				} catch (Exception e) {

				}
				try {
					if (out != null)
						out.close();
				} catch (Exception e) {

				}
			}

		} else if (type.isArray() && !toType.isArray()){ 
				//|| !type.isArray()
				//&& toType.isArray()) {
			// if (type.getName().startsWith("[")
			// && !toType.getName().startsWith("[")
			// || !type.getName().startsWith("[")
			// && toType.getName().startsWith("["))
			throw new IllegalArgumentException(new StringBuffer("类型无法转换,不支持[")
					.append(type.getName()).append("]向[").append(
							toType.getName()).append("]转换").append(",value is ").append(obj).toString());
		} else if (type == String.class && toType == Class.class) {
			try {
				return getClass((String) obj);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(new StringBuffer(
						"类型无法转换,不支持[").append(type.getName()).append("]向[")
						.append(toType.getName()).append("]转换").toString(),e);
			}
		}
		Object arrayObj;

		/**
		 * 基本类型转换和基本类型之间相互转换
		 */
		if (!type.isArray() && !toType.isArray()) {
			arrayObj = basicTypeCast(obj, type, toType,dateformat);
		}

		/**
		 * 字符串数组向其他类型数组之间转换
		 * 数组和数组之间的转换
		 * 基础类型数据向数组转换
		 */
		else {

			arrayObj = arrayTypeCast(obj, type, toType,dateformat);
		}
		return arrayObj;
	}
	
	public final static Object typeCastWithDateformat(Object obj, Class type, Class toType,SimpleDateFormat dateformat)
			throws NoSupportTypeCastException, NumberFormatException,
			IllegalArgumentException {
		if (obj == null)
			return null;
		if (isSameType(type, toType, obj))
			return obj;

		if (type.isAssignableFrom(toType)) // type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
			// return shell(toType,obj);
			if (!java.util.Date.class.isAssignableFrom(type))
//				return toType.cast(obj);
				return cast(obj,toType);
		}

		if (type == byte[].class && toType == String.class) {
			return new String((byte[]) obj);
		} else if (type == String.class && toType == byte[].class) {
			return ((String) obj).getBytes();
		}
		else if (java.sql.Blob.class.isAssignableFrom(type) ) {
			
			try
			{
				if( File.class.isAssignableFrom(toType))
				{
					File tmp = File.createTempFile(java.util.UUID.randomUUID().toString(),".tmp");
					getFileFromBlob((Blob)obj,tmp);
					return tmp;
				}
				else if( byte[].class.isAssignableFrom(toType))
				{
					return ValueObjectUtil.getByteArrayFromBlob((Blob)obj);
				}
				else
					return ValueObjectUtil.getStringFromBlob((Blob)obj);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向[")
				.append(toType.getName()).append("]转换").append(",value is ").append(obj).toString());
			}
			
			
		}
		else if (java.sql.Clob.class.isAssignableFrom(type) ) {
			
			try
			{
				if( File.class.isAssignableFrom(toType))
				{
					File tmp = File.createTempFile(java.util.UUID.randomUUID().toString(),".tmp");
					getFileFromClob((Clob)obj,tmp);
					return tmp;
				}
				else if( byte[].class.isAssignableFrom(toType))
				{
					return ValueObjectUtil.getByteArrayFromClob((Clob)obj);
				}
				else
					return ValueObjectUtil.getStringFromClob((Clob)obj);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向[")
				.append(toType.getName()).append("]转换").append(",value is").append(obj).toString());
			}
			
			
		}

		else if (type == byte[].class && File.class.isAssignableFrom(toType)) {
			java.io.ByteArrayInputStream byteIn = null;
			java.io.FileOutputStream fileOut = null;
			if(!(obj instanceof byte[]))
			{
				Object[] object = (Object[]) obj;
				
				try {
					byteIn = new java.io.ByteArrayInputStream((byte[]) object[0]);
					fileOut = new java.io.FileOutputStream((File) object[1]);
					byte v[] = new byte[1024];
	
					int i = 0;
	
					while ((i = byteIn.read(v)) > 0) {
						fileOut.write(v, 0, i);
	
					}
					fileOut.flush();
					return object[1];
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (byteIn != null)
							byteIn.close();
					} catch (Exception e) {
	
					}
					try {
						if (fileOut != null)
							fileOut.close();
					} catch (Exception e) {
	
					}
				}
			}
			else
			{
				try {
					byteIn = new java.io.ByteArrayInputStream((byte[]) obj);
					File f = File.createTempFile(java.util.UUID.randomUUID().toString(), ".soa");
					fileOut = new java.io.FileOutputStream(f);
					byte v[] = new byte[1024];
	
					int i = 0;
	
					while ((i = byteIn.read(v)) > 0) {
						fileOut.write(v, 0, i);
	
					}
					fileOut.flush();
					return f;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (byteIn != null)
							byteIn.close();
					} catch (Exception e) {
	
					}
					try {
						if (fileOut != null)
							fileOut.close();
					} catch (Exception e) {
	
					}
				}
			}
		} else if (List.class.isAssignableFrom(toType)) {

			if (!type.isArray()) {
				List valueto = new java.util.ArrayList();
				valueto.add(obj);
				return valueto;
			} else {
				if (type == String[].class) {
					List valueto = new java.util.ArrayList();
					for (String data : (String[]) obj)
						valueto.add(data);
					return valueto;
				}
			}

		}

		else if (Set.class.isAssignableFrom(toType)) {

			if (!type.isArray()) {
				Set valueto = new java.util.TreeSet();
				valueto.add(obj);
				return valueto;
			} else {
				if (type == String[].class) {
					Set valueto = new java.util.TreeSet();
					for (String data : (String[]) obj)
						valueto.add(data);
					return valueto;
				}

			}

		} else if (File.class.isAssignableFrom(toType)
				&& toType == byte[].class) {
			java.io.FileInputStream in = null;
			java.io.ByteArrayOutputStream out = null;
			try {
				int i = 0;
				in = new FileInputStream((File) obj);
				out = new ByteArrayOutputStream();
				byte v[] = new byte[1024];
				while ((i = in.read(v)) > 0) {
					out.write(v, 0, i);
				}
				return out.toByteArray();
			} catch (Exception e) {

			} finally {
				try {
					if (in != null)
						in.close();
				} catch (Exception e) {

				}
				try {
					if (out != null)
						out.close();
				} catch (Exception e) {

				}
			}

		} else if (type.isArray() && !toType.isArray()){ 
				//|| !type.isArray()
				//&& toType.isArray()) {
			// if (type.getName().startsWith("[")
			// && !toType.getName().startsWith("[")
			// || !type.getName().startsWith("[")
			// && toType.getName().startsWith("["))
			throw new IllegalArgumentException(new StringBuffer("类型无法转换,不支持[")
					.append(type.getName()).append("]向[").append(
							toType.getName()).append("]转换").append(",value is ").append(obj).toString());
		} else if (type == String.class && toType == Class.class) {
			try {
				return getClass((String) obj);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(new StringBuffer(
						"类型无法转换,不支持[").append(type.getName()).append("]向[")
						.append(toType.getName()).append("]转换").toString(),e);
			}
		}
		Object arrayObj;

		/**
		 * 基本类型转换和基本类型之间相互转换
		 */
		if (!type.isArray() && !toType.isArray()) {
			arrayObj = basicTypeCastWithDateformat(obj, type, toType,dateformat);
		}

		/**
		 * 字符串数组向其他类型数组之间转换
		 * 数组和数组之间的转换
		 * 基础类型数据向数组转换
		 */
		else {

			arrayObj = arrayTypeCastWithDateformat(obj, type, toType,dateformat);
		}
		return arrayObj;
	}
	
	
	

	/**
	 * 通过BeanShell脚本来转换对象类型
	 * 
	 * @param toType
	 * @param obj
	 * @return
	 */
	public static Object shell(Class toType, Object obj) {
		Interpreter interpreter = new Interpreter();
		String shell = toType.getName() + " ret = (" + toType.getName()
				+ ")obj;return ret;";
		try {
			interpreter.set("obj", obj);
			Object ret = interpreter.eval(shell);
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public final static Object basicTypeCast(Object obj, Class type,
			Class toType) throws NoSupportTypeCastException,
			NumberFormatException 
			{
		return  basicTypeCast( obj, type,
				toType,null);
			}
	
//	public static SimpleDateFormat getDateFormat(
//			String dateformat)
//	{
//		if(dateformat == null || dateformat.equals(""))
//			return format;
//		SimpleDateFormat f = dataformats.get(dateformat);
//		if(f != null)
//			return f;
//		f = new SimpleDateFormat(dateformat);
//		dataformats.put(dateformat, f);
//		return f;
//	}
	

	public static SimpleDateFormat getDefaultDateFormat(){
			return DataFormatUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			return new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
	}
	public static SimpleDateFormat getDateFormat(
			String dateformat)
	{
		if(dateformat == null || dateformat.equals(""))
			return  getDefaultDateFormat();
		SimpleDateFormat f = DataFormatUtil.getSimpleDateFormat(dateformat);
//		if(f != null)
//			return f;
		
//		dataformats.put(dateformat, f);
		return f;
	}
	
	public static Boolean toBoolean(Object obj)
	{
		if(obj == null)
			return new Boolean(false);
		if(obj instanceof Boolean)
		{
			return ((Boolean)obj);
		}
		else if(obj instanceof String)
		{
			String ret = obj.toString();
			if (ret.equals("1") || ret.equals("true")) {
				return new Boolean(true);
			} else if (ret.equals("0") || ret.equals("false")) {
				return new Boolean(false);
			}
			else
				return false;
		}
		else if(obj instanceof Integer)
		{
			return ((Integer)obj).intValue() > 0;
		}
		else if(obj instanceof Long)
		{
			return ((Long)obj).longValue() > 0;
		}
		else if(obj instanceof Double)
		{
			return ((Double)obj).doubleValue() > 0;
		}
		else if(obj instanceof Float)
		{
			return ((Float)obj).floatValue() > 0;
		}
		else if(obj instanceof Short)
		{
			return ((Short)obj).shortValue() > 0;
		}
		else if(obj instanceof BigInteger)
		{
			return ((BigInteger)obj).intValue() > 0;
		}
		else if(obj instanceof BigDecimal)
		{
			return ((BigDecimal)obj).floatValue() > 0;
		}
		
		return false;
	}
	/**
	 * Description:基本的数据类型转圜
	 * 
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws NoSupportTypeCastException
	 * @throws NumberFormatException
	 * 
	 */
	public final static Object basicTypeCast(Object obj, Class type,
			Class toType,String dateformat) throws NoSupportTypeCastException,
			NumberFormatException {
		if (obj == null)
			return null;
		if (isSameType(type, toType, obj))
			return obj;

		if (type.isAssignableFrom(toType)) // type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
			if (!java.util.Date.class.isAssignableFrom(type))
				return shell(toType, obj);
		}
		/**
		 * 如果obj的类型不是String型时直接抛异常, 不支持非字符串和字符串数组的类型转换
		 */
		// if (type != String.class)
		// throw new NoSupportTypeCastException(
		// new StringBuffer("不支持[")
		// .append(type)
		// .append("]向[")
		// .append(toType)
		// .append("]的转换")
		// .toString());
		/*******************************************
		 * 字符串向基本类型及其包装器转换 * 如果obj不是相应得数据格式,将抛出 * NumberFormatException *
		 ******************************************/
		if (toType == long.class || toType == Long.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).longValue();
			else if(java.util.Date.class.isAssignableFrom(type))
			{
				return ((java.util.Date)obj).getTime();
			}
			return Long.parseLong(obj.toString());
		}
		if (toType == int.class || toType == Integer.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).intValue();
			return Integer.parseInt(obj.toString());
		}
		if (toType == float.class || toType == Float.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).floatValue();
			return Float.parseFloat(obj.toString());
		}
		if (toType == short.class || toType == Short.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).shortValue();
			return Short.parseShort(obj.toString());
		}
		if (toType == double.class || toType == Double.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).doubleValue();
			return Double.parseDouble(obj.toString());
		}
		if (toType == char.class || toType == Character.class)
			return new Character(obj.toString().charAt(0));

		if (toType == boolean.class || toType == Boolean.class) {
//			String ret = obj.toString();
//			if (ret.equals("1") || ret.equals("true")) {
//				return new Boolean(true);
//			} else if (ret.equals("0") || ret.equals("false")) {
//				return new Boolean(false);
//			}
//			return new Boolean(ret);
			return toBoolean(obj);
		}

		if (toType == byte.class || toType == Byte.class)
			return new Byte(obj.toString());
		if(toType == BigDecimal.class)
		{
			return converObjToBigDecimal(obj);
			
		}
		if(toType == BigInteger.class)
		{
			return converObjToBigInteger(obj);
			
		}
		// 如果是字符串则直接返回obj.toString()
		if (toType == String.class) {
			if (obj instanceof java.util.Date)
			{
				
				if(!"".equals(obj))
					return getDateFormat(dateformat).format(obj);
				return null;
			}
			
			return obj.toString();
		}
		
		Object date = convertObjToDate( obj,toType,dateformat);
		if(date != null)
			return date;
		
		if (type == String.class && toType == Class.class) {
			try {
				return getClass((String) obj);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(new StringBuffer(
						"类型无法转换,不支持[").append(type.getName()).append("]向[")
						.append(toType.getName()).append("]转换").toString(),e);
			}
		}
		
		if (type == String.class && toType.isEnum())
		{
			
			try {
				return convertStringToEnum((String )obj,toType);
			} catch (SecurityException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			}
		}

		throw new NoSupportTypeCastException(new StringBuffer("不支持[").append(
				type).append("]向[").append(toType).append("]的转换").append(",value is").append(obj).toString());

	}
	
	public final static Object basicTypeCastWithDateformat(Object obj, Class type,
			Class toType,SimpleDateFormat dateformat) throws NoSupportTypeCastException,
			NumberFormatException {
		if (obj == null)
			return null;
		if (isSameType(type, toType, obj))
			return obj;

		if (type.isAssignableFrom(toType)) // type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
			if (!java.util.Date.class.isAssignableFrom(type))
				return shell(toType, obj);
		}
		/**
		 * 如果obj的类型不是String型时直接抛异常, 不支持非字符串和字符串数组的类型转换
		 */
		// if (type != String.class)
		// throw new NoSupportTypeCastException(
		// new StringBuffer("不支持[")
		// .append(type)
		// .append("]向[")
		// .append(toType)
		// .append("]的转换")
		// .toString());
		/*******************************************
		 * 字符串向基本类型及其包装器转换 * 如果obj不是相应得数据格式,将抛出 * NumberFormatException *
		 ******************************************/
		if (toType == long.class || toType == Long.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).longValue();
			else if(java.util.Date.class.isAssignableFrom(type))
			{
				return ((java.util.Date)obj).getTime();
			}
			return Long.parseLong(obj.toString());
		}
		if (toType == int.class || toType == Integer.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).intValue();
			return Integer.parseInt(obj.toString());
		}
		if (toType == float.class || toType == Float.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).floatValue();
			return Float.parseFloat(obj.toString());
		}
		if (toType == short.class || toType == Short.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).shortValue();
			return Short.parseShort(obj.toString());
		}
		if (toType == double.class || toType == Double.class) {
			if (ValueObjectUtil.isNumber(obj))
				return ((Number) obj).doubleValue();
			return Double.parseDouble(obj.toString());
		}
		if (toType == char.class || toType == Character.class)
			return new Character(obj.toString().charAt(0));

		if (toType == boolean.class || toType == Boolean.class) {
//			String ret = obj.toString();
//			if (ret.equals("1") || ret.equals("true")) {
//				return new Boolean(true);
//			} else if (ret.equals("0") || ret.equals("false")) {
//				return new Boolean(false);
//			}
//			return new Boolean(obj.toString());
			return toBoolean(obj);
		}

		if (toType == byte.class || toType == Byte.class)
			return new Byte(obj.toString());
		if(toType == BigDecimal.class)
		{
			return converObjToBigDecimal(obj);
			
		}
		// 如果是字符串则直接返回obj.toString()
		if (toType == String.class) {
			if (obj instanceof java.util.Date)
			{
				
				if(!"".equals(obj))
				{
					if(dateformat == null)
						dateformat = ValueObjectUtil.getDefaultDateFormat();
					return dateformat.format(obj);
				}
				return null;
			}
			
			return obj.toString();
		}
		
		Object date = convertObjToDateWithDateformat( obj,toType,dateformat);
		if(date != null)
			return date;
		
		if (type == String.class && toType == Class.class) {
			try {
				return getClass((String) obj);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(new StringBuffer(
						"类型无法转换,不支持[").append(type.getName()).append("]向[")
						.append(toType.getName()).append("]转换").toString(),e);
			}
		}
		
		if (type == String.class && toType.isEnum())
		{
			
			try {
				return convertStringToEnum((String )obj,toType);
			} catch (SecurityException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException(new StringBuffer(
				"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
				.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
			}
		}

		throw new NoSupportTypeCastException(new StringBuffer("不支持[").append(
				type).append("]向[").append(toType).append("]的转换").append(",value is").append(obj).toString());

	}
	
	
	public static BigDecimal converObjToBigDecimal(Object obj)
	{
		if(obj.getClass() == long.class || obj.getClass() == Long.class)
			return BigDecimal.valueOf((Long)obj);
		if(obj.getClass() == short.class || obj.getClass() == Short.class)
			return BigDecimal.valueOf((Short)obj);
		if(obj.getClass() == int.class || obj.getClass() == Integer.class)
			return BigDecimal.valueOf((Integer)obj);
		if(obj.getClass() == double.class || obj.getClass() == Double.class)
			return BigDecimal.valueOf((Double)obj);
		if(obj.getClass() == float.class || obj.getClass() == Float.class)
			return BigDecimal.valueOf((Float)obj);
		return new BigDecimal(obj.toString());
	}
	public static BigInteger converObjToBigInteger(Object obj)
	{
		if(obj.getClass() == long.class || obj.getClass() == Long.class)
			return BigInteger.valueOf((Long)obj);
		if(obj.getClass() == short.class || obj.getClass() == Short.class)
			return BigInteger.valueOf((Short)obj);
		if(obj.getClass() == int.class || obj.getClass() == Integer.class)
			return BigInteger.valueOf((Integer)obj);
		if(obj.getClass() == double.class || obj.getClass() == Double.class)
			return BigInteger.valueOf(((Double)obj).longValue());
		if(obj.getClass() == float.class || obj.getClass() == Float.class)
			return BigInteger.valueOf(((Float)obj).longValue());
		return new BigInteger(obj.toString());
	}
	public static Object convertObjToDate(Object obj,Class toType,String dateformat)
	{
		if(dateformat == null)
			return convertObjToDateWithDateformat(obj,toType,null);
		else
			return convertObjToDateWithDateformat(obj,toType,ValueObjectUtil.getDateFormat(dateformat));
	}
	public static Object convertObjToDateWithDateformat(Object obj,Class toType,SimpleDateFormat dateformat)
	{
		if(dateformat == null)
			dateformat = ValueObjectUtil.getDefaultDateFormat();
		/**
		 * 字符串向java.util.Date和java.sql.Date 类型转换
		 */
		if (toType == java.util.Date.class) {
			// if(obj instanceof java.sql.Date
			// || obj instanceof java.sql.Time
			// || obj instanceof java.sql.Timestamp)
			if (java.util.Date.class.isAssignableFrom(obj.getClass()))
				return obj;
			if(obj.getClass() == long.class || obj.getClass() == Long.class)
			{
				return new java.util.Date((Long)obj);
			}
			String data_str = obj.toString();
			try {
				
				if(!"".equals(data_str))					
					return dateformat.parse(data_str);
				else
					return null;
			} catch (ParseException e) {
				try
				{
					long dl = Long.parseLong(data_str);
					return new java.util.Date(dl);
				}
				catch (Exception e1)
				{
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}
				
				log.error(e.getMessage(),e);
			}
			return new java.util.Date(data_str);
		}

		if (toType == java.sql.Date.class) {

			// if(obj instanceof java.sql.Date
			// || obj instanceof java.sql.Time
			// || obj instanceof java.sql.Timestamp)
			if (java.util.Date.class.isAssignableFrom(obj.getClass()))
				return new java.sql.Date(((java.util.Date) obj).getTime());
			if(obj.getClass() == long.class || obj.getClass() == Long.class)
			{
				return new java.sql.Date((Long)obj);
			}
			String data_str = obj.toString();
			try {
				
				
				if(!"".equals(data_str))					
					return new java.sql.Date(dateformat.parse(data_str).getTime());
				else
					return null;
			} catch (ParseException e) {
				try
				{
					long dl = Long.parseLong(data_str);
					return new java.util.Date(dl);
				}
				catch (Exception e1)
				{
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}
				log.error(e.getMessage(),e);
			}
			java.sql.Date date = java.sql.Date.valueOf(data_str);// (new
			// java.util.Date(obj.toString()).getTime());

			return date;
		}
		
		if (toType == java.sql.Timestamp.class) {

			// if(obj instanceof java.sql.Date
			// || obj instanceof java.sql.Time
			// || obj instanceof java.sql.Timestamp)
			if (java.util.Date.class.isAssignableFrom(obj.getClass()))
				return new java.sql.Timestamp(((java.util.Date) obj).getTime());
			if(obj.getClass() == long.class || obj.getClass() == Long.class)
			{
				return new java.sql.Timestamp((Long)obj);
			}
			String data_str = obj.toString();
			try {
				
				if(!"".equals(data_str))					
					return new java.sql.Timestamp((dateformat).parse(data_str).getTime());
				else
					return null;
			} catch (ParseException e) {
				try
				{
					long dl = Long.parseLong(data_str);
					return new java.util.Date(dl);
				}
				catch (Exception e1)
				{
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}
				log.error(e.getMessage(),e);
			}
			java.sql.Timestamp date = new Timestamp(java.sql.Date.valueOf(data_str).getTime());// (new
			// java.util.Date(obj.toString()).getTime());

			return date;
		}
		
		return null;
	}
	public static <T> T convertStringToEnum(String value,Class<T> enumType) throws SecurityException, NoSuchMethodException, 
				IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		
		Method valueof = enumType.getMethod("valueOf", String.class);
		return (T)valueof.invoke(null, value);
		
	}
	
	/**
	 * 
	 * @param <T>
	 * @param value
	 * @param enumType
	 * @param arrays
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static <T> T[] convertStringToEnumArray(String value,Class<T> enumType,T[] arrays) throws SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
	
		Method valueof = enumType.getMethod("valueOf", String.class);
		
		Array.set(arrays, 0, valueof.invoke(null, value));
		return arrays;
		
	}
	
	public static <T> T[] convertStringsToEnumArray(String[] value,Class<T> enumType,T[] arrays) throws SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		
		if(value == null)
			return null;
//		if(value.length == 0)
		
		int i = 0;
		Method valueof = enumType.getMethod("valueOf", String.class);
		for(String v:value)
		{			
			Array.set(arrays, i, valueof.invoke(null, value[i]));
			i ++;
		}
		
		return arrays;
	
	}
	
	public static <T> T[] convertStringToEnumArray(String value,Class<T> enumType) throws SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
	
		Method valueof = enumType.getMethod("valueOf", String.class);
		Object retvalue = Array.newInstance(enumType, 1);
		Array.set(retvalue, 0, valueof.invoke(null, value));
		return (T[])retvalue;
		
	}
	
	public static <T> T[] convertStringsToEnumArray(String[] value,Class<T> enumType) throws SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		
		if(value == null)
			return null;
//		if(value.length == 0)
		Object retvalue = Array.newInstance(enumType, value.length);
		int i = 0;
		Method valueof = enumType.getMethod("valueOf", String.class);
		for(String v:value)
		{			
			Array.set(retvalue, i, valueof.invoke(null, value[i]));
			i ++;
		}
		
		return (T[])retvalue;
//		return temp_;
	
	}
	
	

	public final static Object arrayTypeCast(Object obj, Class type,
			Class toType) throws NoSupportTypeCastException,
			NumberFormatException {
		return arrayTypeCast(obj, type,
				toType,null);
	}
	public final static Object arrayTypeCast(Object obj, Class type,
			Class toType,String dateformat) throws NoSupportTypeCastException,
			NumberFormatException{
		SimpleDateFormat dateformat_ = null;
		if(dateformat != null)
			dateformat_ = DataFormatUtil.getSimpleDateFormat(dateformat);
		return arrayTypeCastWithDateformat(obj, type,
				toType,dateformat_);
	}
	/**
	 * 数组类型转换 支持字符串数组向一下类型数组得自动转换: int[] Integer[] long[] Long[] short[] Short[]
	 * double[] Double[] boolean[] Boolean[] char[] Character[] float[] Float[]
	 * byte[] Byte[] java.sql.Date[] java.util.Date[]
	 * 
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws NoSupportTypeCastException
	 * @throws NumberFormatException
	 */
	public final static Object arrayTypeCastWithDateformat(Object obj, Class type,
			Class toType,SimpleDateFormat dateformat) throws NoSupportTypeCastException,
			NumberFormatException {
		if (isSameType(type, toType, obj))
			return obj;
		// if (type != String[].class)
		// throw new NoSupportTypeCastException(
		// new StringBuffer("不支持[")
		// .append(type)
		// .append("]向[")
		// .append(toType)
		// .append("]的转换")
		// .toString());
		if(dateformat == null)
			dateformat = ValueObjectUtil.getDefaultDateFormat();
		if (toType == long[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				if(java.util.Date.class.isAssignableFrom(type))
				{
					java.util.Date date = (java.util.Date)obj;
					return new long[]{date.getTime()};
				}
				else if(!isDateArray(obj))
				{
					String[] values = (String[]) obj;
					long[] ret = new long[values.length];
					for (int i = 0; i < values.length; i++) {
						ret[i] = Long.parseLong(values[i]);
					}
					return ret;	
				}
				else
				{
					int len = Array.getLength(obj);
					long[] ret = new long[len];
					for(int i = 0;  i < len; i ++)
					{
						java.util.Date date = (java.util.Date)Array.get(obj, i);
						ret[i] = date.getTime();
					}
					return ret;
					
				}
				
			} else {
				return ValueObjectUtil.tolongArray(obj, componentType);

			}
		}
		if (toType == Long[].class) {

			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				if(!isDateArray(obj))
				{
					String[] values = (String[]) obj;
					Long[] ret = new Long[values.length];
					for (int i = 0; i < values.length; i++) {
						ret[i] = new Long(values[i]);
					}
					
					return ret;
				}
				else
				{

					int len = Array.getLength(obj);
					Long[] ret = new Long[len];
					for(int i = 0;  i < len; i ++)
					{
						java.util.Date date = (java.util.Date)Array.get(obj, i);
						ret[i] = date.getTime();
					}
					return ret;
				}
			}
			else
			{
				return ValueObjectUtil.toLongArray(obj, componentType);
			}
		}

		if (toType == int[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				int[] ret = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = Integer.parseInt(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toIntArray(obj, componentType);
			}
		}
		if (toType == Integer[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				Integer[] ret = new Integer[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = new Integer(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toIntegerArray(obj, componentType);
			}
		}
		if (toType == float[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				float[] ret = new float[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = Float.parseFloat(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.tofloatArray(obj, componentType);
			}
		}
		if (toType == Float[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				Float[] ret = new Float[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = new Float(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toFloatArray(obj, componentType);
			}
		}
		if (toType == short[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				short[] ret = new short[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = Short.parseShort(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toshortArray(obj, componentType);
			}
		}
		if (toType == Short[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				Short[] ret = new Short[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = new Short(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toShortArray(obj, componentType);
			}
		}
		if (toType == double[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				double[] ret = new double[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = Double.parseDouble(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.todoubleArray(obj, componentType);
			}
		}
		if (toType == Double[].class) {
			Class componentType = ValueObjectUtil.isNumberArray(obj);
			if (componentType == null) {
				String[] values = (String[]) obj;
				Double[] ret = new Double[values.length];
				for (int i = 0; i < values.length; i++) {
					ret[i] = new Double(values[i]);
				}
				return ret;
			}
			else
			{
				return ValueObjectUtil.toDoubleArray(obj, componentType);
			}
		}
		
		if(toType == BigDecimal[].class)
		{
			
			return toBigDecimalArray(obj, null);
			
		}
		
		if(toType == BigInteger[].class)
		{
			
			return toBigIntegerArray(obj, null);
			
		}

		if (toType == char[].class) {
			String[] values = (String[]) obj;
			char[] ret = new char[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].charAt(0);
			}
			return ret;
		}
		if (toType == Character[].class) {
			String[] values = (String[]) obj;
			Character[] ret = new Character[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Character(values[i].charAt(0));
			}
			return ret;
		}

		if (toType == boolean[].class) {
			String[] values = (String[]) obj;
			boolean[] ret = new boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = toBoolean(values[i]);//new Boolean(values[i]).booleanValue();
			}
			return ret;
		}
		if (toType == Boolean[].class) {
			String[] values = (String[]) obj;
			Boolean[] ret = new Boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = toBoolean(values[i]);//new Boolean(values[i]);
			}
			return ret;
		}
		if (toType == byte[].class) {
			String[] values = (String[]) obj;
			byte[] ret = new byte[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Byte(values[i]).byteValue();
			}
			return ret;
		}
		if (toType == Byte[].class) {
			String[] values = (String[]) obj;
			Byte[] ret = new Byte[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Byte(values[i]);
			}
			return ret;
		}

		// 如果是字符串则直接返回obj.toString()
		if (toType == String[].class) {
			{
				if (obj.getClass() == java.util.Date[].class)
					return SimpleStringUtil.dateArrayTOStringArray((Date[]) obj);
				String[] values = (String[]) obj;
				return values;
			}
			// short[] ret = new short[values.length];
			// for(int i = 0; i < values.length; i ++)
			// {
			// ret[i] = Short.parseShort(values[i]);
			// }
			// return ret;
		}
		/**
		 * 字符串向java.util.Date和java.sql.Date 类型转换
		 */
		Object dates = convertObjectToDateArrayWithDateFormat( obj,type,toType, dateformat);
		if(dates != null)
			return dates;
		/**
		 * 枚举数组类型处理转换
		 */
		if(toType.isArray() && toType.getComponentType().isEnum())
		{
			if(type == String.class )
			{
				try {
					Object value = convertStringToEnumArray((String )obj,toType.getComponentType());
					
					return value;
				} catch (SecurityException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (InvocationTargetException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				}
			}
			else if(type == String[].class )
			{
				try {
					Object value = convertStringsToEnumArray((String[] )obj,toType.getComponentType());
					
					return value;
				} catch (SecurityException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (NoSuchMethodException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				} catch (InvocationTargetException e) {
					throw new IllegalArgumentException(new StringBuffer(
					"类型无法转换,不支持[").append(type.getName()).append("]向枚举类型[")
					.append(toType.getName()).append("]转换，超出枚举值范围").append(",value is").append(obj).toString());
				}
			}
				
		}

		throw new NoSupportTypeCastException(new StringBuffer("不支持[").append(
				type).append("]向[").append(toType).append("]的转换").append(",value is").append(obj).toString());

	}
	public static Object convertObjectToDateArray(Object obj,Class type,Class toType,String dateformat)
	{
		if(dateformat == null)
			return convertObjectToDateArrayWithDateFormat(obj,type,toType,ValueObjectUtil.getDefaultDateFormat());
		else
			return convertObjectToDateArrayWithDateFormat(obj,type,toType,ValueObjectUtil.getDateFormat(dateformat));
	}
	public static Object convertObjectToDateArrayWithDateFormat(Object obj,Class type,Class toType,SimpleDateFormat dateformat)
	{
		if(dateformat == null)
			dateformat = ValueObjectUtil.getDefaultDateFormat();
		if (toType == java.util.Date[].class) {
			if(type.isArray())
			{
				if(type == String[].class)
				{
					String[] values = (String[]) obj;
					return SimpleStringUtil.stringArrayTODateArray(values,(dateformat));
				}
				else
				{
					long[] values = (long[])obj;
					return SimpleStringUtil.longArrayTODateArray(values,(dateformat));
				}
			}
			else
			{
				if(type == String.class)
				{
					String[] values = new String[] {(String)obj};
					return SimpleStringUtil.stringArrayTODateArray(values,dateformat);
				}
				else 
				{
					long[] values = new long[] {((Long)obj).longValue()};
					return SimpleStringUtil.longArrayTODateArray(values,(dateformat));
				}
			}
		}
		if (toType == java.sql.Date[].class) {
			if(type.isArray())
			{
				if(type == String[].class)
				{
					String[] values = (String[] )obj;
					return SimpleStringUtil.stringArrayTOSQLDateArray(values,(dateformat));
				}
				else
				{
					long[] values = (long[] )obj;
					
					return SimpleStringUtil.longArrayTOSQLDateArray(values,(dateformat));
				}
			}
			else
			{
				if(type == String.class)
				{
					String[] values = new String[] {(String)obj};
					return SimpleStringUtil.stringArrayTOSQLDateArray(values,(dateformat));
				}
				else 
				{
					long[] values = new long[] {((Long)obj).longValue()};
					
					return SimpleStringUtil.longArrayTOSQLDateArray(values,(dateformat));
				}
			}
			
		}
		
		if (toType == java.sql.Timestamp[].class) {
			
			
			if(type.isArray())
			{
				if(type == String[].class)
				{
					String[] values = (String[] )obj;
					return SimpleStringUtil.stringArrayTOTimestampArray(values,dateformat);
				}
				else
				{
					long[] values = (long[] )obj;
					
					return SimpleStringUtil.longArrayTOTimestampArray(values,(dateformat));
				}
			}
			else
			{
				if(type == String.class)
				{
					String[] values = new String[] {(String)obj};
					return SimpleStringUtil.stringArrayTOTimestampArray(values,(dateformat));
				}
				else 
				{
					long[] values = new long[] {((Long)obj).longValue()};					
					return SimpleStringUtil.longArrayTOTimestampArray(values,(dateformat));
				}
			}
		}
		return null;
	}

	public static void getFileFromString(String value, File outfile)
			throws SQLException {
		if(value == null)
			return;
		byte[] bytes = value.getBytes();
		getFileFromBytes(bytes, outfile);

	}

	public static void getFileFromBytes(byte[] bytes, File outfile)
			throws SQLException {
		if(bytes == null)
			return;
		FileOutputStream out = null;
		java.io.ByteArrayInputStream in = null;
		try {
			out = new FileOutputStream(outfile);
			byte v[] = (byte[]) bytes;
			in = new ByteArrayInputStream(v);
			byte b[] = new byte[1024];
			int i = 0;

			while ((i = in.read(b)) > 0) {
				out.write(b, 0, i);

			}

			out.flush();
		} catch (IOException e) {
			throw new NestedSQLException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception e) {

			}
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {

			}
		}
	}

	public static void getFileFromClob(Clob value, File outfile)
			throws SQLException {
		if(value == null)
			return;
		Writer out = null;
		Reader stream = null;

		try {

			out = new FileWriter(outfile);
			Clob clob = (Clob) value;
			stream = clob.getCharacterStream();
			char[] buf = new char[1024];
			int i = 0;

			while ((i = stream.read(buf)) > 0) {
				out.write(buf, 0, i);

			}
			out.flush();
		} catch (IOException e) {
			throw new NestedSQLException(e);
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (Exception e) {

			}
			try {
				if (out != null)
					out.close();
			} catch (Exception e) {

			}
		}
	}

	public static void getFileFromBlob(Blob value, File outfile)
			throws SQLException {
		if(value == null)
			return ;
		FileOutputStream out = null;
		InputStream in = null;
		try {
			out = new FileOutputStream(outfile);
			Blob blob = (Blob) value;
			byte v[] = new byte[1024];

			in = blob.getBinaryStream();
			int i = 0;

			while ((i = in.read(v)) > 0) {
				out.write(v, 0, i);

			}
			out.flush();
		} catch (IOException e) {
			throw new NestedSQLException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in = null;

			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
		}
	}

	/**
	 * 根据参数值的类型修正先前定义的参数类型数组中对应的参数
	 * params中的类型与paramArgs对应位置相同类型的不修改，不相同的修改为paramArgs中相应的类型
	 * 
	 * @param params
	 * @param paramArgs
	 * @return
	 */
	public static Class[] synParamTypes(Class[] params, Object[] paramArgs) {
		Class[] news = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			if (paramArgs[i] != null)
				news[i] = paramArgs[i].getClass();
			else {
				news[i] = params[i];
			}
		}
		return news;

	}

	public static Constructor getConstructor(Class clazz, Class[] params_,
			Object[] paramArgs) {
		return getConstructor(clazz, params_, paramArgs, false);

	}

	/**
	 * 根据参数类型params_，获取clazz的构造函数，paramArgs为参数的值，如果synTypes为true方法会
	 * 通过参数的值对参数类型进行校正 当符合params_类型的构造函数有多个时，返回最开始匹配上的构造函数，但是当synTypes为true时，
	 * 就会返回严格符合paramArgs值类型对应的构造函数 paramArgs值的类型也会作为获取构造函数的辅助条件，
	 * 
	 * @param clazz
	 * @param params_
	 * @param paramArgs
	 * @param synTypes
	 * @return
	 */
	public static Constructor getConstructor(Class clazz, Class[] params_,
			Object[] paramArgs, boolean synTypes) {
		if (params_ == null || params_.length == 0) {
			return null;

		}
		Class[] params = null;
		if (synTypes)
			params = synParamTypes(params_, paramArgs);
		else
			params = params_;
		try {

			// Class[] params_ = this.getParamsTypes(params);
			Constructor constructor = null;
			// if (params_ != null)
			constructor = clazz.getConstructor(params);

			return constructor;
		} catch (NoSuchMethodException e) {
			Constructor[] constructors = clazz.getConstructors();
			if (constructors == null || constructors.length == 0)
				throw new CurrentlyInCreationException(
						"Inject constructor error: no construction define in the "
								+ clazz + ",请检查配置文件是否配置正确,参数个数是否正确.");
			int l = constructors.length;
			int size = params.length;
			Class[] types = null;
			Constructor fault_ = null;
			for (int i = 0; i < l; i++) {
				Constructor temp = constructors[i];
				types = temp.getParameterTypes();
				if (types != null && types.length == size) {
					if (fault_ == null)
						fault_ = temp;
					if (isSameTypes(types, params, paramArgs))
						return temp;
				}

			}
			if (fault_ != null)
				return fault_;
			throw new CurrentlyInCreationException(
					"Inject constructor error: Parameters with construction defined in the "
							+ clazz
							+ " is not matched with the config paramenters .请检查配置文件是否配置正确,参数个数是否正确.");

			// TODO Auto-generated catch block
			// throw new BeanInstanceException("Inject constructor error:"
			// +clazz.getName(),e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BeanInstanceException("Inject constructor error:"
					+ clazz.getName(), e);
		}

	}

	

	public static Object typeCast(Object value, Class requiredType,
			MethodParameter methodParam) throws NumberFormatException,
			IllegalArgumentException, NoSupportTypeCastException {
		// TODO Auto-generated method stub
		return ValueObjectUtil.typeCast(value, requiredType);
	}

	public static Object typeCast(Object oldValue, Object newValue,
			Class oldtype, Class toType, WrapperEditorInf editorInf) {
		// TODO Auto-generated method stub
		if (editorInf != null)
			return editorInf.getValueFromObject(newValue, oldValue);
		else {
			return ValueObjectUtil.typeCast(newValue, oldtype, toType);
		}
	}

	public static Object getDefaultValue(Class toType) {
		// 如果是字符串则直接返回obj.toString()
		if (toType == String.class) {
			return null;
		}
		if (toType == long.class )
			return 0l;
		if (toType == int.class )
			return 0;
		if (toType == float.class )
			return 0.0f;
		if (toType == double.class )
			return 0.0d;
		if (toType == boolean.class ) 

		{
			return false;
		}
		if (toType == short.class )
			return (short) 0;
		if (toType == char.class )
			return '0';
		
		if( toType == Long.class)
			return null;
		
		if( toType == Integer.class)
			return null;
		
		
		if( toType == Float.class)
			return null;
		
		if( toType == Short.class)
			return null;
		
		if( toType == Double.class)
			return null;
		
		if (toType == Character.class)
			return null;
		

		if(toType == Boolean.class)
			return null;

		if (toType == byte.class)
			return (byte) 0;
		
		if (toType == Byte.class)
			return new Byte((byte) 0);

		
		return null;
	}

	//	
	// @org.junit.Test
	// public void doubletoint()
	// {
	// double number = 10.0;
	// int i = (int)number;
	// System.out.println(i);
	// }
//	@org.junit.Test
//	public void floatoint() {
//		float number = 10.1f;
//		int i = ((Number) number).intValue();
//		System.out.println(i);
//	}
//
//	@org.junit.Test
//	public void numberArray() {
//		double[] number = new double[] { 10.1 };
//		numberArray(number);
//	}
//
//	public void numberArray(Object tests) {
//		System.out.println(isNumberArray(tests));
//
//	}

	/**
	 * 对象比较功能，value1 > value2 返回1，value1 < value2 返回-1，value1 == value2 返回0
	 * 比较之前首先将value2转换为value1的类型
	 * 目前只支持数字和String，日期类型的比较，复杂类型不能使用改方法进行比较
	 */
	public static int typecompare(Object value1,Object value2)
	{
		if(value1 == null && value2 != null)
			return -1;
		if(value1 != null && value2 == null)
			return 1;
		if(value1 == null && value2 == null)
			return 0;
		Class vc1 = value1.getClass();

		try
		{
			if (value1 instanceof String && value2 instanceof String)
			{
				return ((String)value1).compareTo((String)value2);
			}
			else if (value1 instanceof String )
			{
				return ((String)value1).compareTo(String.valueOf(value2));
			}		
			else if (vc1 == int.class || Integer.class.isAssignableFrom(vc1))
			{
				return intcompare(((Integer)value1).intValue(),value2);
			}
			else
			{
				if(value2 instanceof String)
				{
					if(((String)value2).equals(""))
						return -100;
				}
				if(vc1 == long.class
						|| Long.class.isAssignableFrom(vc1))
					return longCompare(((Long)value1).longValue(),value2);
				else if(vc1 == double.class
						|| Double.class.isAssignableFrom(vc1))
					return doubleCompare(((Double)value1).doubleValue(),value2);
				else if(vc1 == float.class
						|| Float.class.isAssignableFrom(vc1))
					return floatCompare(((Float)value1).floatValue(),value2);
				else if(vc1 == short.class
						|| Short.class.isAssignableFrom(vc1))
					return shortCompare(((Short)value1).shortValue(),value2);
				else if(java.util.Date.class.isAssignableFrom(vc1))
					return dateCompare((java.util.Date)value1,value2);
				else if(value1 instanceof java.util.Date && value2 instanceof java.util.Date)
					return ((java.util.Date)value1).compareTo(((java.util.Date)value2));
			}
		}
		catch(Throwable e)
		{
			log.error("compare value1=" + value1 + ",value2=" + value2 + " failed,use default String compare rules instead.",e);
			return String.valueOf(value1).compareTo(String.valueOf(value2));
		}
		
		return String.valueOf(value1).compareTo(String.valueOf(value2));
		
	}
	
	
	public static int intcompare(int value1,Object value2)
	{
		Class vc2 = value2.getClass();
		if(String.class.isAssignableFrom(vc2))
		{
			int v2 = Integer.parseInt((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Integer.class.isAssignableFrom(vc2))
		{
			int v2 = ((Integer)value2).intValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Long.class.isAssignableFrom(vc2))
		{
			long v2 = (Long)value2;
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Double.class.isAssignableFrom(vc2))
		{
			double v2 = ((Double)value2).doubleValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Float.class.isAssignableFrom(vc2))
		{
			float v2 = ((Float)value2).floatValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Short.class.isAssignableFrom(vc2))
		{
			short v2 = ((Short)value2).shortValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		
		else if(java.util.Date.class.isAssignableFrom(vc2))
		{
			long v2 = ((java.util.Date)value2).getTime();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else
		{
			int v2 = Integer.parseInt((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
			
		
		
	}
	
	public static int IntegerCompare(Integer value1,Object value2)
	{
		return intcompare(value1.intValue(),value2);
	}
	
	public static int longCompare(long value1,Object value2)
	{
		Class vc2 = value2.getClass();
		 if(String.class.isAssignableFrom(vc2))
		{
			long v2 = Long.parseLong((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		 else if(Integer.class.isAssignableFrom(vc2))
		{
			int v2 = ((Integer)value2).intValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Long.class.isAssignableFrom(vc2))
		{
			long v2 = (Long)value2;
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Double.class.isAssignableFrom(vc2))
		{
			double v2 = ((Double)value2).doubleValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Float.class.isAssignableFrom(vc2))
		{
			float v2 = ((Float)value2).floatValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Short.class.isAssignableFrom(vc2))
		{
			short v2 = ((Short)value2).shortValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		
		else if(java.util.Date.class.isAssignableFrom(vc2))
		{
			long v2 = ((java.util.Date)value2).getTime();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else
		{
			long v2 = Long.parseLong((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
	}
	
	public static int LongCompare(Long value1,Object value2)
	{
		return longCompare(value1.longValue(),value2);
	}
	
	public static int doubleCompare(double value1,Object value2)
	{
		Class vc2 = value2.getClass();
		if(String.class.isAssignableFrom(vc2))
		{
			double v2 = Double.parseDouble((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Integer.class.isAssignableFrom(vc2))
		{
			int v2 = ((Integer)value2).intValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Long.class.isAssignableFrom(vc2))
		{
			long v2 = (Long)value2;
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Double.class.isAssignableFrom(vc2))
		{
			double v2 = ((Double)value2).doubleValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Float.class.isAssignableFrom(vc2))
		{
			float v2 = ((Float)value2).floatValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Short.class.isAssignableFrom(vc2))
		{
			short v2 = ((Short)value2).shortValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		
		else if(java.util.Date.class.isAssignableFrom(vc2))
		{
			long v2 = ((java.util.Date)value2).getTime();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else
		{
			double v2 = Double.parseDouble((String)value2);
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
	}
	
	public static int DoubleCompare(Double value1,Object value2)
	{
		return doubleCompare(value1.doubleValue(),value2);
	}
	
	public static int floatCompare(float value1,Object value2)
	{
		Class vc2 = value2.getClass();
		if(String.class.isAssignableFrom(vc2))
		{
			float v2 = Float.parseFloat(String.valueOf(value2));
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Integer.class.isAssignableFrom(vc2))
		{
			int v2 = ((Integer)value2).intValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Long.class.isAssignableFrom(vc2))
		{
			long v2 = (Long)value2;
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Double.class.isAssignableFrom(vc2))
		{
			double v2 = ((Double)value2).doubleValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Float.class.isAssignableFrom(vc2))
		{
			float v2 = ((Float)value2).floatValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Short.class.isAssignableFrom(vc2))
		{
			short v2 = ((Short)value2).shortValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		
		else if(java.util.Date.class.isAssignableFrom(vc2))
		{
			long v2 = ((java.util.Date)value2).getTime();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else
		{
			float v2 = Float.parseFloat(String.valueOf(value2));
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
	}
	
	public static int FloatCompare(Float value1,Object value2)
	{
		return floatCompare(value1.floatValue(),value2);
	}
	
	public static int shortCompare(short value1,Object value2)
	{
		Class vc2 = value2.getClass();
		if(String.class.isAssignableFrom(vc2))
		{
			short v2 = Short.parseShort(String.valueOf(value2));
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Integer.class.isAssignableFrom(vc2))
		{
			int v2 = ((Integer)value2).intValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Long.class.isAssignableFrom(vc2))
		{
			long v2 = (Long)value2;
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Double.class.isAssignableFrom(vc2))
		{
			double v2 = ((Double)value2).doubleValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Float.class.isAssignableFrom(vc2))
		{
			float v2 = ((Float)value2).floatValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else if(Short.class.isAssignableFrom(vc2))
		{
			short v2 = ((Short)value2).shortValue();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		
		else if(java.util.Date.class.isAssignableFrom(vc2))
		{
			long v2 = ((java.util.Date)value2).getTime();
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
		else
		{
			short v2 = Short.parseShort(String.valueOf(value2));
			if(value1 == v2)
				return 0;
			else if(value1 > v2)
				return 1;
			else	
				return -1;
		}
	}
	
	public static int ShortCompare(Short value1,Object value2)
	{
		return shortCompare(value1.shortValue(),value2);
	}
	
	public static int dateCompare(java.util.Date value1,Object value2)
	{
		 SimpleDateFormat format = DataFormatUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Class vc2 = value2.getClass();
			if(java.util.Date.class.isAssignableFrom(vc2))
			{
				java.util.Date v2 = ((java.util.Date)value2);
				return dateCompare(value1,v2);
			}
			else if(String.class.isAssignableFrom(vc2))
			{
				java.util.Date v2 = format.parse((String)value2);
				return dateCompare(value1,v2);
			}
			else if(Long.class.isAssignableFrom(vc2))
			{
				java.util.Date v2 = new java.util.Date(((Long)value2).longValue());
				return dateCompare(value1,v2);
			}
			if(Integer.class.isAssignableFrom(vc2))
			{
				
				java.util.Date v2 = new java.util.Date(((Integer)value2).intValue());
				return dateCompare(value1,v2);
			}
			
			else if(Double.class.isAssignableFrom(vc2))
			{
				
				java.util.Date v2 = new java.util.Date(((Double)value2).longValue());
				return dateCompare(value1,v2);
			}
			else if(Float.class.isAssignableFrom(vc2))
			{
				java.util.Date v2 = new java.util.Date(((Float)value2).longValue());
				return dateCompare(value1,v2);
			}
			else if(Short.class.isAssignableFrom(vc2))
			{
				java.util.Date v2 = new java.util.Date(((Short)value2).longValue());
				return dateCompare(value1,v2);
			}
			else
			{
				java.util.Date v2 = format.parse(String.valueOf(value2));
				return dateCompare(value1,v2);
			}
				
			
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static int dateCompare(java.util.Date value1,java.util.Date value2)
	{
		return value1.compareTo(value2);
	}

	public static boolean isNumber(Object value) {

		boolean isnumber = value instanceof Number;
		// System.out.println(isnumber);
		// isnumber = number_d instanceof Number;
		// System.out.println(isnumber);
		return isnumber;

	}

	public static Class isNumberArray(Object value) {
		if (!value.getClass().isArray())
			return null;

		Class componentType = value.getClass().getComponentType();
		if(String.class.isAssignableFrom(componentType))
			return null;
		if (componentType == int.class
				|| Integer.class.isAssignableFrom(componentType)
				|| componentType == long.class
				|| Long.class.isAssignableFrom(componentType)
				|| componentType == double.class
				|| Double.class.isAssignableFrom(componentType)
				|| componentType == float.class
				|| Float.class.isAssignableFrom(componentType)
				|| componentType == short.class
				|| Short.class.isAssignableFrom(componentType))
			return componentType;
		return null;
	}
	
	public static boolean isDateArray(Object value) {
		if (!value.getClass().isArray())
			return false;

		Class componentType = value.getClass().getComponentType();
		if(java.util.Date.class.isAssignableFrom(componentType))
			return true;
		
		return false;
	}

	public static short[] toshortArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new short[] { ((Number) value).shortValue() };
			} else {
				return new short[] { Short.parseShort(value.toString()) };
			}
		}

		short[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short)values[i];
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (componentType == long.class){
			long[] values = (long[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;
		}
			
		if (Long.class.isAssignableFrom(componentType)) {
			Long[] values = (Long[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (short[]) value;
		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		return null;
	}
	
	public static Short[] toShortArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new Short[] { ((Number) value).shortValue()};
			} else {
				return new Short[] { Short.parseShort(value.toString()) };
			}
		}

		Short[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] =(short) values[i];
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (Long.class.isAssignableFrom(componentType)){
			Long[] values = (Long[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
			
		}
			
		if (long.class == componentType) {
			long[] values = (long[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] =(short) values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].shortValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (short) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (Short[]) value;
		}

		return null;
	}
	
	
	
	public static int[] toIntArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new int[] { ((Number) value).intValue() };
			} else {
				return new int[] { Integer.parseInt(value.toString()) };
			}
		}

		int[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (int[])value;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}

		if (componentType == long.class){
			long[] values = (long[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;
		}
		if (Long.class.isAssignableFrom(componentType)) {
			Long[] values = (Long[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		return null;
	}
	
	public static Integer[] toIntegerArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new Integer[] { ((Number) value).intValue() };
			} else {
				return new Integer[] { Integer.parseInt(value.toString()) };
			}
		}

		Integer[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Integer(values[i]);
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (Integer[]) value;
			
		}

		if (Long.class.isAssignableFrom(componentType)){
			Long[] values = (Long[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}
			
		if (long.class == componentType) {
			long[] values = (long[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int)values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] =  (int) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (int) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].intValue();
			}
			return ret;
		}

		return null;
	}
	
	public static long[] tolongArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new long[] { ((Number) value).longValue() };
			} else {
				return new long[] { Long.parseLong(value.toString()) };
			}
		}

		long[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}

		if (componentType == long.class)
			return (long[]) value;
		if (Long.class.isAssignableFrom(componentType)) {
			Long[] values = (Long[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		return null;
	}
	
	public static Long[] toLongArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new Long[] { ((Number) value).longValue() };
			} else {
				return new Long[] { Long.parseLong(value.toString()) };
			}
		}

		Long[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Long(values[i]);
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		if (Long.class.isAssignableFrom(componentType))
			return (Long[]) value;
		if (long.class == componentType) {
			long[] values = (long[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (long) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].longValue();
			}
			return ret;
		}

		return null;
	}
	public static float[] tofloatArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new float[] { ((Number) value).floatValue() };
			} else {
				return new float[] { Float.parseFloat(value.toString()) };
			}
		}

		float[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}

		if (componentType == long.class){
			long[] values = (long[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;
			
		}
		
		if (Long.class.isAssignableFrom(componentType)) {
			Long[] values = (Long[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (float[]) value;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		return null;
	}
	
	public static Float[] toFloatArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new Float[] { ((Number) value).floatValue() };
			} else {
				return new Float[] { Float.parseFloat(value.toString()) };
			}
		}

		Float[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Float(values[i]);
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		if (Long.class.isAssignableFrom(componentType)){
			Long[] values = (Long[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}
			
		if (long.class == componentType) {
			long[] values = (long[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float)values[i];
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (Float[]) value;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (float) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].floatValue();
			}
			return ret;
		}

		return null;
	}
	public static double[] todoubleArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new double[] { ((Number) value).doubleValue()};
			} else {
				return new double[] { Double.parseDouble(value.toString()) };
			}
		}

		double[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i];
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		if (componentType == double.class)
			return (double[]) value;
		if (Long.class.isAssignableFrom(componentType)) {
			Long[] values = (Long[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}
		if (componentType == long.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			long[] values = (long[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] =  values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Double[] values = (Double[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (double) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] =  values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		return null;
	}
	
	public static BigDecimal[] toBigDecimalArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			
				return new BigDecimal[] { converObjToBigDecimal(value)};
			
		}

		BigDecimal[] ret = null;
		int length = Array.getLength(value);
		ret = new BigDecimal[length];
		for (int i = 0; i < length; i++) {
			ret[i] = converObjToBigDecimal(Array.get(value, i));
		}
		return ret;
		

		

		
	}
	public static BigInteger[] toBigIntegerArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			
				return new BigInteger[] { converObjToBigInteger(value)};
			
		}

		BigInteger[] ret = null;
		int length = Array.getLength(value);
		ret = new BigInteger[length];
		for (int i = 0; i < length; i++) {
			ret[i] = converObjToBigInteger(Array.get(value, i));
		}
		return ret;
		

		

		
	}
	
	
	public static Double[] toDoubleArray(Object value, Class componentType) {
		if (!value.getClass().isArray()) {
			if (isNumber(value)) {
				return new Double[] { ((Number) value).doubleValue() };
			} else {
				return new Double[] { Double.parseDouble(value.toString()) };
			}
		}

		Double[] ret = null;
		if (componentType == int.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			int[] values = (int[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Double(values[i]);
			}
			return ret;

		}
		if (Integer.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Integer[] values = (Integer[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		if (Long.class.isAssignableFrom(componentType)){
			Long[] values = (Long[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
		     }
		return ret;
		}
		if (long.class == componentType) {
			long[] values = (long[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Double(values[i]);
			}
			return ret;
		}
		if (componentType == double.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			double[] values = (double[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (double) values[i];
			}
			return ret;

		}
		if (Double.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			return (Double[]) value;
		}

		if (componentType == float.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			float[] values = (float[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (double) values[i];
			}
			return ret;

		}
		if (Float.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Float[] values = (Float[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		if (componentType == short.class)
		// || Integer.class.isAssignableFrom(componentType))
		{
			short[] values = (short[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = (double) values[i];
			}
			return ret;

		}
		if (Short.class.isAssignableFrom(componentType))
		// || Integer.class.isAssignableFrom(componentType))
		{
			Short[] values = (Short[]) value;
			ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = values[i].doubleValue();
			}
			return ret;
		}

		return null;
	}
	static enum Test
	{
		A,B,C
	}
	public static void main(String[] args) throws ClassNotFoundException
	{
//		Test[][] a = new Test[][]{{Test.A,Test.B}};
//		String ttt = a.getClass().getName();
//		Class x= Class.forName(ttt);
//		System.out.println();
		System.out.println(ValueObjectUtil.typecompare(0,"0"));
		System.out.println(ValueObjectUtil.typecompare(0,0));
		Class clazz = List.class; 
//		try {
//			Test[] temp = ValueObjectUtil.convertStringsToEnumArray(new String[]{"A","B","C"}, Test.class);
//			System.out.println(temp.getClass().getComponentType());
//			System.out.println(temp[0].getClass());
//			Method f=  Calle.class.getMethod("testEnum", new Class[]{Test[].class});
//			
//			f.invoke(null, new Object[]{temp});
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(a.getClass().isArray());
//		System.out.println(a.getClass().getComponentType().isEnum());
	}
	
	static class Calle
	{
		public static void testEnum(Test... tests)
		{
			System.out.println(tests);
		}
	}
	
	public static String getTypeName(Class array)
	{
		if(array == null)
			return null;
		if(array.isArray())
		{	
			String ret = array.getComponentType().getName();
			if(ret.equals("java.lang.String"))
				return "String[]";
			else if(ret.equals("java.lang.Object"))
				return "Object[]";
			else if(ret.equals("java.lang.Class"))
				return "Class[]";
			else if(ret.equals("byte"))
				return "byte[]";	
			else
			{
				ret = array.getName();
			}
			
			return ret;
		}
		else
		{
			String ret = array.getName() ;
			if(ret.equals("java.lang.String"))
				return "String";
			else if(ret.equals("java.lang.Object"))
				return "Object";
			else if(ret.equals("java.lang.Class"))
				return "Class";
			
			return ret;
		}
			
		
	}
	
	public static String getSimpleTypeName(Class array)
	{
		if(array == null)
			return null;
		if(array.isArray())
		{	
			String ret = array.getComponentType().getName();
			if(ret.equals("java.lang.String"))
				return "String[]";
			else if(ret.equals("java.lang.Object"))
				return "Object[]";
			else if(ret.equals("java.lang.Class"))
				return "Class[]";
			else if(ret.equals("byte"))
				return "byte[]";	
		
			return array.getName();
		}
		else
		{
			String ret = array.getName() ;
			if(ret.equals("java.lang.String"))
				return "String";
			else if(ret.equals("java.util.ArrayList"))
				return "ArrayList";
			else if(ret.equals("java.util.HashMap"))
				return "HashMap";
			
			else if(ret.equals("java.lang.Class"))
				return "Class";
			
			else if(ret.equals("java.util.TreeSet"))
				return "TreeSet";
			else if(ret.equals("java.lang.Object"))
				return "Object";
			
			return ret;
		}
			
		
	}
	
	/**
	 * 获取数组元素类型名称
	 * @param array
	 * @return
	 */
	public static String getComponentTypeName(Class array)
	{
		if(array == null)
			return null;
		if(array.isArray())
		{	
			String ret = array.getComponentType().getName() ;
		
			if(ret.equals("java.lang.String"))
				return "String";
			else if(ret.equals("java.lang.Object"))
				return "Object";
			else if(ret.equals("java.lang.Class"))
				return "Class";
			else if(ret.equals("byte"))
				return "byte";	
			
			return ret;
		}
		else
		{
			String ret = array.getName() ;
			
			if(ret.equals("java.lang.String"))
				return "String";
			else if(ret.equals("java.lang.Object"))
				return "Object";
			else if(ret.equals("java.lang.Class"))
				return "Class";
			return ret;
		}
			
		
	}
	
	public static Class<?> getClass(String type) throws ClassNotFoundException {
		if(type == null)
			return null;
		else if(type.equals("String") )
		{
			return String.class;
		}
		else if (type.equals("int"))
			return int.class;
		else if (type.equals("Integer"))
			return Integer.class;
		else if (type.equals("long"))
			return long.class;
		else if (type.equals("Long"))
			return Long.class;
		else if (type.equals("boolean"))
			return boolean.class;
		else if (type.equals("double"))
			return double.class;
		else if (type.equals("float"))
			return float.class;
		else if (type.equals("ArrayList"))
			return ArrayList.class;

		else if (type.equals("HashMap"))
			return HashMap.class;
		else if (type.equals("string") ||  type.equals("java.lang.String")
				|| type.equals("java.lang.string"))
			return String.class;

			
		else if (type.equals("short"))
			return short.class;
		else if (type.equals("char"))
			return char.class;
	

		else if (type.equals("Boolean"))
			return Boolean.class;
		else if (type.equals("Double"))
			return Double.class;
		else if (type.equals("Float"))
			return Float.class;
		else if (type.equals("Short"))
			return Short.class;
		else if (type.equals("Char") || type.equals("Character")
				|| type.equals("character"))
			
			return Character.class;
		else if (type.equals("bigint") )
			
			return BigInteger.class;
		else if (type.equals("bigdecimal") )
			
			return BigDecimal.class;
		else if( type.equals("String[]"))
		{
			return String[].class;
		}
		else if (type.equals("int[]"))
			return int[].class;
		else if (type.equals("Integer[]"))
			return Integer[].class;
		else if (type.equals("byte[]"))
			return byte[].class;
		else if (type.equals("string[]")  || type.equals("java.lang.String[]"))
			return String[].class;
		else if (type.equals("boolean[]"))
			return boolean[].class;
		else if (type.equals("double[]"))
			return double[].class;
		else if (type.equals("float[]"))
			return float[].class;
		else if (type.equals("short[]"))
			return short[].class;
		else if (type.equals("char[]"))
			return char[].class;
		else if (type.equals("long[]"))
			return long[].class;
		else if (type.equals("Long[]"))
			return Long[].class;

		else if (type.equals("Boolean[]"))
			return Boolean[].class;
		else if (type.equals("Double[]"))
			return Double[].class;
		else if (type.equals("Float[]"))
			return Float[].class;
		else if (type.equals("Short[]"))
			return Short[].class;
		else if (type.equals("bigint[]") )
			
			return BigInteger[].class;
		else if (type.equals("bigdecimal[]") )
			
			return BigDecimal[].class;
		else if (type.equals("Char[]") || type.equals("Character[]")
				|| type.equals("character[]"))
			return Character[].class;
		else if (type.equals("Class") || type.equals("class"))
			return Class.class;
		else if (type.equals("Class[]") || type.equals("class[]"))
			return Class[].class;
		else if (type.equals("byte"))
			return byte.class;
		else if (type.equals("TreeSet"))
			return TreeSet.class;
		else if(type.endsWith("[]"))
		{
			int len = type.length() - 2;
			int idx = type.indexOf("[");
			String subClass = type.substring(0,idx);
			String array = type.substring(idx);
			int count = 0;
			StringBuilder builder = new StringBuilder();
			
			
			for(int i = 0; i < array.length(); i ++)
			{
				char c = array.charAt(i);
				if(c == '[')
					builder.append("[");
			}
			builder.append("L").append(subClass).append(";");
			return Class.forName(builder.toString());
			
		    
		}
		Class<?> Type = Class.forName(type);
		return Type;
	}
	
	public static String byteArrayEncoder(byte[] contents)
	{
		BASE64Encoder en = new BASE64Encoder();
		return en.encode(contents);
	}
	
	public static byte[] byteArrayDecoder(String contents) throws Exception
	{
		if(contents == null)
			return null;
		BASE64Decoder en = new BASE64Decoder();
		try {
			return en.decodeBuffer(contents);
		} catch (IOException e) {
			throw e;
		}
		
	}
	
	public static String getFileContent(String configFile)
    {
    	try {
			return getFileContent(configFile,"UTF-8");
		} catch (Exception e) {
			return null;
		}
    }
    
//    public static String getFileContent(String configFile,String charset)
//    {
//    	
//        
//        	try {
//				return getFileContent(getClassPathFile(configFile),charset);
//			} catch (Exception e) {
//				return null;
//			}
//       
//    }
    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = (java.lang.Thread.class).getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            return null;
        }
        return (ClassLoader)method.invoke(Thread.currentThread(), null);
    }
    /**
     * InputStream reader = null;
					ByteArrayOutputStream swriter = null;
					OutputStream temp = null;
					try {
						reader = ValueObjectUtil
								.getInputStreamFromFile(PoolManConstants.XML_CONFIG_FILE_TEMPLATE);

						swriter = new ByteArrayOutputStream();
						temp = new BufferedOutputStream(swriter);

						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = reader.read(buffer)) > 0) {
							temp.write(buffer, 0, len);
						}
						temp.flush();
						pooltemplates = swriter.toString("UTF-8");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (reader != null)
							try {
								reader.close();
							} catch (IOException e) {
							}
						if (swriter != null)
							try {
								swriter.close();
							} catch (IOException e) {
							}
						if (temp != null)
							try {
								temp.close();
							} catch (IOException e) {
							}
					}
     * @param file
     * @param charSet
     * @return
     * @throws IOException
     */
    public static String getFileContent(File file,String charSet) throws IOException
    {
    	ByteArrayOutputStream swriter = null;
        OutputStream temp = null;
        InputStream reader = null;
        try
        {
        	reader = new FileInputStream(file);
        	swriter = new ByteArrayOutputStream();
        	temp = new BufferedOutputStream(swriter);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0)
            {
            	temp.write(buffer, 0, len);
            }
            temp.flush();
            return swriter.toString(charSet);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
            if (temp != null)
                try
                {
                	temp.close();
                }
                catch (IOException e)
                {
                }
        }
    }
    
    public static String getFileContent(String file,String charSet) 
    {
    	ByteArrayOutputStream swriter = null;
        OutputStream temp = null;
        InputStream reader = null;
        try
        {
        	reader = getInputStreamFromFile(file);
        	swriter = new ByteArrayOutputStream();
        	temp = new BufferedOutputStream(swriter);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0)
            {
            	temp.write(buffer, 0, len);
            }
            temp.flush();
            return swriter.toString(charSet);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	return "";
		}
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
            if (temp != null)
                try
                {
                	temp.close();
                }
                catch (IOException e)
                {
                }
        }
    }
    
    public static byte[] getBytesFileContent(String file) 
    {
    	ByteArrayOutputStream swriter = null;
        OutputStream temp = null;
        InputStream reader = null;
        try
        {
        	reader = getInputStreamFromFile(file);
        	swriter = new ByteArrayOutputStream();
        	temp = new BufferedOutputStream(swriter);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0)
            {
            	temp.write(buffer, 0, len);
            }
            temp.flush();
            return swriter.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
			// TODO Auto-generated catch block
        	return null;
		}
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
            if (temp != null)
                try
                {
                	temp.close();
                }
                catch (IOException e)
                {
                }
        }
    }
    
    public static File getClassPathFile(String configFile) throws Exception
    {
    	String url = configFile;
        try {
        	
        	
	            URL confURL = ValueObjectUtil.class.getClassLoader().getResource(configFile);
	            
	            if (confURL == null)
	            
	                confURL = ValueObjectUtil.class.getClassLoader().getResource("/" + configFile);
	            else
	            {
//	            	String path = confURL.toString();
//	            	System.out.println(path);
	            }
	
	            if (confURL == null)
	                confURL = getTCL().getResource(configFile);
	            if (confURL == null)
	                confURL = getTCL().getResource("/" + configFile);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource(configFile);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource("/" + configFile);
	
	            if (confURL == null) {
	                url = System.getProperty("user.dir");
	                url += "/" + configFile;
	                File f = new File(url);
	            	if(f.exists())
	            		return f;
	            	return null;
	            } else {
	                url = confURL.getFile();
	                File f = new File(url);
	            	if(f.exists())
	            		return f;
	            	else
	            		f = new File(confURL.toString());
	            	return f;
	            }
        	
	        
        	
        	
        }
    	catch(Exception e)
    	{
    		throw e;
    	}
    }
    
    public static InputStream getInputStreamFromFile(String configFile) throws Exception
    {
    	String url = configFile;
        try {
            URL confURL = ValueObjectUtil.class.getClassLoader().getResource(configFile);
            if (confURL == null)
                confURL = ValueObjectUtil.class.getClassLoader().getResource("/" + configFile);

            if (confURL == null)
                confURL = getTCL().getResource(configFile);
            if (confURL == null)
                confURL = getTCL().getResource("/" + configFile);
            if (confURL == null)
                confURL = ClassLoader.getSystemResource(configFile);
            if (confURL == null)
                confURL = ClassLoader.getSystemResource("/" + configFile);

            if (confURL == null) {
                url = System.getProperty("user.dir");
                url += "/" + configFile;
                return new FileInputStream(new File(url));
            } else {
            	return confURL.openStream();
            }
        }
    	catch(Exception e)
    	{
    		  return new FileInputStream(configFile);
    	}
    }
    
    /**
     * 判断类type是否是基础数据类型或者基础数据类型数组
     * @param type
     * @return
     */
    public static boolean isPrimaryType(Class type)
    {
    	if(!type.isArray())
    	{
    		if(type.isEnum())
    			return true;
	    	for(Class primaryType:ValueObjectUtil.baseTypes)
	    	{
	    		if(primaryType.isAssignableFrom(type))
	    			return true;
	    	}
	    	return false;
    	}
    	else
    	{
    		return isPrimaryType(type.getComponentType());
    	}
    	
    }
    
    /**
     * 判断类type是否是基础数据类型
     * @param type
     * @return
     */
    public static boolean isBasePrimaryType(Class type)
    {
    	if(!type.isArray())
    	{
    		if(type.isEnum())
    			return true;
	    	for(Class primaryType:ValueObjectUtil.basePrimaryTypes)
	    	{
	    		if(primaryType.isAssignableFrom(type))
	    			return true;
	    	}
	    	return false;
    	}
    	return false;
    	
    }
    
    public static boolean isCollectionType(Class type)
    {
    	return Collection.class.isAssignableFrom(type);
    }



	/**
	 * @param values
	 * @param targetContainer
	 * @param objectType
	 */
	public static void typeCastCollection(String[] values,
			Collection targetContainer, Class elementType) {
		for(int i = 0 ; values != null && i < values.length;i ++)
		{
			String v = values[i];
			targetContainer.add(ValueObjectUtil.typeCast(v, elementType));
		}
		
	}
	
	/**
	 * @param values
	 * @param targetContainer
	 * @param objectType
	 */
	public static void typeCastCollection(Object values,
			Collection targetContainer, Class elementType,String dateformat) {
		if(values == null)
			return;
		if(values.getClass().isArray())
		{
			for(int i = 0 ;  i < Array.getLength(values);i ++)
			{
				Object v = Array.get(values,i);
				targetContainer.add(ValueObjectUtil.typeCast(v, elementType,dateformat));
			}
		}
		else
		{
			targetContainer.add(ValueObjectUtil.typeCast(values, elementType));
		}
		
	}
	public static Collection createCollection(Class targetContainerType)
	{
		if (List.class.isAssignableFrom(targetContainerType)) {
			List valueto = new java.util.ArrayList();
			return valueto;
		}
		else //if (Set.class.isAssignableFrom(targetContainerType)) 
		{			
			Set valueto = new java.util.TreeSet();
			return valueto;
		}
	}
	/**
	 * @param values
	 * @param targetContainer
	 * @param objectType
	 */
	public static Object typeCastCollection(Object values,
			Class targetContainerType, Class elementType,String dateformat) {
		if(values == null)
			return null;
		Collection targetContainer = createCollection( targetContainerType);
		if(values.getClass().isArray())
		{
			for(int i = 0 ;  i < Array.getLength(values);i ++)
			{
				Object v = Array.get(values,i);
				targetContainer.add(ValueObjectUtil.typeCast(v, elementType,dateformat));
			}
		}
		else
		{
			targetContainer.add(ValueObjectUtil.typeCast(values, elementType,dateformat));
		}
		return targetContainer;
		
	}

	
}
