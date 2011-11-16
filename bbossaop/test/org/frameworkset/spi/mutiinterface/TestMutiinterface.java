package org.frameworkset.spi.mutiinterface;

import org.frameworkset.spi.ApplicationContext;
import org.junit.Test;

public class TestMutiinterface {
	static ApplicationContext context_provider = ApplicationContext.getApplicationContext("org/frameworkset/spi/mutiinterface/service-assemble.xml");
    @Test
    public void testProvider()
    {
    	 
        AI rpc = (AI)context_provider
        .getProvider("mutiinfservice");
        System.out.println("rpc.testAI():" + rpc.testAI());
        System.out.println("rpc.testBaseAI():" + rpc.testBaseAI());
        AnotherAI arpc = (AnotherAI)context_provider
        .getProvider("mutiinfservice");
        System.out.println("arpc.testAnotherAI():" + arpc.testAnotherAI());
        System.out.println("arpc.testBaseAI():" + arpc.testBaseAI());
        BaseAI brpc = (BaseAI)context_provider
        .getProvider("mutiinfservice");
        
        System.out.println("brpc.testBaseAI():" + brpc.testBaseAI());
    }
    static ApplicationContext context_bean = ApplicationContext.getApplicationContext("org/frameworkset/spi/mutiinterface/service-bean-assemble.xml");
    @Test
    public void testProperty()
    {
    	 
        AI rpc = (AI)context_bean
        .getBeanObject("(rmi::172.16.17.216:1099)/mutiinfservice");
//        AI rpc = (AI)context_bean
//        .getBeanObject("(rmi::172.16.17.216:1099)/mutiinfservice");
        System.out.println("rpc.testAI():" + rpc.testAI());
        System.out.println("rpc.testBaseAI():" + rpc.testBaseAI());
        AnotherAI arpc = (AnotherAI)rpc;
        System.out.println("arpc.testAnotherAI():" + arpc.testAnotherAI());
        System.out.println("arpc.testBaseAI():" + arpc.testBaseAI());
        BaseAI brpc = (BaseAI)rpc;
        
        System.out.println("brpc.testBaseAI():" + brpc.testBaseAI());
    }
    
    public static void main(String args[])
    {
    	TestMutiinterface test = new TestMutiinterface();
    	long start = System.currentTimeMillis();
    	for(int i = 0; i < 1000 ; i ++)
    	{
    		test.testProperty();
    	}
    	long end = System.currentTimeMillis();
    	
    	
    	
    	long start1 = System.currentTimeMillis();
    	for(int i = 0; i < 1000 ; i ++)
    	{
    		test.testProperty();
    	}
    	
    	
    	long end1 = System.currentTimeMillis();
    	long start2 = System.currentTimeMillis();
    	for(int i = 0; i < 1000 ; i ++)
    	{
    		test.testProperty();
    	}
    	
    	
    	long end2 = System.currentTimeMillis();
    	System.out.println("ºÄÊ±:"+(end - start));
    	System.out.println("ºÄÊ± 1:"+(end1 - start1));
    	System.out.println("ºÄÊ± 2:"+(end2 - start2));
    }

}
