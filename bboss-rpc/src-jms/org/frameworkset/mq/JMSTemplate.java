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

/**
 * <p>
 * Title: JMSTemplate.java
 * </p>
 * <p>
 * Description: JMS模板方法
 * 包含
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-11-19 下午01:32:48
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSTemplate extends AbstractTemplate
{ 

    public JMSTemplate(ConnectionFactory connectionFactory, String destination) throws JMSException
    {
        super(connectionFactory,destination);
    }
    
    public JMSTemplate(JMSConnectionFactory connectionFactory, String destination) throws JMSException
    {
        super(connectionFactory,destination);
    }
 
    

    

    public JMSTemplate(JMSConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive) throws JMSException
    {
        super(connectionFactory, transactioned, destinationType,  destination, persistent, prior, timeToLive,null);
        // TODO Auto-generated constructor stub
    }
    
    public JMSTemplate(ConnectionFactory connectionFactory,boolean transactioned,
            int destinationType, String destination,boolean persistent,int prior,long timeToLive) throws JMSException
    {
        super(connectionFactory,transactioned,  destinationType,  destination, persistent, prior, timeToLive,null);
        // TODO Auto-generated constructor stub
    }  

    

    public JMSTemplate(ConnectionFactory connectionFactory) throws JMSException
    {
        super(connectionFactory);
        // TODO Auto-generated constructor stub
    }

    public JMSTemplate(JMSConnectionFactory connectionFactory) throws JMSException
    {
        super(connectionFactory);
        // TODO Auto-generated constructor stub
    }

    
    
    
    
    
    
    
 // Method descriptor #12 ()Ljavax/jms/Message;
   
    
    

   
    
    
}
