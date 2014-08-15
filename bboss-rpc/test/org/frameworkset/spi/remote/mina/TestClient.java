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

package org.frameworkset.spi.remote.mina;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.TestBase;
import org.frameworkset.spi.remote.mina.client.ClinentTransport;
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
	@Test
	public  void testWithParameter()
	{
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(mina::192.168.1.22:12346)/rpc.test?parameterKey=多多",RPCTestInf.class);
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(mina::192.168.1.22:12346)/rpc.test?parameterKey=多多");
		
		for(int i = 0; i < 10; i ++)
		{
			Object ret = testInf.getParameter();
			
			
				System.out.println("ret_1186: = "+ret);
			
//			Object ret_1186 = context.getRPCResult(0, ret);
//	        Object ret_1187 = context.getRPCResult(1, ret);
//	        Object ret_1188 = context.getRPCResult(2, ret);
//	        Object ret_1189 = context.getRPCResult(3, ret,Target.BROADCAST_TYPE_JRGOUP);
			
//			System.out.println("ret_1187:" + i + " = "+ret_1187);
//			System.out.println("ret_1188:" + i + " = "+ret_1188);
//			System.out.println("ret_1189:" + i + " = "+ret_1189);
		}
		
		
	}
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
            RPCAddress address = new RPCAddress("127.0.0.1",12346,null,Target.BROADCAST_TYPE_MINA);
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
	    /**
	     * 测试单线程调用
	     */
//	    testMinaRPC();
	    
	    /**
	     * 无返回值测试
	     */
//	    testNoReturnMinaRPC();
		/**
		 * 测试多线程调用
		 */
//	    testMutiThreadMinaRPC();
	    
	    /**
	     * 校验地址是否可连通
	     */
//	    testValidateAddress();
	    /**
	     * 测试mina多播调用
	     */
//	    testMuticastMinaRPC(); 

	    /**
	     * 测试本地调用
	     */
//	    testMinaLocal();
	    /**
	     * 测试mina连接器创建功能
	     */
//	    testClinentTransport();
//	         System.exit(0);
//		

	}
	
	@Test
	public void testFuture() throws InterruptedException, ExecutionException, TimeoutException
	{
		final RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(mina::172.16.17.216:12347)/rpc.test?user=admin&password=123456",RPCTestInf.class);
//	    final RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(mina::172.16.17.216:12347)/rpc.test?user=admin&password=123456");
	    FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>(){
            public Object call() throws Exception
            {
               Object ret = testInf.getCount();
               return ret;
               
            }});
	    
	    ExecutorService executor = java.util.concurrent.Executors.newCachedThreadPool();
	    executor.execute(task);
        Object ret = task.get();//一直等待返回结果
        //Object ret = task.get(1000,TimeUnit.MICROSECONDS);//等待1秒，1秒钟内返回结果正常，超过一秒后没返回结果抛出TimeoutException异常
        
	}
	
	@Test
	public void testMinaRPC()
	{
		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(mina::172.16.17.216:12347)/rpc.test?user=admin&password=123456",RPCTestInf.class);
//		final RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(mina::172.16.17.216:12347)/rpc.test?user=admin&password=123456");
		
		Object ret = testInf.getCount();
		
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		long start = System.currentTimeMillis();
		
		for(int i = 0; i < 1; i ++)
		    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		long end = System.currentTimeMillis();
		System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
		
		
		
	}
	
	@Test
	public void testNoReturnMinaRPC()
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
	public void localcall()
	{
	    RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	    Object ret = testInf.getCount();
	}
	
	@Test
	public void testMutiThreadMinaRPC()
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
	        Thread t = new Thread(new RunMinaRPC(i, testInf));
	        t.start();
	    }
                
                
        }
	
	 class RunMinaRPC implements Runnable
	{
	    int i = 0;
	    RPCTestInf testInf;
	    RunMinaRPC(int i,RPCTestInf testInf)
	    {
	    	this.testInf = testInf;
	        this.i = i;
	    }
            public void run()
            {
                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12346)/rpc.test");
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
	public void testMuticastMinaRPC()
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
	public void testMinaLocal()
	{
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12345)/rpc.test");
		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
		for(int i = 0; i < 100; i ++)
		System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
		
		
	}
	
	
	public static void testClinentTransport()
	{
		ClinentTransport tp = ClinentTransport.getClinentTransport("127.0.0.1", 12345, MinaUtil.getRPCServerIoHandler());
//		tp.write(new test());
//		tp.disconnect();
	}
	
	public static class test implements java.io.Serializable
	{
		
	}

}
