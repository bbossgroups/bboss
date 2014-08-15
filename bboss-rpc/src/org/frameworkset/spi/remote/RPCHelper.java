package org.frameworkset.spi.remote;

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
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.frameworkset.netty.NettyRPCServer;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.RemoteCallContext;
import org.frameworkset.spi.remote.health.RPCValidator;
import org.frameworkset.spi.remote.http.HttpServer;
import org.frameworkset.spi.remote.jms.JMSServer;
import org.frameworkset.spi.remote.mina.server.MinaRPCServer;
import org.frameworkset.spi.remote.rmi.RMIServer;
import org.frameworkset.spi.serviceidentity.ServiceIDImpl;
import org.frameworkset.spi.serviceidentity.TargetImpl;

import bboss.org.jgroups.Address;
import bboss.org.jgroups.blocks.GroupRequest;
import bboss.org.jgroups.blocks.RpcDispatcher;
import bboss.org.jgroups.stack.IpAddress;
import bboss.org.jgroups.util.Rsp;
import bboss.org.jgroups.util.RspList;

/**
 * 
 * <p>
 * Title: RPCHelper.java
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
 * @Date 2009-4-20 下午05:28:32
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCHelper
{
    public  List<String> startupProtocols = new ArrayList<String>();
    public static final String rpc_startup_mode_mannual = "mannual";
    public static final String rpc_startup_mode_auto = "auto";
    
    private static RPCHelper instance;
    
    private RPCHelper()
    {
        
    }
    public static RPCHelper getRPCHelperInstance()
    {
    	return instance ;
    }
    public static RPCHelper getRPCHelper()
    {
        if(instance != null)
            return instance;
        synchronized(RPCHelper.class)
        {
            if(instance != null)
                return instance;
            RPCHelper instance_ = new RPCHelper();
            instance_.webserviceenabled = BaseSPIManager.getBooleanProperty("rpc.webservice.enable",false);
            instance = instance_;		
//            instance.startServers();
        }
        return instance;
            
    }
    
    private boolean started = false;
    
    public  void startServers()
    {
        if(Util.rpc_startup_mode.equals(rpc_startup_mode_auto))
            startRPCServices();        
    }
    
    public synchronized void startRPCServices()
    {
        if(started)
            return;
        String temp = Util.rpc_startup_protocols;
        if (temp != null && !temp.trim().equals(""))
        {
            StringTokenizer tokenizer = new StringTokenizer(temp, ",", false);
            while (tokenizer.hasMoreElements())
            {
                String protocol = tokenizer.nextToken();
                startupProtocols.add(protocol);
                
            }
        }
        startServerProtocols(startupProtocols);
        this.started = true;
    }
    
    
    
    
    
    

    public boolean clusterenabled = false;

    public boolean minaenabled = false;
    
    public boolean jmsenabled = false;
    public boolean rmienabled = false;
    public boolean httpenabled = false;
    public boolean webserviceenabled = false;

    public Object rpcService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters ,RemoteCallContext callContext// 服务参数
    ) throws Throwable
    {

        Target target = ((RemoteServiceID)serviceID).getTarget();
        return innerRpcService(serviceID,// 服务标识
                method,// 需要在服务上调用的方法
                parameters ,callContext,// 服务参数
                target
        ) ;
    }
    
    
    private Object innerRpcService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters ,RemoteCallContext callContext,// 服务参数
            Target target
    ) throws Throwable
    {

//        Target target = serviceID.getTarget();
        if (target.protocol_jgroup())
        {
            return rpcJGroupService(serviceID,// 服务标识
                    method,// 需要在服务上调用的方法
                    parameters // 服务参数
                    ,callContext
            );
        }
        else if (target.protocol_mina())
        {
            return rpcMinaService(serviceID,// 服务标识
                    method,// 需要在服务上调用的方法
                    parameters // 服务参数
                    ,callContext
            );
        }
        else if (target.protocol_netty())
        {
            return rpcNettyService(serviceID,// 服务标识
                    method,// 需要在服务上调用的方法
                    parameters // 服务参数
                    ,callContext
            );
        }
        else if (target.protocol_jms())
        {
//            throw new RemoteException("不支持的协议类型 [target][" + target.getStringTargets() + "]");
        	return rpcJMSService( serviceID,// 服务标识
               	                      method,// 需要在服务上调用的方法
               	                      parameters // 服务参数
               	                   ,callContext
               	             );
        }
        else if (target.protocol_webservice())
        {
        	return rpcWebServiceService( serviceID,// 服务标识
        	                      method,// 需要在服务上调用的方法
        	                      parameters // 服务参数
        	                      ,callContext
        	             );
//            throw new RemoteException("不支持的协议类型 [target][" + target.getStringTargets() + "]");
        }
        
        else if (target.protocol_rmi())
        {
        	return rpcRMIService( serviceID,// 服务标识
        	                      method,// 需要在服务上调用的方法
        	                      parameters // 服务参数
        	                      ,callContext
        	             );
//            throw new RemoteException("不支持的协议类型 [target][" + target.getStringTargets() + "]");
        }
        else if (target.protocol_http())
        {
        	return rpcHTTPService( serviceID,// 服务标识
        	                      method,// 需要在服务上调用的方法
        	                      parameters // 服务参数
        	                      ,callContext
        	             );
//            throw new RemoteException("不支持的协议类型 [target][" + target.getStringTargets() + "]");
        }
        else if (target.protocol_rest())
        {
        	return rpcRestService( serviceID,// 服务标识
               	                      method,// 需要在服务上调用的方法
               	                      parameters // 服务参数
               	                      ,callContext
               	             );
        }
        else 
        {
            throw new RemoteException("不支持的协议类型 [target][" + target.getStringTargets() + "]");
        }
    }
    
    private Object rpcRestService(RemoteServiceID serviceID, Method method,
			Object[] parameters, RemoteCallContext callContext) throws Throwable
	{
    	
    	Target target = ((RemoteServiceID)serviceID).getRestfulTarget();
//    	/**
//    	 * 构建特定组件管理容器远程请求调用上下文中参数头信息和安全上下文信息
//    	 * @fixed biaoping.yin 2010-10-11 begin
//    	 */
//    	RemoteServiceID restServiceID = (RemoteServiceID)serviceID.getRestfulServiceID();
//    	if(restServiceID != null && restServiceID.getUrlParams() != null)
//    	{
//    		ClientProxyContext.buildCallContext(restServiceID.getUrlParams(), callContext, null);
//    	}
    	/**
    	 * @fixed biaoping.yin 2010-10-11 end
    	 */
    	 return innerRpcService(serviceID,// 服务标识
                 method,// 需要在服务上调用的方法
                 parameters ,callContext,// 服务参数
                 target
         ) ;
	}

	/**
     * 将通用地址列表转换为jgroup地址列表
     * @param addresses
     * @return
     */
    private Vector<Address> convertJGroupAddress(List<RPCAddress> addresses)
    {
    	Vector<Address> ret = new Vector<Address>(addresses.size());
    	for(RPCAddress address :addresses)
    	{
    		if(address.getOrigineAddress() == null)
    			throw new IllegalArgumentException("address[" + address.getServer_uuid() + "]不存在.");
    		ret.add((Address)address.getOrigineAddress());
    	}
    	return ret;
    }

    private Object rpcJGroupService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {
        Target target = !serviceID.isRestStyle()?(serviceID).getTarget():(serviceID).getRestfulTarget();
        List<RPCAddress> list = target.getTargets();
//        Vector<Address> list = (Vector<Address>) target.getTargets();
        RpcDispatcher dispatcher = JGroupHelper.getJGroupHelper().getRpcDispatcher();
        Class<?>[] paramsTypes = method.getParameterTypes();
        Object[] params = new Object[] { serviceID, method.getName(), parameters, paramsTypes };
        Class[] rpTypes = new Class[] { RemoteServiceID.class, String.class, Object[].class, Class[].class };

        if (list.size() == 1)
        {
            Object ret = dispatcher.callRemoteMethod((Address)list.get(0).getOrigineAddress(), "callMethod", params, rpTypes, serviceID
                    .getResultMode(), serviceID.getTimeout(),callContext);
            return ret;
        }
        else if (target.isAll())
        {

            RspList rsp_list = dispatcher.callRemoteMethods(null, "callMethod", params, rpTypes, serviceID
                    .getResultMode(), serviceID.getTimeout(),callContext);

            return buildResult(rsp_list, serviceID.getResultType());

        }
        else
        {
            // if (target.ismuticast())
            {
                RspList rsp_list = dispatcher.callRemoteMethods(convertJGroupAddress(list), "callMethod", params, rpTypes, serviceID
                        .getResultMode(), serviceID.getTimeout(),true,callContext);
                return buildResult(rsp_list, serviceID.getResultType());
            }
 
        }
    }

    @SuppressWarnings("unchecked")
    public Object rpcMinaService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext) throws Throwable
    {

//        Target target = serviceID.getTarget();
//        List<RPCAddress> list = target.getTargets();
//
//        Class[] paramsTypes = method.getParameterTypes();
//        Object[] params = new Object[] { serviceID, method.getName(), parameters, paramsTypes };
//        Class[] rpTypes = new Class[] { ServiceID.class, String.class, Object[].class, Class[].class };
//
//        if (list.size() == 1 && !target.isAll())
//        {
//            RPCClient client = RPCClient.getInstance();
//            Object ret = client.callRemoteMethod(list.get(0), "callMethod", params, rpTypes, serviceID.getResultMode(),
//                    serviceID.getTimeout());
//            return ret;
//        }
//        else if (target.isAll())
//        {
//            RPCClient client = RPCClient.getInstance();
//            try
//            {
//                RPCResponseList result = client.callRemoteMethod(Util.getAllAddress(), "callMethod",
//                        params, rpTypes, serviceID.getResultMode(), serviceID.getTimeout(), false, null);
//                return buildResult(result, serviceID.getResultType());
//            }
//            catch (Throwable e)
//            {
//                // TODO Auto-generated catch block
//                throw e;
//            }
//
//        }
//        else
//        {
//            RPCClient client = RPCClient.getInstance();
//            try
//            {
//                RPCResponseList result = client.callRemoteMethod(list, "callMethod", params, rpTypes, serviceID
//                        .getResultMode(), serviceID.getTimeout(), false, null);
//                return buildResult(result, serviceID.getResultType());
//            }
//            catch (Throwable e)
//            {
//                // TODO Auto-generated catch block
//                throw e;
//            }
//            //			
//
//        }
	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_MINA,callContext);
    }
    
    @SuppressWarnings("unchecked")
    public Object rpcNettyService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext) throws Throwable
    {

//        Target target = serviceID.getTarget();
//        List<RPCAddress> list = target.getTargets();
//
//        Class[] paramsTypes = method.getParameterTypes();
//        Object[] params = new Object[] { serviceID, method.getName(), parameters, paramsTypes };
//        Class[] rpTypes = new Class[] { ServiceID.class, String.class, Object[].class, Class[].class };
//
//        if (list.size() == 1 && !target.isAll())
//        {
//            RPCClient client = RPCClient.getInstance();
//            Object ret = client.callRemoteMethod(list.get(0), "callMethod", params, rpTypes, serviceID.getResultMode(),
//                    serviceID.getTimeout());
//            return ret;
//        }
//        else if (target.isAll())
//        {
//            RPCClient client = RPCClient.getInstance();
//            try
//            {
//                RPCResponseList result = client.callRemoteMethod(Util.getAllAddress(), "callMethod",
//                        params, rpTypes, serviceID.getResultMode(), serviceID.getTimeout(), false, null);
//                return buildResult(result, serviceID.getResultType());
//            }
//            catch (Throwable e)
//            {
//                // TODO Auto-generated catch block
//                throw e;
//            }
//
//        }
//        else
//        {
//            RPCClient client = RPCClient.getInstance();
//            try
//            {
//                RPCResponseList result = client.callRemoteMethod(list, "callMethod", params, rpTypes, serviceID
//                        .getResultMode(), serviceID.getTimeout(), false, null);
//                return buildResult(result, serviceID.getResultType());
//            }
//            catch (Throwable e)
//            {
//                // TODO Auto-generated catch block
//                throw e;
//            }
//            //            
//
//        }
            return _rpcService(serviceID,// 服务标识
                        method,// 需要在服务上调用的方法
                        parameters, // 服务参数
                        Target.BROADCAST_TYPE_NETTY,callContext);
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    public Object rpcWebServiceService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {


	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_WEBSERVICE,callContext);
    }
    
    @SuppressWarnings("unchecked")
    public Object rpcJMSService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {


	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_JMS,callContext);
    }
    
    @SuppressWarnings("unchecked")
    public Object rpcRMIService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {


	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_RMI,callContext);
    }
    
    public Object rpcHTTPService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {


	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_HTTP,callContext);
    }
    
    @SuppressWarnings("unchecked")
    public Object rpcEJBService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters // 服务参数
            ,RemoteCallContext callContext
    ) throws Throwable
    {


	    	return _rpcService(serviceID,// 服务标识
	    	            method,// 需要在服务上调用的方法
	    	            parameters, // 服务参数
	    	    		Target.BROADCAST_TYPE_EJB,callContext);
    }
    
    
    @SuppressWarnings("unchecked")
    public Object _rpcService(RemoteServiceID serviceID,// 服务标识
            Method method,// 需要在服务上调用的方法
            Object[] parameters, // 服务参数
    		String protocol,RemoteCallContext callContext) throws Throwable
    {
    	//setDebug
    	Target target = !serviceID.isRestStyle()?(serviceID).getTarget():(serviceID).getRestfulTarget();
        List<RPCAddress> list = target.getTargets();

        Class[] paramsTypes = method.getParameterTypes();
        Object[] params = new Object[] { serviceID, method.getName(), parameters, paramsTypes };
        Class[] rpTypes = new Class[] { RemoteServiceID.class, String.class, Object[].class, Class[].class };

        if (list.size() == 1 && !target.isAll())
        {
            RPCClient client = RPCClient.getInstance();
            Object ret = client.callRemoteMethod(list.get(0), "callMethod", params, rpTypes, serviceID.getResultMode(),
                    serviceID.getTimeout(),protocol,callContext);
            return ret;
        }
        else if (target.isAll())
        {
            RPCClient client = RPCClient.getInstance();
            try
            {
                RPCResponseList result = client.callRemoteMethod(Util.getAllAddress(protocol), "callMethod",
                        params, rpTypes, serviceID.getResultMode(), serviceID.getTimeout(), false, null,protocol,callContext);
                return buildResult(result, serviceID.getResultType());
            }
            catch (Throwable e)
            {                
                throw e;
            }

        }
        else
        {
            RPCClient client = RPCClient.getInstance();
            try
            {
                RPCResponseList result = client.callRemoteMethod(list, "callMethod", params, rpTypes, serviceID
                        .getResultMode(), serviceID.getTimeout(), false, null,protocol,callContext);
                return buildResult(result, serviceID.getResultType());
            }
            catch (Throwable e)
            {
                // TODO Auto-generated catch block
                throw e;
            }
            //			

        }
    }

    private Map buildMapResult(RspList rsp_list)
    {

        Map map = new HashMap();
        Collection values = rsp_list.values();
        for (Iterator it = values.iterator(); it.hasNext();)
        {
            Rsp rsp = (Rsp) it.next();
            if (rsp.getValue() != null)
                map.put(rsp.getSender().toString(), rsp.getValue());
        }
        return map;

    }

    private Object getFirst(RspList rsp_list)
    {
        return rsp_list.getFirst();
    }

    /**
     * Returns the results from non-suspected members that are not null.
     */
    private List getListResults(RspList rsp_list)
    {

        return rsp_list.getResults();
    }

    private Object getFirst(RPCResponseList rsp_list)
    {

        return rsp_list.getFirst();

    }

    /**
     * Returns the results from non-suspected members that are not null.
     */
    private List getListResults(RPCResponseList rsp_list)
    {

        return rsp_list.getResults();
    }

    private Map buildMapResult(RPCResponseList rsp_list)
    {

        Map map = new HashMap();
        Collection values = rsp_list.values();
        for (Iterator it = values.iterator(); it.hasNext();)
        {
        	RPCResponse rsp = (RPCResponse) it.next();
            if (rsp.getValue() != null)
                map.put(rsp.getSender().toString(), rsp.getValue());
        }
        return map;

    }

    private Object buildResult(RspList rsp_list, int resultType)
    {

        if (rsp_list != null && rsp_list.size() > 0)
        {
            switch (resultType)
            {
            case RemoteServiceID.result_first:
                return getFirst(rsp_list);
            case RemoteServiceID.result_list:
                return getListResults(rsp_list);
            case RemoteServiceID.result_map:
                return buildMapResult(rsp_list);
            case RemoteServiceID.result_object:
                return getFirst(rsp_list);
            case RemoteServiceID.result_rsplist:
//                return rsp_list;
            	return convertJGroupResult(rsp_list);
            default:
//                return rsp_list;
            	return convertJGroupResult(rsp_list);
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 将jgroup的结果集转换为RPCResponseList的通用类型
     * @param rsp_list
     * @return
     */
    private RPCResponseList convertJGroupResult(RspList rsp_list)
    {
    	Collection<Rsp> rsps = rsp_list.values();
    	
    	if(rsps != null)
    	{
    		List<RPCResponse> rpcrsps = new ArrayList<RPCResponse>();
    		Map<RPCAddress,RPCResponse> rsps_=new HashMap<RPCAddress,RPCResponse>();
    		for(Rsp rsp :rsps)
    		{
    			//jgroups的地址已唯一标识为准
//    			Address ipaddr = (Address)JGroupHelper.getJGroupHelper().getPhysicalAddress(rsp.getSender()); 
    			
                RPCAddress rpcaddr = new RPCAddress(rsp.getSender().toString(),Target.BROADCAST_TYPE_JRGOUP);
                //System.out.println(",rsp.getValue():"+rsp.getValue());
    			RPCResponse response = new RPCResponse(rpcaddr,rsp.getValue());
    			rpcrsps.add(response);
    			rsps_.put(rpcaddr, response);
    		}
    		return new RPCResponseList(rpcrsps,rsps_);
    	}
    	return null;
    }

    private Object buildResult(RPCResponseList rsp_list, int resultType)
    {

        if (rsp_list != null && rsp_list.size() > 0)
        {
            switch (resultType)
            {
            case RemoteServiceID.result_first:
                return getFirst(rsp_list);
            case RemoteServiceID.result_list:
                return getListResults(rsp_list);
            case RemoteServiceID.result_map:
                return buildMapResult(rsp_list);
            case RemoteServiceID.result_object:
                return getFirst(rsp_list);
            case RemoteServiceID.result_rsplist:
                return rsp_list;
            default:
                return rsp_list;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * 启动各协议服务，协议配置在文件： org/frameworkset/spi/manager-rpc-service.xml
     * rpc.startup.protocol属性中 <property name="rpc.startup.protocol"
     * value="mina,jgroup"/>
     */

    public void startServerProtocols(List<String> startupProtocols)
    {
        for (String protocol : startupProtocols)
        {
            startServerProtocol(protocol);
        }
    }
    
    public void startJMSServer()
    {
        JMSServer.getJMSServer().start();
        this.jmsenabled = true;
    }
    
    public void startRMIServer()
    {
        RMIServer.getRMIServer().start();
        this.rmienabled = true;
    }
    
    public void startHTTPServer()
    {
        HttpServer.getHttpServer().start();
        this.httpenabled = true;
    }
    public void stopHTTPServer()
    {
    	HttpServer.getHttpServer().stop();
        this.httpenabled = false;
    }
    public void stopJMSServer()
    {
        JMSServer.getJMSServer().stop();
        this.jmsenabled = false;
    }
    
    public void startJGroupServer()
    {
        JGroupHelper.getJGroupHelper().start();
        this.clusterenabled = true;
    }
    public void stopJGroupServer()
    {
        JGroupHelper.getJGroupHelper().stop();
        this.clusterenabled = false;
    }
    public void startMinaServer() 
    {
        MinaRPCServer.getMinaRPCServer().start();
        this.clusterenabled = true;
    }
    
    public void stopMinaServer() 
    {
        MinaRPCServer.getMinaRPCServer().stop();
        this.clusterenabled = false;
    }
    
    public void startNettyServer() 
    {
        NettyRPCServer.getNettyRPCServer().start();
        this.clusterenabled = true;
    }
    
    public void stopNettyServer() 
    {
        NettyRPCServer.getNettyRPCServer().stop();
        this.clusterenabled = false;
    }

    public void startServerProtocol(String protocol)
    {
        if (protocol.equals(Target.BROADCAST_TYPE_MINA))
        {
            
            try
            {
                this.startMinaServer();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_NETTY))
        {
            this.startNettyServer();
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
        {
            this.startJGroupServer();
        }
        
        else if (protocol.equals(Target.BROADCAST_TYPE_JMS))
        {
            this.startJMSServer();
        }
        
        else if (protocol.equals(Target.BROADCAST_TYPE_RMI))
        {
            this.startRMIServer();
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_HTTP))
        {
            this.startHTTPServer();
        }
    }
    public void stopServerProtocol(String protocol)
    {
        if (protocol.equals(Target.BROADCAST_TYPE_MINA))
        {
            
            try
            {
                this.stopMinaServer();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_NETTY))
        {
            this.stopNettyServer();
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
        {
            this.stopJGroupServer();
        }
        
        else if (protocol.equals(Target.BROADCAST_TYPE_JMS))
        {
            this.stopJMSServer();
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_RMI))
        {
            this.stopRMIServer();
        }
        else if (protocol.equals(Target.BROADCAST_TYPE_HTTP))
        {
            this.stopHTTPServer();
        }
    }
    
    
    
    
    
    public void stopRMIServer() {
		RMIServer.getRMIServer().stop();
		
	}

	public List<RPCAddress> getAllNodes()
    {
        return getAllNodes(Util.default_protocol);
    }
    
    public List<RPCAddress> getAllNodes(String protocol)
    {
        List<RPCAddress> nodes = null;
        //目前系统只提供两种协议实现：mina和jgroup
        if(protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
        {
            try
            {
                List<Address> jgoups = JGroupHelper.getJGroupHelper().getAppservers();
                nodes = new ArrayList<RPCAddress>();
                for (Address addr : jgoups)
                {
                    IpAddress ipaddr = (IpAddress)addr; 
                    RPCAddress rpcaddr = new RPCAddress(ipaddr.getIpAddress(), ipaddr.getPort(),ipaddr,protocol);
                    nodes.add(rpcaddr);
                }   
            }
            catch(Exception e)
            {
                e.printStackTrace();
                nodes = new ArrayList<RPCAddress>(0);
            }
//            return ret;
        }
        else
        {
            //mina协议中本地无需启动远程服务mina，即可获取其监控的所有的远程服务器地址，这一点和
            //jroup不一样，jgroup必须启动远程jgroup协议才能获取到所有的服务器地址
            //获取到的远程地址是否可以使用还需要对地址进行校验才行
            nodes = Util.getAllAddress(protocol);
            if(nodes == null)
                nodes = new ArrayList<RPCAddress>(0);
        }
        return nodes;
        
        
    }
    
    /**
     * 校验远程地址，是否可以使用     * 
     * 目前系统只提供两种协议实现：mina和jgroup
     * @param address
     * @return
     */
    public boolean validateAddress(RPCAddress address)
    {
//        if(address.getProtocol().equals(Target.BROADCAST_TYPE_JRGOUP))//jgroup
//        {
//            return JGroupHelper.getJGroupHelper().validateAddress(address);
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_MINA))//mina
//        {
//            return MinaRPCServer.getMinaRPCServer().validateAddress(address);
//        }
//        
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_NETTY))//NETTY
//        {
//            return NettyRPCServer.getNettyRPCServer().validateAddress(address);
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_JMS))//jms
//        {
//            return true;
//        }        
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_WEBSERVICE))//webservice
//        {
//            return true;
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_RMI))//jms
//        {
//            return true;
//        }        
//        
//        else
//            return false;
    	return RPCValidator.validator(address);
    }
    
    /**
     * 校验远程地址，是否可以使用     * 
     * 目前系统只提供两种协议实现：mina和jgroup
     * @param address
     * @return
     */
    public boolean validateAddress(RPCAddress address,String user,String password)
    {
//        if(address.getProtocol().equals(Target.BROADCAST_TYPE_JRGOUP))//jgroup
//        {
//            return JGroupHelper.getJGroupHelper().validateAddress(address);
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_MINA))//mina
//        {
//            return MinaRPCServer.getMinaRPCServer().validateAddress(address);
//        }
//        
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_NETTY))//NETTY
//        {
//            return NettyRPCServer.getNettyRPCServer().validateAddress(address);
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_JMS))//jms
//        {
//            return true;
//        }        
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_WEBSERVICE))//webservice
//        {
//            return true;
//        }
//        else if(address.getProtocol().equals(Target.BROADCAST_TYPE_RMI))//jms
//        {
//            return true;
//        }        
//        
//        else
//            return false;
    	return RPCValidator.validator(address, user, password);
    }
    
    /**
     * 根据远程调用目标ip地址和端口获取对应服务器中返回的结果
     * 适用于mina和jgroup协议
     * 其他协议应该通过特定协议的接口和带协议的getRPCResult方法获取
     * 对应服务器的接口
     * 
     * @param ip
     * @param port
     * @param values
     * @return
     * @throws Throwable 
     * @deprecated
     */
    public static Object getRPCResult(String ip, String port, Object values) throws Throwable
    {
        if (values == null)
            return null;
        Object value = null; 
        if (values instanceof RspList)
        {
            RspList rl = (RspList) values;
            try
            {
                value =  rl.getValue(new IpAddress(ip, Integer.parseInt(port)));
            }
            catch (NumberFormatException e)
            {
                throw new RemoteException(e);
            }
            catch (UnknownHostException e)
            {
                throw new RemoteException(e);

            }
            catch (Exception e)
            {
                throw new RemoteException(e);
            }
        }
        else if(values instanceof RPCResponseList)
        {
            RPCResponseList rl = (RPCResponseList)values;
            
            value =  rl.getValue(new RPCAddress(ip, Integer.parseInt(port),null,Target.BROADCAST_TYPE_MINA));
        }
        else if (values instanceof Map)
        {
            Map vs = (Map) values;
            value =  vs.get(ip + ":" + port);
        }
        else
        {
            value =  values;
        }
//        if(value != null && value instanceof Throwable)
//        {
//            throw new RemoteException((Throwable)value);
//        }
//        else
        RPCClient.handleException(value);
        {
            return value;
        }
        
    }
    
    /**
     * 根据url地址获取对应的结果值
     * 
     * @param url 对应的远程地址url
     * @param values 多播调用返回的结果集
     * @param protocol rpc对应的远程协议，
     * @see org.frameworkset.remote.Target.BROADCAST_TYPE_MUTICAST
            org.frameworkset.remote.Target.BROADCAST_TYPE_UNICAST        
        org.frameworkset.remote.Target.BROADCAST_TYPE_JRGOUP    
        org.frameworkset.remote.Target.BROADCAST_TYPE_MINA    
        org.frameworkset.remote.Target.BROADCAST_TYPE_JMS        
        org.frameworkset.remote.Target.BROADCAST_TYPE_RMI
        org.frameworkset.remote.Target.BROADCAST_TYPE_EJB
        org.frameworkset.remote.Target.BROADCAST_TYPE_CORBA            
        org.frameworkset.remote.Target.BROADCAST_TYPE_WEBSERVICE
     * @return
     */
    public static Object getRPCResult(String url, Object values,String protocol) throws Throwable
    {
        if (values == null)
            return null;
        Object value = null;
        if (values instanceof RspList)
        {
            RspList rl = (RspList) values;
            try
            {
                String[] address = url.split(":");
                value = rl.getValue(new IpAddress(address[0], Integer.parseInt(address[1])));
            }
            catch (NumberFormatException e)
            {
                throw new RemoteException(e);
            }
            catch (UnknownHostException e)
            {
                throw new RemoteException(e);

            }
            catch (Exception e)
            {
                throw new RemoteException(e);
            }
        }
        else if(values instanceof RPCResponseList)
        {
            RPCResponseList rl = (RPCResponseList)values;
            RPCAddress address = TargetImpl.buildTarget(url,protocol);
            value =  rl.getValue(address);
        }
        else if (values instanceof Map)
        {
            Map vs = (Map) values;
            RPCAddress address = TargetImpl.buildTarget(url,protocol);
            value =  vs.get(address.toString());
        }
        else
        {
            value =  values;
        }
        RPCClient.handleException(value);
//        if(value != null && value instanceof Throwable)
//        {
//            throw new RemoteException((Throwable)value);
//        }
//        else
        {
            return value;
        }
        
    }
    public static int getRPCResultSize(Object values)
    {
    	 if(values instanceof RPCResponseList)
         {
             RPCResponseList rl = (RPCResponseList)values;
//             RPCAddress address = Target.buildTarget(url,protocol);
             return rl.size();
         }
//         else if (values instanceof Map)
//         {
//             Map vs = (Map) values;
//             RPCAddress address = Target.buildTarget(url,protocol);
//             value =  vs.get(address.toString());
//         }
         else
         {
             return 1;
         }
         
    }
    /**
     * 根据url地址获取对应的结果值
     * 
     * @param url 对应的远程地址url
     * @param values 多播调用返回的结果集
     * @param protocol rpc对应的远程协议，
     * @see org.frameworkset.remote.Target.BROADCAST_TYPE_MUTICAST
            org.frameworkset.remote.Target.BROADCAST_TYPE_UNICAST        
        org.frameworkset.remote.Target.BROADCAST_TYPE_JRGOUP    
        org.frameworkset.remote.Target.BROADCAST_TYPE_MINA    
        org.frameworkset.remote.Target.BROADCAST_TYPE_JMS        
        org.frameworkset.remote.Target.BROADCAST_TYPE_RMI
        org.frameworkset.remote.Target.BROADCAST_TYPE_EJB
        org.frameworkset.remote.Target.BROADCAST_TYPE_CORBA            
        org.frameworkset.remote.Target.BROADCAST_TYPE_WEBSERVICE
     * @return
     */
    public static Object getRPCResult(int index, Object values) throws Throwable
    {
        if (values == null)
            return null;
        Object value = null;
//        if (values instanceof RspList)
//        {
//            RspList rl = (RspList) values;
//            try
//            {
//                String[] address = url.split(":");
//                value = rl.getValue(new IpAddress(address[0], Integer.parseInt(address[1])));
//            }
//            catch (NumberFormatException e)
//            {
//                throw new RemoteException(e);
//            }
//            catch (UnknownHostException e)
//            {
//                throw new RemoteException(e);
//
//            }
//            catch (Exception e)
//            {
//                throw new RemoteException(e);
//            }
//        }
//        else 
        if(values instanceof RPCResponseList)
        {
            RPCResponseList rl = (RPCResponseList)values;
//            RPCAddress address = Target.buildTarget(url,protocol);
            value =  rl.getValue(index);
        }
//        else if (values instanceof Map)
//        {
//            Map vs = (Map) values;
//            RPCAddress address = Target.buildTarget(url,protocol);
//            value =  vs.get(address.toString());
//        }
        else
        {
            value =  values;
        }
//        if(value != null && value instanceof Throwable)
//        {
//            throw new RemoteException((Throwable)value);
//        }
//        else
        RPCClient.handleException(value);
        {
            return value;
        }
        
    }
    private static long rpc_request_timeout = BaseSPIManager.getIntProperty("rpc.request.timeout",60) * 1000;
    public static long getRPCRequestTimeout()
    {
        return rpc_request_timeout;
    }
