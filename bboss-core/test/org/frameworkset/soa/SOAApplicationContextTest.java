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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.SOAApplicationContext;
import org.frameworkset.spi.SOAFileApplicationContext;
import org.junit.Test;

import com.frameworkset.util.ValueObjectUtil;


/**
 * <p>Title: SOAApplicationContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-9 下午06:12:52
 * @author biaoping.yin
 * @version 1.0
 */
public class SOAApplicationContextTest {
	@Test
	public void testXMLToBean() throws Exception
	{
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<esb>"+
				"<call>"+
				
				"<!-- 调度中心需要的数据开始 -->"+
					
					"<property name=\"soamethodcall\" " +
						"class=\"org.frameworkset.soa.SOAMethodCall\" "+
						"f:requestor=\"requestor\" "+
						"f:requestid=\"1000000\" "+
						"f:password=\"requestpassword\" "+
						"f:encypt=\"encrypt\" "+
						"f:encyptalgorithem=\"algorithm\" "+
						"f:serviceid=\"hilaryserviceid\" "+
						"f:issynchronized=\"true\" >"+
						"<!-- 调度中心需要的数据结束 -->"+
						"<!-- 调度中心提交给服务提供方的服务方法信息 -->"+
						"<property name=\"soamethodinfo\" class=\"org.frameworkset.soa.SOAMethodInfo\" " +
														"f:methodName=\"methodname\">"+
							"<property name=\"paramTypes\">"+
								"<array componentType=\"Class\">"+
									"<property >String</property>"+
									"<property >String</property>"+
									"<property >String[]</property>"+
								"</array>"+
							"</property>" +
							"<property name=\"params\">"+
								"<array componentType=\"object\">"+
									"<property name=\"condition\">1=1</property>"+
									"<property name=\"orderby\">order by ${A}</property>"+
									"<property>"+
									"	<array componentType=\"String\">"+
										"<property>A</property>"+
										"<property>B</property>"+
										"</array>"+
									"</property>"+
								"</array>"+
							"</property>" +
						"</property>" +
					"</property>"+
				"</call>"+
			"</esb>";
		SOAApplicationContext context  = new SOAApplicationContext(content);
	
		SOAMethodCall object = context.getTBeanObject("soamethodcall",SOAMethodCall.class);
		System.out.println(object);
		String xmlcontent = ObjectSerializable.convertSOAMethodCallToXMLMethod(object, ObjectSerializable.CHARSET_UTF_8);
		System.out.println(xmlcontent);
		
		context  = new SOAApplicationContext(xmlcontent);
		object = context.getTBeanObject("soamethodcall",SOAMethodCall.class);
		
		
		
	}
	
	
	
	@Test
	public void commonToxml()
	{
		SOAFileApplicationContext context = new SOAFileApplicationContext("org/frameworkset/soa/datasource-sql.xml");
		System.out.println(context);
	}
	@Test
	public void bytearraybeantoxml() throws Exception
	{
		ArrayBean bean = new ArrayBean();
		Exception e = new Exception("异常发生。");
		bean.setE(e);
		
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<esb>"+
			"<call>"+
			
			"<!-- 调度中心需要的数据开始 -->"+
				
				"<property name=\"soamethodcall\" " +
					"class=\"org.frameworkset.soa.SOAMethodCall\" "+
					"f:requestor=\"requestor\" "+
					"f:requestid=\"1000000\" "+
					"f:password=\"requestpassword\" "+
					"f:encypt=\"encrypt\" "+
					"f:encyptalgorithem=\"algorithm\" "+
					"f:serviceid=\"hilaryserviceid\" "+
					"f:issynchronized=\"true\" >"+
					"<!-- 调度中心需要的数据结束 -->"+
					"<!-- 调度中心提交给服务提供方的服务方法信息 -->"+
					"<property name=\"soamethodinfo\" class=\"org.frameworkset.soa.SOAMethodInfo\" " +
													"f:methodName=\"methodname\">"+
						"<property name=\"paramTypes\">"+
							"<array componentType=\"Class\">"+
								"<property >String</property>"+
								"<property >String</property>"+
								"<property >String[]</property>"+
							"</array>"+
						"</property>" +
						"<property name=\"params\">"+
							"<array componentType=\"object\">"+
								"<property name=\"condition\">1=1</property>"+
								"<property name=\"orderby\">order by ${A}</property>"+
								"<property>"+
								"	<array componentType=\"String\">"+
									"<property>A</property>"+
									"<property>B</property>"+
									"</array>"+
								"</property>"+
							"</array>"+
						"</property>" +
					"</property>" +
				"</property>"+
			"</call>"+
		"</esb>";
		bean.setArrays(content.getBytes() );
		String xmlcontent = ObjectSerializable.convertBeanObjectToXML("beanarray",bean, bean.getClass());
		System.out.println(xmlcontent);
		ArrayBean bean1 = ObjectSerializable.convertXMLToBeanObject("beanarray",xmlcontent,ArrayBean.class);
		System.out.println(new String(bean1.getArrays()));
		bean1.getE().printStackTrace();
		
	}
	
