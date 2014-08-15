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

package org.frameworkset.spi.remote.mina.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RemoteException;
import org.frameworkset.spi.remote.SSLHelper;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;
import org.frameworkset.spi.remote.mina.DummyIOHandler;
import org.frameworkset.spi.remote.mina.server.MinaRPCServer;
import org.frameworkset.spi.remote.mina.server.MinaRunException;
import org.frameworkset.spi.remote.mina.server.RPCServerIoHandler;

/**
 * <p>
 * Title: ClinentTransport.java
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
 * @Date 2009-10-12 下午09:41:15
 * @author biaoping.yin
 * @version 1.0
 */
public class ClinentTransport
{
    private String host;

    private RPCAddress local_addr;

    private int port;

    private SocketConnector connector;

    private IoSession session;

    // private RPCServerIoHandler corr;
    private static Map<String, ClinentTransport> rpcClients = new HashMap<String, ClinentTransport>();

//    /**
//     * 
//     * @param host
//     * @param port
//     * @param corr
//     * @return
//     */
//    public static ClinentTransport getClinentTransport(RPCAddress address, IoHandler corr)
//    {
//        // String key = host + ":" + port;
//        //		
//        // ClinentTransport instance = rpcClients.get(key);
//        // if(instance != null)
//        // return instance;
//        // synchronized(RPCClient.class)
//        // {
//        // instance = rpcClients.get(key);
//        // if(instance != null)
//        // return instance;
//        // instance = new ClinentTransport(host,
//        // port,corr,defaultlongconnection);
//        // BaseSPIManager.addShutdownHook(new ShutDown());
//        // rpcClients.put(key, instance);
//        // }
//        // return instance;
//        return getClinentTransport(address, corr);
//    }
    
