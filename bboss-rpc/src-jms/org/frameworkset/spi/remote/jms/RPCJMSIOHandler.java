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
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.frameworkset.mq.JMSConnectionFactory;
import org.frameworkset.mq.JMSTemplate;
import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.Header;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.serviceidentity.TargetImpl;

/**
 * <p>
 * Title: RPCJMSIOHandler.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-11-11 下午10:26:14
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCJMSIOHandler extends BaseRPCIOHandler implements org.frameworkset.spi.InitializingBean,org.frameworkset.spi.DisposableBean
{
    private static final Logger log = Logger.getLogger(RPCJMSIOHandler.class);

//    private javax.jms.Session session;

    private JMSConnectionFactory connectionFactory;

    /**
     * 监听远程请求消息的目标地址
     */
    private String destination;

    /**
     * 监听响应消息的目标地址
     */
    private String replyto;
    
    /**
     * 服务器标识，做为接收远程服务调用（请求和响应）的过滤条件
     * 
     * 
     */
    private String server_uuid ;
   

//    private RequestDispatcher dispatcher;
    private JMSTemplate request ;
    private JMSTemplate reply ;

    public RPCJMSIOHandler(String name, RequestHandler handler, JMSConnectionFactory connectionFactory,
            String destination, String replyto,String server_uuid)
    {

        super(name, handler, server_uuid);
        this.connectionFactory = connectionFactory;
        
        this.destination = destination;
        this.replyto = replyto;
        this.server_uuid = server_uuid;
        src_address = TargetImpl.buildTarget(this.server_uuid, Target.BROADCAST_TYPE_JMS);
    }

    /**
     * 为了确保服务器已经启动，执行该方法
     */
    private void enforceStartup()
    {
        if(this.jmsrpcstarted())
            return;
        try
        {
            start();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg, RPCAddress address)
    {
        enforceStartup();
        return new RPCJMSFuture(srcmsg,address,this,this.request,this.src_address);
    }
    public class InnerMessageListener extends org.frameworkset.mq.JMSMessageListener
    {

		public void onMessage(Message msg)
		{
			receiveMessage(msg);
		}
    	
    }
    public void receiveMessage(Message msg)
    {
    	if (msg instanceof TextMessage)
    	{
    		try
            {
    			TextMessage msg_ = (TextMessage)msg;
                RPCMessage rpcmsg = (RPCMessage) Util.getDecoder().decoder(msg_.getText());
//                javax.jms.Destination reply = msg.getJMSReplyTo();

                Header hdr = rpcmsg.getHeader(name);
                RPCMessage rsp = super.messageReceived(rpcmsg);
                switch (hdr.getType())
                {
                    case Header.REQ:
                        if (rsp != null)
                        {
                        	//回复消息
                            TextMessage response = this.reply.createTextMessage((String)Util.getEncoder().encoder(rsp));
                            RPCAddress src_address = rpcmsg.getSrc_addr();
                            response.setJMSCorrelationID(src_address.getServer_uuid());
//                            response.setObject(rsp);
                            this.reply.send(response);
                        }
                        break;
                    case Header.RSP:
                        break;
                    default:

                        break;
                }
            }
            catch (JMSException e)
            {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                log.error(e.getMessage(),e);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(),e);
            }
    	}
    	else if (msg instanceof ObjectMessage)
        {
            try
            {
                RPCMessage rpcmsg = (RPCMessage) (((ObjectMessage) msg).getObject());
//                javax.jms.Destination reply = msg.getJMSReplyTo();

                Header hdr = rpcmsg.getHeader(name);
                RPCMessage rsp = super.messageReceived(rpcmsg);
                switch (hdr.getType())
                {
                    case Header.REQ:
                        if (rsp != null)
                        {
                        	//回复消息
                            ObjectMessage response = this.reply.createObjectMessage();
                            RPCAddress src_address = rpcmsg.getSrc_addr();
                            response.setJMSCorrelationID(src_address.getServer_uuid());
                            response.setObject(rsp);
                            this.reply.send(response);
                        }
                        break;
                    case Header.RSP:
                        break;
                    default:

                        break;
                }
            }
            catch (JMSException e)
            {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                log.error(e.getMessage(),e);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(),e);
            }
        }
        else

        {
        	throw new java.lang.IllegalArgumentException("" + msg);
        }
        // TODO Auto-generated method stub

    }
    
    private boolean jms_rpc_started = false;
    
    public boolean jmsrpcstarted ()
    {
        return this.jms_rpc_started;
    }
    
    public synchronized void start() throws Exception
    {
        if(jms_rpc_started)
            return;
        
        
//        if(MQUtil.rpc_jms_enable)
        {
            System.out.println("启动jms 远程请求接收和响应接收队列开始......");
            
            this.request = new JMSTemplate(this.connectionFactory.getConectionFactory(),this.destination);
            InnerMessageListener requestListener = new InnerMessageListener();
            this.reply = new JMSTemplate(this.connectionFactory.getConectionFactory(),this.replyto);
            InnerMessageListener responseListener = new InnerMessageListener();
            request.getConsumerWithSelector("JMSCorrelationID='" + this.server_uuid + "'").setMessageListener(requestListener);
            System.out.println("启动jms 远程请求接收队列[rpc.request.queue="+ this.destination +"]完毕，JMSCorrelationID='" + this.server_uuid + "'");
            reply.getConsumerWithSelector("JMSCorrelationID='" + this.server_uuid + "'").setMessageListener(responseListener);
            System.out.println("启动jms 远程响应接收队列[rpc.reponse.queue="+ this.replyto +"]完毕，JMSCorrelationID='" + this.server_uuid + "'");
            
            this.jms_rpc_started = true; 
        }
    }
    
    public void stop() 
    {
        if(!this.jms_rpc_started )
            return;
        try
        {
            System.out.println("停止jms 远程请求接收队列[rpc.request.queue="+ this.destination +"]开始，JMSCorrelationID='" + this.server_uuid + "'");
            this.request.stop();
            
            System.out.println("停止jms 远程请求接收队列[rpc.request.queue="+ this.destination +"]完毕，JMSCorrelationID='" + this.server_uuid + "'");
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
        
        try
        {
            System.out.println("停止jms 远程响应接收队列[rpc.reponse.queue="+ this.replyto +"]开始，JMSCorrelationID='" + this.server_uuid + "'");
            this.reply.stop();
            System.out.println("停止jms 远程响应接收队列[rpc.reponse.queue="+ this.replyto +"]完毕，JMSCorrelationID='" + this.server_uuid + "'");
            
        }
        catch(Exception e)
        {
            log.error(e.getMessage(),e);
        }
        
        jms_rpc_started = false;
    }

    public void afterPropertiesSet() throws Exception
    {
        //do nothing
    	
//        dispatcher = connectionFactory.buildRequestDispatcher(destination, replyto);
//        dispatcher.setMessageListener(this);

    }


    public void destroy() throws Exception
    {
        stop() ;
        
    }
    
    

}
