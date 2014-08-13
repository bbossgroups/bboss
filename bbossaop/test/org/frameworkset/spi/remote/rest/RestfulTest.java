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

package org.frameworkset.spi.remote.rest;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.TestBase;
import org.junit.Test;



/**
 * <p>Title: RestfulTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-7 下午12:11:36
 * @author biaoping.yin
 * @version 1.0
 */
public class RestfulTest extends TestBase
{
	@Test
	public  void testJgroupRestRPC()
	{
		RPCHelper.getRPCHelper().startJGroupServer();
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rest::a/b/c/d)/rpc.test?user=admin&password=123456",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rest::a/b/c/d)/rpc.test?user=admin&password=123456");
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		{
		    try
		    {
		        System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		        
		    }
		}
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
		
		
		
	}
	
	@Test
    public  void testRestRPC()
    {     
//        RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rest::a/b/c/d)/rpc.test?user=admin&password=123456");
        RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rest::a/b)/rpc.test?server_uuid=多多",RPCTestInf.class);
//      RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
        long start = System.currentTimeMillis();
        
        for(int i = 0; i < 1; i ++)
        {
            try
            {
                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
            }
        }
        
        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
        
        
        
    }
	
	
	@Test
    public  void testSingleRestRPC()
    {     
//        RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rest::a)/rpc.test?user=admin&password=123456");
        RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rest::a)/rpc.test?user=admin&password=123456",RPCTestInf.class);
//      RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
        long start = System.currentTimeMillis();
        
        for(int i = 0; i < 10000; i ++)
        {
            try
            {
                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
            }
        }
        
        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
        
        
        
    }
	@Test
	public  void testNoReturnRestRPC()
        {
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(127.0.0.1:12347)/rpc.test",RPCTestInf.class);
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12347)/rpc.test");
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                long start = System.currentTimeMillis();
                
                for(int i = 0; i < 1; i ++)
                    testInf.sayHello("尹标平");
                
                long end = System.currentTimeMillis();
                System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
                
                
                
        }
	@Test
	public  void testMutiThreadMinaRPC()
        {
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12346)/rpc.test");
////              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
//                long start = System.currentTimeMillis();
//                
//                for(int i = 0; i < 100; i ++)
//                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
//                
//                long end = System.currentTimeMillis();
//                System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(127.0.0.1:12346)/rpc.test",RPCTestInf.class);
	    for(int i = 0; i < 10; i ++)
	    {
	        Thread t = new Thread(new RunRestRPC(i,testInf));
	        t.start();
	    }
                
                
        }
	
	 class RunRestRPC implements Runnable
	{
	    int i = 0;
	    RPCTestInf testInf;
	    RunRestRPC(int i,RPCTestInf testInf)
	    {
	        this.i = i;
	        this.testInf = testInf;
	    }
            public void run()
            {
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12346)/rpc.test");
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                long start = System.currentTimeMillis();
                
                for(int i = 0; i < 100; i ++)
                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
                
                long end = System.currentTimeMillis();
                System.out.println("任务【" + i + "】消耗时间：" + (end - start) / 1000 + "秒");
                
            }
	    
	}
	@Test
	public  void testSingleTimeout()
	{
		
	}
	
	/**
	 * 多播服务调用
	 */
	@Test
	public  void testMuticastRestRPC()
        {
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(mina::127.0.0.1:12345;127.0.0.1:12346)/rpc.test",RPCTestInf.class);
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(mina::127.0.0.1:12345;127.0.0.1:12346)/rpc.test");
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                Object ret = testInf.getCount();
                try {
					Object ret_12345 = ClientProxyContext.getRPCResult("127.0.0.1", "12345", ret);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                try {
					Object ret_12346 = ClientProxyContext.getMinaRPCResult("127.0.0.1:12346", ret);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //等价的调用方法
                
//                Object ret_12345 = context.getRPCResult("127.0.0.1:12345", ret,Target.BROADCAST_TYPE_MINA);
//                Object ret_12346 = context.getRPCResult("127.0.0.1:12346", ret,Target.BROADCAST_TYPE_MINA);
//                for(int i = 0; i < 100; i ++)
//                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
                
                
                
        }
	@Test
	public void testRestLocal()
	{
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12345)/rpc.test");
		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		for(int i = 0; i < 100; i ++)
		System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		
	}
}
