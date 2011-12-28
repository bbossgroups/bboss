package com.frameworkset.proxy;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TestProxyImpl implements TestProxy{
    private String string;
    public static void main(String[] args) {
        TestProxyImpl testproxyimpl = new TestProxyImpl();
        Object test = ProxyFactory.createProxy(new TestInvocationHandler(testproxyimpl));
        //System.out.println("test:"+test);
        TestProxy proxy = (TestProxy)test;
        try {
            System.out.println(proxy.getString());
        } catch (Throwable ex) {
            ex.printStackTrace();
        }


    }

    public String getString() throws Throwable {
        return string == null?"hello":string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
