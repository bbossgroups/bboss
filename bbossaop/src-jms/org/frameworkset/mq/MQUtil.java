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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.frameworkset.spi.BaseSPIManager;

/**
 * <p>Title: MQUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-16 上午10:06:03
 * @author biaoping.yin
 * @version 1.0
 */
public class MQUtil
{
    public static final int TYPE_QUEUE = 0;
    public static final int TYPE_TOPIC = 1;
    
    public static final int TYPE_ROUTER = 2;
    
    
    public static final byte TEMP_MASK = 0x04;
    public static final byte TYPE_TEMP_TOPIC = TYPE_TOPIC | TEMP_MASK;
    public static final byte TYPE_TEMP_QUEUE = TYPE_QUEUE | TEMP_MASK;

    public static final String QUEUE_QUALIFIED_PREFIX = "queue://";
    public static final String TOPIC_QUALIFIED_PREFIX = "topic://";
    public static final String TEMP_QUEUE_QUALIFED_PREFIX = "temp-queue://";
    public static final String TEMP_TOPIC_QUALIFED_PREFIX = "temp-topic://";
    public static String getTypeDesc(int type)
    {
        switch (type)
        {
        case TYPE_TOPIC:
            return "TYPE_TOPIC";
        case TYPE_QUEUE:
            return "TYPE_QUEUE";
        case TYPE_ROUTER:
            return "TYPE_ROUTER";
        case TYPE_TEMP_TOPIC:
            return "TYPE_TEMP_TOPIC";
        case TYPE_TEMP_QUEUE:
            return "TYPE_TEMP_QUEUE";            
        default:
            return "TYPE_UNKNOWN";

        }
    }
    private static boolean ignore(String name)
    {
    	for(String header:jmsheaders)
    	{
	        if (name.equals(header) )
	            return true;
    	}
        return false;
    }
    
    private static boolean isjmsheader(String name)
    {
    	for(String header:jmsheaders)
    	{
	        if (name.equals(header) )
	            return true;
    	}
        return false;
    }
    public static final String JMSCorrelationID = "JMSCorrelationID";
    public static final String JMSMessageID = "JMSMessageID";
    public static final String JMSType = "JMSType";
    public static final String JMSReplyTo = "JMSReplyTo";
    public static final String JMSDestination = "JMSDestination";
    
    public static String[] jmsheaders = new String[]{JMSCorrelationID,JMSMessageID,JMSType,JMSReplyTo,JMSDestination};
    
    public static JMSProperties buildProperties(String keys[],String[] values)
    {
    	if(keys == null || keys.length <= 0)
    		return null;
    	JMSProperties properties = new JMSProperties();
    	HeaderProperty p = null;
    	for(int i = 0; i < keys.length; i ++)
    	{
    		p = new HeaderProperty();
    		p.setPRO_NAME(keys[i]);
    		p.setVALUE_string(values[i]);
    		p.setPRO_TYPE(HeaderProperty.protype_string);
    		properties.addHeader(p);
    	}
    	return properties;
    	
    	
    }
    public static void initMessage(Message msg, JMSProperties properties) throws JMSException
    {
        if (properties == null)
            return;
        if (properties.isCompress() && msg instanceof org.apache.activemq.command.Message)
        {
//            org.apache.activemq.command.Message msgamg = (org.apache.activemq.command.Message) msg;
//            msgamg.setCompressed(properties.isCompress());

        }
        else if(JMSConnectionFactory.JMSProvider_TLQ.equals(properties.getJmsProvider()))
        {
        	msg.setBooleanProperty("JMS_TLQ_Compressed",properties.isCompress());//兼容东方通的消息压缩，2010.6.10 
        }
        // msg.setBooleanProperty("isZip", properties.isCompress());
        // msg.setBooleanProperty("isEncrypt", properties.isEncrypt());

        if (properties.getJMSCorrelationID() != null)
            msg.setJMSCorrelationID(properties.getJMSCorrelationID());
        if (properties.getJMSMessageID() != null)
            msg.setJMSMessageID(properties.getJMSMessageID());
        if (properties.getJMSType() != null)
            msg.setJMSType(properties.getJMSType());

        if (properties.getJMSReplyTo() != null)
            msg.setJMSReplyTo(properties.getJMSReplyTo());
        
        if (properties.getListheaders() != null && properties.getListheaders().size() > 0)
        {
            for (HeaderProperty header : properties.getListheaders())
            {
                if (ignore(header.getPRO_NAME()))
                {
                    continue;
                }
                if (header.isString())
                {
                    msg.setStringProperty(header.getPRO_NAME(), header.getVALUE_string());
                }
                else if (header.isBoolean())
                {
                    msg.setBooleanProperty(header.getPRO_NAME(), header.isVALUE_BOOLEAN());
                }
                else if (header.isInteger())
                {
                    msg.setIntProperty(header.getPRO_NAME(), header.getVALUE_int());
                }
                else if (header.isObject())
                {
                    msg.setObjectProperty(header.getPRO_NAME(), header.getVALUE_OBJECT());
                }
                else if (header.isLong())
                {
                    msg.setLongProperty(header.getPRO_NAME(), header.getVALUE_long());
                }
                else if (header.isDouble())
                {
                    msg.setDoubleProperty(header.getPRO_NAME(), header.getVALUE_double());
                }
                else if (header.isFloat())
                {
                    msg.setFloatProperty(header.getPRO_NAME(), header.getVALUE_float());
                }
                else if (header.isString())
                {
                    msg.setStringProperty(header.getPRO_NAME(), header.getVALUE_string());
                }
            }
        }

    }

