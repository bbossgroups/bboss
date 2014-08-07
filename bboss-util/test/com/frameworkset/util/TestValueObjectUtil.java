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
package com.frameworkset.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;



/**
 * <p>TestValueObjectUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2009-9-20
 * @author biaoping.yin
 * @version 1.0
 */
public class TestValueObjectUtil extends TestCase 
{
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	public void testTypeEqual()
	{
		Class test = Test.class;
		
		Class abstractTest = AbstractTest.class;
		Class interfaceTest = InterfaceTest.class;
		System.out.println("test.isAssignableFrom(abstractTest):" + test.isAssignableFrom(abstractTest));
		System.out.println("abstractTest.isAssignableFrom(test):" + abstractTest.isAssignableFrom(test));
		System.out.println("interfaceTest.isAssignableFrom(test):" + interfaceTest.isAssignableFrom(test));
	}
	
	public void testTypeConvert()
	{
		InterfaceTest interfaceTest = new Test();
		Test other = (Test) ValueObjectUtil.shell(Test.class, interfaceTest);
		Class test = Test.class;
		
		
		System.out.println(other);
		other = (Test) test.cast(interfaceTest);
		System.out.println(other);
		
		interfaceTest = new AbstractTest();
		
		other = (Test) ValueObjectUtil.shell(Test.class, interfaceTest);
		
		
		
		System.out.println(other);
		other = (Test) test.cast(interfaceTest);
		System.out.println(other);
	}
	
	public void testTypeMatch()
	{
	    Object value = new Test();
	    super.assertEquals(true, ValueObjectUtil.isSameType(Object.class, Test.class, value));
	}
	
	public void testTypeMatchValuenull()
    {
        Object value =null;
        super.assertEquals(true, ValueObjectUtil.isSameType(Object.class, Test.class, value));
    }
	
	
	public void testConstructget()
    {
        Object value =null;        
        Class clazz = TestM.class;
        Class[] params = new Class[] {String.class,Test.class};
        
        Class[] params_ = new Class[] {String.class,Object.class};
        Object[] paramArgs = new Object[] {"name",new Test()};
        java.lang.reflect.Constructor c = ValueObjectUtil.getConstructor(clazz, params, paramArgs);
        System.out.println(c);
        
        c = ValueObjectUtil.getConstructor(clazz, params_, paramArgs);
        System.out.println(c);
        
        paramArgs = new Object[] {"name",null};
        
        c = ValueObjectUtil.getConstructor(clazz, params, paramArgs);
        System.out.println(c);
        
        c = ValueObjectUtil.getConstructor(clazz, params_, paramArgs);
        System.out.println(c);
//        super.assertEquals(true, ValueObjectUtil.isSameType(Object.class, Test.class, value));
    }
	
	
	public static class TestM
	{
	    public TestM(String name,Object test)
        {
            
        }
	    public TestM(String name,Test test)
	    {
	        
	    }
	    
	    
	}
	
	public static class OTest
	{
	    
	}
	@org.junit.Test
	public void testLongtoDate()
	{
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.util.Date.class));
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.sql.Date.class));
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.sql.Timestamp.class));
	}
	@org.junit.Test
	public void testStringtoDate()
	{
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.util.Date.class));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.sql.Date.class));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.sql.Timestamp.class));
	}
	
	@org.junit.Test
	public void testStringtoDateBySepecialFormat()
	{
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.util.Date.class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.sql.Date.class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.sql.Timestamp.class,"yyyy-MM-dd"));
	}
	
	@org.junit.Test
	public void testDatetoLongArray()
	{
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date(), long[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Date(new java.util.Date().getTime()), long[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Timestamp(new java.util.Date().getTime()), long[].class));
	}
	@org.junit.Test
	public void testDatestoLongArray()
	{
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date[]{new java.util.Date()}, long[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Date[]{new java.sql.Date(new java.util.Date().getTime())}, long[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Timestamp[]{new java.sql.Timestamp(new java.util.Date().getTime())}, long[].class));
	}
	@org.junit.Test
	public void testDatetoLong()
	{
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date(), long.class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Date(new java.util.Date().getTime()), long.class));
		System.out.println(ValueObjectUtil.typeCast(new java.sql.Timestamp(new java.util.Date().getTime()), long.class));
	}
	@org.junit.Test
	public void testLongtoDateArray()
	{
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.util.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.sql.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new java.util.Date().getTime(), java.sql.Timestamp[].class));
	}
	
	
	@org.junit.Test
	public void testStringtoDateArray()
	{
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.util.Date[].class));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.sql.Date[].class));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30 22:02:47", java.sql.Timestamp[].class));
	}
	
	@org.junit.Test
	public void testStringtoDateArrayBySepecialFormat()
	{
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.util.Date[].class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.sql.Date[].class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast("2011-04-30", java.sql.Timestamp[].class,"yyyy-MM-dd"));
	}
	
	
	
	@org.junit.Test
	public void testLongstoDateArray()
	{
		System.out.println(ValueObjectUtil.typeCast(new long[]{new java.util.Date().getTime()}, java.util.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new long[]{new java.util.Date().getTime()}, java.sql.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new long[]{new java.util.Date().getTime()}, java.sql.Timestamp[].class));
	}
	@org.junit.Test
	public void testStringstoDateArray()
	{
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30 22:02:47"}, java.util.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30 22:02:47"}, java.sql.Date[].class));
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30 22:02:47"}, java.sql.Timestamp[].class));
	}
	
	@org.junit.Test
	public void testStringstoDateArrayBySepecialFormat()
	{
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30"}, java.util.Date[].class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30"}, java.sql.Date[].class,"yyyy-MM-dd"));
		System.out.println(ValueObjectUtil.typeCast(new String[]{"2011-04-30"}, java.sql.Timestamp[].class,"yyyy-MM-dd"));
	}
	@org.junit.Test
	public void testtimeconvert()
	{
		String time = "2011-05-02 17:45:15";
		String formate = "yyyy-MM-dd hh:mm:ss";
		Timestamp tt = null;
		SimpleDateFormat f = new SimpleDateFormat(formate);
		try {
			System.out.println(f.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@org.junit.Test
	public void testcast()
	{
		Object value = ValueObjectUtil.typeCast(new Boolean(true), Boolean.class, boolean.class);
		System.out.println(value);
	}
	
//	public static void main(String[] args)
//	{
//		testTypeEqual();
//		testTypeConvert();
//	}
}
