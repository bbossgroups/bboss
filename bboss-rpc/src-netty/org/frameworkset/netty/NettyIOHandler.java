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

package org.frameworkset.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.Header;
import org.frameworkset.spi.remote.IllegalMessage;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;
import org.frameworkset.spi.remote.Util;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

/**
 * <p>Title: NettyIOHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-19 下午05:26:24
 * @author biaoping.yin
 * @version 1.0
 */
public class NettyIOHandler extends BaseRPCIOHandler implements ChannelUpstreamHandler
{
    private  ExecutorService executor = Executors.newCachedThreadPool();
    final AtomicReference<Throwable> exception = new AtomicReference<Throwable>();
    private static final InternalLogger logger =
        InternalLoggerFactory.getInstance(NettyIOHandler.class.getName());

   
    /**
     * Creates a new instance.
     */
    public NettyIOHandler(String name, RequestHandler handler)
    {
        super(name, handler, null);
        
//        this.src_address = NettyRPCServer.getNettyRPCServer().getLocalAddress();
        // this.local_addr = local_addr;
    }

    /**
     * {@inheritDoc}  Down-casts the received upstream event into more
     * meaningful sub-type event and calls an appropriate handler method with
     * the down-casted event.
     */
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {

        if (e instanceof MessageEvent) {
            messageReceived(ctx, (MessageEvent) e);
        } else if (e instanceof WriteCompletionEvent) {
            WriteCompletionEvent evt = (WriteCompletionEvent) e;
            writeComplete(ctx, evt);
        } else if (e instanceof ChildChannelStateEvent) {
            ChildChannelStateEvent evt = (ChildChannelStateEvent) e;
            if (evt.getChildChannel().isOpen()) {
                childChannelOpen(ctx, evt);
            } else {
                childChannelClosed(ctx, evt);
            }
        } else if (e instanceof ChannelStateEvent) {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            switch (evt.getState()) {
            case OPEN:
                if (Boolean.TRUE.equals(evt.getValue())) {
                    channelOpen(ctx, evt);
                } else {
                    channelClosed(ctx, evt);
                }
                break;
            case BOUND:
                if (evt.getValue() != null) {
                    channelBound(ctx, evt);
                } else {
                    channelUnbound(ctx, evt);
                }
                break;
            case CONNECTED:
                if (evt.getValue() != null) {
                    channelConnected(ctx, evt);
                } else {
                    channelDisconnected(ctx, evt);
                }
                break;
            case INTEREST_OPS:
                channelInterestChanged(ctx, evt);
                break;
            default:
                ctx.sendUpstream(e);
            }
        } else if (e instanceof ExceptionEvent) {
            exceptionCaught(ctx, (ExceptionEvent) e);
        } else {
            ctx.sendUpstream(e);
        }
    }
    
    protected void assertMessage(Object message) throws IllegalMessage
    {
        if (message instanceof RPCMessage)
        {

        }
        else throw new IllegalMessage(message.toString());
    }

    /**
     * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
     * from a remote peer.
     */
    public void messageReceived(
            ChannelHandlerContext ctx,final MessageEvent e) throws Exception {
        executor.execute(new Runnable(){

            public void run()
            {
            	
            	
                 RPCMessage rsp;
				try {
					Object m = e.getMessage();
					RPCMessage message_ = (RPCMessage) Util.getDecoder().decoder(m);
//	            	 RPCMessage message_ = (RPCMessage) e.getMessage();
	                 Header hdr = message_.getHeader(name);
					rsp = messageReceived( message_);
				
	                 switch (hdr.getType())
	                 {
	                     case Header.REQ:
	                         if (rsp != null)
	                        	 e.getChannel().write( rsp);
	                         break;
	                     case Header.RSP:                
	                         break;    
	                     default:
	                         
	                         break;
	                 }
				} catch (Exception e1) {
					log.error(e1.getMessage(),e1);
				}
               
//                if (channel.getParent() != null) {
//                    channel.write(m);
//                }

              
            }
            
        });
    }

    /**
     * Invoked when an exception was raised by an I/O thread or a
     * {@link ChannelHandler}.
     */
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//        if (this == ctx.getPipeline().getLast()) {
//            logger.warn(
//                    "EXCEPTION, please implement " + getClass().getName() +
//                    ".exceptionCaught() for proper handling.", e.getCause());
//        }
//        ctx.sendUpstream(e);
        e.getCause().printStackTrace();
        if (exception.compareAndSet(null, e.getCause())) {
            e.getChannel().close();
        }
        
    }

    /**
     * Invoked when a {@link Channel} is open, but not bound nor connected.
     */
    public void channelOpen(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
        NettyRPCServer.allChannels.add(e.getChannel());   
          
    }

    /**
     * Invoked when a {@link Channel} is open and bound to a local address,
     * but not connected.
     */
    public void channelBound(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a {@link Channel} is open, bound to a local address, and
     * connected to a remote address.
     */
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a {@link Channel}'s {@link Channel#getInterestOps() interestOps}
     * was changed.
     */
    public void channelInterestChanged(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }
    
    

    /**
     * Invoked when a {@link Channel} was disconnected from its remote peer.
     */
    public void channelDisconnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a {@link Channel} was unbound from the current local address.
     */
    public void channelUnbound(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a {@link Channel} was closed and all its related resources
     * were released.
     */
    public void channelClosed(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when something was written into a {@link Channel}.
     */
    public void writeComplete(
            ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a child {@link Channel} was open.
     * (e.g. a server channel accepted a connection)
     */
    public void childChannelOpen(
            ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    /**
     * Invoked when a child {@link Channel} was closed.
     * (e.g. the accepted connection was closed)
     */
    public void childChannelClosed(
            ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg, RPCAddress address)
    {
        // TODO Auto-generated method stub
        return new NettyFutureCall(srcmsg,address,this);
    }
    
    
}
