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

package org.frameworkset.spi.remote.jms;

import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;

/**
 * <p>Title: JMSServer.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-24 ÉÏÎç10:29:37
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSServer
{
    private static JMSServer jmsServer;
    static 
    {
        jmsServer = new JMSServer();
    }
    private JMSServer()
    {
        
    }
    
    public static JMSServer getJMSServer()
    {
        return jmsServer;
    }
    
    public boolean started()
    {
        return ((RPCJMSIOHandler)Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS)).jmsrpcstarted();
    }
    
    public void stop()
    {
        ((RPCJMSIOHandler)Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS)).stop();
//        start = true;
    }
    public void start()
    {
//        try
//        {
//            ((RPCJMSIOHandler)Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS)).start();
//        }
//        catch (Exception e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        DeamonThread t = new DeamonThread();
        t.setDaemon(true);
        t.start();
        try
        {
            t.join();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
    
    public static class DeamonThread extends Thread
    {
        public void run()
        {
            boolean start = false;
            do
            {
                try
                {
                    ((RPCJMSIOHandler)Util.getRPCIOHandler(Target.BROADCAST_TYPE_JMS)).start();
                    start = true;
                }
                catch (Exception e)
                {
                    
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    synchronized(this)
                    {
                        try
                        {
                            wait(5000);
                        }
                        catch (InterruptedException e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }
            }
            while(!start);
        }
    }
    
}
