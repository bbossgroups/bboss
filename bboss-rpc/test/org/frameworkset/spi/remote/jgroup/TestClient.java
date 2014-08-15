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

package org.frameworkset.spi.remote.jgroup;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.TestBase;

import bboss.org.jgroups.Address;

import org.junit.Test;


/**
 * <p>Title: TestClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-7 下午11:23:47
 * @author biaoping.yin
 * @version 1.0
 */
public class TestClient extends TestBase
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
    	
    	String address_ = JGroupHelper.getJGroupHelper().getAppservers().get(0).toString();
        RPCAddress address = new RPCAddress(address_,Target.BROADCAST_TYPE_JRGOUP);
        boolean connected = RPCHelper.getRPCHelper().validateAddress(address);
        System.out.println("connected:"+connected );
        
    }
    
    public static void testGetPyaddress()
    {
    	Address address_ = JGroupHelper.getJGroupHelper().getAppservers().get(0);
    	System.out.println(JGroupHelper.getJGroupHelper().getPhysicalAddress(address_));
    }
/**
 * @param args
 */
public static void main(String[] args)
{
	JGroupHelper.getJGroupHelper().start();	
//    TestDamone t = new TestDamone();
//    t.start();
	/**
	 * all组播调用
	 */
//	testJGroupAll();
//	testJGroupAllWithParameter();
//	testJGroupSelfProtocolRPC();
	/**
	 * 测试带jgroup协议头的rpc调用
	 */
//	testJGroupProtocolRPC();
//	testValidateAddress();
//	testGetPyaddress();
    /**
     * 测试单线程调用
     */
//    testJGroupRPC();
	/**
	 * 测试多线程调用
	 */
//    testMutiThreadJGroupRPC();
    
    /**
     * 校验地址是否可连通
     */
//    testValidateAddress();
    /**
     * 测试JGroup多播调用
     */
//    testMuticastJGroupRPC(); 

    /**
     * 测试本地调用
     */
//    testJGroupLocal();
    /**
     * 测试JGroup连接器创建功能
     */
//    testClinentTransport();
//         System.exit(0);
//	

}

@Test
public  void testJGroupProtocolRPC()
{
	String address_ = "test";
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::" + address_ + ")/rpc.test",(RPCTestInf.class));
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::" + address_ + ")/rpc.test");
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	long start = System.currentTimeMillis();
	
	for(int i = 0; i < 10; i ++)
	    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
	long end = System.currentTimeMillis();
	System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
	
	
	
}

@Test
public   void testJGroupSelfProtocolRPC()
{
	Address address_ = JGroupHelper.getJGroupHelper().getAppservers().get(2);
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::" + address_ + ")/rpc.test",(RPCTestInf.class));
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::" + address_ + ")/rpc.test");
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	long start = System.currentTimeMillis();
	
	for(int i = 0; i < 10; i ++)
	    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
	long end = System.currentTimeMillis();
	System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
	
	
	
}

@Test
public   void testMutiThreadJGroupRPC()
    {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:12346)/rpc.test");
////          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
//            long start = System.currentTimeMillis();
//            
//            for(int i = 0; i < 100; i ++)
//                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
//            
//            long end = System.currentTimeMillis();
//            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::all)/rpc.test",(RPCTestInf.class));
    for(int i = 0; i < 10; i ++)
    {
        Thread t = new Thread(new RunJGroupRPC(i,testInf));
        t.start();
    }
            
            
    }

 class RunJGroupRPC implements Runnable
{
    int i = 0;
    RPCTestInf testInf;
    RunJGroupRPC(int i,RPCTestInf testInf)
    {
    	this.testInf = testInf;
        this.i = i;
    }
        public void run()
        {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::all)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 10; i ++)
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
public   void testMuticastJGroupRPC()
    {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::creator-cc-27488;creator-cc-51859)/rpc.test");
	 		RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::creator-cc-27488;creator-cc-51859)/rpc.test",RPCTestInf.class);
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            Object ret = testInf.getCount();
            Object ret_40561;
			try {
				ret_40561 = ClientProxyContext.getRPCResult("creator-cc-27488", ret,Target.BROADCAST_TYPE_JRGOUP);
				System.out.println("ret_40561:"+ret_40561);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Object ret_64357;
			try {
				ret_64357 = ClientProxyContext.getRPCResult("creator-cc-51859", ret,Target.BROADCAST_TYPE_JRGOUP);
				System.out.println("ret_64357:"+ret_64357);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          //等价的调用方法
            
            
            
//          Object ret_12345 = context.getRPCResult("192.168.11.102:1186", ret,Target.BROADCAST_TYPE_JRGOUP);
//          Object ret_12346 = context.getRPCResult("192.168.11.102:12346", ret,Target.BROADCAST_TYPE_JRGOUP);
//            for(int i = 0; i < 100; i ++)
//                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            
            
    }
@Test
public   void testJGroupLocal()
{
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
	for(int i = 0; i < 100; i ++)
	System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
	
	
}

@Test
public   void testJGroupAll()
{
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
	RPCTestInf testInf =  ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::all)/rpc.test?parameterKey=多多", RPCTestInf.class);
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::all)/rpc.test?parameterKey=多多");
	
	for(int i = 0; i < 10; i ++)
	{
		Object ret = testInf.getCount();
		int size = ClientProxyContext.getRPCResultSize(ret);
		for(int j = 0; j < size; j ++)
		{
			Object ret_1186;
			try {
				ret_1186 = ClientProxyContext.getRPCResult(j, ret);
				System.out.println("ret_1186:" + j + " = "+ret_1186);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		Object ret_1186 = context.getRPCResult(0, ret);
//        Object ret_1187 = context.getRPCResult(1, ret);
//        Object ret_1188 = context.getRPCResult(2, ret);
//        Object ret_1189 = context.getRPCResult(3, ret,Target.BROADCAST_TYPE_JRGOUP);
		
//		System.out.println("ret_1187:" + i + " = "+ret_1187);
//		System.out.println("ret_1188:" + i + " = "+ret_1188);
//		System.out.println("ret_1189:" + i + " = "+ret_1189);
	}
	
	
}

@Test
public   void testJGroupAllWithParameter()
{
	JGroupHelper.getJGroupHelper().start();	
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
	 
//	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(jgroup::all)/rpc.test?server_uuid=aa");
	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(jgroup::all)/rpc.test?server_uuid=aa",RPCTestInf.class);
	for(int i = 0; i < 10; i ++)
	{
		Object ret = testInf.getParameter();
		int size = ClientProxyContext.getRPCResultSize(ret);
		for(int j = 0; j < size; j ++)
		{
			Object ret_1186;
			try {
				ret_1186 = ClientProxyContext.getRPCResult(j, ret);
				System.out.println("ret_1186:" + j + " = "+ret_1186);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		Object ret_1186 = context.getRPCResult(0, ret);
//        Object ret_1187 = context.getRPCResult(1, ret);
//        Object ret_1188 = context.getRPCResult(2, ret);
//        Object ret_1189 = context.getRPCResult(3, ret,Target.BROADCAST_TYPE_JRGOUP);
		
//		System.out.println("ret_1187:" + i + " = "+ret_1187);
//		System.out.println("ret_1188:" + i + " = "+ret_1188);
//		System.out.println("ret_1189:" + i + " = "+ret_1189);
	}
	
	
}



}