	@Test
	public void filebeantoxml() throws Exception
	{
		FileBean bean = new FileBean();
		
		bean.setFile(ValueObjectUtil.getClassPathFile("org/frameworkset/soa/datasource-sql.xml"));
		String xmlcontent = ObjectSerializable.convertBeanObjectToXML("beanfile",bean, bean.getClass());
		System.out.println(xmlcontent);
		FileBean bean1 = ObjectSerializable.convertXMLToBeanObject("beanfile",xmlcontent,FileBean.class);
//		System.out.println(ValueObjectUtil.getFileContent(bean1.getFile(),"UTF-8"));
	}
	@Test
	public void beanstoxml() throws Exception
	{
		FileBean fbean = new FileBean();
		
		fbean.setFile(ValueObjectUtil.getClassPathFile("org/frameworkset/soa/datasource-sql.xml"));
		ArrayBean bean = new ArrayBean();
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<esb>"+
			"<call>"+
			
			"<!-- 调度中心需要的数据开始 -->"+
				
				"<property name=\"soamethodcall\" " +
					"class=\"org.frameworkset.soa.SOAMethodCall\" "+
					"f:requestor=\"requestor\" "+
					"f:requestid=\"1000000\" "+
					"f:password=\"requestpassword\" "+
					"f:encypt=\"encrypt\" "+
					"f:encyptalgorithem=\"algorithm\" "+
					"f:serviceid=\"hilaryserviceid\" "+
					"f:issynchronized=\"true\" >"+
					"<!-- 调度中心需要的数据结束 -->"+
					"<!-- 调度中心提交给服务提供方的服务方法信息 -->"+
					"<property name=\"soamethodinfo\" class=\"org.frameworkset.soa.SOAMethodInfo\" " +
													"f:methodName=\"methodname\">"+
						"<property name=\"paramTypes\">"+
							"<array componentType=\"Class\">"+
								"<property >String</property>"+
								"<property >String</property>"+
								"<property >String[]</property>"+
							"</array>"+
						"</property>" +
						"<property name=\"params\">"+
							"<array componentType=\"object\">"+
								"<property name=\"condition\">1=1</property>"+
								"<property name=\"orderby\">order by ${A}</property>"+
								"<property>"+
								"	<array componentType=\"String\">"+
									"<property>A</property>"+
									"<property>B</property>"+
									"</array>"+
								"</property>"+
							"</array>"+
						"</property>" +
					"</property>" +
				"</property>"+
			"</call>"+
		"</esb>";
		bean.setArrays(content.getBytes() );
		List beans = new ArrayList();
		beans.add(fbean);
		beans.add(bean);
		
		String xmlcontent = ObjectSerializable.convertBeanObjectToXML("listObject",beans, List.class);
		System.out.println(xmlcontent);
		
		List copybeans = ObjectSerializable.convertXMLToBeanObject("listObject", xmlcontent, List.class);
		System.out.println(copybeans.size());
		
	}
	@org.junit.Test
	public void testMap() throws Exception
	{
		Map mapdata = new HashMap();
		mapdata.put("data", "datavalue");
		String xmlcontent = ObjectSerializable.convertBeanObjectToXML("mapObject",mapdata, Map.class);
		System.out.println(xmlcontent);
		
		Map copybeans = ObjectSerializable.convertXMLToBeanObject("mapObject", xmlcontent, Map.class);
		System.out.println(copybeans.size());
	}
		
	public static void main(String[] args) {
		System.out.println("Test:");
		double d = Double.parseDouble("2.2250738585072012e-308");
		System.out.println("Value: " + d);
	
	}
	@org.junit.Test
	public void testSerialPlugin()
	{		
		try {
			java.util.Locale locale = java.util.Locale.CHINESE;
			String xml = ObjectSerializable.toXML(locale);
			locale = ObjectSerializable.toBean(xml, java.util.Locale.class);
			System.out.println(locale);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	@org.junit.Test
	public void testMagicNumber()
	{		
		try {
			java.util.Locale locale = java.util.Locale.CHINESE;
			String xml = ObjectSerializable.toXML(locale);
			locale = ObjectSerializable.toBean(xml, java.util.Locale.class);
			System.out.println(locale);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

}
