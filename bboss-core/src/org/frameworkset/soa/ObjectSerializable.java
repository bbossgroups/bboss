/*
 *  Copyright 2008-2010 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.soa;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.frameworkset.soa.SerialFactory.MagicClass;
import org.frameworkset.spi.SOAApplicationContext;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;

import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.spi.assemble.CurrentlyInCreationException;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>
 * Title: ObjectSerializable.java
 * </p>
 * <p>
 * Description: 解决对象方法调用转换为xml串的功能
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2011-5-10 上午11:14:21
 * @author biaoping.yin
 * @version 1.0
 */
public class ObjectSerializable {
	public static final String content_header_gbk = "<?xml version=\"1.0\" encoding=\"gbk\"?>";
	public static final String content_header_utf_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String call_header = "<esb><call>";
	public static final String call_tailer = "</call></esb>";

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_GBK = "GBK";

	public static final String[] throwable_filterattributes = new String[] {
			"message", "cause" };

	/**
	 * 标识null值类型
	 */
	public static final String NULL_TYPE = "s:nvl";

	/**
	 * @deprecated use method toObject(xml,Class)
	 * @return
	 */
	public static <T> T convertXMLToBeanObject(String name, String beanxml,
			Class<T> beantype) {
		SOAApplicationContext context = new SOAApplicationContext(beanxml);
		T object = context.getTBeanObject(name, beantype);
		context.destroy();
		return object;

	}

	private static void convertMethodCallToXMLMethod(Writer ret,
			String methodName, Object[] params, Class[] paramTypes,
			String charset) throws Exception {
		SerialStack stack = new SerialStack();
		ret
				.append("<p n=\"soamethodcall\" ")
				.append("cs=\"org.frameworkset.soa.SOAMethodCall\" >")
				.append(
						"<p n=\"soamethodinfo\" cs=\"org.frameworkset.soa.SOAMethodInfo\" ")
				.append("f:methodName=\"").append(methodName).append("\">");
		if (paramTypes == null || paramTypes.length == 0) {
			ret.append("<p n=\"params\" s:nvl=\"true\" s:t=\"").append(
					ValueObjectUtil.getSimpleTypeName(Object[].class)).append(
					"\"/>");
		} else {

			ret.append("<p n=\"params\">").append("<a cmt=\"").append(
					ValueObjectUtil.getComponentTypeName(Object[].class))
					.append("\">");

			{
				
				stack.addStack(params, "params");
				ObjectSerializable.convertParams(ret, params, paramTypes, null,stack,"params");
				
			}
			ret.append("</a></p>");
		}
		stack.clear();
		ret.append("</p></p>");

		// SOAMethodCall method = new SOAMethodCall();
		// return convertSOAMethodCallToXMLMethod(method);
	}

	public static String convertMethodCallToXMLMethod(Method method,
			Object[] params, Class[] paramTypes, String charset)
			throws Exception {
		Writer ret = null;
		try {
			ret = new BBossStringWriter();
//			if (charset.equals(CHARSET_UTF_8)) {
//				ret.append(content_header_utf_8);
//			} else
//				ret.append(content_header_gbk);
			ret.append(call_header);
			convertMethodCallToXMLMethod(ret, method.getName(), params, paramTypes,
					charset);
			ret.append(call_tailer);
//			ret.flush();
//			return out.toString(charset);
			return ret.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}		
		finally
		{
			
			if(ret != null)
				 
					ret = null;
				 
		}

	}

