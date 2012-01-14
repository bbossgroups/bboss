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

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.soa.TransientFieldBean;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
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
	 @Test
	 public void testTransientBeantoxml() throws NumberFormatException, IllegalArgumentException, IntrospectionException
	{
		TransientFieldBean transientFieldBean = new TransientFieldBean("onlyField");
		transientFieldBean.setExcludeField("exccc");
		transientFieldBean.setStaticFiled("staticFiled");
		transientFieldBean.setTransientField("transientField");
		String xml = ObjectSerializable.toXML(transientFieldBean);
		TransientFieldBean transientFieldBean_new = ObjectSerializable.toBean(xml, TransientFieldBean.class);
		System.out.println();
		
	}
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
	
	private void convertBeanToXStreamXml(int count,Test1 joe)
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
	
	private void convertBeanToXml(int count,Test1 joe)
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
	@Test
	public void testJAVASerializable() throws Exception
	{
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		
		byte[] cs = oldObjectToByteBuffer(test1) ;
		
		Test1 test1_ =  (Test1)oldObjectFromByteBuffer(cs, 0, cs.length);
		System.out.println();
		
		
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJsonSerializable() throws Exception
	{
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter wt = new StringWriter(); 
		objectMapper.writeValue(wt, test1);
		String ss = wt.toString();
		
		Test1 test1_ =  objectMapper.readValue(new StringReader(ss), Test1.class);
		System.out.println();
		
		
	}
	
	@Test
	public void testXSTreamSerializable() throws Exception
	{
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		
//		byte[] cs = oldObjectToByteBuffer(test1) ;
		String ss = xStream.toXML(test1);
		Test1 test1_ =  (Test1)xStream.fromXML(ss);
		System.out.println();
		
		
	}
	
	
	@Test
	public void testBBossSerializable() throws Exception
	{
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		String ss = ObjectSerializable.toXML(test1);
		Test1 test1_ =  (Test1)ObjectSerializable.toBean(ss,Test1.class);
		
		
		
	}
	
	@Test
	public void testFullBBossSerializable() throws Exception
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/soa/xblink/test.xml");
		Test1 test1 = context.getTBeanObject("test1",  Test1.class);
		
		
//		byte[] cs = oldObjectToByteBuffer(test1) ;
		String ss = ObjectSerializable.toXML(test1);
		Test1 test1_ =  (Test1)ObjectSerializable.toBean(ss,Test1.class);
		System.out.println();
		
		
	}
	
	 /**
     * Serializes/Streams an object into a byte buffer.
     * The object has to implement interface Serializable or Externalizable
     * or Streamable.  Only Streamable objects are interoperable w/ jgroups-me
     */
    public static byte[] oldObjectToByteBuffer(Object obj) throws Exception {
        byte[] result=null;
        ObjectOutputStream out= null;
        ByteArrayOutputStream out_stream = null;
        try
        {
        	out_stream=new ByteArrayOutputStream();
        
            out=new ObjectOutputStream(out_stream);
            out.writeObject(obj);
            
            result=out_stream.toByteArray();
            return result;
        }
        catch(Exception e)
        {
        	throw e;
        }
        finally
        {
        	try
			{
				if (out_stream != null)
					out_stream.close();
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
			try
			{
				if (out != null)
					out.close();
			}
			catch (Exception e2)
			{
				// TODO: handle exception
			}
        	
        }
        
    }
    
    public static Object oldObjectFromByteBuffer(byte[] buffer, int offset, int length) throws Exception {
        if(buffer == null) return null;
        Object retval=null;

        try {  // to read the object as an Externalizable
            ByteArrayInputStream in_stream=new ByteArrayInputStream(buffer, offset, length);
            ObjectInputStream in=new ObjectInputStream(in_stream); // changed Nov 29 2004 (bela)
            retval=in.readObject();
            in.close();
        }
        catch(StreamCorruptedException sce) {
//            try {  // is it Streamable?
//                ByteArrayInputStream in_stream=new ByteArrayInputStream(buffer, offset, length);
//                DataInputStream in=new DataInputStream(in_stream);
//                retval=readGenericStreamable(in);
//                in.close();
//            }
//            catch(Exception ee) {
                IOException tmp=new IOException("unmarshalling failed");
                tmp.initCause(sce);
                throw tmp;
//            }
        }

        if(retval == null)
            return null;
        return retval;
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
	public void testTest1()
	{
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		
	
		
		
		try
		{
			//预热bboss和xstream
			String xml = ObjectSerializable.toXML(test1);
			Test1 test1_ =  (Test1)ObjectSerializable.toBean(xml,Test1.class);
			String xmlXstream = xStream.toXML(test1);
			Test1 p = (Test1)xStream.fromXML(xmlXstream);
			System.out.println(xmlXstream);
			
			System.out.println();System.out.println("bboss序列化测试用例开始");System.out.println();
			
			long start = System.currentTimeMillis();			
			ObjectSerializable.toXML(test1);			
			long end = System.currentTimeMillis();			
			System.out.println("执行bboss beantoxml 1次，耗时:"+(end - start) +"毫秒");
			
			
			convertBeanToXml(10,test1);
			
			
			convertBeanToXml(100,test1);
				
			
			convertBeanToXml(1000,test1);			
			
			
			convertBeanToXml(10000,test1);
			System.out.println();System.out.println("xstream序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();			
				xStream.toXML(test1);			
				 end = System.currentTimeMillis();			
				System.out.println("执行XStream beantoxml 1次，耗时:"+(end - start) +"毫秒");
				
			convertBeanToXStreamXml(10,test1);
			convertBeanToXStreamXml(100,test1);
			convertBeanToXStreamXml(1000,test1);
			convertBeanToXStreamXml(10000,test1);
			
			System.out.println();System.out.println("bboss反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			test1 =  ObjectSerializable.toBean(xml, Test1.class);
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
	public void testXMLTest1()
	{
		
		Test1 test1 = new Test1();
		Test2 test2 = new Test2();
		Test3 test3 = new Test3();
		test2.setTest1(test1);
		test1.setTest2(test2);
		test1.setTest3(test3);
		test3.setTest2(test2);
		
	
		
		
		try
		{
			String bigcontent = FileUtil.getFileContent(new File("D:\\workspace\\bbossgroups-3.2\\bboss-soa\\test\\org\\frameworkset\\soa\\testxstream.xml"), "GBK");
			//预热bboss和xstream
			test1.setXmlvalue(bigcontent);
			String xml = ObjectSerializable.toXML(test1);
			Test1 test1_ =  (Test1)ObjectSerializable.toBean(xml,Test1.class);
			String xmlXstream = xStream.toXML(test1);
			Test1 p = (Test1)xStream.fromXML(xmlXstream);
			System.out.println(xmlXstream);
			
			System.out.println();System.out.println("bboss序列化测试用例开始");System.out.println();
			
			long start = System.currentTimeMillis();			
			ObjectSerializable.toXML(test1);			
			long end = System.currentTimeMillis();			
			System.out.println("执行bboss beantoxml 1次，耗时:"+(end - start) +"毫秒");
			
			
			convertBeanToXml(10,test1);
			
			
			convertBeanToXml(100,test1);
				
			
			convertBeanToXml(1000,test1);			
			
			
			convertBeanToXml(10000,test1);
			System.out.println();System.out.println("xstream序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();			
				xStream.toXML(test1);			
				 end = System.currentTimeMillis();			
				System.out.println("执行XStream beantoxml 1次，耗时:"+(end - start) +"毫秒");
				
			convertBeanToXStreamXml(10,test1);
			convertBeanToXStreamXml(100,test1);
			convertBeanToXStreamXml(1000,test1);
			convertBeanToXStreamXml(10000,test1);
			
			System.out.println();System.out.println("bboss反序列化测试用例开始");System.out.println();
			start = System.currentTimeMillis();
			test1 =  ObjectSerializable.toBean(xml, Test1.class);
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
