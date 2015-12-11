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

package com.frameworkset.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.frameworkset.util.DataFormatUtil;

import bsh.Interpreter;

import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.util.SimpleStringUtil;

/**
 * @author biaoping.yin
 * 改类充分使用java.lang.reflection中提供的功能，提供以下工具：
 * 从对象中获取对应属性的值
 */
public class ValueObjectUtil implements Serializable{
	private static final Logger log = Logger.getLogger(ValueObjectUtil.class);
//	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	

	/**
	 * Description:获取对象obj的property属性值
	 * @param obj
	 * @param property
	 * @return
	 * Object
	 */
	public static Object getValue(Object obj, String property) {
		return getValue(obj, property,null);
	}

	/**
	 * Description:获取对象obj的property属性值,params为参数数组
	 * @param obj
	 * @param property
	 * @param params 获取属性方法值的参数
	 * @return
	 * Object
	 */
	public static Object getValue(Object obj, String property,Object[] params) {
		if (obj == null || property == null || property.trim().length() == 0)
			return null;
		Object ret = getValueByMethodName(obj, getMethodName(property), params);
		if(ret == null)
		{
		    //log.debug("Try to get Boolean attribute for property[" + property + "].");
		    ret = getValueByMethodName(obj, getBooleanMethodName(property), params);
		    if(ret != null)
		        log.debug("Get Boolean property[" + property + "=" + ret + "].");
		}

		return ret;
	}

	/**
	 * Description:根据方法名称获取，
	 * 在对象obj上调用改方法并且返回调用的返回值
	 * @param obj
	 * @param methodName 方法名称
	 * @param params 方法的参数
	 * @return
	 * Object
	 */
	public static Object getValueByMethodName(
		Object obj,
		String methodName,
		Object[] params) {
		if (obj == null
			|| methodName == null
			|| methodName.trim().length() == 0)
			return null;
		return getValueByMethodName(obj, methodName, params,null);
	}

	/**
	 * Description:根据方法名称获取，
	 * 在对象obj上调用改方法并且返回调用的返回值
	 * @param obj
	 * @param methodName 方法名称
	 * @param params 方法的参数
	 * @param paramsTtype 方法的参数类型
	 * @return
	 * Object
	 */
	public static Object getValueByMethodName(
		Object obj,
		String methodName,
		Object[] params,
		Class[] paramsTtype) {
		if (obj == null
			|| methodName == null
			|| methodName.trim().length() == 0)
			return null;
		try {
			Method method = obj.getClass().getMethod(methodName, paramsTtype);
			if (method != null)
				return method.invoke(obj, params);
		} catch (Exception e) {
			//e.printStackTrace();
			log.info("NoSuchMethodException:" + e.getMessage());
		}
		return null;

	}

	/**
	 * Description:实现在对象调用method并为该方法传入参数数组params
	 * @param obj 对象
	 * @param method 待调用的方法
	 * @param params 参数数组
	 * @return Object
	 * @throws Exception
	 * Object
	 */
	public static Object invoke(Object obj, Method method, Object[] params)
		throws Exception {
		return method.invoke(obj, params);
	}


