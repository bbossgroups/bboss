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

package org.frameworkset.spi.remote.jms;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.TestBase;
import org.junit.Test;


/**
 * <p>Title: TestClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-7 下午10:00:29
 * @author biaoping.yin
 * @version 1.0
 */
public class TestClient  extends TestBase
{

        public static class TestDamone extends java.lang.Thread
        {
            public TestDamone()
            {
                this.setDaemon(true);
            }
            @Override
            protected void finalize() throws Throwable
            {
                System.out.println("finalize");
//                System.exit(2);
//                super.finalize();
            }
            public void run()
            {
                while(true)
                {
                    System.out.println("run");
                    synchronized(this)
                    {
                        try
                        {
                            wait();
                        }
                        catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                 
            }
        }
        
        /**
         * 测试地址是否可连通
         */
        public static void testValidateAddress()
        {
            RPCAddress address = new RPCAddress("00-23-54-5A-E6-3A-jms");
            boolean connected = RPCHelper.getRPCHelper().validateAddress(address);
            System.out.println("connected:"+connected );
            
        }
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//	    TestDamone t = new TestDamone();
//	    t.start();
//	    RPCHelper s = new RPCHelper(); 
	    /**
	     * 测试单线程调用
	     */
//	    testJMSRPC();
	    
	    /**
	     * 无返回值测试
	     */
//	    testNoReturnJMSRPC();
		/**
		 * 测试多线程调用
		 */
//	    testMutiThreadJMSRPC();
	    
	    /**
	     * 校验地址是否可连通
	     */
//	    testValidateAddress();
	    /**
	     * 测试JMS多播调用
	     */
//	    testMuticastJMSRPC(); 

	    /**
	     * 测试本地调用
	     */
//	    testJMSLocal();
	    /**
	     * 测试JMS连接器创建功能
	     */
//	    testClinentTransport();
	         System.exit(0);
//		

	}
	
	@Test
	public void testJMSRPC()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jms::yinbiaoping-jms)/rpc.test",RPCTestInf.class);
    	
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jms::yinbiaoping-jms)/rpc.test");
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		testInf.getCount();
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 10; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start)  + "毫秒");
		
		
		
	}
	
	@Test
	public void testNoReturnJMSRPC()
        {
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jms::00-23-54-5A-E6-3A-jms)/rpc.test",RPCTestInf.class);
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jms::00-23-54-5A-E6-3A-jms)/rpc.test");
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                long start = System.currentTimeMillis();
                
                for(int i = 0; i < 1; i ++)
                    testInf.sayHello("尹标平");
                
                long end = System.currentTimeMillis();
                System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
                
                
                
        }
	
	
	
	@Test
	public void testMutiThreadJMSRPC()
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
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jms::00-23-54-5A-E6-3A-jms)/rpc.test",RPCTestInf.class);
	    for(int i = 0; i < 10; i ++)
	    {
	        Thread t = new Thread(new RunJMSRPC(i,testInf));
	        t.start();
	    }
                
                
        }
	
	 class RunJMSRPC implements Runnable
	{
	    int i = 0;
	    RPCTestInf testInf;
	    RunJMSRPC(int i,RPCTestInf testInf)
	    {
	    	this.testInf = testInf;
	        this.i = i;
	    }
            public void run()
            {
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jms::00-23-54-5A-E6-3A-jms)/rpc.test");
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                long start = System.currentTimeMillis();
                
                for(int i = 0; i < 100; i ++)
                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
                
                long end = System.currentTimeMillis();
                System.out.println("任务【" + i + "】消耗时间：" + (end - start) / 1000 + "秒");
                
            }
	    
	}
	
	public static void testSingleTimeout()
	{
		
	}
	
	/**
	 * 多播服务调用
	 */
	@Test
	public void testMuticastJMSRPC()
        {
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jms::yinbiaoping-jms;xiongchao-jms)/rpc.test");
                RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jms::yinbiaoping-jms;xiongchao-jms)/rpc.test",RPCTestInf.class);
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                Object ret = testInf.getCount();
                Object ret_12345 = null;
				try {
					ret_12345 = ClientProxyContext.getJMSRPCResult("yinbiaoping-jms", ret);
					Object ret_12346 = ClientProxyContext.getJMSRPCResult("xiongchao-jms", ret);
					System.out.println("00-23-54-5A-E6-3A-jms:" +ret_12345);
	                System.out.println("00-13-A9-2B-FC-E9-jms:" +ret_12346);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                
                //等价的调用方法
//                Object ret_12345 = context.getRPCResult("00-23-54-5A-E6-3A-jms", ret,Target.BROADCAST_TYPE_JMS);
//                Object ret_12346 = context.getRPCResult("11-23-54-5A-E6-3A-jms", ret,Target.BROADCAST_TYPE_JMS);
                
//                for(int i = 0; i < 100; i ++)
//                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
                
                
                
        }
	@Test
	public void testJMSLocal()
	{
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12345)/rpc.test");
		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		for(int i = 0; i < 100; i ++)
		System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		
	}
	
	
	
}
