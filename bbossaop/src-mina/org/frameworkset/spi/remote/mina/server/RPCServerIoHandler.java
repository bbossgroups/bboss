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

package org.frameworkset.spi.remote.mina.server;


import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.Header;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.serviceidentity.TargetImpl;

/**
 * <p>
 * Title: RPCServerIoHandler.java
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
 * @Date 2009-10-7 下午05:08:18
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCServerIoHandler extends BaseRPCIOHandler implements RPCIOHandler, IoHandler
{
    
//    private String name;
//
//    public String getName()
//    {
//
//        return name;
//    }

    protected static final Logger log = Logger.getLogger(RPCServerIoHandler.class);

//    /**
//     * The table of pending requests (keys=Long (request IDs), values=
//     * <tt>RequestEntry</tt>)
//     */
//    protected final ConcurrentMap<Long, ResponseCollector> requests = new ConcurrentHashMap<Long, ResponseCollector>();
//
//    /**
//     * The handler for the incoming requests. It is called from inside the
//     * dispatcher thread
//     */
//    protected RequestHandler request_handler = null;
//
//    protected Marshaller2 marshaller = null;
    
    public RPCServerIoHandler(String name, RequestHandler handler)
    {
        super(name, handler, null);
        
        this.src_address = MinaRPCServer.getMinaRPCServer().getLocalAddress();
        // this.local_addr = local_addr;
    }

    public RPCServerIoHandler(String name, RequestHandler handler,String localaddress)
    {
        super(name, handler, localaddress);
        if(this.localaddress != null && !this.localaddress.equals(""))
            this.src_address = TargetImpl.buildTarget(this.localaddress,Target.BROADCAST_TYPE_MINA);
        // this.local_addr = local_addr;
    }

//    protected void handleRequest(RPCMessage req, Header hdr, IoSession session)
//    {
//        
//
//        RPCMessage rsp = super.handleRequest(req, hdr);
//        if (rsp != null)
//            session.write(rsp);
//        
//    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception
    {
        cause.printStackTrace();
        
    }

    

    public void messageReceived(IoSession session, Object message) throws Exception
    {
        // System.out.println(message);
        assertMessage(message);
//        RPCMessage message_ = (RPCMessage) message;
        RPCMessage message_ = (RPCMessage)Util.getDecoder().decoder(message);
        Header hdr = message_.getHeader(name);
        RPCMessage rsp = super.messageReceived(message_);
        switch (hdr.getType())
        {
            case Header.REQ:
                if (rsp != null)
                    session.write(rsp);
                break;
            case Header.RSP:                
                break;    
            default:
                
                break;
        }
        
//        if(hdr.type == Header.REQ)
//        {
//            executor.execute(new Runnable(){
//
//                public void run()
//                {
//                    RPCMessage rsp = null;
//                    try
//                    {
//                        rsp = messageReceived((RPCMessage) message);
//                        if (rsp != null)
//                            session.write(rsp);
//                    }
//                    catch (Exception e)
//                    {
//                        log.debug(e);
//                    }            
//                    
//                    
//                }
//                
//            });
//            
//            
//        }
//        else
//        {
//            
//        }

    }

   

    public void sessionCreated(IoSession session) throws Exception
    {
//    	  SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();  
//    	  cfg.setSoLinger(0);
        log.debug("sessionCreated:" + session.getLocalAddress());
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.core.service.IoHandler#messageSent(org.apache.mina.core
     * .session.IoSession, java.lang.Object)
     */
    public void messageSent(IoSession session, Object message) throws Exception
    {
        

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.core.service.IoHandler#sessionClosed(org.apache.mina.
     * core.session.IoSession)
     */
    public void sessionClosed(IoSession session) throws Exception
    {
    	log.debug("session Closed:" + session.getLocalAddress());

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.core.service.IoHandler#sessionIdle(org.apache.mina.core
     * .session.IoSession, org.apache.mina.core.session.IdleStatus)
     */
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.core.service.IoHandler#sessionOpened(org.apache.mina.
     * core.session.IoSession)
     */
    public void sessionOpened(IoSession session) throws Exception
    {
       

    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.remote.BaseRPCIOHandler#buildBaseFutureCall(org.frameworkset.spi.remote.RPCMessage, org.frameworkset.spi.remote.RPCAddress, org.frameworkset.spi.remote.RPCIOHandler)
     */
    @Override
    protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg, RPCAddress address)
    {
        // TODO Auto-generated method stub
        return new MinaFutureCall( srcmsg,  address,  this);
    }

}
