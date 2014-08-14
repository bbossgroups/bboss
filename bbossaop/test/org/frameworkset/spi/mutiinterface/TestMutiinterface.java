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
    @Test
    public void testBean()
    {
    	 
        AI rpc = (AI)context_bean
        .getProvider("mutiinfservice");
        System.out.println("rpc.testAI():" + rpc.testAI());
        System.out.println("rpc.testBaseAI():" + rpc.testBaseAI());
        AnotherAI arpc = (AnotherAI)context_bean
        .getProvider("mutiinfservice");
        System.out.println("arpc.testAnotherAI():" + arpc.testAnotherAI());
        System.out.println("arpc.testBaseAI():" + arpc.testBaseAI());
        BaseAI brpc = (BaseAI)context_bean
        .getProvider("mutiinfservice");
        
        System.out.println("brpc.testBaseAI():" + brpc.testBaseAI());
    }
    static ApplicationContext context_bean = ApplicationContext.getApplicationContext("org/frameworkset/spi/mutiinterface/service-bean-assemble.xml");
    
   

}
