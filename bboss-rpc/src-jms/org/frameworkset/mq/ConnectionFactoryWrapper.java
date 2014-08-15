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
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

/**
 * <p>Title: ConnectionFactoryWrapper.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-13 下午01:53:07
 * @author biaoping.yin
 * @version 1.0
 */
public class ConnectionFactoryWrapper implements ConnectionFactory
{
    private ConnectionFactory factory;
    private JMSConnectionFactory jmsConnectionFactory;
    public ConnectionFactoryWrapper(ConnectionFactory factory,JMSConnectionFactory jmsConnectionFactory)
    {
        this.factory = factory;
        this.jmsConnectionFactory = jmsConnectionFactory;
    }
    public Connection createConnection() throws JMSException
    {   
        return new JMSConnection(this.factory.createConnection(),jmsConnectionFactory);
    }

    public Connection createConnection(String arg0, String arg1) throws JMSException
    {   
        return new JMSConnection(this.factory.createConnection(arg0,arg1),jmsConnectionFactory);
    }

}
