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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

/**
 * <p>Title: JMSMessageConsumer.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-18 ÉÏÎç11:33:20
 * @author biaoping.yin
 * @version 1.0
 */
public class JMSMessageConsumer implements MessageConsumer
{

    private MessageConsumer consumer;
    
            // private Session session;
            
            private ReceiveDispatcher receivor = null;
    
            public JMSMessageConsumer(MessageConsumer consumer,ReceiveDispatcher receivor)
            {
                this.consumer = consumer;               
                this.receivor = receivor;
            }
    
            public void close() throws JMSException
            {
    
                consumer.close();
    
            }
    
            public MessageListener getMessageListener() throws JMSException
            {
    
                return consumer.getMessageListener();
            }
    
            public String getMessageSelector() throws JMSException
            {
                return consumer.getMessageSelector();
    
            }
    
            public Message receive() throws JMSException
            {
    
                return consumer.receive();
            }
    
            public Message receive(long arg0) throws JMSException
            {
    
                return consumer.receive(arg0);
            }
    
            public Message receiveNoWait() throws JMSException
            {
    
                return consumer.receiveNoWait();
            }
    
            public void setMessageListener(MessageListener arg0) throws JMSException
            {
                if (arg0 instanceof JMSMessageListener)
                {
                    JMSMessageListener l = (JMSMessageListener) arg0;
                    
                    l.setReceivor(this.receivor);
                }
                this.consumer.setMessageListener(arg0);
            }

}
