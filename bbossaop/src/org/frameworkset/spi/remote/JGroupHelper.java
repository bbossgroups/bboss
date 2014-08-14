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
package org.frameworkset.spi.remote;

import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseSPIManager;

import bboss.org.jgroups.Address;
import bboss.org.jgroups.Channel;
import bboss.org.jgroups.ChannelException;
import bboss.org.jgroups.JChannel;
import bboss.org.jgroups.blocks.RpcDispatcher;
import bboss.org.jgroups.stack.IpAddress;

/**
 * 
 * <p>
 * Title: JGroupHelper.java
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
 * @Date Apr 24, 2009 10:49:22 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class JGroupHelper
{
    private static final Logger log = Logger.getLogger(JGroupHelper.class);

    RpcDispatcher remoteDispatcher;

    // static RpcDispatcher poolmanDispatcher;
    Channel channel;

    JGroupConfig JGroupConfig;

    // boolean cluster_enable = false;
    // boolean cluster_enable_mbean = false;
    String clusterName = "Cluster";

    public String getClusterName()
    {
        return clusterName;
    }

    String REMOTE_CLUSTER_NAME = "REMOTE." + clusterName;

    boolean clusterstarted = false;

    boolean inited = false;

    private static JGroupHelper JGroupHelper;

//    private static String cluster_protocol_tcp_configfile = BaseSPIManager.getProperty(
//            "cluster_protocol.tcp.configfile", "etc/META-INF/replSync-service-aop-tcp.xml");
//
//    private static String cluster_protocol_udp_configfile = BaseSPIManager.getProperty(
//            "cluster_protocol.udp.configfile", "etc/META-INF/replSync-service-aop.xml");

    

    public static JGroupHelper getJGroupHelper()
    {
        if (JGroupHelper == null)
        {
            synchronized (JGroupHelper.class)
            {
                if (JGroupHelper != null)
                    return JGroupHelper;
                // boolean cluster_enable =
                // BaseSPIManager.getBooleanProperty("cluster_enable", false);
                // boolean cluster_enable_mbean =
                // BaseSPIManager.getBooleanProperty("cluster_enable_mbean",
                // false);
                
                JGroupHelper = new JGroupHelper(Util.clusterName);
                return JGroupHelper;
            }
        }
        else
        {
            return JGroupHelper;
        }
    }

    // public boolean cluster_enable_mbean()
    // {
    // return this.cluster_enable_mbean;
    // }

    private JGroupHelper(String clusterName)
    {
        this.clusterName = clusterName;
        this.REMOTE_CLUSTER_NAME = "REMOTE." + clusterName;
        // this.init();
    }

    private void assertStarted()
    {
        if (!this.inited)
            throw new RuntimeException("JGroup protocol 没有启动。");
    }

    public Channel getChannel()
    {
        // init();
        assertStarted();
        return channel;
    }

    public boolean clusterstarted()
    {
        return this.clusterstarted;
    }

    public RpcDispatcher getRpcDispatcher()
    {
        // init();
        assertStarted();
        return this.remoteDispatcher;
    }

//    /**
//     * 获取集群/多实例各服务器的信息
//     * 
//     * @return
//     */
//    public Vector<Address> getAppservers()
//    {
//        // init();
////        assertStarted();
//
//        if (clusterstarted())
//        {
//        	Map<UUID,Address> addresses = channel.getAllLocalPhysicalAddress();
//        	
//        	Vector<Address> ddd = new Vector<Address>();
//        	ddd.addAll(addresses.values());
//            return ddd;
//        }
//        return new Vector<Address>();
//    }
//    
//    public Address converPhysicAddrToUUIDAddress(Address address)
//    {
//    	Map<UUID,Address> addresses = channel.getAllLocalPhysicalAddress();
//    	Iterator<Map.Entry<UUID,Address>> itrs = addresses.entrySet().iterator();
//    	while(itrs.hasNext())
//    	{
//    		Map.Entry<UUID,Address> entry = itrs.next();
//    		if(address.equals(entry.getValue()))
//			{
//    			return entry.getKey();
//			}
//    	}
//    	throw new RuntimeException("Convert physic address to uuid address failed:" + address + " don't exist.");
//    	
//    }
    /**
     * 获取集群/多实例各服务器的信息
     * 
     * @return
     */
    public Vector<Address> getAppservers()
    {
        // init();
//        assertStarted();

        if (clusterstarted())
        {
//        	Map<UUID,Address> addresses = channel.getAllLocalPhysicalAddress();
//        	
//        	Vector<Address> ddd = new Vector<Address>();
//        	ddd.addAll(addresses.values());
        	
            return channel.getView().getMembers();
        }
        return new Vector<Address>();
    }
    
    public Address getAddress(String address)
    {
    	Vector<Address> addresses = this.getAppservers();
    	for(int i = 0 ; i < addresses.size(); i ++)
    	{
    		Address address_ = addresses.get(i);
    		if(address_.toString().equals(address))
    			return address_;
    	}
    	return null;
    }
    
