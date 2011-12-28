package org.frameworkset.spi.rpc;

public class RPCTestImpl implements RPCTest{
    public Object test()
    {
        System.out.println("rpc call");
        return new Integer(1);
    }

}