    public static byte[] readTxtFileByte(InputStream stFileInputStream) throws Exception
    {

        // FileInputStream stFileInputStream = null;

        java.io.ByteArrayOutputStream out = null;

        try
        {
            // File f = new File(fullpath);
            if (stFileInputStream == null)
            {
                System.out.println("File is not exist! ");
                return null;
            }

            // stFileInputStream = new FileInputStream(f);

            out = new ByteArrayOutputStream();

            int arraySize = 1024;
            byte buffer[] = new byte[arraySize];
            int bytesRead;
            while ((bytesRead = stFileInputStream.read(buffer)) != -1)
            {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            return out.toByteArray();

        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (stFileInputStream != null)
                try
                {
                    stFileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (out != null)
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }

    public static boolean readTxtFileByte(BytesMessage msg, InputStream stFileInputStream)
    {

        // FileInputStream stFileInputStream = null;

        try
        {
            // File f = new File(fullpath);
            if (stFileInputStream == null)
            {
                System.out.println("File is not exist! ");
                return false;
            }

            // stFileInputStream = new FileInputStream(f);

            int arraySize = 1024;
            byte buffer[] = new byte[arraySize];
            int bytesRead;
            boolean flag = false;
            while ((bytesRead = stFileInputStream.read(buffer)) != -1)
            {
                msg.writeBytes(buffer, 0, bytesRead);
                if (!flag)
                    flag = true;
            }
            return flag;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (stFileInputStream != null)
                try
                {
                    stFileInputStream.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }
    
    /**
     * 将kb，mb，gb转换成byte字节，支持的单位有：mb,m,gb,kb
     * @param maxFileLength
     * @return
     */
    public static long maxFileLengthChange(String maxFileLength) {
            long maxLength = 0;
            if (maxFileLength.toLowerCase().endsWith("mb")) {
                    int size = Integer.parseInt((maxFileLength.toLowerCase().substring(
                                    0, maxFileLength.toLowerCase().indexOf("mb"))).trim());
                    maxLength = 1024 * 1024 * size;

            } else if (maxFileLength.toLowerCase().endsWith("m")) {
                    int size = Integer.parseInt((maxFileLength.toLowerCase().substring(
                                    0, maxFileLength.toLowerCase().indexOf("m"))).trim());
                    maxLength = 1024 * 1024 * size;
            } else if (maxFileLength.toLowerCase().endsWith("gb")) {
                    int size = Integer.parseInt((maxFileLength.toLowerCase().substring(
                                    0, maxFileLength.toLowerCase().indexOf("gb"))).trim());
                    maxLength = 1024 * 1024 * 1024 * size;
            } else {
                    int size = Integer.parseInt((maxFileLength.toLowerCase().substring(
                                    0, maxFileLength.toLowerCase().indexOf("kb"))).trim());
                    maxLength = 1024 * size;
            }
            return maxLength;
    }
    
    /**
     *<!-- 
        是否启用rpc服务的jms协议
        true--启用，框架将启动接收服务调用的jms队列和接收响应的jms目标队列
     -->
     */
    public static boolean rpc_jms_enable = BaseSPIManager.getBooleanProperty("rpc.jms.enable",false);
    
    public static Destination createDestination(Session session, String name, int defaultType) throws JMSException
    {
        if (name == null || name.equals(""))
        {
           
            throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name="
                    + name);
           

        }
        if (name.startsWith(MQUtil.QUEUE_QUALIFIED_PREFIX))
        {
            return session.createQueue(name.substring(MQUtil.QUEUE_QUALIFIED_PREFIX.length()));
        }
        else if (name.startsWith(MQUtil.TOPIC_QUALIFIED_PREFIX))
        {
            return session.createTopic(name.substring(MQUtil.TOPIC_QUALIFIED_PREFIX.length()));
        }
        else if (name.startsWith(MQUtil.TEMP_QUEUE_QUALIFED_PREFIX))
        {
            throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name="
                    + name);
        }
        else if (name.startsWith(MQUtil.TEMP_TOPIC_QUALIFED_PREFIX))
        {
            throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name="
                    + name);
        }

        switch (defaultType)
        {
            case MQUtil.TYPE_QUEUE:
                return session.createQueue(name);
            case MQUtil.TYPE_TOPIC:
                return session.createTopic(name);
            case MQUtil.TYPE_TEMP_QUEUE:
                throw new IllegalArgumentException("Invalid default destination type: " + TYPE_TEMP_QUEUE + ",name="
                        + name);
            case MQUtil.TYPE_TEMP_TOPIC:
                throw new IllegalArgumentException("Invalid default destination type: " + TYPE_TEMP_TOPIC + ",name="
                        + name);
            default:
                throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name=" + name);
        }
    }
    
    
    
}