//    public static Map<String, ServiceID> serviceids = new java.util.WeakHashMap<String, ServiceID>();

//    public static ServiceID buildServiceID(Map<String,ServiceID> serviceids,String serviceid, int serviceType, String providertype,BaseApplicationContext applicationcontext)
//    {
//        if(!serviceid.startsWith("(rest::"))//rest风格的地址不做缓冲
//        {
//            String key = serviceid ;
//            if(providertype != null)
//                key = serviceid + "|" + providertype;
//    //        SoftReference<ServiceID> reference;
//            ServiceID serviceID = serviceids.get(key);
//            if (serviceID != null)
//                return serviceID;
//            
//            synchronized (serviceids)
//            {
//                serviceID = serviceids.get(key);
//                if (serviceID != null)
//                    return serviceID;
//                long timeout = getRPCRequestTimeout();
//                serviceID = new ServiceIDImpl(serviceid, providertype, GroupRequest.GET_ALL, timeout, RemoteServiceID.result_rsplist,
//                        serviceType, applicationcontext);
//               
//                serviceids.put(key, serviceID);
//            }
//            return serviceID;
//        }
//        else
//        {
//            long timeout = getRPCRequestTimeout();
//            ServiceID serviceID = new ServiceIDImpl(serviceid, providertype, GroupRequest.GET_ALL, timeout, RemoteServiceID.result_rsplist,
//                    serviceType,applicationcontext);
//            return serviceID;
//        }
//
//    }
    
