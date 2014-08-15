package org.frameworkset.spi.rpc;

import java.util.List;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.SPIException;
import org.frameworkset.spi.properties.injectbean.InjectServiceInf;
import org.frameworkset.spi.remote.RPCAddress;

public class Test {
    public static void testMutirpcCall() {
        try {
        	RPCTest rpc = ClientProxyContext.getApplicationClientBean("(172.16.17.51:1185; 172.16.17.56:1185)/event.remoteserivce",RPCTest.class);
//            RPCTest rpc = (RPCTest)BaseSPIManager
//                .getProvider("(172.16.17.51:1185; 172.16.17.56:1185)/event.remoteserivce");
            // RPCTest rpc = (RPCTest)BaseSPIManager.getProvider("managerid");
            // RPCTest rpc =
            // (RPCTest)BaseSPIManager.getProvider("(_self)/managerid");
            // RPCTest rpc1 = (RPCTest)BaseSPIManager.getProvider(
            // "(172.16.17.52: 1185;172.16.17.56: 1185)/managerid ");
            // RPCTest rpc2 =
            // (RPCTest)BaseSPIManager.getProvider("(all)/managerid");
            long s = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
                Object object = rpc.test();
                System.out.println(ClientProxyContext.getRPCResult("172.16.17.51", "1185", object));
                try {
					System.out.println(ClientProxyContext.getRPCResult("172.16.17.56", "1185", object));
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
            long e = System.currentTimeMillis();

            System.out.println((e - s) / 1000 + "秒");
            
        } catch (SPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void testGetAllNodes()
    {
        List<RPCAddress> addrs = ClientProxyContext.getAllNodes();
    }

    public static void testsinglerpcCall() {
        try {
//            RPCTest rpc = (RPCTest)BaseSPIManager.getProvider("(192.168.11.102:1186)/event.remoteserivce");
            RPCTest rpc = ClientProxyContext.getApplicationClientBean("(192.168.11.102:1186)/event.remoteserivce", RPCTest.class);
//            long s = System.currentTimeMillis();

            for (int i = 0; i < 1; i++) {
                Integer object = (Integer)rpc.test();
               
//                System.out.println(object);
                
            }
        	
//            long e = System.currentTimeMillis();

//            System.out.println((e - s) / 1000 + "秒");
           
        } catch (SPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void testProPertyesinglerpcCall() {
        try {
//            InjectServiceInf rpc = (InjectServiceInf)BaseSPIManager.getBeanObject("(192.168.11.102:1186)/inject.c.injectbean");
            InjectServiceInf rpc = ClientProxyContext.getApplicationClientBean("(192.168.11.102:1186)/inject.c.injectbean", InjectServiceInf.class);
//            long s = System.currentTimeMillis();

            for (int i = 0; i < 1; i++) {
                String object = (String)rpc.getRefattr();
               
                System.out.println(object);
                
            }
               
//            long e = System.currentTimeMillis();

//            System.out.println((e - s) / 1000 + "秒");
           
        } catch (SPIException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void mutiThreadTest()
    {
    	for(int j = 0; j < 20; j ++)
    	{
    		Worker in = new Worker();
    		in.start();
    		
//    		i ++;
    		
    	}
    	System.out.println("start i = " + i);
    	start = System.currentTimeMillis();
    	synchronized(lock)
    	{
    		lock.notifyAll();
    	}
    	while(i != 0)
    	{
    		synchronized(lock)
        	{
        		try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
    		
    	}
    	System.out.println("i=:" + i);
    	end = System.currentTimeMillis();
        System.out.println("total times:" + (end - start) / 1000 + "秒");
    }
    static Object lock = new Object(); 
    static int i = 20;
    static long start = 0l;
    static long end = 0l;
    static class Worker extends Thread
    {
    	public void run()
    	{
//    		while(i != 19)
    			synchronized(lock)
    			{
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		testsinglerpcCall();
    		synchronized(lock)
    		{
    			i --;
    			lock.notifyAll();
    		}
    	}
    }
    public static void testAllrpcCall() {
        try {
//            RPCTest rpc = (RPCTest)BaseSPIManager.getProvider("(all)/managerid");
            RPCTest rpc = ClientProxyContext.getApplicationClientBean("(all)/managerid", RPCTest.class);
            long s = System.currentTimeMillis();

            for (int i = 0; i < 1; i++) {
                Object object = rpc.test();
                try {
					System.out.println(ClientProxyContext.getRPCResult("172.16.17.51", "1185", object));
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					System.out.println(ClientProxyContext.getRPCResult("172.16.17.52", "1185", object));
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                try {
					System.out.println(ClientProxyContext.getRPCResult("172.16.17.56", "1185", object));
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            
            }
            long e = System.currentTimeMillis();

            System.out.println((e - s) / 1000 + "秒");
           
        } catch (SPIException e) {
          
            e.printStackTrace();
        }
    }
    
    public static void main(String args[])
    {
//        Test.testAllrpcCall();
//        Test.testMutirpcCall();
//        Test.testsinglerpcCall();
    	testsinglerpcCall();
    	testProPertyesinglerpcCall();
        
//    	mutiThreadTest();
    }

}
