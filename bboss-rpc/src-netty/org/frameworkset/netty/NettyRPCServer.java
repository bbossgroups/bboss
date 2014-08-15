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

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.Target;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


/**
 * <p>Title: NettyRPCServer.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-19 下午05:35:56
 * @author biaoping.yin
 * @version 1.0
 */
public class NettyRPCServer
{
    private Executor bossExecutor;
    private Executor workerExecutor;
    private static Logger log = Logger.getLogger(NettyRPCServer.class);
    public ProMap conparams = null;
    final static ChannelGroup allChannels = new DefaultChannelGroup("netty-server" );   
//  public static boolean defaultlongconnection ;
    
    private long CONNECT_TIMEOUT = 10;
    NioServerSocketChannelFactory channelFactory;
    ServerBootstrap sb;
        
   
    public long getCONNECT_TIMEOUT()
    {
        return CONNECT_TIMEOUT;
    }
    private boolean started = false;
    private RPCAddress localAddress;
    private String ip = "127.0.0.1";
    
    int PORT = -1;
    public boolean started()
    {
        return this.started;
    }
    public boolean validateAddress(RPCAddress address)
    {
        //首先判断地址是否在地址范围中
        
        return NettyClinentTransport.validateAddress(address);
    }
    private static NettyRPCServer server;
    
    public static NettyRPCServer getNettyRPCServer()
    {
        if(server != null)
            return server;
        synchronized(NettyRPCServer.class)
        {
            if(server != null)
                return server;
            NettyRPCServer server_ = (NettyRPCServer)BaseSPIManager.getBeanObject("rpc.netty.server");
            
           
            server = server_;
            
        }
        
        return server;
    }
    
    private static class ShutDownNettyServer implements Runnable
    {
        NettyRPCServer server;
        ShutDownNettyServer(NettyRPCServer server)
        {
            this.server = server;
        }
        public void run()
        {
            server.stop();
            
        }
        
        
    }
    
    public RPCAddress getLocalAddress()
    {
        return this.localAddress;
    }
    
    public NettyRPCServer(ProMap conparams)
    {
        this.conparams = conparams;
        CONNECT_TIMEOUT = conparams.getInt("connection.timeout",10) * 1000;
        String ip = conparams.getString("connection.bind.ip");
        if(ip != null)
            this.ip = ip;
        PORT = conparams.getInt("connection.bind.port");
        this.localAddress = new RPCAddress(this.ip,PORT,null,Target.BROADCAST_TYPE_NETTY);
    }
    
    private java.util.concurrent.locks.ReentrantLock lock = new ReentrantLock();
    public void start() 
    {
        if(started)
            return;
        else
        {
            lock.lock();
            try
            {
                if(started)
                    return;
                // Create a class that handles sessions, incoming and outgoing data
                System.out.println("Start Netty server.....");   
//                RPCServerIoHandler handler = MinaUtil.getRPCServerIoHandler();
                
                // This socket acceptor will handle incoming connections
                this.bossExecutor = Executors.newCachedThreadPool();
                this.workerExecutor =  Executors.newCachedThreadPool();
                channelFactory = new NioServerSocketChannelFactory(bossExecutor, workerExecutor);
                sb = new ServerBootstrap(channelFactory);
               
                sb.setPipelineFactory(new NettyChannelPipelineFactory(conparams.getInt("maxFramgeLength_",NettyChannelPipelineFactory.maxFramgeLength_),
                                                                      conparams.getInt("estimatedLength_",NettyChannelPipelineFactory.estimatedLength_)));
//                ChannelUpstreamHandler sh = null;
//                sh = (ChannelUpstreamHandler)BaseSPIManager.getBeanObject(Util.rpc_netty_RPCServerIoHandler); 
//                sb.getPipeline().addLast("decoder", new ObjectDecoder());
//                sb.getPipeline().addLast("encoder", new ObjectEncoder());
//                sb.getPipeline().addLast("handler", sh);

             
                Channel sc = sb.bind(new InetSocketAddress(this.PORT));
                allChannels.add(sc);

               
                

             

                
                //sh.channel.close().awaitUninterruptibly();
                
                //sc.close().awaitUninterruptibly();

//                if (sh.exception.get() != null && !(sh.exception.get() instanceof IOException)) {
//                    throw sh.exception.get();
//                }
//              
//                if (sh.exception.get() != null) {
//                    throw sh.exception.get();
//                }
                
                System.out.println("Netty server is listenig at port " + PORT);
                System.out.println("Netty server started.");
                BaseApplicationContext.addShutdownHook(new ShutDownNettyServer(this));
                this.started = true;
            }
            catch(Exception e)
            {
                throw new NettyRunException(e);
            }
            finally
            {
                lock.unlock();
            }
        }
        
    }

    public static void main(String[] args) throws Exception {
        NettyRPCServer.getNettyRPCServer().start();
//      Class.forName("String");
    }
    
    public void stop()
    {
        if(!this.started)
            return;
//        sh.channel.close().awaitUninterruptibly();
        
       // sc.close().awaitUninterruptibly();
        log.debug("Stop netty server [" + getLocalAddress() + "] begin.");
        if(allChannels != null)
        {
            try
            {
                ChannelGroupFuture future = allChannels.close();
                future.awaitUninterruptibly();
            }
            catch (Exception e)
            {
                // TODO: handle exception
            }   
        }
        try
        {
            if(channelFactory != null)
                channelFactory.releaseExternalResources();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        try
        {
            if(sb != null)
                sb.releaseExternalResources();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        this.started = false;
        log.debug("Stop netty server [" + getLocalAddress() + "] end.");
    }
    
    

       
}
