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

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

/**
 * <p>Title: JMSReceiveTemplate.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-13 下午04:11:12
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSReceiveTemplate extends AbstractTemplate
{

   
    public JMSReceiveTemplate(ConnectionFactory connectionFactory,String destination)
            throws JMSException
    {
        super(connectionFactory,destination);
        // TODO Auto-generated constructor stub
    } 
    

   
    public JMSReceiveTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid) throws JMSException
    {
        super(connectionFactory, transactioned, destinationType,  destination, persistent, prior, timeToLive,clientid);
        // TODO Auto-generated constructor stub
    }
    
    public JMSReceiveTemplate(ConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid) throws JMSException
    {
        super(connectionFactory, transactioned, destinationType,  destination, persistent, prior, timeToLive,clientid);
        // TODO Auto-generated constructor stub
    }
    public JMSReceiveTemplate(ConnectionFactory connectionFactory) throws JMSException
    {
        super(connectionFactory);
        // TODO Auto-generated constructor stub
    }

    public JMSReceiveTemplate(JMSConnectionFactory connectionFactory, String destination) throws JMSException
    {
        super(connectionFactory, destination);
        // TODO Auto-generated constructor stub
    }
    
    public JMSReceiveTemplate(String clientid,JMSConnectionFactory connectionFactory) throws JMSException
    {
    	//AbstractTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
//        int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid)
    	this( connectionFactory,false,
    	      MQUtil.TYPE_TOPIC,null,false,4,0,clientid);
        // TODO Auto-generated constructor stub
    }
    
    public JMSReceiveTemplate(String clientid,boolean persisent,JMSConnectionFactory connectionFactory) throws JMSException
    {
    	//AbstractTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
//        int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid)
    	this( connectionFactory,false,
    	      MQUtil.TYPE_TOPIC,null,persisent,4,0,clientid);
        // TODO Auto-generated constructor stub
    }
    
    public JMSReceiveTemplate(String clientid,boolean persisent,int prior,long timetolive,JMSConnectionFactory connectionFactory) throws JMSException
    {
    	//AbstractTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
//        int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid)
    	this( connectionFactory,false,
    	      MQUtil.TYPE_TOPIC,null,persisent,prior,timetolive,clientid);
        // TODO Auto-generated constructor stub
    }

    public JMSReceiveTemplate(JMSConnectionFactory connectionFactory) throws JMSException
    {
        super(connectionFactory);
        // TODO Auto-generated constructor stub
    }
    
    
    
    
    public TopicSubscriber getTopicSubscriber(String destination, String name) throws JMSException
    {
        return getTopicSubscriberWithSelector(destination, name, null);
    }
    
    public void subscribeTopic(String destination, String name,MessageListener listener) throws JMSException
    {
        getTopicSubscriberWithSelector(destination, name, null).setMessageListener(listener);
    }
    public void subscribeTopicWithSelector(String destination, String name,String selector,MessageListener listener) throws JMSException
    {
        getTopicSubscriberWithSelector(destination, name, selector).setMessageListener(listener);
    }
    
    public void subscribeTopicWithSelector(String name,String selector,MessageListener listener) throws JMSException
    {
        this.requestDispatcher.getTopicSubscriberWithSelector(this.destination,name, selector).setMessageListener(listener);
    }
    public TopicSubscriber getTopicSubscriber(Topic destination, String name) throws JMSException
    {
        return getTopicSubscriber(destination, name, null);
    }
    public TopicSubscriber getTopicSubscriber(Topic destination, String name, String selector) throws JMSException
    {
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection);
            TopicSubscriber sub = dispatcher.getTopicSubscriberWithSelector(destination, name, selector);
            this.tempdispatcher.add(dispatcher);
            return sub;
        }
        catch(JMSException e)
        {
            if(dispatcher != null)
                dispatcher.stop();
            throw e;
        }
//        return this.requestDispatcher.getTopicSubscriber(destination, name, selector);
    }
    public TopicSubscriber getTopicSubscriberWithSelector(String destination, String name, String selector)
    throws JMSException
    {
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection);
            TopicSubscriber sub = dispatcher.getTopicSubscriberWithSelector(destination, name, selector);
            this.tempdispatcher.add(dispatcher);
            return sub;
        }
        catch(JMSException e)
        {
            if(dispatcher != null)
                dispatcher.stop();
            throw e;
        }
        
        
    }



    
    

    public void unsubscribe(String unsubscribename) throws JMSException
    {
        this.requestDispatcher.unsubscribe(unsubscribename);
    }
   

    

}
