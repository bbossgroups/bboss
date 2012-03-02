/*
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

package org.frameworkset.spi.beans;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.remote.restful.RestfulServiceConvertor;
import org.junit.Test;

/**
 * <p>Title: TestBeanContext.java</p> 
 * <p>Description: 测试独立的配置文件上下文的bean工厂类</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-21 下午08:43:44
 * @author biaoping.yin
 * @version 1.0
 */
public class TestBeanContext {
	@Test
	public void testSingleConfigeFile()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset" +
				"/spi/beans/testapplicationcontext.xml");
//		RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("rpc.restful.convertor");
		RestfulServiceConvertor convertor = context.getTBeanObject("rpc.restful.convertor",RestfulServiceConvertor.class);
		
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/beans/testapplicationcontext.xml");
		ApplicationContext defaultcontext = ApplicationContext.getApplicationContext("manager-provider.xml");
		
        RestfulServiceConvertor dconvertor = (RestfulServiceConvertor)defaultcontext.getBeanObject("rpc.restful.convertor");
        defaultcontext = ApplicationContext.getApplicationContext("manager-provider.xml");
        org.junit.Assert.assertEquals(convertor, dconvertor);
        
		
	}
	@Test
	public void testRPCContext()
	{
	    ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/beans/testapplicationcontext.xml");
	    RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.restful.convertor");
	    System.out.println(convertor.toString());
	    String ret =	    convertor.convert("a", "rpc.test");
//	    RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("rpc.restful.convertor");
//	    String ret = convertor.convert("a", "rpc.test");
//	    super.toString();
	    System.out.println(ret);
	    
	    
	}
	
	
	@Test
	public void testDefaultContext()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/beans/testapplicationcontext.xml");
	    RestfulServiceConvertor convertor = context.getTBeanObject("rpc.restful.convertor",RestfulServiceConvertor.class);
	    System.out.println(convertor.toString());
	    String ret =	    convertor.convert("a", "rpc.test");
//	    RestfulServiceConvertor convertor = (RestfulServiceConvertor)context.getBeanObject("rpc.restful.convertor");
//	    String ret = convertor.convert("a", "rpc.test");
//	    super.toString();
	    System.out.println(ret);
	    
	    
	}
	
	@Test
	public void testBeanInfoAware()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/beans/manager-beans.xml");
		BeanInfoAwareTest convertor = context.getTBeanObject("test.beaninfoawary",BeanInfoAwareTest.class);
		//从组件实例中获取组件配置元数据对象
		Pro beaninfo = convertor.getBeaninfo();

	    
	}
	
	public static void main(String[] args)
	{
		TestBeanContext test = new TestBeanContext();
		test.testRPCContext();
		
	}

}
