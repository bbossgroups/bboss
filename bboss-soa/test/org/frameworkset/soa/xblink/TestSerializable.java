/**
 *  Copyright 2008 biaoping.yin
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
package org.frameworkset.soa.xblink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.frameworkset.soa.ObjectSerializable;
import org.junit.Test;

import com.frameworkset.util.FileUtil;
import com.thoughtworks.xstream.XStream;


/**
 * <p>TestSerializable.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public class TestSerializable
{
	private static Logger log = Logger.getLogger(TestSerializable.class);
	 private static XStream xStream = new XStream(); 
	private void convertBeanToXStreamXml(int count,Person joe)
	{
		try
		{
			long start = System.currentTimeMillis();
			for(int i = 0; i < count; i++)
			{
				xStream.toXML(joe);
				
			}
			long end = System.currentTimeMillis();
			
			System.out.println("执行xtream beantoxml "+count+"次，耗时:"+(end - start)+"毫秒");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void convertXStreamXMLToBean(int count,String xml)
	{
		try
		{
			long start = System.currentTimeMillis();
			for(int i = 0; i < count; i++)
			{
				xStream.fromXML(xml);
			}
			long end = System.currentTimeMillis();
			
			System.out.println("执行xStream xmltobean "+count+"次，耗时:"+(end - start)+"毫秒");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void convertBeanToXml(int count,Person joe)
	{
		try
		{
			long start = System.currentTimeMillis();
			for(int i = 0; i < count; i++)
			{
				ObjectSerializable.toXML(joe);
			}
			long end = System.currentTimeMillis();
			
			System.out.println("执行bboss beantoxml "+count+"次，耗时:"+(end - start)+"毫秒");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void convertXMLToBean(int count,String xml)
	{
		try
		{
			long start = System.currentTimeMillis();
			for(int i = 0; i < count; i++)
			{
				ObjectSerializable.toBean( xml, Person.class);
			}
			long end = System.currentTimeMillis();
			
			System.out.println("执行bboss xmltobean "+count+"次，耗时:"+(end - start)+"毫秒");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 */
	@Test
	public void test()
	{
		PhoneNumber phone = new PhoneNumber();
		phone.setCode(123);
		phone.setNumber("1234-456");

		PhoneNumber fax = new PhoneNumber();
		fax.setCode(123);
		fax.setNumber("<aaaa>9999-999</bbbb>");

	   Set dataSet = new TreeSet();
	   dataSet.add("aa");
	   dataSet.add("bb");
	   
	   List dataList = new ArrayList();
	   dataList.add("aa");
	   dataList.add("bb");
	   
	   Map dataMap = new HashMap();
	   dataMap.put("aa","aavalue");
	   dataMap.put("bb","bbvalue");
	   
	   String[] dataArray = new String[]{"aa","bb"};
	   String[][] datadoubleArray = new String[][]{{"aaa","bbb"},{"cccc","dddd"}};
		
		Person joe = new Person();
		joe.setFirstname("Joe");
		joe.setDataDoubleArray(datadoubleArray);
//		joe.setLastname("Walnes");
		//用来验证bboss和Xstream是否会按照null值传递，也就是说lastname有默认值"ssss"
		//这样我们手动把lastname设置为null，理论上来说反序列化后joe中的lastname应该是null而不是默认值"ssss"
		joe.setBirthdate(new Date());
		Date[] updates = new Date[]{new Date(),new Date()};
		joe.setUpdatedate(updates);
		joe.setLastname(null);
		joe.setPhone(phone);
		joe.setFax(fax);
		joe.setDataArray(dataArray);
		joe.setDataList(dataList);
		joe.setDataMap(dataMap);
		joe.setDataSet(dataSet);
		EnumData sex = EnumData.M;
		joe.setSex(sex);
		
		
		
		try
		{
			//预热bboss和xstream
			String xml = ObjectSerializable.toXML(joe);
			
			
			System.out.println(xml);
			Person person = ObjectSerializable.toBean(xml, Person.class);
			
			String xmlXstream = xStream.toXML(joe);
			Person p = (Person)xStream.fromXML(xmlXstream);
			System.out.println(xmlXstream);
			
			System.out.println();System.out.println("bboss序列化测试用例开始");System.out.println();
			
			long start = System.currentTimeMillis();			
			ObjectSerializable.toXML(joe);			
			long end = System.currentTimeMillis();			
			System.out.println("执行bboss beantoxml 1次，耗时:"+(end - start) +"毫秒");
			
			
			convertBeanToXml(10,joe);
			
			
			convertBeanToXml(100,joe);
				
			
			convertBeanToXml(1000,joe);			
			
			
			convertBeanToXml(10000,joe);
			System.out.println();System.out.println("xstream序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();			
				xStream.toXML(joe);			
				 end = System.currentTimeMillis();			
				System.out.println("执行XStream beantoxml 1次，耗时:"+(end - start) +"毫秒");
				
			convertBeanToXStreamXml(10,joe);
			convertBeanToXStreamXml(100,joe);
			convertBeanToXStreamXml(1000,joe);
			convertBeanToXStreamXml(10000,joe);
			
			System.out.println();System.out.println("bboss反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			person =  ObjectSerializable.toBean(xml, Person.class);
			end = System.currentTimeMillis();			
			System.out.println("执行bboss xmltobean 1次，耗时:"+(end - start)+"豪秒");			
			convertXMLToBean(10,xml);			
			convertXMLToBean(100,xml);
			convertXMLToBean(1000,xml);
			convertXMLToBean(10000,xml);
			
			System.out.println();System.out.println("xstream反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			xStream.fromXML(xmlXstream);
			end = System.currentTimeMillis();			
			System.out.println("执行XStream xmltobean 1次，耗时:"+(end - start)+"豪秒");
			convertXStreamXMLToBean(10,xmlXstream);
			convertXStreamXMLToBean(100,xmlXstream);
			convertXStreamXMLToBean(1000,xmlXstream);
			convertXStreamXMLToBean(10000,xmlXstream);
			
			//测试用例结束
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test1()
	{
		PhoneNumber phone = new PhoneNumber();
		phone.setCode(123);
		phone.setNumber("1234-456");

		PhoneNumber fax = new PhoneNumber();
		fax.setCode(123);
		fax.setNumber("<aaaa>9999-999中文乱码</bbbb>");

	   Set dataSet = new TreeSet();
	   dataSet.add("aa");
	   dataSet.add("bb");
	   
	   List dataList = new ArrayList();
	   dataList.add("aa");
	   dataList.add("bb");
	   
	   dataList.add(1);
	   
	   Map dataMap = new HashMap();
	   dataMap.put("aa","aavalue");
	   dataMap.put("bb","bbvalue");
	   
	   String[] dataArray = new String[]{"aa","bb"};
	   String[][] datadoubleArray = new String[][]{{"aaa","bbb"},{"cccc","dddd"}};
		
		Person joe = new Person();
		joe.setFirstname("Joe");
		joe.setDataDoubleArray(datadoubleArray);
//		joe.setLastname("Walnes");
		//用来验证bboss和Xstream是否会按照null值传递，也就是说lastname有默认值"ssss"
		//这样我们手动把lastname设置为null，理论上来说反序列化后joe中的lastname应该是null而不是默认值"ssss"
		joe.setBirthdate(new Date());
		Date[] updates = new Date[]{new Date(),new Date()};
		joe.setUpdatedate(updates);
		joe.setLastname(null);
		joe.setPhone(phone);
		joe.setFax(fax);
		joe.setDataArray(dataArray);
		joe.setDataList(dataList);
		joe.setDataMap(dataMap);
		joe.setDataSet(dataSet);
		EnumData sex = EnumData.M;
		joe.setSex(sex);
		
		
		
		try
		{
			//预热bboss和xstream
			String xml = ObjectSerializable.toXML(joe);
			
			
			System.out.println(xml);
			Person person = ObjectSerializable.toBean(xml, Person.class);
			System.out.println();
			
			
			//测试用例结束
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testBigData() throws IOException
	{
		//这个文件中内容有47565 字节，约47k的数据
		String bigcontent = FileUtil.getFileContent(new File("D:\\workspace\\bbossgroups-3.2\\bboss-soa\\test\\org\\frameworkset\\soa\\testxstream.xml"), "GBK");
		PhoneNumber phone = new PhoneNumber();
		phone.setCode(123);
		phone.setNumber("1234-456");

		PhoneNumber fax = new PhoneNumber();
		fax.setCode(123);
		fax.setNumber(bigcontent);
		
	   Set dataSet = new TreeSet();
	   dataSet.add("aa");
	   dataSet.add("bb");
	   
	   List dataList = new ArrayList();
	   dataList.add("aa");
	   dataList.add("bb");
	   
	   Map dataMap = new HashMap();
	   dataMap.put("aa","aavalue");
	   dataMap.put("bb","bbvalue");
	   
	   String[] dataArray = new String[]{"aa","bb"};
	   
		
		Person joe = new Person();
		joe.setFirstname("Joe");
//		joe.setLastname("Walnes");
		//用来验证bboss和Xstream是否会按照null值传递，也就是说lastname有默认值"ssss"
		//这样我们手动把lastname设置为null，理论上来说反序列化后joe中的lastname应该是null而不是默认值"ssss"
		
		joe.setLastname(null);
		joe.setPhone(phone);
		joe.setFax(fax);
		joe.setDataArray(dataArray);
		joe.setDataList(dataList);
		joe.setDataMap(dataMap);
		joe.setDataSet(dataSet);
		
		
		
		try
		{
			//预热bboss和xstream
			String xml = ObjectSerializable.toXML(joe);
			
			
			System.out.println(xml);
			Person person = ObjectSerializable.toBean(xml, Person.class);
			
			String xmlXstream = xStream.toXML(joe);
			Person p = (Person)xStream.fromXML(xmlXstream);
			System.out.println(xmlXstream);
			
			System.out.println();System.out.println("bboss序列化测试用例开始");System.out.println();
			
			long start = System.currentTimeMillis();			
			ObjectSerializable.toXML(joe);			
			long end = System.currentTimeMillis();			
			System.out.println("执行bboss beantoxml 1次，耗时:"+(end - start) +"毫秒");
			
			
			convertBeanToXml(10,joe);
			
			
			convertBeanToXml(100,joe);
				
			
			convertBeanToXml(1000,joe);			
			
			
			convertBeanToXml(10000,joe);
			System.out.println();System.out.println("xstream序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();			
				xStream.toXML(joe);			
				 end = System.currentTimeMillis();			
				System.out.println("执行XStream beantoxml 1次，耗时:"+(end - start) +"毫秒");
				
			convertBeanToXStreamXml(10,joe);
			convertBeanToXStreamXml(100,joe);
			convertBeanToXStreamXml(1000,joe);
			convertBeanToXStreamXml(10000,joe);
			
			System.out.println();System.out.println("bboss反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			person =  ObjectSerializable.toBean( xml, Person.class);
			end = System.currentTimeMillis();			
			System.out.println("执行bboss xmltobean 1次，耗时:"+(end - start)+"豪秒");			
			convertXMLToBean(10,xml);			
			convertXMLToBean(100,xml);
			convertXMLToBean(1000,xml);
			convertXMLToBean(10000,xml);
			
			System.out.println();System.out.println("xstream反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			xStream.fromXML(xmlXstream);
			end = System.currentTimeMillis();			
			System.out.println("执行XStream xmltobean 1次，耗时:"+(end - start)+"豪秒");
			convertXStreamXMLToBean(10,xmlXstream);
			convertXStreamXMLToBean(100,xmlXstream);
			convertXStreamXMLToBean(1000,xmlXstream);
			convertXStreamXMLToBean(10000,xmlXstream);
			
			//测试用例结束
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @throws IOException 
	 * 
	 */
	@Test
	public void testFileData() throws IOException
	{
		//这个文件中内容有47565 字节，约47k的数据
		File fileData = new File("D:\\workspace\\bbossgroups-3.2\\bboss-soa\\test\\org\\frameworkset\\soa\\testxstream.xml");
		PhoneNumber phone = new PhoneNumber();
		phone.setCode(123);
		phone.setNumber("1234-456");

		PhoneNumber fax = new PhoneNumber();
		fax.setCode(123);
		fax.setNumber("<aaaa>9999-999</bbbb>");
		
	   Set dataSet = new TreeSet();
	   dataSet.add("aa");
	   dataSet.add("bb");
	   
	   List dataList = new ArrayList();
	   dataList.add("aa");
	   dataList.add("bb");
	   
	   Map dataMap = new HashMap();
	   dataMap.put("aa","aavalue");
	   dataMap.put("bb","bbvalue");
	   
	   String[] dataArray = new String[]{"aa","bb"};
	   
		
		FilePerson joe = new FilePerson();
		joe.setFileData(fileData);
		joe.setFirstname("Joe");
//		joe.setLastname("Walnes");
		//用来验证bboss和Xstream是否会按照null值传递，也就是说lastname有默认值"ssss"
		//这样我们手动把lastname设置为null，理论上来说反序列化后joe中的lastname应该是null而不是默认值"ssss"
		
		joe.setLastname(null);
		joe.setPhone(phone);
		joe.setFax(fax);
		joe.setDataArray(dataArray);
		joe.setDataList(dataList);
		joe.setDataMap(dataMap);
		joe.setDataSet(dataSet);
		
		
		
		try
		{
			//预热bboss和xstream
			String xml = ObjectSerializable.toXML(joe);
			
			
			System.out.println(xml);
			Person person = ObjectSerializable.toBean( xml, Person.class);
			
			String xmlXstream = xStream.toXML(joe);
			Person p = (Person)xStream.fromXML(xmlXstream);
			System.out.println(xmlXstream);
			
			System.out.println();System.out.println("bboss序列化测试用例开始");System.out.println();
			
			long start = System.currentTimeMillis();			
			ObjectSerializable.toXML(joe);			
			long end = System.currentTimeMillis();			
			System.out.println("执行bboss beantoxml 1次，耗时:"+(end - start) +"毫秒");
			
			
			convertBeanToXml(10,joe);
			
			
			convertBeanToXml(100,joe);
				
			
			convertBeanToXml(1000,joe);			
			
			
			convertBeanToXml(10000,joe);
			System.out.println();System.out.println("xstream序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();			
				xStream.toXML(joe);			
				 end = System.currentTimeMillis();			
				System.out.println("执行XStream beantoxml 1次，耗时:"+(end - start) +"毫秒");
				
			convertBeanToXStreamXml(10,joe);
			convertBeanToXStreamXml(100,joe);
			convertBeanToXStreamXml(1000,joe);
			convertBeanToXStreamXml(10000,joe);
			
			System.out.println();System.out.println("bboss反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			person =  ObjectSerializable.toBean( xml, Person.class);
			end = System.currentTimeMillis();			
			System.out.println("执行bboss xmltobean 1次，耗时:"+(end - start)+"豪秒");			
			convertXMLToBean(10,xml);			
			convertXMLToBean(100,xml);
			convertXMLToBean(1000,xml);
			convertXMLToBean(10000,xml);
			
			System.out.println();System.out.println("xstream反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			xStream.fromXML(xmlXstream);
			end = System.currentTimeMillis();			
			System.out.println("执行XStream xmltobean 1次，耗时:"+(end - start)+"豪秒");
			convertXStreamXMLToBean(10,xmlXstream);
			convertXStreamXMLToBean(100,xmlXstream);
			convertXStreamXMLToBean(1000,xmlXstream);
			convertXStreamXMLToBean(10000,xmlXstream);
			
			//测试用例结束
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
