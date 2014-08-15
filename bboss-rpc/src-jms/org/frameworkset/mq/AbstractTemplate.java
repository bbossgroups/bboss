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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.frameworkset.log.Logger;

/**
 * <p>Title: AbstractTemplate.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-13 下午04:14:43
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractTemplate implements org.frameworkset.spi.DisposableBean
{
//    protected boolean transacted = false;

//    protected int destinationType = MQUtil.TYPE_QUEUE;

//    protected String requestMessageSelector;
    
//    protected String responseMessageSelector;
    protected int destinationType;
    protected  String destination;
    
    protected  int prior;
    protected long timeToLive = 0;
    

    protected String clientid;

    

    protected ConnectionFactory connectionFactory;

    protected Connection connection;
    
    protected RequestDispatcher requestDispatcher;   
    
    
    
//    protected RequestDispatcher responseDispatcher;

//  protected String  replyto;

//    protected int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;

    protected boolean persistent;
    protected List<ReceiveDispatcher> tempdispatcher = new ArrayList<ReceiveDispatcher>();
    
    public AbstractTemplate(JMSConnectionFactory connectionFactory) throws JMSException
    {
        this(connectionFactory ,null);   
        
    }
    
    public AbstractTemplate(JMSConnectionFactory connectionFactory,String destination) throws JMSException
    {
        this(connectionFactory,false,
                MQUtil.TYPE_QUEUE, destination,false,4,0,null);
    }
    
    public AbstractTemplate(ConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid) throws JMSException
    {
        this.destinationType = destinationType;
        
        this.destination = destination;
        this.clientid = clientid;
        this.persistent = persistent;
        this.prior = prior;
        this.timeToLive = timeToLive; 
        
//        this.replyto = replyto;
       
//        this.responseMessageSelector = responseMessageSelector;
        
        if(connectionFactory instanceof ConnectionFactoryWrapper)
            this.connectionFactory = connectionFactory;
        else
            this.connectionFactory = new ConnectionFactoryWrapper(connectionFactory,null);
        
        connection = this.connectionFactory.createConnection();
        if (this.clientid != null && !this.clientid.equals(""))
            this.connection.setClientID(clientid);
        connection.start();        
//      if(this.destination != null)
        this.requestDispatcher = new RequestDispatcher(this.connection,transactioned,Session.AUTO_ACKNOWLEDGE,this.destinationType,this.destination,this.persistent,this.prior,this.timeToLive);
    }
    
    public AbstractTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive,String clientid) throws JMSException
    {
       
        this(connectionFactory.getConectionFactory(),transactioned,
                destinationType, destination,persistent,prior,timeToLive,clientid);
    }
    
    
    public AbstractTemplate(ConnectionFactory connectionFactory) throws JMSException
    {
        this(connectionFactory ,null);
    }
    
    public AbstractTemplate(ConnectionFactory connectionFactory, String destination) throws JMSException
    {
        this(connectionFactory,false,
                MQUtil.TYPE_QUEUE, destination,false,4,0,null);
    }
        
    
   
    
    public void destroy() throws Exception
    {
        this.stop();
        
        
    }
    
    public void stop()
    {
        if(this.tempdispatcher.size() > 0)
        {
            for(ReceiveDispatcher dispatcher:this.tempdispatcher)
            {
                try
                {
                    dispatcher.stop();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        if(this.requestDispatcher != null)
        {
            this.requestDispatcher.stop();
        }
//      if(this.responseDispatcher != null)
//      {
//          this.responseDispatcher.stop();
//      }
        if(this.connection != null)
            try
        {
            connection.stop();
         
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
//            try
//            {
//                
//                connection.setClientID(null);
//            }
//            catch(Exception e)
//            {
//                e.printStackTrace();
//            }
            
            try
            {              
                
                this.connection.close();
            }
            catch (JMSException e)
            {
                e.printStackTrace();
            }
    }
    
    public javax.jms.Message receiveNoWait(String destination) throws javax.jms.JMSException
    {
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection,destination);
            Message msg =   dispatcher.receiveNoWait();
            return msg;
        }
        finally
        {
            if(dispatcher != null)
            {
                dispatcher.stop();
            }
//          dispatcher.stop();
        }
    }
    
    public javax.jms.Message receive() throws javax.jms.JMSException
    {
        
        
        return this.requestDispatcher.receive();
    }
    public javax.jms.Message receive(long timeout) throws javax.jms.JMSException
    {
        return this.requestDispatcher.receive(timeout);
    }
    
    public javax.jms.Message receiveNoWait() throws javax.jms.JMSException
    {
        return this.requestDispatcher.receiveNoWait();
    }
    
    public javax.jms.Message receive(String destination) throws javax.jms.JMSException
    {
        
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection,destination);
            Message msg = dispatcher.receive();
            return msg;
        }
        finally
        {
            if(dispatcher != null)
            {
                dispatcher.stop();
            }
//          dispatcher.stop();
        }
        
    }
    public javax.jms.Message receive(String destination,long timeout) throws javax.jms.JMSException
    {
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection,destination);
            Message msg =  dispatcher.receive(timeout);
            return msg;
        }
        finally
        {
            if(dispatcher != null)
            {
                dispatcher.stop();
            }
        }
    }
    
    public BytesMessage createBytesMessage() throws JMSException
    {
        return this.requestDispatcher.createBytesMessage();

    }

    public ObjectMessage createObjectMessage() throws JMSException
    {
        return this.requestDispatcher.createObjectMessage();

    }
    
    

    public ObjectMessage createObjectMessage(java.io.Serializable object) throws JMSException
    {
       return this.requestDispatcher.createObjectMessage(object);

    }
    

    public  TextMessage createTextMessage() throws JMSException
    {
        return this.requestDispatcher.createTextMessage();

    }

    public  TextMessage createTextMessage(String msg) throws JMSException
    {
        return this.requestDispatcher.createTextMessage(msg);

    }

    public  MapMessage createMapMessage() throws JMSException
    {
        return this.requestDispatcher.createMapMessage();

    }

    public  StreamMessage createStreamMessage() throws JMSException
    {
        return this.requestDispatcher.createStreamMessage();

    }
    
    public MessageConsumer getConsumer() throws JMSException
    {
        return this.requestDispatcher.getConsumer();
    }
    public MessageConsumer getConsumer(Destination destination) throws JMSException
    {
        return requestDispatcher.getConsumer(destination);
    }
    public MessageConsumer getConsumer(Destination destination, String messageSelector, boolean noLocal)
    throws JMSException
    {
        return this.requestDispatcher.getConsumer(destination, messageSelector, noLocal);
    }
    public MessageConsumer getConsumer(int destinationType, String destination_) throws JMSException
    {
        return this.requestDispatcher.getConsumer(destinationType,destination);
    }
    public MessageConsumer getConsumer(int destinationType, String destination_, String messageSelector)
    throws JMSException
    {
    
            return getConsumer(destinationType, destination_, messageSelector, false);
    }
    public MessageConsumer getConsumer(int destinationType, String destination_, String messageSelector, boolean noLocal)
    throws JMSException
    {
        return this.requestDispatcher.getConsumer(destinationType, destination_, messageSelector);
    }
    public MessageConsumer getConsumerWithSelector(String selector) throws JMSException
    {

        return this.requestDispatcher.getConsumerWithSelector(selector);
    }
    
    public javax.jms.MessageListener getMessageListener() throws javax.jms.JMSException
    {
       
        return this.requestDispatcher.getMessageListener();
    }
    
    public java.lang.String getMessageSelector() throws javax.jms.JMSException
    {
//        assertConsumerNull();
        return requestDispatcher.getMessageSelector();
    }
    
    // Method descriptor #10 (Ljavax/jms/MessageListener;)V
    public void setMessageListener(javax.jms.MessageListener listener) throws javax.jms.JMSException
    {
        this.requestDispatcher.setMessageListener(listener);
    }
    
    
    public void setMessageListener(String destination,javax.jms.MessageListener listener) throws javax.jms.JMSException
    {
        ReceiveDispatcher dispatcher = null;
        try
        {
            dispatcher = new ReceiveDispatcher(this.connection,destination);
            if(listener instanceof JMSMessageListener)
            {
                JMSMessageListener temp = (JMSMessageListener)listener;
                temp.setReceivor(dispatcher);
            }
            dispatcher.setMessageListener(listener);
            
            tempdispatcher.add(dispatcher);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
        
    }
    
    public void receive(String destination,javax.jms.MessageListener listener) throws javax.jms.JMSException
    {
    	setMessageListener( destination, listener);
        
    }
    public boolean isClientAcknowledge() throws JMSException
    {
        
        return this.requestDispatcher.isClientAcknowledge();
    }
    
    public void send(String destination,String message) throws JMSException
    {
        send(destination,message,false);
//      session.createProducer(arg0)
    }
    
    public void send(String destination,String message,boolean persistent) throws JMSException
    {
        send(MQUtil.TYPE_QUEUE,destination,message,persistent);
        
//      session.createProducer(arg0)
    }
    
    public void send(int desttype,String destination,String message) throws JMSException
    {
        send(desttype,destination,message,false);
//      session.createProducer(arg0)
    }
    
    public void send(int desttype,String destination,String message,boolean persistent) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection,false,Session.AUTO_ACKNOWLEDGE,desttype,destination,null,persistent);
            dispatcher.send(message,(JMSProperties)null);
        }
        finally
        {
            dispatcher.stop();
        }
//      session.createProducer(arg0)
    }
    
    public void send(int desttype,String destination,String message,boolean persistent,JMSProperties properties) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection,false,Session.AUTO_ACKNOWLEDGE,desttype,destination,null,persistent);
            dispatcher.send(message,properties);
        }
        finally
        {
            dispatcher.stop();
        }
//      session.createProducer(arg0)
    }
    
//    public void commitRequest() throws JMSException
//    {
//      if(this.requestDispatcher != null)
//          this.requestDispatcher.commit();
//    }
    
//    public void commitReply() throws JMSException
//    {
//      if(this.responseDispatcher != null)
//          this.responseDispatcher.commit();
//    }
    
    public void commit() throws JMSException
    {
        if(this.requestDispatcher != null)
            this.requestDispatcher.commit();
    }
//    public void rollbackRequest() throws JMSException
//    {
//      if(this.requestDispatcher != null)
//          this.requestDispatcher.rollback();
//    }
//    public void rollbackReply() throws JMSException
//    {
//      if(this.responseDispatcher != null)
//          this.responseDispatcher.rollback();
//    }
    
    public void rollback() throws JMSException
    {
        if(this.requestDispatcher != null)
            this.requestDispatcher.rollback();
    }    
    
    public void send(String msg) throws JMSException
    {
        this.requestDispatcher.send(msg,(JMSProperties)null);
    }
    
//    public void sendReply(String msg) throws JMSException
//    {
//      this.responseDispatcher.send(msg);
//    }
    
    public void send(Message msg) throws JMSException
    {
        this.requestDispatcher.send(msg,(JMSProperties)null);
    }
    
    public void send(String msg,JMSProperties properties) throws JMSException
    {
        this.requestDispatcher.send(msg, properties);
    }
    
//    public void sendReply(String msg) throws JMSException
//    {
//      this.responseDispatcher.send(msg);
//    }
    
    public void send(Message msg,JMSProperties properties) throws JMSException
    {
        this.requestDispatcher.send(msg,properties);
    }
    
//    public void sendReply(Message msg) throws JMSException
//    {
//      this.responseDispatcher.send(msg);
//    }
    
    public void send(InputStream in,JMSProperties properties) throws JMSException
    {
        this.requestDispatcher.send(in, properties);
    }
    
//    public void sendReply(InputStream in,JMSProperties properties) throws JMSException
//    {
//      this.responseDispatcher.send(in, properties);
//    }
    
    public void send(InputStream in,JMSProperties properties,Logger log) throws JMSException
    {
        this.requestDispatcher.send(in, properties,log);
    }
    
//    public void sendReply(InputStream in,JMSProperties properties,Logger log) throws JMSException
//    {
//      this.responseDispatcher.send(in, properties,log);
//    }
    
    public void send(Message msg,Logger logger) throws JMSException
    {
        this.requestDispatcher.send(msg, logger,(JMSProperties)null);
    }
    
    public void send(Message msg,Logger logger,JMSProperties properties) throws JMSException
    {
        this.requestDispatcher.send(msg, logger, properties);
    }
    
//    public void sendReply(Message msg,Logger logger) throws JMSException
//    {
//      this.responseDispatcher.send(msg,logger);
//    }
    
    
    
    
//    public void sendReply(Message msg,Logger logger) throws JMSException
//    {
//      this.responseDispatcher.send(msg,logger);
//    }
    

    public  void send(int destinationType, String destination_,boolean persistent,int priority, long timeToLive,Message message, Logger step) throws JMSException
    {

        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,priority, timeToLive,message, step,(JMSProperties )null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
        
    }
    
    public  void send(int destinationType, String destination_,
    		boolean persistent,int priority, long timeToLive,Message message, Logger step,JMSProperties properties) throws JMSException
    {

        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,priority, timeToLive,message, step,properties);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
        
    }
    
    public  void send(int destinationType, String destination_,boolean persistent,int priority, long timeToLive,Message message) throws JMSException
    {

        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,priority, timeToLive,message,(JMSProperties) null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    
    public  void send(int destinationType, String destination_,boolean persistent,int priority, long timeToLive,Message message,JMSProperties properties) throws JMSException
    {

        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,priority, timeToLive,message, properties);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }

    
    
    public  void send(int destinationType, String destination_, boolean persistent,Message message, Logger logger) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message,logger,(JMSProperties )null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public  void send(int destinationType, String destination_, boolean persistent,Message message, Logger logger,JMSProperties properties) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message,logger,properties);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public  void send(int destinationType, String destination_, boolean persistent,Message message) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message,(JMSProperties )null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public  void send(int destinationType, String destination_, boolean persistent,Message message,JMSProperties properties) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message, properties);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public  void send(int destinationType, String destination_, boolean persistent,String message) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message,(JMSProperties )null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public  void send(int destinationType, String destination_, boolean persistent,String message,JMSProperties properties) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,persistent,message, properties);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    
    public   void send(int destinationType, String destination_, Message message
          ,boolean persistent,
          int priority,
          long timeToLive) throws JMSException
    {
        RequestDispatcher dispatcher = null;
        try
        {
            dispatcher = new RequestDispatcher(this.connection);
            dispatcher.send(destinationType, destination_,  message
                              , persistent,
                               priority,
                               timeToLive,(JMSProperties )null);
        }
        finally
        {
            dispatcher = null;
//          dispatcher.stop();
        }
    }
    
    public   void send(int destinationType, String destination_, Message message
            ,boolean persistent,
            int priority,
            long timeToLive,JMSProperties properties) throws JMSException
      {
          RequestDispatcher dispatcher = null;
          try
          {
              dispatcher = new RequestDispatcher(this.connection);
              dispatcher.send(destinationType, destination_,  message
                                , persistent,
                                 priority,
                                 timeToLive, properties);
          }
          finally
          {
              dispatcher = null;
//            dispatcher.stop();
          }
      }
    
    public   void send(int destinationType, String destination_, String message
            ,boolean persistent,
            int priority,
            long timeToLive) throws JMSException
      {
          RequestDispatcher dispatcher = null;
          try
          {
              dispatcher = new RequestDispatcher(this.connection);
              dispatcher.send(destinationType, destination_
                                , persistent,
                                 priority,
                                 timeToLive,message,(JMSProperties )null);
          }
          finally
          {
//              dispatcher = null;
            dispatcher.stop();
          }
      }
    
    public   void send(int destinationType, String destination_, String message
            ,boolean persistent,
            int priority,
            long timeToLive,JMSProperties properties) throws JMSException
      {
          RequestDispatcher dispatcher = null;
          try
          {
              dispatcher = new RequestDispatcher(this.connection);
              dispatcher.send(destinationType, destination_
                                , persistent,
                                 priority,
                                 timeToLive,message, properties);
          }
          finally
          {
//              dispatcher = null;
            dispatcher.stop();
          }
      }
    
    public   void send(String destination_, String message
            ,boolean persistent,
            int priority,
            long timeToLive) throws JMSException
      {
        send(this.destinationType, destination_, message
                ,persistent,
                priority,
                timeToLive);
      }
}
