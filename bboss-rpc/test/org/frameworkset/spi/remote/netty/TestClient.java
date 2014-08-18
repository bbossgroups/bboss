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

package org.frameworkset.spi.remote.netty;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCTest;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.TestBase;
import org.frameworkset.spi.security.SecurityManager;
import org.junit.Test;

import com.frameworkset.util.ValueObjectUtil;


/**
 * <p>Title: TestClient.java</p> 
 * <p>Description: netty客服端测试用例</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-20 上午11:47:12
 * @author biaoping.yin
 * @version 1.0
 */
public class TestClient extends TestBase
{
	public void init()
	{
		
	}
	@Test
	public void testBigDatas()
	{
		byte[] datas = ValueObjectUtil.getBytesFileContent("D:/workspace/bbossgroups-3.2/文档/bbossgroups框架培训教程.ppt") ;
//		RPCTest test = context.getTBeanObject("(netty::172.16.33.46:12347)/rpc.test",RPCTest.class);
		RPCTest test = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(netty::172.16.33.46:12347)/rpc.test",RPCTest.class);
		datas = test.echo18M(datas);
		
	}
	@Test
	public  void testWithParameter()
	{
//		AssembleUtil.registAssembleCallback(new WebDocbaseAssembleCallback("D:/workspace/bbossgroup-2.0-RC2/bbossaop/"));
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(192.168.11.102:1186)/rpc.test");
		
//		RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(netty::192.168.1.22:12347)/rpc.test?user=admin&password=123456&server_uuid=多多");
		
		for(int i = 0; i < 10; i ++)
		{
			RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml",
					"(netty::10.25.192.142:12347)/rpc.test?user=admin&password=123456&server_uuid=多多",RPCTestInf.class);
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
    public static void main(String[] args)
    {
//        testMutiThreadRPC();
//        testNettyRPC();
//        testMuticastNettyRPC();
    }
    @Test
	public void testMutiThreadRPC()
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
        for(int i = 0; i < 10; i ++)
        {
            Thread t = new Thread(new RunNettyRPC(i));
            t.start();
        }
                
                
        }
    
    /**
     * 多播服务调用
     */
    @Test
	public void testMuticastNettyRPC()
        {
//                RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(netty::172.16.17.216:12345;172.16.17.216:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456");
                RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml",
                		"(netty::10.25.192.142:12345;10.25.192.142:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456",
                		RPCTestInf.class);
//              RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
                Object ret = testInf.getCount();
                Object ret_12345 = null;
				try {
					ret_12345 = ClientProxyContext.getNettyRPCResult("10.25.192.142:12345", ret);
					System.out.println("ret_12345："+ret_12345);
	                
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                Object ret_12347 = null;
				try {
					ret_12347 = ClientProxyContext.getNettyRPCResult("10.25.192.142:12347", ret);
					
	                System.out.println("ret_12347："+ret_12347);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                //等价的调用方法
                
//                Object ret_12345 = context.getRPCResult("127.0.0.1:12345", ret,Target.BROADCAST_TYPE_NETTY);
//                Object ret_12346 = context.getRPCResult("127.0.0.1:12346", ret,Target.BROADCAST_TYPE_NETTY);
//                for(int i = 0; i < 100; i ++)
//                    System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
                
                
                
        }
    
    @Test
    public void testException()
    {
//    	RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(netty::172.16.7.108:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456");
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(netty::172.16.7.108:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456",RPCTestInf.class);
    	try {
			testInf.throwexcpetion();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
	public void testNettyRPC()
    {
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(netty::172.16.17.216:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456",RPCTestInf.class);
//        RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(netty::172.16.17.216:12347)/rpc.test?" + SecurityManager.USER_ACCOUNT_KEY + "=admin&" + SecurityManager.USER_PASSWORD_KEY + "=123456");
    //  RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
        long start = System.currentTimeMillis();
        
        for(int i = 0; i < 10; i ++)
        {
            System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            if(i == 1)
                System.out.println();
        }
        
        long end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start)  + "秒");
        
        start = System.currentTimeMillis();
        
        for(int i = 0; i < 10; i ++)
        {
            System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            if(i == 1)
                System.out.println();
        }
        
         end = System.currentTimeMillis();
        System.out.println("消耗时间：" + (end - start)  + "豪秒");
    }
    
     class RunNettyRPC implements Runnable
    {
        int i = 0;
        RunNettyRPC(int i)
        {
            this.i = i;
        }
        public void run()
        {  
            testNettyRPC();
        }
        
    }
}
