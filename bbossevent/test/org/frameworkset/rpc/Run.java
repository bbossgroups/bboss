package org.frameworkset.rpc;

import org.frameworkset.event.ExampleListener;
import org.frameworkset.spi.remote.RunAop;

public class Run {

    public static void main(String[] args)
    {
        ExampleListener listener = new ExampleListener();
        //调用init方法注册监听器，这样就能收到事件发布器发布的事件
        listener.init();
        RunAop.main(null);
    }
}