    /**
     * 校验地址是否有效
     * @param address
     * @return
     */
    public static boolean validateAddress(RPCAddress address)
    {
        ClinentTransport transport = null;
        try
        {
            transport = ClinentTransport.queryAndCreateClinentTransport(address);
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
    public static ClinentTransport getClinentTransport(String ip, int port, RPCServerIoHandler corr)
    {
        RPCAddress address = new RPCAddress(ip, port,null,Target.BROADCAST_TYPE_MINA);
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
    public static ClinentTransport getClinentTransport(RPCAddress address, IoHandler corr)
    {
//        if (longconnection)
        {
            String key = address.getIp() + ":" + address.getPort();

            ClinentTransport instance = rpcClients.get(key);
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
                        instance = new ClinentTransport(address, corr);
                        ApplicationContext.addShutdownHook(new ShutDownMina());
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
                instance = new ClinentTransport(address, corr);
                ApplicationContext.addShutdownHook(new ShutDownMina());
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
    
    public static ClinentTransport queryAndCreateClinentTransport(RPCAddress address)
    {
        String key = address.getIp() + ":" + address.getPort();

        ClinentTransport instance = rpcClients.get(key);
        if (instance != null)
            return instance;
        else
        {
            boolean dummy = true;
            instance = new ClinentTransport(address,dummy);
            return instance;
        }
    }

    static class ShutDownMina implements Runnable
    {

        public void run()
        {
            Collection<ClinentTransport> ClinentTransports = rpcClients.values();
            if (ClinentTransports != null && ClinentTransports.size() > 0)
            {
                Iterator<ClinentTransport> it = ClinentTransports.iterator();
                ClinentTransport t = null;
                while (it.hasNext())
                {
                    t = it.next();
                    t.disconnect();
                }
            }

        }

    }

    private Object object = new Object();

    public RPCAddress buildRPCAddress(IoSession session)
    {
        if (this.local_addr != null)
            return this.local_addr;
        synchronized (object)
        {
            if (this.local_addr != null)
                return this.local_addr;
            InetSocketAddress inet = (InetSocketAddress) session.getLocalAddress();
            local_addr = new RPCAddress(inet.getAddress(),inet.getPort(),null,Target.BROADCAST_TYPE_MINA);
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
            buildRPCAddress(session);
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
    private ClinentTransport(RPCAddress rpcaddress, IoHandler corr)
    {
        this.host = rpcaddress.getIp();
        this.port = rpcaddress.getPort();
        this.rpcaddress = rpcaddress;
       
        
        connector = new NioSocketConnector();
        /**
         * 增加ssl的技术支持
         */
        ProMap commons = Util.defaultContext.getMapProperty("rpc.protocol.mina.params");
        boolean enablessl = Util.defaultContext.getMapProperty("rpc.protocol.mina.params").getBoolean("enablessl",false);
        if(enablessl)
        {
            try
            {
                ProMap ssls =  Util.defaultContext.getMapProperty("rpc.protocol.mina.ssl.client");
                if(ssls == null)
                {
                    throw new MinaRunException("启用了ssl模式， 但是没有指定rpc.protocol.mina.ssl.client 参数，请检查文件org/frameworkset/spi/manager-rpc-mina.xml是否正确设置了该参数。");
                }
                String keyStore = ssls.getString("keyStore");
                String keyStorePassword = ssls.getString("keyStorePassword");
                String trustStore = ssls.getString("trustStore");
                String trustStorePassword = ssls.getString("trustStorePassword");
                SslFilter sslFilter = new SslFilter(SSLHelper.createSSLContext(keyStore, keyStorePassword, trustStore, trustStorePassword));
                

                /** 设置为客户端模式 **/
                sslFilter.setUseClientMode(true);
                String[] enabledCipherSuites = (String[])commons.getObject("enabledCipherSuites",SSLHelper.enabledCipherSuites);
                sslFilter.setEnabledCipherSuites(enabledCipherSuites);
                String[] protocols = (String[])commons.getObject("enabledProtocols");
                if(protocols != null)
                    sslFilter.setEnabledProtocols(protocols);
                /** 设置加密过滤器 **/
                connector.getFilterChain().addLast("SSL", sslFilter);
            }
            catch (GeneralSecurityException e)
            {
                throw new MinaRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-mina.xml是否正确设置了客服端的ssl参数rpc.protocol.mina.ssl.client。",e);
            }
            catch (IOException e)
            {
                throw new MinaRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-mina.xml是否正确设置了客服端的ssl参数rpc.protocol.mina.ssl.client。",e);
            }
            catch(MinaRunException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new MinaRunException("启用了ssl模式， 请检查文件org/frameworkset/spi/manager-rpc-mina.xml是否正确设置了客服端的ssl参数rpc.protocol.mina.ssl.client。",e);
            }
        }
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setConnectTimeoutMillis(MinaRPCServer.getMinaRPCServer().getCONNECT_TIMEOUT());

        connector.setHandler(corr);
        // scheduler = Executors.newScheduledThreadPool(1);

        // ScheduledThreadPoolExecutor scheduler = new
        // ScheduledThreadPoolExecutor
        // (Runtime.getRuntime().availableProcessors());
        // filter = new RequestResponseFilter(new RPCRequestInterceptor(),
        // scheduler);
        // connector.getFilterChain().addLast("reqres", filter);

        this.connect();
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
    
    
    public ClinentTransport(RPCAddress rpcaddress, boolean dummy)
    {
        this.host = rpcaddress.getIp();
        this.port = rpcaddress.getPort();
        this.rpcaddress = rpcaddress;
        this.dummy = dummy;

        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        connector.setConnectTimeoutMillis(MinaRPCServer.getMinaRPCServer().getCONNECT_TIMEOUT());

        connector.setHandler(new DummyIOHandler());
        // scheduler = Executors.newScheduledThreadPool(1);

        // ScheduledThreadPoolExecutor scheduler = new
        // ScheduledThreadPoolExecutor
        // (Runtime.getRuntime().availableProcessors());
        // filter = new RequestResponseFilter(new RPCRequestInterceptor(),
        // scheduler);
        // connector.getFilterChain().addLast("reqres", filter);

        this.connect();
    }

    public boolean isConnected()
    {
        return (session != null && session.isConnected());
    }

    public void connect()
    {
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(host, port));
        boolean successed = connectFuture.awaitUninterruptibly().isConnected();
        if(!successed )
        	throw new RuntimeIoException("connected failed:unreachable " + host + " " + port);
        try
        {
        
            session = connectFuture.getSession();
            if(session == null)
            	throw new RuntimeIoException("create session failed:unreachable " + host + " " + port);
        }
        catch (RuntimeIoException e)
        {
            throw e;
            //           
        }
    }

    public void disconnect()
    {
        if (session != null)
        {
            session.close(true).awaitUninterruptibly(MinaRPCServer.getMinaRPCServer().getCONNECT_TIMEOUT());
            session = null;
        }
        if (this.connector != null)
            connector.dispose();
    }

    public void write(RPCMessage message)
    {
        boolean active = this.connector.isActive() ;
        if(!active)
            throw new RemoteException(message,0);
        try
        {
            setSourceAddress(message);
            Object msg = Util.getEncoder().encoder(message);
//            session.write(message);
            session.write(msg);
        }
        catch(Exception e)
        {
            throw new RemoteException(message,e); 
        }

    }

//    public void write(Object message)
//    {
//        // message.setSrc(this.local_addr);
//        // setSourceAddress(message) ;
//        session.write(message);
//
//    }
    
    public boolean validate()
    {
        boolean active = this.connector.isActive();
        return active;
    }

}
