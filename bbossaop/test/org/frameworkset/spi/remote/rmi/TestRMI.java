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

package org.frameworkset.spi.remote.rmi;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.TestBase;
import org.junit.Test;

/**
 * <p>Title: TestRMI.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-17 下午03:35:18
 * @author biaoping.yin
 * @version 1.0
 */
public class TestRMI extends TestBase{
	ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/manager-rpc-test.xml"); 
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
//            System.exit(2);
//            super.finalize();
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
        RPCAddress address = new RPCAddress("172.16.25.108",1099,"rmi");
        boolean connected = RPCHelper.getRPCHelper().validateAddress(address,"admin","123456");
        System.out.println("connected:"+connected );
        
    }
/**
 * @param args
 */
public static void main(String[] args)
{
//    TestDamone t = new TestDamone();
//    t.start();
//    RPCHelper s = new RPCHelper(); 
    /**
     * 测试单线程调用
     */
//    testRMIRPC();
//	testRMIExceptionRPC();
    
    /**
     * 无返回值测试
     */
//    testNoReturnJMSRPC();
	/**
	 * 测试多线程调用
	 */
//    testMutiThreadJMSRPC();
    
    /**
     * 校验地址是否可连通
     */
    testValidateAddress();
    /**
     * 测试JMS多播调用
     */
//    testMuticastJMSRPC(); 

    /**
     * 测试本地调用
     */
//    testJMSLocal();
    /**
     * 测试JMS连接器创建功能
     */
//    testClinentTransport();
         System.exit(0);
//	

}

@Test
public  void testRMIRPC()
{
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::192.168.1.22:1090)/rpc.test?user=admin&password=123456&server_uuid=app1",RPCTestInf.class);
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::192.168.1.22:1090)/rpc.test?user=admin&password=123456&server_uuid=app1");
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	testInf.getCount();
	long start = System.currentTimeMillis();
	
	for(int i = 0; i < 1; i ++)
	    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
	long end = System.currentTimeMillis();
	System.out.println("消耗时间：" + (end - start)  + "豪秒");
	
	
	
}

@Test
public  void testWithParameter()
{
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::192.168.1.22:1090)/rpc.test",RPCTestInf.class);
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::192.168.1.22:1090)/rpc.test");
	
	for(int i = 0; i < 10; i ++)
	{
		Object ret = testInf.getParameter();
		
		
			System.out.println("ret_1186: = "+ret);
		
//		Object ret_1186 = context.getRPCResult(0, ret);
//        Object ret_1187 = context.getRPCResult(1, ret);
//        Object ret_1188 = context.getRPCResult(2, ret);
//        Object ret_1189 = context.getRPCResult(3, ret,Target.BROADCAST_TYPE_JRGOUP);
		
//		System.out.println("ret_1187:" + i + " = "+ret_1187);
//		System.out.println("ret_1188:" + i + " = "+ret_1188);
//		System.out.println("ret_1189:" + i + " = "+ret_1189);
	}
	
long start = System.currentTimeMillis();
	

	    testInf.getCount();
	
	long end = System.currentTimeMillis();
	System.out.println("消耗时间：" + (end - start)  + "豪秒");
	
}


@Test
public void testRMIExceptionRPC()
{
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::172.16.17.216:1099)/rpc.test?server_uuid=app1",RPCTestInf.class);
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test?server_uuid=app1");
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	long start = System.currentTimeMillis();
	try {
		testInf.throwexcpetion();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	long end = System.currentTimeMillis();
	System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
	
	
	
}

@Test
public void testNoReturnRMIRPC()
    {
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::172.16.17.216:1099)/rpc.test",RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 1; i ++)
                testInf.sayHello("尹标平");
            
            long end = System.currentTimeMillis();
            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
            
            
            
    }



@Test
public void testMutiThreadRMIRPC()
    {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12346)/rpc.test");
////          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
//            long start = System.currentTimeMillis();
//            
//            for(int i = 0; i < 100; i ++)
//                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
//            
//            long end = System.currentTimeMillis();
//            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::172.16.17.216:1099)/rpc.test",RPCTestInf.class);
    for(int i = 0; i < 10; i ++)
    {
        Thread t = new Thread(new RunRMIRPC(i,testInf));
        t.start();
    }
            
            
    }

 class RunRMIRPC implements Runnable
{
    int i = 0;
    RPCTestInf testInf;
    RunRMIRPC(int i,RPCTestInf testInf)
    {
    	this.testInf = testInf;
        this.i = i;
    }
        public void run()
        {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
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
public void testMuticastRMIRPC()
    {
			RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(rmi::172.16.17.216:1099;172.16.17.194:1099)/rpc.test",RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(rmi::172.16.17.216:1099;172.16.17.194:1099)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            Object ret = testInf.getCount();
            Object ret_12345 = null;
			try {
				ret_12345 = ClientProxyContext.getRMIRPCResult("172.16.17.216:1099", ret);
				 System.out.println("172.16.17.216:1099:" +ret_12345);
		            
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Object ret_12346 = null;
			try {
				ret_12346 = ClientProxyContext.getRMIRPCResult("172.16.17.194:1099", ret);
				System.out.println("172.16.17.194:1099:" +ret_12346);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
            //等价的调用方法
//            Object ret_12345 = context.getRPCResult("00-23-54-5A-E6-3A-jms", ret,Target.BROADCAST_TYPE_JMS);
//            Object ret_12346 = context.getRPCResult("11-23-54-5A-E6-3A-jms", ret,Target.BROADCAST_TYPE_JMS);
            
//            for(int i = 0; i < 100; i ++)
//                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            
            
    }
@Test
public void testRMILocal()
{
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12345)/rpc.test");
	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	for(int i = 0; i < 100; i ++)
	System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
	
}

}