	public static String convertSOAMethodCallToXMLMethod(SOAMethodCall method,
			String charset) throws Exception,
			IntrospectionException {
		Writer ret = null;
		try {
			ret = new BBossStringWriter();
//			if (charset.equals(CHARSET_UTF_8)) {
//				ret.append(content_header_utf_8);
//			} else
//				ret.append(content_header_gbk);
			ret.append(call_header);
			SerialStack stack = new SerialStack();
			convertBeanObjectToXML("soamethodcall", method, method.getClass(),false,
					null, ret,stack,"soamethodcall",false);
			stack.clear();
			ret.append(call_tailer);
			return ret.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		finally
		{
			
			if(ret != null)
				
					
					ret = null;
				
		}
	}

	public final static String convertBeanObjectToXML(Object obj, Class type,
			String dateformat) throws Exception,
			IntrospectionException {
		Writer ret = null;
		try {
			ret = new BBossStringWriter();
			SerialStack stack = new SerialStack();
			String name = UUID.randomUUID().toString();
			convertBeanObjectToXML(name, obj, type, ValueObjectUtil.isBasePrimaryType(type),dateformat, ret,stack,name,false);
			stack.clear();
			
			return ret.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		
		
		
		finally
		{
			
			if(ret != null)
				 
					ret = null;
				 
		}
	}

	/**
	 * @deprecated use Method toXML( Object obj).
	 */
	public final static String convertBeanObjectToXML(String name, Object obj,
			Class type) throws Exception {
		return convertBeanObjectToXML(name, obj, type, (String) null,
				CHARSET_UTF_8);
	}

	public final static String toXML(Object obj) throws Exception {
		if (obj == null)
			return null;
		return convertBeanObjectToXML("_dflt_", obj, obj.getClass(),
				(String) null, CHARSET_UTF_8);
	}
	
	public final static String toXML(Object obj,String charset) throws Exception {
		if (obj == null)
			return null;
		return convertBeanObjectToXML("_dflt_", obj, obj.getClass(),
				(String) null, charset);
	}

	public final static void toXML(Object obj, Writer out)
			throws Exception {
		if (obj == null)
			return ;
		convertBeanObjectToXML("_dflt_", obj,
				obj.getClass(), null, CHARSET_UTF_8,out);
//		return convertBeanObjectToXML("_dflt_", obj, obj.getClass(),
//				(String) null, CHARSET_UTF_8);
		
	}
	public static <T> T toBean(String beanxml, Class<T> beantype,String charset)
	{
		SOAApplicationContext context = new SOAApplicationContext(beanxml,charset,false);
		context.setSerial(true);
		context.init();
		T object = context.getTBeanObject("_dflt_", beantype);
		context.destroy();
		return object;
	}
	public static <T> T toBean(String beanxml, Class<T> beantype) {
		if (beanxml == null || beanxml.equals(""))
			return null;
		SOAApplicationContext context = new SOAApplicationContext(beanxml,false);
		context.setSerial(true);
		context.init();
		T object = context.getTBeanObject("_dflt_", beantype);
		context.destroy();
		return object;

	}

	public static <T> T toBean(InputStream instream, Class<T> beantype) {
		if (instream == null)
			return null;
		SOAApplicationContext context = new SOAApplicationContext(instream,false);
		context.setSerial(true);
		context.init();
		T object = context.getTBeanObject("_dflt_", beantype);
		context.destroy();
		return object;

	}

//	public final static String convertBeanObjectToXML(String name, Object obj,
//			Class type, String dateformat, String charset)
//			throws NumberFormatException, IllegalArgumentException,
//			IntrospectionException {
//		StringBuffer ret = new StringBuffer();
//
//		if (charset.equals(CHARSET_UTF_8)) {
//			ret.append(content_header_utf_8);
//		} else
//			ret.append(content_header_utf_8);
//		ret.append("<ps>");
//		convertBeanObjectToXML(name, obj, type, dateformat, ret);
//		ret.append("</ps>");
//		return ret.toString();
//	}
	
	
	public final static String convertBeanObjectToXML1(String name, Object obj,
			Class type, String dateformat, String charset)
			throws Exception {
		Writer ret = null;
		try
		{
			ret = new BBossStringWriter();
			convertBeanObjectToXML(name, obj,
					type, dateformat, charset,ret);
//			return new String(out.toByteArray(),charset);
			return ret.toString();
		}
		finally
		{
			
			if(ret != null)
				 
					ret = null;
				 
		}
	}
	
	public final static String convertBeanObjectToXML(String name, Object obj,
			Class type, String dateformat, String charset)
			throws Exception {
//		java.io.ByteArrayOutputStream out = null;
		Writer ret = null;
		try
		{
//			out = new java.io.ByteArrayOutputStream();
//			ret = new OutputStreamWriter(out,Charset.forName(charset));
			ret = new BBossStringWriter();
			convertBeanObjectToXML(name, obj,
					type, dateformat, charset,ret);

			//			return new String(out.toByteArray(),charset);
//			return out.toString(charset);
			return ret.toString();
		}
		finally
		{
//			if(out != null)
//				try {
//					out.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			if(ret != null)
				 
					ret = null;
				 
		}
	}
	
	
	public final static void convertBeanObjectToXML(String name, Object obj,
			Class ptype, String dateformat, String charset,Writer ret)
			throws Exception {
//		StringBuffer ret = new StringBuffer();

		try {
//			if (charset.equals(CHARSET_UTF_8)) {
//				ret.append(content_header_utf_8);
//			} else
//				ret.append(content_header_gbk);
			ret.append("<ps>");
			SerialStack stack = new SerialStack();
			convertBeanObjectToXML(name, obj, ptype,ValueObjectUtil.isBasePrimaryType(ptype), dateformat, ret,stack,name,false);
			stack.clear();
			ret.append("</ps>");
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
//		return ret.toString();
	}

	public final static String convertBeanObjectToXML(Object obj, Class type)
			throws Exception {
		Writer ret = new BBossStringWriter();
		try {
			SerialStack stack = new SerialStack();
			String name = UUID.randomUUID().toString();
			convertBeanObjectToXML(name, obj, type,ValueObjectUtil.isBasePrimaryType(type), null, ret,stack,name,false);
			stack.clear();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return ret.toString();
//		
//		java.io.ByteArrayOutputStream out = null;
//		OutputStreamWriter ret = null;
//		
//
//		try
//		{
//			out = new java.io.ByteArrayOutputStream();
//			ret = new OutputStreamWriter(out,Charset.forName(CHARSET_UTF_8));
//			SerialStack stack = new SerialStack();
//			String name = UUID.randomUUID().toString();
//			convertBeanObjectToXML(name, obj, type, null, ret,stack,name);
//			stack.clear();
//			ret.flush();
//			return out.toString(CHARSET_UTF_8);
//		}
//		finally
//		{
//			if(out != null)
//				try {
//					out.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			if(ret != null)
//				try {
//					ret.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
	}

	public final static String convertBeanObjectsToXML(List<String> names,
			List<Object> objs, List<Class> types) throws Exception {

		
			return convertBeanObjectsToXML(names, objs, types, CHARSET_UTF_8);
		
	}

	public final static String convertBeanObjectsToXML(List<String> names,
			List<Object> objs, List<Class> types, String charset)
			throws Exception {
		Writer ret = null;
		try {
			ret = new BBossStringWriter();
//			if (charset.equals(CHARSET_UTF_8)) {
//				ret.append(content_header_utf_8);
//			} else
//				ret.append(content_header_gbk);
			ret.append("<ps>");
			if (objs != null && objs.size() > 0) {
				int i = 0;
				SerialStack stack = new SerialStack();
				for (Object obj : objs) {
					convertBeanObjectToXML(names.get(i), obj, types.get(i), ValueObjectUtil.isBasePrimaryType(types.get(i)),null,
							ret,stack,names.get(i),false);
					i++;
				}
			}
			ret.append("</ps>");
			
			return ret.toString();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		
		finally
		{
			if(ret != null)
			 
					ret = null;
				 
			
		}
	}
	private static void arraytoxml(Writer ret,Object obj,String dateformat,String name,Class vtype,SerialStack serialStack,String currentAddress) throws Exception
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getTypeName(vtype)).append(
						"\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getTypeName(vtype)).append(
						"\"/>");
			return;
		} else {
//			if (name == null)
//				ret.append("<p s:t=\"").append(
//						ValueObjectUtil.getSimpleTypeName(type)).append(
//						"\">");
//			else
//			{
//				ret.append("<p n=\"").append(name).append("\" s:t=\"")
//						.append(ValueObjectUtil.getSimpleTypeName(type))
//						.append("\">");
//				currentAddress = currentAddress + "->" + name;
//			}
			
			if (name == null)
				ret.append("<p s:t=\"").append(
						ValueObjectUtil.getTypeName(vtype)).append(
						"\">");
			else
			{
				ret.append("<p n=\"").append(name).append("\" s:t=\"")
						.append(ValueObjectUtil.getTypeName(vtype))
						.append("\">");
//				currentAddress = currentAddress + "->" + name;
			}

		}
		ret.append("<a cmt=\"").append(
				ValueObjectUtil.getComponentTypeName(vtype)).append("\">");
		Object value = null;
		int len = Array.getLength(obj);

		for (int i = 0; i < len; i++) {
			value = Array.get(obj, i);

			if (value == null)
				convertBeanObjectToXML(null, value, (Class)null, false,dateformat, ret,serialStack,currentAddress + "[" + i + "]",false);
			else
				convertBeanObjectToXML(null, value, value.getClass(), ValueObjectUtil.isBasePrimaryType(value.getClass()),
						dateformat, ret,serialStack,currentAddress + "[" + i + "]",false);
		}
		ret.append("</a>");
		ret.append("</p>");
	}
	private static void maptoxml(Writer ret,Object obj,String dateformat,String name,Class vtype,SerialStack serialStack,String currentAddress,ClassInfo classInfo) throws Exception
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");

		}
		else 
		{
			obj = serialContainerObject( ret,
					 obj,
					 name,
					 vtype,
					 classInfo,false) ;
			if(obj != null)
			{
				Map datas = (Map) obj;
	
	//			if (name == null)
	//				ret.append("<p s:t=\"").append(
	//						ValueObjectUtil.getSimpleTypeName(vtype)).append(
	//						"\">");
	//			else
	//			{
	//				ret.append("<p n=\"").append(name).append("\" s:t=\"")
	//						.append(ValueObjectUtil.getSimpleTypeName(vtype))
	//						.append("\">");
	////				currentAddress = currentAddress + "->" + name;
	//			}
				ret.append("<m cmt=\"bean\">");
				Object value = null;
				Iterator itr = datas.entrySet().iterator();
				while (itr.hasNext())
	
				{
					Map.Entry entry = (Map.Entry) itr.next();
					value = entry.getValue();
					if (value == null)
						convertBeanObjectToXML(String.valueOf(entry.getKey()),
								value, (Class)null,false, dateformat, ret,serialStack,currentAddress + "[" + entry.getKey() + "]",false);
					else
						convertBeanObjectToXML(String.valueOf(entry.getKey()),
								value, value.getClass(), ValueObjectUtil.isBasePrimaryType(value.getClass()),dateformat, ret,serialStack,currentAddress + "[" + entry.getKey() + "]",false);
				}
				ret.append("</m>");
				ret.append("</p>");
			}
		}
	}
	private static void settoxml(Writer ret,Object obj,String dateformat,String name,Class vtype,SerialStack serialStack,String currentAddress,ClassInfo classInfo) throws Exception
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");

		} 
		else 
		{
			obj = serialContainerObject( ret,
					 obj,
					 name,
					 vtype,
					 classInfo,false) ;
			if(obj != null)
			{
				Set datas = (Set) obj;

//				if (name == null)
//					ret.append("<p s:t=\"").append(
//							ValueObjectUtil.getSimpleTypeName(vtype)).append(
//							"\">");
//				else
//				{
//					ret.append("<p n=\"").append(name).append("\" s:t=\"")
//							.append(ValueObjectUtil.getSimpleTypeName(vtype))
//							.append("\">");
//	//				currentAddress = currentAddress + "->" + name;
//				}
				ret.append("<s cmt=\"bean\">");
				Object value = null;
				Iterator itr = datas.iterator();
				int i = 0;
				while (itr.hasNext())
	
				{
					value = itr.next();
					if (value == null)
						convertBeanObjectToXML(null, value, (Class)null,false, dateformat,
								ret,serialStack,currentAddress + "[" + i + "]",false);
					else
						convertBeanObjectToXML(null, value, value.getClass(),ValueObjectUtil.isBasePrimaryType(value.getClass()),
								dateformat, ret,serialStack,currentAddress + "[" + i + "]",false);
					i ++;
				}
				ret.append("</s>");
				ret.append("</p>");
			}
		}
	}
	
	private static void pluginserial(Object obj,MagicClass magicclass,String handleObjectClass,String name,Writer ret) throws IOException
	{
		String object = magicclass.getSerailObject().serialize(obj);
		if (name == null)
		{
			if(handleObjectClass != null)
			{
				ret.append("<p cs=\"")
						.append(handleObjectClass)
						.append("\" mg=\"")
						.append(magicclass.getMagicnumber())
						.append("\">");
			}
			else
			{
				ret.append("<p mg=\"")
				.append(magicclass.getMagicnumber())
				.append("\">");
			}
		}
					
		
		else
		{
			if(handleObjectClass != null)
			{
				ret.append("<p cs=\"")
						.append(handleObjectClass)
						.append("\" n=\"").append(name).append("\" mg=\"").append(magicclass.getMagicnumber()).append("\">");
			}
			else
			{
				ret.append("<p n=\"").append(name).append("\" mg=\"").append(magicclass.getMagicnumber()).append("\">");
			}
		}
		ret.append("<![CDATA[")
		.append(object)
		.append("]]></p>");
	}
	private static void appendmghead(String name,MagicClass magicclass,String handleObjectClass,Writer ret,String className) throws IOException
	{
		if (name == null)
		{
			if(handleObjectClass != null)
			{
				ret.append("<p cs=\"")
						.append(handleObjectClass)
						.append("\" mg=\"")
						.append(magicclass.getMagicnumber())
						.append("\">");
			}
			else
			{
				ret.append("<p mg=\"")
				.append(magicclass.getMagicnumber())
				.append("\">");
			}
		}
		else
		{
			if(handleObjectClass != null)
			{
				ret.append("<p cs=\"")
				.append(handleObjectClass)
				.append("\" n=\"").append(name).append("\" mg=\"").append(
				magicclass.getMagicnumber()).append("\">");
			}
			else
			{
				ret.append("<p n=\"").append(name).append("\" mg=\"").append(
				magicclass.getMagicnumber()).append("\">");
			}
			
		}
	}
	
	private static Object serialContainerObject(Writer ret,
			Object obj,
			String name,
			Class vtype,
			ClassInfo classInfo,boolean isobject) throws IOException
	{
		String className = classInfo.getName();
		MagicClass magicclass = SerialFactory.getSerialFactory().getMagicClass(className);
		String handleObjectClass = null;
		if(magicclass != null)
		{
			
			if(magicclass.getPreserialObject() != null)
			{
				obj = magicclass.getPreserialObject().prehandle(obj);
				handleObjectClass = obj.getClass().getName();
				if(handleObjectClass.equals(className))
					handleObjectClass = null;
					
			}
			if(magicclass.getSerailObject() != null)//指定了序列化插件				
			{
				
				pluginserial( obj, magicclass, handleObjectClass, name, ret);
				return null;
			}
			
			else//根据魔法数字构建list头
			{
				appendmghead( name, magicclass, handleObjectClass, ret, className);
				
			}
		}
		else
		{
			String typename = null;
			if(isobject)
				typename = "cs";
			else
				typename = "s:t";
			if (name == null)
			{
				ret.append("<p ").append(typename).append(
						"=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\">");
				
			}
			else
			{
				ret.append("<p n=\"").append(name).append("\" ").append(typename).append(
						"=\"")
						.append(ValueObjectUtil.getSimpleTypeName(vtype))
						.append("\">");
//				currentAddress = currentAddress + "->" + name;
			}
		}
		return obj;
	}
	private static void listtoxml(Writer ret,Object obj,String dateformat,String name,Class vtype,SerialStack serialStack,String currentAddress,ClassInfo classInfo) throws Exception
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");
			return;

		} 
		else 
		{
			obj = serialContainerObject( ret,
					 obj,
					 name,
					 vtype,
					 classInfo,false) ;//如果已经序列化完毕，则obj为null
			if(obj != null)
			{			
			
				List datas = (List) obj;	
				ret.append("<l cmt=\"bean\">");
				Object value = null;
				for (int i = 0; i < datas.size(); i++) {
					value = datas.get(i);
					if (value == null)
						/**
						 * convertBeanObjectToXML(String name, Object obj,
			Class type, String dateformat, Writer ret,SerialStack serialStack,String currentAddress)
						 */
						convertBeanObjectToXML(null, value, (Class)null,false, dateformat,
								ret,serialStack,currentAddress + "[" + i + "]",false);
					else
						convertBeanObjectToXML(null, value, value.getClass(),ValueObjectUtil.isBasePrimaryType(value.getClass()),
								dateformat, ret,serialStack,currentAddress + "[" + i + "]",false);
				}
				ret.append("</l>");
				ret.append("</p>");
			}
			
		}
	}
	private static void stringtoxml(Writer ret,Object obj,String name,Class vtype) throws IOException
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"String\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"String\"/>");

		} else {
			if (name == null)
			{
//				if(!obj.equals(""))
				{
					ret.append("<p s:t=\"String\"><![CDATA[").append((String)obj)
							.append("]]></p>");
				}
//				else
//				{
//					ret.append("<p s:t=\"String\" v=\"\"/>");
//				}
			}
			else
			{
//				if(!obj.equals(""))
				{
					ret.append("<p n=\"").append(name).append(
						"\" s:t=\"String\"><![CDATA[").append((String)obj).append(
						"]]></p>");
				}
//				else
//				{
//					ret.append("<p n=\"").append(name).append(
//							"\" s:t=\"String\" v=\"\"/>");
//				}
			}
		}
	}
	private static void filetoxml(Writer ret,Object obj,String name,Class vtype) throws IOException
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"File\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"File\"/>");

			return;
		} else {
			File object = (File) obj;

			java.io.FileInputStream byteIn = null;
			java.io.ByteArrayOutputStream fileOut = null;
			try {
				byteIn = new java.io.FileInputStream(object);
				fileOut = new java.io.ByteArrayOutputStream();
				byte v[] = new byte[1024];

				int i = 0;

				while ((i = byteIn.read(v)) > 0) {
					fileOut.write(v, 0, i);

				}
				fileOut.flush();
				if (name == null)
					ret.append("<p s:t=\"File\"><![CDATA[").append(
							ValueObjectUtil.byteArrayEncoder(fileOut
									.toByteArray())).append("]]></p>");
				else
					ret.append("<p n=\"").append(name).append(
							"\" s:t=\"File\"><![CDATA[").append(
							ValueObjectUtil.byteArrayEncoder(fileOut
									.toByteArray())).append("]]></p>");
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
	}
	private static void bytearraytoxml(Writer ret,Object obj,String name,Class vtype) throws IOException
	{
		if (obj == null) {
			if (name == null)
				ret.append("<p s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:nvl=\"true\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\"/>");

			return;
		} else {
			
			if (!File.class.isAssignableFrom(vtype)) {
				
				if (name == null)
				{
					ret
							.append("<p s:t=\"")
							.append(ValueObjectUtil.getSimpleTypeName(vtype))
							.append("\"><![CDATA[")
							.append(
									ValueObjectUtil
											.byteArrayEncoder((byte[]) obj))
							.append("]]></p>");
				}
				else
					ret
							.append("<p n=\"")
							.append(name)
							.append("\" s:t=\"")
							.append(ValueObjectUtil.getSimpleTypeName(vtype))
							.append("\"><![CDATA[")
							.append(
									ValueObjectUtil
											.byteArrayEncoder((byte[]) obj))
							.append("]]></p>");
			} else {

				File object = (File) obj;

				java.io.FileInputStream byteIn = null;
				java.io.ByteArrayOutputStream fileOut = null;
				try {
					byteIn = new java.io.FileInputStream(object);
					fileOut = new java.io.ByteArrayOutputStream();
					byte v[] = new byte[1024];

					int i = 0;

					while ((i = byteIn.read(v)) > 0) {
						fileOut.write(v, 0, i);

					}
					fileOut.flush();
					if (name == null)
						ret.append("<p s:t=\"").append(
								ValueObjectUtil.getSimpleTypeName(vtype))
								.append("\"><![CDATA[").append(
										ValueObjectUtil
												.byteArrayEncoder(fileOut
														.toByteArray()))
								.append("]]></p>");
					else
						ret.append("<p n=\"").append(name).append(
								"\" s:t=\"").append(
								ValueObjectUtil.getSimpleTypeName(vtype))
								.append("\"><![CDATA[").append(
										ValueObjectUtil
												.byteArrayEncoder(fileOut
														.toByteArray()))
								.append("]]></p>");
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
		}
	}
	/**
	 * 根据对象值，对象类型查找到对应的方法，这个玩意儿，比较麻烦
	 * 需要判读currentAddress为空的情况，biaoping.yin
	 * @param obj
	 * @param type
	 * @param dateformat
	 * @param ret
	 * @throws Exception 
	 */
	private final static void convertBeanObjectToXML(String name, Object obj,
			Class ptype,boolean pisbasetype, String dateformat, Writer ret,SerialStack serialStack,String currentAddress,boolean frombeanpropety)
			throws Exception {
		ClassInfo classinfo = null;
		Class vtype = null;
		if (obj != null)
		{
			
			
			if(!pisbasetype)
			{
				vtype = obj.getClass();
				classinfo = ClassUtil.getClassInfo(vtype);
				String address = serialStack.getRefID(obj);
				if(address != null)
				{
					if (name == null)
					{
						ret
								.append("<p refid=\"attr:").append(address)
								.append("\"/>");
					}
					else
					{
						ret
								.append("<p n=\"")
								.append(name)
								.append("\" refid=\"attr:").append(address)
								.append("\"/>");
					}
					return;
				}
				else
				{
					serialStack.addStack(obj, currentAddress);
				}
			}
			else
				vtype = ptype;
			
		}
		else
		{
			if(!frombeanpropety)
				vtype = ptype;
			else
				return;
		}
		
		if (vtype == byte[].class) {
			bytearraytoxml( ret, obj, name, vtype);
			return;
		}

		else if (vtype != null && File.class.isAssignableFrom(vtype)) {
			filetoxml(ret, obj, name, vtype);

			return;
		} else if (vtype == String.class) {
			stringtoxml( ret, obj, name, vtype);
			return;
		} else if (vtype != null && List.class.isAssignableFrom(vtype)) {
			listtoxml( ret, obj, dateformat, name, vtype, serialStack, currentAddress,classinfo);
			return;
		}

		else if (vtype != null && Set.class.isAssignableFrom(vtype)) {
			settoxml( ret, obj, dateformat, name, vtype, serialStack, currentAddress,classinfo);
			

		} else if (vtype != null && Map.class.isAssignableFrom(vtype)) {
			maptoxml( ret, obj, dateformat, name, vtype, serialStack, currentAddress,classinfo);
			

		} else if (vtype != null && vtype.isArray()) {
			arraytoxml( ret, obj, dateformat, name, vtype, serialStack, currentAddress);
			
		}

		else {
			basicTypeCast(name, obj, vtype, classinfo, dateformat, ret, serialStack,currentAddress);
		}

		
	}

	/**
	 * Description:基本的数据类型转圜
	 * 
	 * @param obj
	 * @param type
	 * @param toType
	 * @return Object
	 * 
	 * @throws Exception 
	 * 
	 */
	private final static boolean basicTypeCast(String name, Object obj,
			Class ptype, ClassInfo classInfo,String dateformat, Writer ret,SerialStack stack,String currentAddress)
			throws Exception {
		if (obj == null) {
			if (name == null)
			{
				if(ptype != null)
					ret.append("<p s:nvl=\"true\" s:t=\"").append(
							ValueObjectUtil.getSimpleTypeName(ptype)).append("\"/>");
				else
					ret.append("<p s:nvl=\"true\"/>");
			}
			else
			{
				if(ptype != null)
				{
					ret.append("<p n=\"").append(name).append(
							"\" s:nvl=\"true\" s:t=\"").append(
							ValueObjectUtil.getSimpleTypeName(ptype)).append("\"/>");
				}
				else
				{
					ret.append("<p n=\"").append(name).append(
							"\" s:nvl=\"true\"/>");
				}
			}

			return true;
		}

		Class vtype = obj.getClass();
		//		
		// if (type.isAssignableFrom(toType)) //
		// type是toType的父类，父类向子类转换的过程，这个转换过程是不安全的
		// {
		// if (!java.util.Date.class.isAssignableFrom(type))
		// return shell(toType, obj);
		// }
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
		// 如果是字符串则直接返回obj.toString()
		if (ptype == String.class) {
			if (name == null)
				ret.append("<p s:t=\"String\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"String\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		} 
		else if (ptype == long.class ) {
			// if (ValueObjectUtil.isNumber(obj))
			// return ((Number) obj).longValue();
			// else if (java.util.Date.class.isAssignableFrom(type)) {
			// return ((java.util.Date) obj).getTime();
			// }
			// return Long.parseLong(obj.toString());
			if (name == null)
				ret.append("<p s:t=\"long\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"long\" v=\"").append(obj.toString()).append("\"/>");

			return true;
		}
		else if (ptype == Long.class) {
			// if (ValueObjectUtil.isNumber(obj))
			// return ((Number) obj).longValue();
			// else if (java.util.Date.class.isAssignableFrom(type)) {
			// return ((java.util.Date) obj).getTime();
			// }
			// return Long.parseLong(obj.toString());
			if (name == null)
				ret.append("<p s:t=\"Long\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Long\" v=\"").append(obj.toString()).append("\"/>");

			return true;
		}
		else if (ptype == int.class ) {
			if (name == null)
				ret.append("<p s:t=\"int\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name)
						.append("\" s:t=\"int\" v=\"").append(obj.toString()).append(
								"\"/>");

			return true;
		}
		else if (ptype == Integer.class) {
			if (name == null)
				ret.append("<p s:t=\"Integer\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name)
						.append("\" s:t=\"Integer\" v=\"").append(obj.toString()).append(
								"\"/>");

			return true;
		}
		else if (ptype == float.class) {
			if (name == null)
				ret.append("<p s:t=\"float\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"float\" v=\"").append(obj.toString()).append("\"/>");

			return true;
		}
		else if (ptype == Float.class) {
			if (name == null)
				ret.append("<p s:t=\"Float\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Float\" v=\"").append(obj.toString()).append("\"/>");

			return true;
		}
		else if (ptype == short.class) {
			if (name == null)
				ret.append("<p s:t=\"short\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"short\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == Short.class) {
			if (name == null)
				ret.append("<p s:t=\"Short\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Short\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == double.class) {
			if (name == null)
				ret.append("<p s:t=\"double\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"double\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == Double.class) {
			if (name == null)
				ret.append("<p s:t=\"Double\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Double\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == char.class ) {
			if (name == null)
				ret.append("<p s:t=\"char\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"char\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if ( ptype == Character.class) {
			if (name == null)
				ret.append("<p s:t=\"Character\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Character\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == boolean.class) {
			if (name == null)
				ret.append("<p s:t=\"boolean\" v=\"").append(obj.toString())
						.append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"boolean\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if (ptype == Boolean.class) {
			if (name == null)
				ret.append("<p s:t=\"Boolean\" v=\"").append(obj.toString())
						.append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Boolean\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}

		else if (ptype == byte.class) {
			if (name == null)
				ret.append("<p s:t=\"byte\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"byte\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}
		else if ( ptype == Byte.class) {
			if (name == null)
				ret.append("<p s:t=\"Byte\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Byte\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}

		else if (java.util.Date.class.isAssignableFrom(vtype)) {
			// String value = ValueObjectUtil.getDateFormat(dateformat).format(
			// (java.util.Date) obj);
			long value = ((java.util.Date) obj).getTime();
			if (name == null)
				ret.append("<p s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\" v=\"").append(String.valueOf(value)).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append("\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\" v=\"").append(String.valueOf(value)).append("\"/>");
			return true;
		}
		 else if (vtype == BigInteger.class) {
				if (name == null)
					ret.append("<p s:t=\"bigint\" v=\"").append(
							(obj.toString())).append("\"/>");
				else
					ret.append("<p n=\"").append(name).append(
							"\" s:t=\"bigint\" v=\"").append(obj.toString()).append("\"/>");
				return true;
			}
		 else if (vtype == BigDecimal.class) {
				if (name == null)
					ret.append("<p s:t=\"bigdecimal\" v=\"").append(
							(obj.toString())).append("\"/>");
				else
					ret.append("<p n=\"").append(name).append(
							"\" s:t=\"bigdecimal\" v=\"").append(obj.toString()).append("\"/>");
				return true;
			}
		else if (ptype == Class.class) {
			if (name == null)
				ret.append("<p s:t=\"Class\" v=\"").append(
						((Class) obj).getName()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append(
						"\" s:t=\"Class\" v=\"").append(
						((Class) obj).getName()).append("\"/>");
			return true;
		} else if (vtype.isEnum()) {
			if (name == null)
				ret.append("<p s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\" v=\"").append(obj.toString()).append("\"/>");
			else
				ret.append("<p n=\"").append(name).append("\" s:t=\"").append(
						ValueObjectUtil.getSimpleTypeName(vtype)).append(
						"\" v=\"").append(obj.toString()).append("\"/>");
			return true;
		}

		else // 对象转换及对象状态转换
		{
			
			if (StackTraceElement.class.isAssignableFrom(vtype))
			{
				obj = serialContainerObject(ret, obj, name, vtype, classInfo,true);
				if(obj != null)
				{
					appendStackTraceElementProperties(obj, vtype, dateformat, ret, stack,currentAddress);
				}
				else
				{
					return true;
				}
//				if (name == null)
//					ret.append("<p cs=\"")
//							.append(obj.getClass().getName())
//							.append("\">");
//				else
//					ret.append("<p n=\"").append(name).append("\" cs=\"").append(
//							obj.getClass().getName()).append("\">");
//				appendStackTraceElementProperties(obj, vtype, dateformat, ret, stack,currentAddress);
			}
			else if (Throwable.class.isAssignableFrom(vtype))
			{
				obj = serialContainerObject(ret, obj, name, vtype, classInfo,true);
				if(obj != null)
				{
					appendThrowableProperties(obj, vtype, dateformat, ret,stack,currentAddress);
				}
				else
				{
					return true;
				}
//				if (name == null)
//					ret.append("<p cs=\"")
//							.append(obj.getClass().getName())
//							.append("\">");
//				else
//					ret.append("<p n=\"").append(name).append("\" cs=\"").append(
//							obj.getClass().getName()).append("\">");
//				appendThrowableProperties(obj, vtype, dateformat, ret,stack,currentAddress);
			}
			else
			{
//				ClassInfo classInfo = ClassUtil.getClassInfo(obj.getClass());
				obj = serialContainerObject(ret, obj, name, vtype, classInfo,true);
				if(obj != null)
				{
					appendBeanProperties(obj, vtype,classInfo, dateformat, ret,stack,currentAddress);					
				}
				else
				{
					return true;
				}
				
				
				
				
			}
			ret.append("</p>");
			return true;
		}

	}

	private static void appendThrowableProperties(Object obj, Class type,
			String dateformat, Writer ret,SerialStack stack,String currentAddress) throws Exception {

//		BeanInfo beanInfo = Introspector.getBeanInfo(type);
//		ClassInfo beanInfo = ClassUtil.getClassInfo(type);
//
//		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();

		ret.append("<construction>");

		try {

			Object value = ValueObjectUtil.getValue(obj, "message");

			convertBeanObjectToXML("message", value, String.class, true,dateformat,
					ret,stack,currentAddress + "{0}"  ,false);
			value = ValueObjectUtil.getValue(obj, "cause");
			if (value != null) {
				convertBeanObjectToXML("cause", value, value.getClass(),false,
						dateformat, ret,stack,currentAddress + "{1}" ,false);
			} else {
				convertBeanObjectToXML("cause", value, Throwable.class,false,
						dateformat, ret,stack,currentAddress + "{2}",false );
			}

		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (BeanInstanceException e) {
			throw e;
		}

		catch (Exception e) {
			// e.printStackTrace();
			throw new CurrentlyInCreationException("", e);
		}

		ret.append("</construction>");

		appendBeanProperties(obj, type,ClassUtil.getClassInfo(type), dateformat, ret,
				throwable_filterattributes,stack,currentAddress);

	}

	private static void appendStackTraceElementProperties(Object obj,
			Class type, String dateformat, Writer ret,SerialStack serialStack,String currentAddress)
			throws Exception {

//		BeanInfo beanInfo = Introspector.getBeanInfo(type);
//
//		PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();

		ret.append("<construction>");

		try {

			Object value = ValueObjectUtil.getValue(obj, "className");

			convertBeanObjectToXML("declaringClass", value, String.class,true,
					dateformat, ret,serialStack, currentAddress + "{0}",false);
			value = ValueObjectUtil.getValue(obj, "methodName");

			convertBeanObjectToXML("methodName", value, String.class,true,
					dateformat, ret,serialStack, currentAddress + "{1}",false);
			value = ValueObjectUtil.getValue(obj, "fileName");

			convertBeanObjectToXML("fileName", value, String.class, true,dateformat,
					ret,serialStack, currentAddress + "{2}",false);
			value = ValueObjectUtil.getValue(obj, "lineNumber");

			convertBeanObjectToXML("lineNumber", value, int.class, true,dateformat,
					ret,serialStack, currentAddress + "{3}",false);
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (BeanInstanceException e) {
			throw e;
		}

		catch (Exception e) {
			// e.printStackTrace();
			throw new CurrentlyInCreationException("", e);
		}

		ret.append("</construction>");

	}

	private static void appendBeanProperties(Object obj, Class type,ClassInfo beanInfo,
			String dateformat, Writer ret,SerialStack stack,String currentAddress) throws Exception {

		appendBeanProperties(obj, type, beanInfo, dateformat, ret, null, stack,currentAddress);

	}

	private static boolean isexclusive(String name, String[] filters) {
		if (filters == null || filters.length == 0)
			return false;
		for (String filter : filters) {
			if (name.equals(filter))
				return true;
		}
		return false;
	}

	private static void appendBeanProperties(Object obj, Class type1,ClassInfo beanInfo,
			String dateformat, Writer ret, String[] filters,SerialStack stack,String currentAddress)
			throws Exception {
//		ClassInfo beanInfo = ClassUtil.getClassInfo(type);		
//		beanInfo_.getDeclaredFields();
//		beanInfo_.getPropertyDescriptor("");
		
//		BeanInfo beanInfo = Introspector.getBeanInfo(type);
//		PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		for (int n = 0; attributes != null && n < attributes.size(); n++) {

			// get bean attribute name
			PropertieDescription propertyDescriptor = attributes.get(n);
			String attrName = propertyDescriptor.getName();
//			if (attrName.equals("class"))
//				continue;
//			else 
			if (isexclusive(attrName, filters)) {
				continue;
			}
			Class ptype = propertyDescriptor.getPropertyType();

			// create attribute value of correct type
//			Method readmethod = propertyDescriptor.getReadMethod();
//			Method writermethod = propertyDescriptor.getWriteMethod();
//			if ((readmethod == null || writermethod == null) && propertyDescriptor.getField() == null )
			if(!propertyDescriptor.canseriable())
				continue;
 
			try {

				Object value = propertyDescriptor.getValue(obj);
				boolean pisbasetype = ValueObjectUtil.isBasePrimaryType(ptype);
				if (!pisbasetype && value != null && ValueObjectUtil.isBasePrimaryType(value.getClass()))
				{
					pisbasetype = true;
					ptype = value.getClass();
				}
				
				convertBeanObjectToXML(attrName, value, ptype, pisbasetype,dateformat, ret,stack,currentAddress + "->" + attrName,true);
			} catch (IllegalArgumentException e) {
				throw new CurrentlyInCreationException("", e);
			} catch (IllegalAccessException e) {
				throw new CurrentlyInCreationException("", e);
			} catch (InvocationTargetException e) {
				throw new CurrentlyInCreationException("", e);
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (BeanInstanceException e) {
				throw e;
			}

			catch (Exception e) {
				// e.printStackTrace();
				throw new CurrentlyInCreationException("", e);
			}

		}

	}

	private static void convertParams(Writer ret, Object[] params,
			Class[] paramTypes, String dateformat,SerialStack stack,String currentAddress)
			throws Exception {
		
		for (int i = 0; i < params.length; i++) {
			 
			ObjectSerializable.convertBeanObjectToXML(null, params[i],
					paramTypes[i], ValueObjectUtil.isBasePrimaryType(paramTypes[i]),dateformat, ret, stack,currentAddress + "[" + i+"]",false);
		}
	}

}