//    public static ServiceID buildServiceID(String serviceid, int serviceType, BaseApplicationContext applicationcontext)
//    {
//       
////        SoftReference<ServiceID> reference;
//        
//        
//            long timeout = getRPCRequestTimeout();
//            ServiceID serviceID = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, timeout, RemoteServiceID.result_rsplist,
//                    serviceType,applicationcontext);
//           
//           
//        return serviceID;
//
//    }
    public static RemoteServiceID buildClientServiceIDFromRestID(RemoteServiceID restid)
    {
    	 long timeout = getRPCRequestTimeout();
         RemoteServiceID serviceID = new ServiceIDImpl(restid.getNextRestfulServiceAddress(), 
								        		 restid.getProviderID(), 
								        		 restid.getApplicationContext(),
								        		 restid.getContainerType(),
								        		 restid.getResultMode(), 
								        		 timeout, 
								        		 restid.getResultType(),
								        		 restid.getBean_type());
//         serviceID.setUrlParams(restid.getUrlParams());
//         serviceID.apendUrlParams(restid);
         serviceID.setInfType(restid.getInfType());
         return serviceID;
    }
    public static RemoteServiceID buildClientServiceIDFromRestID(RemoteServiceID restid,String serviceid)
    {
    	 long timeout = getRPCRequestTimeout();
         RemoteServiceID serviceID = new ServiceIDImpl(serviceid, 
								        		 restid.getProviderID(), 
								        		 restid.getApplicationContext(),
								        		 restid.getContainerType(),
								        		 restid.getResultMode(), 
								        		 timeout, 
								        		 restid.getResultType(),
								        		 restid.getBean_type());
         serviceID.setInfType(restid.getInfType());
//         serviceID.apendUrlParams(restid);
         return serviceID;
    }
    public static RemoteServiceID buildClientServiceID(String serviceid,  String applicationcontext,int containerType)
    {
       
//        SoftReference<ServiceID> reference;
        
        
            long timeout = getRPCRequestTimeout();
            RemoteServiceID serviceID = new ServiceIDImpl(serviceid, null, applicationcontext,containerType,GroupRequest.GET_ALL, timeout, RemoteServiceID.result_rsplist,
                                                    ServiceID.PROPERTY_BEAN_SERVICE);
           
           
        return serviceID;

    }
    public static void destroy()
    {
    	if(instance != null)
    	{
    		instance.stopRPCServices();
    		instance = null;
    	}
    }
    public void stopRPCServices()
    {
        if(startupProtocols == null || startupProtocols.size() <= 0)
            return;
        for (String protocol : startupProtocols)
        {
            stopServerProtocol(protocol);
        }
    }
    
    public static String buildContextAddress(String protocol,String ip,String port)
    {
        StringBuffer address = new StringBuffer();
        address.append("(").append(protocol).append("::").append(ip).append(":").append(port)
        .append(")");
        return address.toString();
    }
    
    public static String buildContextAddress(String protocol, String ip,
			String port, String contextpath) {
    	StringBuffer address = new StringBuffer();
        address.append("(").append(protocol).append("::").append(ip).append(":").append(port);
        if(contextpath != null && contextpath.length() > 1 )
        {
        	if(contextpath.startsWith("/"))
        		address.append(contextpath);
        	else
        		address.append("/").append(contextpath);
        }
        address.append(")");
        return address.toString();
	}
    
    public static String buildContextAddress(String protocol,String url)
    {
        StringBuffer address = new StringBuffer();
        address.append("(").append(protocol).append("::").append(url)
        .append(")");
        return address.toString();
    }
    
    public static String buildAddress(String protocol,String ip,String port,String serviceid)
    {
        StringBuffer address = new StringBuffer();
        address.append(buildContextAddress( protocol, ip, port)).append("/").append(serviceid);
        return address.toString();
    }
    
    public static String buildAddress(String protocol,String url,String serviceid)
    {
        StringBuffer address = new StringBuffer();
        address.append(buildContextAddress( protocol, url))
        .append("/").append(serviceid);
        return address.toString();
    }
    
    public static String buildAddress(RPCAddress rpcaddress,String serviceid)
    {
        StringBuffer address = new StringBuffer();
        address.append(TargetImpl.buildURL(rpcaddress))
        .append("/").append(serviceid);
        return address.toString();
    }
    
    
    public static String buildAuthAddress(String protocol,String ip,String port,String serviceid,String user,String password)
    {
        StringBuffer address = new StringBuffer();
        address.append(buildAddress( protocol, ip, port, serviceid)).append("?user=").append(user).append("&password=").append(password);
        return address.toString();
    }
    
    public static String buildAuthAddress(String protocol,String url,String serviceid,String user,String password)
    {
        StringBuffer address = new StringBuffer();
        address.append(buildAddress( protocol, url, serviceid)).append("?user=").append(user).append("&password=").append(password);
        return address.toString();
    }
    
    public static String buildAuthAddress(RPCAddress rpcadress,String serviceid,String user,String password)
    {
        StringBuffer address = new StringBuffer();
        address.append(buildAddress( rpcadress, serviceid)).append("?user=").append(user).append("&password=").append(password);
        return address.toString();
    }
    
    static
    {
    	
            // use reflection and catch the Exception to allow PoolMan to work with 1.2 VM's
            BaseApplicationContext.addShutdownHook(new Runnable(){

				public void run() {
					 try {
				           
				           destroy();
				           // GenericPoolManager.getInstance().destroyPools();
				        } catch (Exception e) {
				            System.out.println("Unable to properly shutdown: " + e);
				        }
					
				}
            	
            });
    }

	

}