	/**
	 * 获取fieldName的getter方法名称
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
		//System.out.println("method name:" + ret);
		return ret;

	}

	public static String getBooleanMethodName(String fieldName) {
		String ret = null;
		if (fieldName == null)
			return null;
		String letter = String.valueOf(fieldName.charAt(0));
		letter = letter.toUpperCase();
		ret = "is" + letter + fieldName.substring(1);
		//System.out.println("method name:" + ret);
		return ret;

	}

	/**
	 * 获取fieldName的setter方法
	 * @param fieldName
	 * @return String
	 */
	public static String getSetterMethodName(String fieldName) {
		String ret = null;
		if (fieldName == null)
			return null;
		String letter = String.valueOf(fieldName.charAt(0));
		letter = letter.toUpperCase();
		ret = "set" + letter + fieldName.substring(1);
		//System.out.println("method name:" + ret);
		return ret;

	}
	public final static boolean isSameType(Class type, Class toType)
	{
		if(toType == Object.class)
			return true;
		
		else if(type == toType) 
			return true;
		else if(toType.isAssignableFrom(type))//检查toType是不是type的父类或者是type所实现的接口
		{
			return true;
		}
		
		else if((type == int.class && toType == Integer.class) ||
				type == Integer.class && toType == int.class)
		{
			return true;
		}
		else if((type == short.class && toType == Short.class) ||
				type == Short.class && toType == short.class)
		{
			return true;
		}
		else if((type == long.class && toType == Long.class) ||
				type == Long.class && toType == long.class)
		{
			return true;
		}
		else if((type == double.class && toType == Double.class) ||
				type == Double.class && toType == double.class)
		{
			return true;
		}
		else if((type == float.class && toType == Float.class) ||
				type == Float.class && toType == float.class)
		{
			return true;
		}
		else if((type == char.class && toType == Character.class) ||
				type == Character.class && toType == char.class)
		{
			return true;
		}
		return false;
		
		
	}
	/**
	 * 将obj对象从类型type转换到类型toType
	 * 支持字符串向其他基本类行转换:
	 * 支持的类型:
	 * int,char,short,double,float,long,boolean,byte
	 * java.sql.Date,java.util.Date,
	 * Integer
	 * Long
	 * Float
	 * Short
	 * Double
	 * Character
	 * Boolean
	 * Byte
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws ClassCastException,NumberFormatException,IllegalArgumentException
	 */
	public final static Object typeCast(Object obj, Class type, Class toType)
		throws
			NoSupportTypeCastException,
			NumberFormatException,
			IllegalArgumentException {
		if(obj == null)
			return null;
		if(isSameType(type,toType))
			return obj;
		
		if(type.isAssignableFrom(toType)) //type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
//			return shell(toType,obj);
			if(!java.util.Date.class.isAssignableFrom(type))
				return toType.cast(obj);
		}
		
		
		if(type == byte[].class && toType == String.class)
		{
			return new String((byte[])obj);
		}
		else if(type == String.class && toType == byte[].class)
		{
			return ((String)obj).getBytes();
		}
		else if(type == byte[].class && File.class.isAssignableFrom(toType))
		{
			Object[] object = (Object[])obj;
			
			java.io.ByteArrayInputStream byteIn = null ;
			java.io.FileOutputStream fileOut = null;
			try {
				byteIn = new java.io.ByteArrayInputStream((byte[])object[0]) ;
				fileOut  = new java.io.FileOutputStream((File)object[1]);
				byte v[] = new byte[1024];

				
				int i = 0;
				
				while ((i = byteIn.read(v)) > 0) {
					fileOut.write(v,0,i);
				
				}
				fileOut.flush();
				return object[1];
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(byteIn != null)
						byteIn.close();
				}
				catch(Exception e)
				{
					
				}
				try
				{
					if(fileOut != null)
						fileOut.close();
				}
				catch(Exception e)
				{
					
				}
			}
		}
		
		else if(File.class.isAssignableFrom(toType) && toType == byte[].class)
		{
			java.io.FileInputStream in = null;
			java.io.ByteArrayOutputStream out = null;
			try
			{				
				int i = 0;
				in = new FileInputStream((File)obj);
				out = new ByteArrayOutputStream();
				byte v[] = new byte[1024];
				while((i = in.read(v)) > 0)
				{
					out.write(v, 0, i);
				}
				return out.toByteArray();
			}
			catch(Exception e)
			{
				
			}
			finally
			{
				try
				{
					if(in != null)
						in.close();
				}
				catch(Exception e)
				{
					
				}
				try
				{
					if(out != null)
						out.close();
				}
				catch(Exception e)
				{
					
				}
			}
			
			
		}
		else if(type.isArray() && !toType.isArray()
			|| !type.isArray() && toType.isArray())
		{
//		if (type.getName().startsWith("[")
//			&& !toType.getName().startsWith("[")
//			|| !type.getName().startsWith("[")
//			&& toType.getName().startsWith("["))
			throw new IllegalArgumentException(
				new StringBuffer("类型无法转换,不支持[")
					.append(type.getName())
					.append("]向[")
					.append(toType.getName())
					.append("]转换")
					.toString());
		}
		Object arrayObj;

		/**
		 * 基本类型转换
		 */
		if (!type.isArray()) {
			arrayObj = basicTypeCast(obj, type, toType);
		}

		/**
		 * 字符串数组向其他类型数组之间转换
		 */
		else {

			arrayObj = arrayTypeCast(obj, type, toType);
		}
		return arrayObj;
	}
	
	public static Object shell(Class toType,Object obj)
	{
		Interpreter interpreter = new Interpreter();
		String shell = toType.getName() + " ret = (" + toType.getName() + ")obj;return ret;";
		try
		{
			interpreter.set("obj", obj);
			Object ret = interpreter.eval(shell);
			return ret;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Description:基本的数据类型转圜
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws NoSupportTypeCastException
	 * @throws NumberFormatException
	 *
	 */
	public final static Object basicTypeCast(
		Object obj,
		Class type,
		Class toType)
		throws NoSupportTypeCastException, NumberFormatException {
		SimpleDateFormat format = DataFormatUtil.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(obj == null)
			return null;
		if (isSameType(type,toType ))
			return obj;
		
		if(type.isAssignableFrom(toType)) //type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		{
			if(!java.util.Date.class.isAssignableFrom(type))
				return shell(toType,obj);
		}
		/**
		 * 如果obj的类型不是String型时直接抛异常,
		 * 不支持非字符串和字符串数组的类型转换
		 */
//		if (type != String.class)
//			throw new NoSupportTypeCastException(
//				new StringBuffer("不支持[")
//					.append(type)
//					.append("]向[")
//					.append(toType)
//					.append("]的转换")
//					.toString());
		/*******************************************
		 * 字符串向基本类型及其包装器转换		   *
		 * 如果obj不是相应得数据格式,将抛出		   *
		 * NumberFormatException				   *
		 ******************************************/
		if (toType == long.class || toType == Long.class)
			return new Long(obj.toString());
		if (toType == int.class || toType == Integer.class)
			return new Integer(obj.toString());
		if (toType == float.class || toType == Float.class)
			return new Float(obj.toString());
		if (toType == short.class || toType == Short.class)
			return new Short(obj.toString());
		if (toType == double.class || toType == Double.class)
			return new Double(obj.toString());
		if (toType == char.class || toType == Character.class)
			return new Character(obj.toString().charAt(0));

		if (toType == boolean.class || toType == Boolean.class)
			return new Boolean(obj.toString());
		if (toType == byte.class || toType == Byte.class)
			return new Byte(obj.toString());
		

		//如果是字符串则直接返回obj.toString()
		if (toType == String.class)
		{
            if (obj instanceof java.util.Date)
                return format.format(obj);
            return obj.toString();
        }
		/**
		 * 字符串向java.util.Date和java.sql.Date 类型转换
		 */
		if (toType == java.util.Date.class)
		{			
//			if(obj instanceof java.sql.Date
//					|| obj instanceof java.sql.Time
//					|| obj instanceof java.sql.Timestamp)
			if(java.util.Date.class.isAssignableFrom(obj.getClass()))
				return obj;
			try {
				return format.parse(obj.toString());
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			return new java.util.Date(obj.toString());
		}

		if (toType == java.sql.Date.class)
		{

//			if(obj instanceof java.sql.Date
//					|| obj instanceof java.sql.Time
//					|| obj instanceof java.sql.Timestamp)
			if(java.util.Date.class.isAssignableFrom(obj.getClass()))
				return new java.sql.Date(((java.util.Date)obj).getTime());
			
			java.sql.Date date = java.sql.Date.valueOf(obj.toString());//(new java.util.Date(obj.toString()).getTime());

			return date;
		}

		throw new NoSupportTypeCastException(
			new StringBuffer("不支持[")
				.append(type)
				.append("]向[")
				.append(toType)
				.append("]的转换")
				.toString());

	}
	/**
	 * 数组类型转换
	 * 支持字符串数组向一下类型数组得自动转换:
	 *	int[]
	 *	Integer[]
	 *	long[]
	 *	Long[]
	 *	short[]
	 *	Short[]
	 *	double[]
	 *	Double[]
	 *	boolean[]
	 *	Boolean[]
	 *	char[]
	 *	Character[]
	 *	float[]
	 *	Float[]
	 *	byte[]
	 *	Byte[]
	 *	java.sql.Date[]
	 *	java.util.Date[]
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * @throws NoSupportTypeCastException
	 * @throws NumberFormatException
	 */
	public final static Object arrayTypeCast(
		Object obj,
		Class type,
		Class toType)
		throws NoSupportTypeCastException, NumberFormatException {
		if (isSameType(type,toType ))
			return obj;
//		if (type != String[].class)
//			throw new NoSupportTypeCastException(
//				new StringBuffer("不支持[")
//					.append(type)
//					.append("]向[")
//					.append(toType)
//					.append("]的转换")
//					.toString());

		if (toType == long[].class) {
			String[] values = (String[]) obj;
			long[] ret = new long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = Long.parseLong(values[i]);
			}
			return ret;
		}
		if (toType == Long[].class) {
			String[] values = (String[]) obj;
			Long[] ret = new Long[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Long(values[i]);
			}
			return ret;
		}

		if (toType == int[].class) {
			String[] values = (String[]) obj;
			int[] ret = new int[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = Integer.parseInt(values[i]);
			}
			return ret;
		}
		if (toType == Integer[].class) {
			String[] values = (String[]) obj;
			Integer[] ret = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Integer(values[i]);
			}
			return ret;
		}
		if (toType == float[].class) {
			String[] values = (String[]) obj;
			float[] ret = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = Float.parseFloat(values[i]);
			}
			return ret;
		}
		if (toType == Float[].class) {
			String[] values = (String[]) obj;
			Float[] ret = new Float[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Float(values[i]);
			}
			return ret;
		}
		if (toType == short[].class) {
			String[] values = (String[]) obj;
			short[] ret = new short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = Short.parseShort(values[i]);
			}
			return ret;
		}
		if (toType == Short[].class) {
			String[] values = (String[]) obj;
			Short[] ret = new Short[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Short(values[i]);
			}
			return ret;
		}
		if (toType == double[].class) {
			String[] values = (String[]) obj;
			double[] ret = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = Double.parseDouble(values[i]);
			}
			return ret;
		}
		if (toType == Double[].class) {
			String[] values = (String[]) obj;
			Double[] ret = new Double[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Double(values[i]);
			}
			return ret;
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
				ret[i] = new Boolean(values[i]).booleanValue();
			}
			return ret;
		}
		if (toType == Boolean[].class) {
			String[] values = (String[]) obj;
			Boolean[] ret = new Boolean[values.length];
			for (int i = 0; i < values.length; i++) {
				ret[i] = new Boolean(values[i]);
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

		//如果是字符串则直接返回obj.toString()
		if (toType == String[].class) {
		{
			if(obj.getClass() == java.util.Date[].class)
				return SimpleStringUtil.dateArrayTOStringArray((Date[])obj);
			String[] values = (String[]) obj;
            return values;
        }
			//			short[] ret = new short[values.length];
			//			for(int i = 0; i < values.length; i ++)
			//			{
			//				ret[i] = Short.parseShort(values[i]);
			//			}
			//			return ret;
		}
		/**
		 * 字符串向java.util.Date和java.sql.Date 类型转换
		 */
		if (toType == java.util.Date.class) {
			String[] values = (String[]) obj;
			return SimpleStringUtil.stringArrayTODateArray(values,null);
		}
		if (toType == java.sql.Date.class) {
			String[] values = (String[]) obj;
			return SimpleStringUtil.stringArrayTOSQLDateArray(values,null);
		}

		throw new NoSupportTypeCastException(
			new StringBuffer("不支持[")
				.append(type)
				.append("]向[")
				.append(toType)
				.append("]的转换")
				.toString());

	}
	
	public static void getFileFromString(String value, File outfile) throws SQLException {
		byte[] bytes = value.getBytes();
		getFileFromBytes(bytes,outfile) ;
		
	}
	public static void getFileFromBytes(byte[] bytes,File outfile) throws SQLException
	{
		FileOutputStream out = null;
		java.io.ByteArrayInputStream in = null;
		try {
			out = new FileOutputStream(outfile);
			byte v[] = (byte[]) bytes;
			in = new ByteArrayInputStream(v);
			byte b[] = new byte[1024];
			int i = 0;
			
			while((i = in.read(b)) > 0)
			{
				out.write(b,0,i);
				
			}

			
			out.flush();
		} catch (IOException e) {
			throw new NestedSQLException(e);
		} finally {
			try
			{
				if (out != null) {
					out.close();
					out = null;
				}
			}
			catch(Exception e)
			{
				
			}
			try
			{
				if (in != null) {
					in.close();
					in = null;
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
	public static void getFileFromClob(Clob value,File outfile) throws SQLException
	{
		Writer out = null;
		Reader stream = null;
		
		try {
			
			out = new FileWriter(outfile);
			Clob clob = (Clob)value;
			stream = clob.getCharacterStream();
			char[] buf = new char[1024];
			int i = 0;
			
			while((i = stream.read(buf)) > 0)
			{
				out.write(buf,0,i);
				
			}
			out.flush();
		}
		catch(IOException e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try
			{
				if(stream != null)
					stream.close();
			}
			catch(Exception e)
			{
				
			}
			try
			{
				if(out != null)
				out.close();
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	public static void getFileFromBlob(Blob value,File outfile) throws SQLException
	{
		FileOutputStream out = null;
		InputStream in = null;
		try {
			out = new FileOutputStream(outfile);
			Blob blob = (Blob) value;
			byte v[] = new byte[1024];

			in = blob.getBinaryStream();
			int i = 0;
			
			while ((i = in.read(v)) > 0) {
				out.write(v,0,i);
			
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
}
