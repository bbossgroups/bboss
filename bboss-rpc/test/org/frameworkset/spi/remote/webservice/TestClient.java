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

package org.frameworkset.spi.remote.webservice;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RPCTestInf;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.TestBase;
import org.junit.Test;

/**
 * <p>Title: TestClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-9 下午04:50:08
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
        RPCAddress address = new RPCAddress("127.0.0.1",12346,null,Target.BROADCAST_TYPE_WEBSERVICE);
        address.setSecurity(false);
        address.setContextpath("WebRoot/cxfservices");
        boolean connected = RPCHelper.getRPCHelper().validateAddress(address);
        System.out.println("connected:"+connected );
        
    }
    
    public static void testSameAddress()
    {
        RPCAddress address = new RPCAddress("127.0.0.1",12346,null,Target.BROADCAST_TYPE_WEBSERVICE);
        address.setSecurity(false);
        address.setContextpath("WebRoot/cxfservices");
        
        RPCAddress address1 = new RPCAddress("127.0.0.1",12346,null,Target.BROADCAST_TYPE_WEBSERVICE);
        address1.setSecurity(true);
        address1.setContextpath("WebRoot/cxfservices");
        boolean SameAddress = address.equals(address1);
        System.out.println("SameAddress:"+SameAddress );
        
    }
    /**
     * @param args
     */
    public static void main(String[] args)
    {
//      TestDamone t = new TestDamone();
//      t.start();
        /**
         * 测试单线程调用
         */
//      testWebserviceRPC();
//      testWebserviceRPC();
//      testWebserviceAuthenticatedFailedRPC();
        
        /**
         * 无返回值测试
         */
//        testNoReturnWebserviceRPC();
//    	testSameAddress();
            /**
             * 测试多线程调用
             */
//      testMutiThreadWebserviceRPC();
        
        /**
         * 校验地址是否可连通
         */
//      testValidateAddress();
        /**
         * 测试Webservice多播调用
         */
//      testMuticastWebserviceRPC(); 

        /**
         * 测试本地调用
         */
//      testWebserviceLocal();
        /**
         * 测试Webservice连接器创建功能
         */
//      testClinentTransport();
//           System.exit(0);
//          

    }
    
    @Test
    public void testWebserviceRPC()
    {
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test?" + org.frameworkset.spi.security.SecurityManager.USER_ACCOUNT_KEY+"=admin&" +org.frameworkset.spi.security.SecurityManager.USER_PASSWORD_KEY + "=123456" ,RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test?" + org.frameworkset.spi.security.SecurityManager.USER_ACCOUNT_KEY+"=admin&" +org.frameworkset.spi.security.SecurityManager.USER_PASSWORD_KEY + "=123456"  );
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 10; i ++)
                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            long end = System.currentTimeMillis();
            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
            
            
            
    }
    
    @Test
    public void testWebserviceAuthenticatedFailedRPC()
    {
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test?" + org.frameworkset.spi.security.SecurityManager.USER_ACCOUNT_KEY+"=admin&" +org.frameworkset.spi.security.SecurityManager.USER_PASSWORD_KEY + "=12345622" ,RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test?" + org.frameworkset.spi.security.SecurityManager.USER_ACCOUNT_KEY+"=admin&" +org.frameworkset.spi.security.SecurityManager.USER_PASSWORD_KEY + "=12345622"  );
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 10; i ++)
                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            long end = System.currentTimeMillis();
            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
            
            
            
    }
    
    @Test
    public void testNoReturnWebserviceRPC()
    {
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test" ,RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 10; i ++)
                testInf.sayHello("尹标平");
            
            long end = System.currentTimeMillis();
            System.out.println("消耗时间：" + (end - start) / 1000 + "秒");
            
            
            
    }
    
    @Test
    public void testMutiThreadWebserviceRPC()
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
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test" ,RPCTestInf.class);
        for(int i = 0; i < 10; i ++)
        {
            Thread t = new Thread(new RunWebserviceRPC(i,testInf));
            t.start();
        }
            
            
    }
    
     class RunWebserviceRPC implements Runnable
    {
        int i = 0;
        RPCTestInf testInf;
        RunWebserviceRPC(int i,RPCTestInf testInf)
        {
        	this.testInf = testInf;
            this.i = i;
        }
        public void run()
        {
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(webservice::http://localhost:8080/WebRoot/cxfservices)/rpc.test");
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
    public void testMuticastWebserviceRPC()
    {
    	RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("org/frameworkset/spi/remote/manager-rpc-test.xml","(webservice::http://localhost:8080/WebRoot/cxfservices;" +
        		"http://localhost:8088/rpcws/cxfservices)/rpc.test" ,RPCTestInf.class);
//            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(webservice::http://localhost:8080/WebRoot/cxfservices;" +
//            		"http://localhost:8088/rpcws/cxfservices)/rpc.test");
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            Object ret = testInf.getCount();
            try {
				Object ret_8080 = ClientProxyContext.getWSRPCResult("http://localhost:8080/WebRoot/cxfservices", ret);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				Object ret_8088 = ClientProxyContext.getWSRPCResult("http://localhost:8088/WebRoot/cxfservices", ret);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //等价的调用方法
            
//            Object ret_8080 = context.getRPCResult("http://localhost:8080/WebRoot/cxfservices", ret,Target.BROADCAST_TYPE_WEBSERVICE);
//            Object ret_8088 = context.getRPCResult("http://localhost:8088/WebRoot/cxfservices", ret,Target.BROADCAST_TYPE_WEBSERVICE);
//            for(int i = 0; i < 100; i ++)
//                System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            
            
    }
    @Test
    public void testWebserviceLocal()
    {
//          RPCTestInf testInf = (RPCTestInf)context.getBeanObject("(127.0.0.1:12345)/rpc.test");
            RPCTestInf testInf = (RPCTestInf)context.getBeanObject("rpc.test");
            for(int i = 0; i < 100; i ++)
            System.out.println("testInf.getCount():" + i + " = "+testInf.getCount());
            
            
    }
    
    
 
    
    public static class test implements java.io.Serializable
    {
            
    }
}
