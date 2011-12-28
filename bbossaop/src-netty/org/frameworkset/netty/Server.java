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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.frameworkset.netty.Client.TestObject;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.util.internal.ExecutorUtil;

/**
 * <p>Title: Server.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-4-17
 * @author biaoping.yin
 * @version 1.0
 */
public class Server {
	private  ExecutorService executor;
	
	/**
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		Server server = new Server();
		server.init();
		server.startup();
	}
	
	public void startup() throws Throwable
	{
		ServerBootstrap sb = new ServerBootstrap(newServerSocketChannelFactory(executor));
        

        EchoHandler sh = new EchoHandler();
       

        sb.getPipeline().addLast("decoder", new ObjectDecoder());
        sb.getPipeline().addLast("encoder", new ObjectEncoder());
        sb.getPipeline().addLast("handler", sh);

     
        Channel sc = sb.bind(new InetSocketAddress(3344));
     
       

       

     

        
        //sh.channel.close().awaitUninterruptibly();
        
        //sc.close().awaitUninterruptibly();

        if (sh.exception.get() != null && !(sh.exception.get() instanceof IOException)) {
            throw sh.exception.get();
        }
      
        if (sh.exception.get() != null) {
            throw sh.exception.get();
        }
        
	}
	
	private class EchoHandler extends SimpleChannelUpstreamHandler {
//        volatile Channel channel;
        final AtomicReference<Throwable> exception = new AtomicReference<Throwable>();
        volatile int counter;

        EchoHandler() {
            super();
        }

        @Override
        public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
//            channel = e.getChannel();
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
                throws Exception {
            executor.execute(new Runnable(){

                public void run()
                {
                    Object m = null;
                    if(e.getMessage() instanceof String)
                    {
                         m = (String) e.getMessage();
                       // System.out.println(m);
                    }
                    
                    if(e.getMessage() instanceof TestObject)
                    {
                        TestObject m_ = (TestObject) e.getMessage();
                        m = m_.getId() + ":" +  m_.getName();
                        System.out.println(m_.getId());
                        System.out.println(m_.getName());
                    }
                    e.getChannel().write("receive:" + m);
//                    if (channel.getParent() != null) {
//                        channel.write(m);
//                    }

                    counter ++;
                    
                }
                
            });
            
        	
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            if (exception.compareAndSet(null, e.getCause())) {
                e.getChannel().close();
            }
        }
    }
	
	protected ChannelFactory newServerSocketChannelFactory(Executor executor) {
        return new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    }

	    public  void init() {
	        executor = Executors.newCachedThreadPool();
	    }

	 
	    public void destroy() {
	        ExecutorUtil.terminate(executor);
	    }

}
