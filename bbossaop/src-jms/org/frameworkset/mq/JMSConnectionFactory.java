/**
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

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * <p>
 * JMSConnectionFactory.java
 * </p>
 * <p>
 * Description: 用来获取各种不同类型的JMS 服务器的连接工厂类,默认的连接工厂提供商为activemq
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2009-11-15
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class JMSConnectionFactory implements org.frameworkset.spi.InitializingBean
{
    protected String connectURI;

    protected String username;

    protected String password;

    protected ConnectionFactory conectionFactory;

    /**
     * 是否采用连接池
     */
    protected boolean usepool = false;

    public ConnectionFactory getConectionFactory()
    {

        return conectionFactory;
    }

    public static String JMSProvider_ACTIVEMQ = "CMQ";
    public static String JMSProvider_CMQ = JMSProvider_ACTIVEMQ;
    public static String JMSProvider_TLQ = "TLQ";
    public static String JMSProvider_OPENJMS = "openjms";

    protected String JMSProvider = JMSProvider_ACTIVEMQ;
    
    public static String MQProvider_name = "MQProvider";

    protected Map extparams = null;

    public JMSConnectionFactory(String connectURI, String username, String password, String JMSProvider,
            boolean usepool, Map extparams)
    {
        this.connectURI = connectURI;
        this.username = username;
        this.password = password;
        this.extparams = extparams;
        this.JMSProvider = JMSProvider == null || JMSProvider.equals("") ? JMSProvider_ACTIVEMQ : JMSProvider;
        this.usepool = usepool;
    }

    public JMSConnectionFactory(String connectURI, String username, String password, String JMSProvider, boolean usepool)
    {
        this(connectURI, username, password, JMSProvider, usepool, null);
    }

    public JMSConnectionFactory(String connectURI, String username, String password, boolean usepool)
    {
        this(connectURI, username, password, null, usepool, null);
    }

    public JMSConnectionFactory(String connectURI, String username, String password)
    {
        this(connectURI, username, password, null, false, null);
    }

    public JMSConnectionFactory(String connectURI, boolean usepool)
    {
        this(connectURI, null, null, null, usepool, null);
    }

    public JMSConnectionFactory(String connectURI)
    {
        this(connectURI, null, null, null, false, null);
    }
    public JMSConnectionFactory()
    {
        
    }

    // public void afterPropertiesSet() throws Exception
    // {
    // if(this.JMSProvider.equals(JMSProvider_ACTIVEMQ))
    // {
    // conectionFactory = new
    // ActiveMQConnectionFactory(username,password,connectURI);
    // BeanAccembleHelper.injectProperties(conectionFactory, extparams);
    //			
    // }
    // }

    public Connection getConnection() throws JMSException
    {
        return new JMSConnection(this.conectionFactory.createConnection(), this);
    }

    // public ReceiveDispatcher buildReceiveDispatcher(String destination,String
    // replyto) throws JMSException
    // {
    // ReceiveDispatcher dispather = new
    // ReceiveDispatcher(this.conectionFactory,false,Session.AUTO_ACKNOWLEDGE,destination,replyto);
    // return dispather;
    // }
    //
    // public RequestDispatcher buildRequestDispatcher(String destination,
    // String replyto) throws JMSException
    // {
    //
    // RequestDispatcher dispather = new
    // RequestDispatcher(this.conectionFactory,false,Session.AUTO_ACKNOWLEDGE,destination,replyto);
    // return dispather;
    // }

    public void afterPropertiesSet() throws Exception
    {
        if(conectionFactory != null)
            return;
        synchronized(this)
        {
            if(conectionFactory == null)
                this.conectionFactory = new ConnectionFactoryWrapper(buildConnectionFactory(),this);
        }
    }

    /**
     * 构建特定提供商的连接工厂
     * 
     * @return
     */
    public abstract ConnectionFactory buildConnectionFactory() throws Exception;

    public Destination createDestination(Session session, String name, int defaultType) throws JMSException
    {
        return MQUtil.createDestination(session, name, defaultType);
    }

    // /**
    // * 根据节点名称识别节点类型，如果节点名称中不包含节点类型信息，则返回默认类型defaultType
    // * @param destName
    // * @param defaultType
    // * @return
    // */
    // public int evaluateDestinationType(String destName,int defaultType)
    // {
    // if(destName == null)
    // return defaultType;
    // if (destName.startsWith(QUEUE_QUALIFIED_PREFIX)) {
    // return QUEUE_TYPE;
    // } else if (destName.startsWith(TOPIC_QUALIFIED_PREFIX)) {
    // return TOPIC_TYPE;
    // } else if (destName.startsWith(TEMP_QUEUE_QUALIFED_PREFIX)) {
    // return TEMP_QUEUE_TYPE;
    // } else if (destName.startsWith(TEMP_TOPIC_QUALIFED_PREFIX)) {
    // return TEMP_TOPIC_TYPE;
    // }
    // else
    // return defaultType;
    // }

    // /**
    // * 根据节点名称识别节点类型，如果节点名称中不包含节点类型信息，则返回默认类型defaultType
    // * @param destName
    // * @param defaultType
    // * @return
    // */
    // public String evaluateDestination(String name)
    // {
    // if(name == null)
    // return name;
    // if (name.startsWith(QUEUE_QUALIFIED_PREFIX)) {
    // return name.substring(QUEUE_QUALIFIED_PREFIX.length());
    // } else if (name.startsWith(TOPIC_QUALIFIED_PREFIX)) {
    // return name.substring(TOPIC_QUALIFIED_PREFIX.length());
    // } else if (name.startsWith(TEMP_QUEUE_QUALIFED_PREFIX)) {
    // return name.substring(TEMP_QUEUE_QUALIFED_PREFIX.length());
    // } else if (name.startsWith(TEMP_TOPIC_QUALIFED_PREFIX)) {
    // return name.substring(TOPIC_QUALIFIED_PREFIX.length());
    // }
    // return name;
    // }
    //	
    public static void main(String[] args)
    {
        System.out.println("QUEUE_TYPE:" + MQUtil.TYPE_QUEUE);
        System.out.println("TOPIC_TYPE:" + MQUtil.TYPE_TOPIC);
        System.out.println("TEMP_TOPIC_TYPE:" + MQUtil.TYPE_TEMP_TOPIC);
        System.out.println("TEMP_QUEUE_TYPE:" + MQUtil.TYPE_TEMP_QUEUE);

    }

}
