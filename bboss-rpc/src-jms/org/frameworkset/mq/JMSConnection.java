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

package org.frameworkset.mq;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * <p>Title: JMSConnection.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-13 ÉÏÎç10:48:45
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSConnection implements Connection
{
    private Connection con;
    private JMSConnectionFactory connectionFactory;
    public JMSConnection(Connection con,JMSConnectionFactory connectionFactory)
    {
        this.con = con;
        this.connectionFactory = connectionFactory; 
    }

    public void close() throws JMSException
    {
        
        con.close();
        
    }

    public ConnectionConsumer createConnectionConsumer(Destination arg0, String arg1, ServerSessionPool arg2, int arg3)
            throws JMSException
    {
        // TODO Auto-generated method stub
        return con.createConnectionConsumer(arg0, arg1, arg2, arg3);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic arg0, String arg1, String arg2,
            ServerSessionPool arg3, int arg4) throws JMSException
    {
        
        return con.createDurableConnectionConsumer(arg0, arg1, arg2, arg3, arg4);
    }

    public Session createSession(boolean arg0, int arg1) throws JMSException
    {
        // TODO Auto-generated method stub
        return con.createSession(arg0, arg1);
    }

    public String getClientID() throws JMSException
    {
        
        return con.getClientID();
    }

    public ExceptionListener getExceptionListener() throws JMSException
    {
        // TODO Auto-generated method stub
        return con.getExceptionListener();
    }

    public ConnectionMetaData getMetaData() throws JMSException
    {
        // TODO Auto-generated method stub
        return con.getMetaData();
    }

    public void setClientID(String arg0) throws JMSException
    {
        
        con.setClientID(arg0);

    }

    public void setExceptionListener(ExceptionListener arg0) throws JMSException
    {
        con.setExceptionListener(arg0);

    }

    public void start() throws JMSException
    {
        con.start();

    }

    public void stop() throws JMSException
    {
        con.stop();

    }
    
    public Destination createDestination(Session session,String destination,int defaultType) throws JMSException
    {
        if(connectionFactory != null)
            return connectionFactory.createDestination(session, destination, defaultType);
        else
        {
            return MQUtil.createDestination(session, destination, defaultType);
        }
    }
    
    public JMSConnectionFactory getJMSConnectionFactory()
    {
        return this.connectionFactory;
    }

}
