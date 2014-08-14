package org.frameworkset.rpc;

import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.SPIException;
import org.frameworkset.spi.rpc.RPCTest;

public class TestUnicast {
    
    public static void testsinglerpcCall() {
        try {
            RPCTest rpc = ClientProxyContext.getApplicationClientBean("(192.168.11.102:1186)/event.remoteserivce",RPCTest.class);
          
//            long s = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++) {
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
    
    public static void main(String[] args)
    {
        testsinglerpcCall();
    }

}
