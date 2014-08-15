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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.frameworkset.mq.JMSReceiveTemplate;
import org.frameworkset.mq.JMSTemplate;
import org.frameworkset.mq.MQUtil;
import org.frameworkset.spi.ApplicationContext;
import org.junit.Test;

/**
 * <p>Title: TestTlq.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-8 下午05:25:36
 * @author biaoping.yin
 * @version 1.0
 */
public class TestTlq {
	@Test
	public void sendMsg()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSTemplate template = (JMSTemplate)context.getBeanObject("test.jmstemplate");
        try
        {
            template.send("testQueue1", "ahello", false, 4, 1000);
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            template.stop();
        }
	}
	
	
	@Test
	public void sendTopicMsg()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSTemplate template = (JMSTemplate)context.getBeanObject("test.jmstemplate");
        try
        {
            template.send(MQUtil.TYPE_TOPIC,"JMSExam", "ahello topic.",false, 4, 1000);
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            template.stop();
        }
	}
	
	
	@Test
	public void receiveMsg()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSTemplate template = (JMSTemplate)context.getBeanObject("test.jmstemplate");
        try
        {
            template.receive("testQueue", new MessageListener() {

                public void onMessage(Message arg0)
                {
                    System.out.println("queue msg comming:"+arg0);
                }                
            });
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
	}
	
	@Test
	public void receiveTQMsg()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSTemplate template = (JMSTemplate)context.getBeanObject("test.jmstemplate");
        try
        {
            template.receive("testQueue11", new MessageListener() {

                public void onMessage(Message arg0)
                {
                    System.out.println("queue msg comming:"+arg0);
                }                
            });
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
	}
	
	
	@Test
	public void receiveTopicMsg()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/remote/jms/tlq_jms.xml");
		JMSReceiveTemplate template = (JMSReceiveTemplate)context.getBeanObject("test.topic.jmstemplate");
        try
        {
            template.subscribeTopic("JMSExam", "test",new MessageListener() {

                public void onMessage(Message arg0)
                {
                    System.out.println("topic msg comming:"+arg0);
                }                
            });
            
        }
        catch (JMSException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
	}
	
	public static void main(String[] args)
	{
		TestTlq test = new TestTlq();
//		test.receiveTopicMsg();
//		test.receiveMsg();
		test.receiveTQMsg();
	}
	
	

}
