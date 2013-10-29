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
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RemoteException;
import org.frameworkset.spi.remote.SSLHelper;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

/**
 * <p>Title: NettyClinentTransport.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-19 下午11:41:28
 * @author biaoping.yin
 * @version 1.0
 */
public class NettyClinentTransport {
	private String host;

    private RPCAddress local_addr;

    private int port;
    private Channel cc;
    private ClientBootstrap cb;

   
    
    private ChannelFactory channelFactory;
	protected ChannelFactory newClientSocketChannelFactory() {
        return new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    }

    // private RPCServerIoHandler corr;
    private static Map<String, NettyClinentTransport> rpcClients = new HashMap<String, NettyClinentTransport>();


    /**
     * 校验地址是否有效
     * @param address
     * @return
     */
    public static boolean validateAddress(RPCAddress address)
    {
        NettyClinentTransport transport = null;
        try
        {
            transport = NettyClinentTransport.queryAndCreateClinentTransport(address);
            if(transport.isdummy())
            {
                transport.disconnect();
            }
            else
            {
                return transport.isConnected();
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
        
    }
    
   

    /**
     * 
     * @param host
     * @param port
     * @param corr
     * @return
     */
    public static NettyClinentTransport getClinentTransport(String ip, int port, NettyIOHandler corr)
    {
        RPCAddress address = new RPCAddress(ip, port,null,Target.BROADCAST_TYPE_NETTY);
        // String key = host + ":" + port;
        //		
        // ClinentTransport instance = rpcClients.get(key);
        // if(instance != null)
        // return instance;
        // synchronized(RPCClient.class)
        // {
        // instance = rpcClients.get(key);
        // if(instance != null)
        // return instance;
        // instance = new ClinentTransport(host,
        // port,corr,defaultlongconnection);
        // BaseSPIManager.addShutdownHook(new ShutDown());
        // rpcClients.put(key, instance);
        // }
        // return instance;
        return getClinentTransport(address, corr);
    }

    /**
     * 
     * @param host
     * @param port
     * @param corr
     * @param longconnection
     * @return
     */
    public static NettyClinentTransport getClinentTransport(RPCAddress address, ChannelUpstreamHandler corr)
    {
//        if (longconnection)
        {
            String key = address.getIp() + ":" + address.getPort();

            NettyClinentTransport instance = rpcClients.get(key);
            if (instance != null)
            {
                if(instance.validate())
                    return instance;
                else
                {
                    rpcClients.remove(key);
                    synchronized (rpcClients)
                    {
                        instance = rpcClients.get(key);
                        if (instance != null)
                            return instance;
                        instance = new NettyClinentTransport(address, corr);
                        ApplicationContext.addShutdownHook(new ShutDownNetty());
                        rpcClients.put(key, instance);
                    }
                    return instance;
                }
            }
            synchronized (rpcClients)
            {
                instance = rpcClients.get(key);
                if (instance != null)
                    return instance;
                instance = new NettyClinentTransport(address, corr);
                ApplicationContext.addShutdownHook(new ShutDownNetty());
                rpcClients.put(key, instance);
            }
            return instance;
        }
//        else
//        {
//            ClinentTransport instance = new ClinentTransport(address, corr);
//            return instance;
//        }
    }
    
    public static NettyClinentTransport queryAndCreateClinentTransport(RPCAddress address)
    {
        String key = address.getIp() + ":" + address.getPort();

        NettyClinentTransport instance = rpcClients.get(key);
        if (instance != null)
            return instance;
        else
        {
            boolean dummy = true;
            instance = new NettyClinentTransport(address,dummy);
            return instance;
        }
    }

    static class ShutDownNetty implements Runnable
    {

        public void run()
        {
            Collection<NettyClinentTransport> ClinentTransports = rpcClients.values();
            if (ClinentTransports != null && ClinentTransports.size() > 0)
            {
                Iterator<NettyClinentTransport> it = ClinentTransports.iterator();
                NettyClinentTransport t = null;
                while (it.hasNext())
                {
                    t = it.next();
                    try {
						t.disconnect();
					} catch (Exception e) {
						// TODO: handle exception
					}
                }
            }

        }

    }

    private Object object = new Object();

    public RPCAddress buildRPCAddress(Channel session)
    {
        if (this.local_addr != null)
            return this.local_addr;
        synchronized (object)
        {
            if (this.local_addr != null)
                return this.local_addr;
            InetSocketAddress inet = (InetSocketAddress) session.getLocalAddress();
            local_addr = new RPCAddress(inet.getAddress(),inet.getPort(),null,Target.BROADCAST_TYPE_NETTY);
            return local_addr;
        }
    }

    public RPCAddress getLocalAddress()
    {
        return local_addr;
    }

    /**
     * If the sender is null, set our own address. We cannot just go ahead and
     * set the address anyway, as we might be sending a message on behalf of
     * someone else ! E.gin case of retransmission, when the original sender has
     * crashed, or in a FLUSH protocol when we have to return all unstable
     * messages with the FLUSH_OK response.
     */
    private void setSourceAddress(RPCMessage msg)
    {

        if (msg.getSrc_addr() == null)
        {
            buildRPCAddress(cc);
            msg.setSrc_addr(local_addr);
        }
    }

    private RPCAddress rpcaddress;

    public RPCAddress getRpcaddress()
    {

        return rpcaddress;
    }

    /**
     * 是否使用长连接
     * 
     * @param host
     *            连接的主机地址
     * @param port
     *            连接的端口地址
     * @param corr
     *            协作处理器
     * @param longconnection
     *            是否使用长连接
     */
    private NettyClinentTransport(RPCAddress rpcaddress, ChannelUpstreamHandler corr)
    {
        this.host = rpcaddress.getIp();
        this.port = rpcaddress.getPort();
        this.rpcaddress = rpcaddress;
       
        this.start(corr);
//        connector = new NioSocketConnector();
       
//        ExecutorService executor = Executors.newCachedThreadPool();
//        for(int i = 0;  i < 10; i ++)
//        {
//            executor.execute(new Runnable(){
//
//                public void run()
//                {
//                    cc.write("hello");
//                    TestObject o = new TestObject();
//                    o.setId("1");
//                    o.setName("duoduo");
//                    cc.write(o);
//                    
//                }
//                
//            });
//            
//        }

      

        

        
      //  ch.channel.close().awaitUninterruptibly();
        

        
//        if (ch.exception.get() != null && !(ch.exception.get() instanceof IOException)) {
//            throw ch.exception.get();
//        }
//        
//        if (ch.exception.get() != null) {
//            throw ch.exception.get();
//        }

//        this.connect();
    }
    private boolean dummy = false;
    
    /**
     * 是不是模拟连接器，true-是，false-否
     * @return
     */
    public boolean isdummy()
    {
        return this.dummy;
    }
    
    public void start(ChannelUpstreamHandler corr)
    {
    	try
        {
 	        channelFactory = newClientSocketChannelFactory();
 	        cb = new ClientBootstrap(channelFactory);
 	
 	        /**
 	         * 增加ssl的技术支持
 	         */
 	
 	        ProMap commons = ApplicationContext.getApplicationContext().getMapProperty("rpc.protocol.netty.params");
 	        boolean enablessl = commons.getBoolean("enablessl",false);
 	        SSLEngine eg = null;
 	        if(enablessl)
 	        {
 	            eg = buildSSLEngine(true);
 	        }
 	       
 	        if(eg != null)
 	            cb.getPipeline().addFirst("ssl", new SslHandler(eg));
 	
 	        
 	        cb.getPipeline().addLast("decoder", new ObjectDecoder(commons.getInt("maxFramgeLength_",NettyChannelPipelineFactory.maxFramgeLength_)));
 	        cb.getPipeline().addLast("encoder", new ObjectEncoder(commons.getInt("estimatedLength_",NettyChannelPipelineFactory.estimatedLength_)));
 	        cb.getPipeline().addLast("handler", corr);
 	
 	        cb.setOption("connectTimeoutMillis", commons.getInt("connection.timeout",10) * 1000);
 	
 	        ChannelFuture ccf = cb.connect(new InetSocketAddress(host, port));
 	        boolean success = ccf.awaitUninterruptibly().isSuccess();
 	        if(!success )
 	        {
 	            throw new NettyRunException("can not connect to:" + host + ":"+ port);
 	        }
 	        cc = ccf.getChannel();
        }
        catch(Exception e)
        {
     	   this.disconnect();
        }
    }
    
    public static SSLEngine buildSSLEngine(boolean isclient)
    {
        ProMap commons = ApplicationContext.getApplicationContext().getMapProperty("rpc.protocol.netty.params");
        String name = "rpc.protocol.netty.ssl.client";
        if(isclient)
            name = "rpc.protocol.netty.ssl.client";
        else
            name = "rpc.protocol.netty.ssl.server";
        
        try
        {
            ProMap ssls = null;
            
            ssls = ApplicationContext.getApplicationContext().getMapProperty(name);
            if(ssls == null)
            {
                throw new NettyRunException("启用了ssl模式， 但是没有指定"+ name +" 参数，请检查文件org/frameworkset/spi/manager-rpc-netty.xml是否正确设置了该参数。");
            }
            String keyStore = ssls.getString("keyStore");
            String keyStorePassword = ssls.getString("keyStorePassword");
            String trustStore = ssls.getString("trustStore");
            String trustStorePassword = ssls.getString("trustStorePassword");
            SSLContext context = SSLHelper.createSSLContext(keyStore, keyStorePassword, trustStore, trustStorePassword);
            SSLEngine sse = context.createSSLEngine();
            
            sse.setUseClientMode(isclient);
            String[] enabledCipherSuites = (String[])commons.getObject("enabledCipherSuites",SSLHelper.enabledCipherSuites);
            sse.setEnabledCipherSuites(enabledCipherSuites);
            String[] protocols = (String[])commons.getObject("enabledProtocols");
            if(protocols != null)
                sse.setEnabledProtocols(protocols);
            if(!isclient)
            {
                boolean needClientAuth = commons.getBoolean("needClientAuth",false);
                boolean wantClientAuth = commons.getBoolean("wantClientAuth",false);
                sse.setNeedClientAuth(needClientAuth);
                sse.setWantClientAuth(wantClientAuth);
            }
            return sse;
          
        }
        catch (GeneralSecurityException e)
        {
            throw new NettyRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-netty.xml是否正确设置了客服端的ssl参数"+ name +"。",e);
        }
        catch (IOException e)
        {
            throw new NettyRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-netty.xml是否正确设置了客服端的ssl参数"+ name +"。",e);
        }
        catch(NettyRunException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new NettyRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-netty.xml是否正确设置了客服端的ssl参数"+ name +"。",e);
        }
    
    }
    
    public NettyClinentTransport(RPCAddress rpcaddress, boolean dummy)
    {
        this.host = rpcaddress.getIp();
        this.port = rpcaddress.getPort();
        this.rpcaddress = rpcaddress;
        this.dummy = dummy;
       
       
        
//        connector = new NioSocketConnector();
//        /**
//         * 增加ssl的技术支持
//         */
       
//        channelFactory = newClientSocketChannelFactory();
//        cb = new ClientBootstrap(channelFactory);
//
//        ProMap commons = ApplicationContext.getApplicationContext().getMapProperty("rpc.protocol.netty.params");
//        boolean enablessl = commons.getBoolean("enablessl",false);
//        SSLEngine eg = null;
//        if(enablessl)
//        {
//            eg = buildSSLEngine(true);
//        }
//       
//        if(eg != null)
//            cb.getPipeline().addFirst("ssl", new SslHandler(eg));
//        cb.getPipeline().addLast("decoder", new ObjectDecoder());
//        cb.getPipeline().addLast("encoder", new ObjectEncoder());
//        cb.getPipeline().addLast("handler", new DummyIOHandler());
//
//     
//        cb.setOption("connectTimeoutMillis", commons.getInt("connection.timeout",10) * 1000);
//        ChannelFuture ccf = cb.connect(new InetSocketAddress(host, port));
//        
//        boolean success = ccf.awaitUninterruptibly().isSuccess();
//        if(!success )
//            throw new NettyRunException("can not connect to:" + host + ":"+ port);
//        cc = ccf.getChannel();
        this.start(new DummyIOHandler());
        
    }

    public boolean isConnected()
    {
        return ( cc != null && cc.isConnected());
    }

   

    public void disconnect()
    {
        if (cc != null)
        {
        	try
            {
                cc.close().awaitUninterruptibly();
                cc = null;
            }
            catch (Exception e)
            {
                // TODO: handle exception
            }
           
        }
        try
        {
            if(channelFactory != null)
                this.channelFactory.releaseExternalResources();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        try
        {
            if(cb != null)
                this.cb.releaseExternalResources();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
       
    }

    public void write(RPCMessage message)
    {
        // message.setSrc(this.local_addr);
        boolean active = validate();
        if(!active)
            throw new RemoteException(message,0);
        try
        {
            setSourceAddress(message);
            Object msg = Util.getEncoder().encoder(message);
            this.cc.write(msg);
        }
        catch(Exception e)
        {
            throw new RemoteException(message,e); 
        }

    }
    
    public boolean validate()
    {
    	if(cc == null)
    		return false;
        boolean active = this.cc.isBound() || this.cc.isConnected() || this.cc.isOpen();
        return active;
    }
}
