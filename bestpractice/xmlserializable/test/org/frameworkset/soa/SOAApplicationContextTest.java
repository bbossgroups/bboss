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
import java.util.ArrayList;
import java.util.List;

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
		System.out.println(bean1.getE());
		
	}
	
	@Test
	public void filebeantoxml() throws Exception
	{
		FileBean bean = new FileBean();
		
		bean.setFile(ValueObjectUtil.getClassPathFile("org/frameworkset/soa/datasource-sql.xml"));
		String xmlcontent = ObjectSerializable.convertBeanObjectToXML("beanfile",bean, bean.getClass());
		System.out.println(xmlcontent);
		FileBean bean1 = ObjectSerializable.convertXMLToBeanObject("beanfile",xmlcontent,FileBean.class);
		System.out.println(ValueObjectUtil.getFileContent(bean1.getFile(),"UTF-8"));
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
	
	

}