//    public Address converPhysicAddrToUUIDAddress(Address address)
//    {
////    	Map<UUID,Address> addresses = channel.getAllLocalPhysicalAddress();
////    	Iterator<Map.Entry<UUID,Address>> itrs = addresses.entrySet().iterator();
////    	while(itrs.hasNext())
////    	{
////    		Map.Entry<UUID,Address> entry = itrs.next();
////    		if(address.equals(entry.getValue()))
////			{
////    			return entry.getKey();
////			}
////    	}
//    	throw new RuntimeException("Convert physic address to uuid address failed:" + address + " don't exist.");
//    	
//    }

    public Address getLocalAddress()
    {
        // init();
        assertStarted();
        if (clusterstarted())
        {
            return channel.getAddress();
        }
        return null;
    }
    
    public Address getPhysicalAddress(Address uuid)
    {
        // init();
        assertStarted();
        if (clusterstarted())
        {
            return channel.getLocalPhysicalAddress(uuid);
        }
        return null;
    }
    
//    public Address getLocalAddress()
//    {
//        // init();
////        assertStarted();
////        if (clusterstarted())
////        {
////            return channel.getLocalPhysicalAddress(channel.getAddress());
////        }
//        return null;
//    }
//    
//    public Address getPhysicalAddress(Address uuid)
//    {
////        // init();
////        assertStarted();
////        if (clusterstarted())
////        {
////            return channel.getLocalPhysicalAddress(uuid);
////        }
//        return null;
//    }
    
    

    public static Vector buildAddresses(String ip, int port) throws UnknownHostException
    {
        IpAddress address = new IpAddress(ip, port);
        Vector dests = new Vector();
        dests.add(address);
        return dests;
    }
    
    public void start()
    {
        if (inited)
        {
            return;
        }
        synchronized (this)
        {
            if (inited)
            {
                return;
            }
            
            try
            {
                JGroupConfig = new JGroupConfig();
                PropertyConfigurator config = new PropertyConfigurator();
//                 String protocol = BaseSPIManager.getProperty("cluster_protocol","udp");
                
//                if (protocol.equals("udp"))
//                {
//                    String cluster_protocol_udp_configfile = BaseSPIManager.getProperty(
////                          "cluster_protocol.tcp.configfile", "etc/META-INF/replSync-service-aop-tcp.xml");
//              //
////                  private static String cluster_protocol_udp_configfile = BaseSPIManager.getProperty(
////                          "cluster_protocol.udp.configfile", "etc/META-INF/replSync-service-aop.xml");
//                    config.configure(JGroupConfig, cluster_protocol_udp_configfile);
//                }
//                else if (protocol.equals("tcp"))
//                {
//                    private static String cluster_protocol_tcp_configfile = BaseSPIManager.getProperty(
//                          "cluster_protocol.udp.configfile", "etc/META-INF/replSync-service-aop-tcp.xml");
//                    config.configure(JGroupConfig, cluster_protocol_tcp_configfile);
//                }
               
                String cluster_protocol_configfile = Util.getProtocolConfigFile();
                log.info("Load jgroup[" + REMOTE_CLUSTER_NAME + "] config from  [" + cluster_protocol_configfile + "]");
                config.configure(JGroupConfig, cluster_protocol_configfile);
                log.info("REMOTE_CLUSTER_NAME = " + REMOTE_CLUSTER_NAME);
                log.info("Start remote service begin.");
                channel = new JChannel(JGroupConfig.getClusterProperties());
                channel.setOpt(Channel.AUTO_RECONNECT, Boolean.TRUE);
                DefaultRemoteHandler remoteHander = (DefaultRemoteHandler) BaseSPIManager
                        .getBeanObject("rpc.server_object");
                remoteDispatcher = new RpcDispatcher(channel, null, null, remoteHander);
                channel.connect(REMOTE_CLUSTER_NAME);
                clusterstarted = true;
                log.info("Start remote service successed.");
                BaseSPIManager.addShutdownHook(new ShutDownJGroup(this));
                // try {
                // Class r = Runtime.getRuntime().getClass();
                // java.lang.reflect.Method m =
                // r.getDeclaredMethod("addShutdownHook",
                // new Class[] {Thread.class});
                // m.invoke(Runtime.getRuntime(), new Object[] {new Thread(new
                // ShutDownChannel())});
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
            }
            catch (ChannelException e)
            {
            	log.error("Start remote service failed.",e);
               

            }
            catch (ConfigureException e)
            {
            	log.error("Start remote service failed.",e);
            }
            catch (Exception e)
            {
            	log.error("Start remote service failed.",e);
            }
            this.inited = true;
        }

    }
    
    public void stop()
    {
        try
        {
            if (!clusterstarted())
                return;
            log.info("Shutdown remoteDispatcher begin.");
            
            if (remoteDispatcher != null)
            {
                remoteDispatcher.stop();
            }
            log.info("Shutdown remoteDispatcher complete.");
            
        }
        catch (Exception e)
        {
            log.error("Shutdown remoteDispatcher failed.",e);
           
        }

        try
        {
            if (channel != null)
            {
                log.info("Shutdown channel begin.");
               
                channel.close();
                log.info("Shutdown channel complete.");
                
            }
        }
        catch (Exception e)
        {
            
            log.error("Shutdown channel failed.",e);
        }
    }

    public static class ShutDownJGroup implements Runnable
    {
        JGroupHelper jgroup;

        public ShutDownJGroup(JGroupHelper jgroup)
        {
            this.jgroup = jgroup;
        }

        public void run()
        {
            if(jgroup != null)
                jgroup.stop();

        }
    }

    public boolean validateAddress(Object address)
    {
        RPCAddress temp = null;
        IpAddress ip = null;
        if(address instanceof RPCAddress)
        {
            temp = (RPCAddress)address;
            ip = (IpAddress)temp.getOrigineAddress();            
        }
        else 
        {
            ip = (IpAddress)address;
        }
        
        
        if(ip != null)
        {
            Vector<Address> servers = getAppservers();
            for (int i = 0; i < servers.size(); i++)
            {
                Address ipAddress =  servers.get(0);                    
                if (ipAddress.equals(ip))
                    return true;
    
            }
            return false;
        }
        else if(temp != null)
        {             
            Vector<Address> servers = getAppservers();
            for (int i = 0; i < servers.size(); i++)
            {
                IpAddress ipAddress =  (IpAddress)servers.get(0);
                if (ipAddress.getIpAddress().getHostAddress().equals(temp.getIp()) 
                        && ipAddress.getPort() == temp.getPort())
                    return true;                
    
            }
            return false;
            
        }
        return false;
        
    }
    
    public Vector getMembers()
    {
        this.assertStarted();
        return getChannel().getView().getMembers();
    }

}
