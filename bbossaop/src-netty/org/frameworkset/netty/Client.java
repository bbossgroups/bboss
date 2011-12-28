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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.util.internal.ExecutorUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * <p>Title: Client.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-4-17
 * @author biaoping.yin
 * @version 1.0
 */
public class Client {
	private class EchoHandler extends SimpleChannelUpstreamHandler {
        volatile Channel channel;
        final AtomicReference<Throwable> exception = new AtomicReference<Throwable>();
        volatile int counter;

        EchoHandler() {
            super();
        }

        @Override
        public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            channel = e.getChannel();
        }

        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {

            String m = (String) e.getMessage();
            
//System.out.println("response:" + m);
            if (channel.getParent() != null) {
                channel.write(m);
            }

            counter ++;
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            if (exception.compareAndSet(null, e.getCause())) {
                e.getChannel().close();
            }
        }
    }
	
	static class TestObject implements java.io.Serializable
	{
		private String id ;
		private String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	private ExecutorService executor;

	  @BeforeClass
	    public  void init() {
	        executor = Executors.newCachedThreadPool();
	    }

	    @AfterClass
	    public void destroy() {
	        ExecutorUtil.terminate(executor);
	    }

        public void startup() throws Throwable
    	{
        	
            ChannelFactory cf = newClientSocketChannelFactory(executor);
            ClientBootstrap cb = new ClientBootstrap(cf);

             
            EchoHandler ch = new EchoHandler();

            
            cb.getPipeline().addLast("decoder", new ObjectDecoder());
            cb.getPipeline().addLast("encoder", new ObjectEncoder());
            cb.getPipeline().addLast("handler", ch);

         

            ChannelFuture ccf = cb.connect(new InetSocketAddress(NettyUtil.getLocalHost(), 3344));
            assertTrue(ccf.awaitUninterruptibly().isSuccess());

            final Channel cc = ccf.getChannel();
            ExecutorService executor = Executors.newCachedThreadPool();
            for(int i = 0;  i < 10; i ++)
            {
                executor.execute(new Runnable(){

                    public void run()
                    {
                        cc.write("hello");
                        TestObject o = new TestObject();
                        o.setId("1");
                        o.setName("duoduo");
                        cc.write(o);
                        
                    }
                    
                });
                
            }

          

            

            
          //  ch.channel.close().awaitUninterruptibly();
            

            
            if (ch.exception.get() != null && !(ch.exception.get() instanceof IOException)) {
                throw ch.exception.get();
            }
            
            if (ch.exception.get() != null) {
                throw ch.exception.get();
            }
    	}
	/**
	 * 
	 * @param args
	 * @throws Throwable 
	 */
	public static void main(String[] args) throws Throwable {
		Client client = new Client();
		client.init();
		client.startup();

	}
	
	 
	    protected ChannelFactory newClientSocketChannelFactory(Executor executor) {
	        return new NioClientSocketChannelFactory(executor, executor);
	    }


}
