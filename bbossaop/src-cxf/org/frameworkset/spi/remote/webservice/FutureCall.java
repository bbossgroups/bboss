/**
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
package org.frameworkset.spi.remote.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.Util;

/**
 * <p>
 * FutureCall.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2009-11-8
 * @author biaoping.yin
 * @version 1.0
 */
public class FutureCall extends BaseFutureCall
{
    private String ws_servertype = "cxf";
    
    static Map<String,RPCCallService> clients = new HashMap<String,RPCCallService>();
    
    /**
     * @param srcmsg
     * @param address
     * @param handler
     */
    public FutureCall(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler,String ws_servertype)
    {
        super(srcmsg, address, handler);
        this.ws_servertype = ws_servertype; 
        
    }
    private static Object lock = new Object();
    protected RPCMessage _call() throws Exception
    {
    	 String url = address.getWebServiceURL(Util
                 .getRPCCallServicePort());
    	RPCCallService client = clients.get(url);
        if(client == null)
        {
            synchronized(lock)
            {
                if(client == null)
                {
                    QName serviceName = new QName("http://webservice.remote.spi.frameworkset.org/", "RPCCallService");
                    QName portName = new QName("http://webservice.remote.spi.frameworkset.org/", Util.getRPCCallServicePort());
            
                    Service service = Service.create(serviceName);
//                    String url = address.getWebServiceURL(Util
//                            .getRPCCallServicePort());
                    service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, url);
                    client = service.getPort(portName,
                            org.frameworkset.spi.remote.webservice.RPCCallService.class);
                 // Okay, are you sick of configuration files ?
                    // This will show you how to configure the http conduit dynamically
                    
                    
                    Client client_ = ClientProxy.getClient(client);                    
                    HTTPConduit http = (HTTPConduit) client_.getConduit();
                    /**
                     * 初始化cxf客服端连接参数开始
                     */
                    Pro client_config = BaseSPIManager.getProBean("cxf.client.config");
                    if(client_config != null)
                    {
                        boolean enable = client_config.getBooleanExtendAttribute("enable");
                        if(enable)
                        {
                            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy(); 
                            org.apache.cxf.transports.http.configuration.ProxyServerType s;
                            try
                            {
                                BeanAccembleHelper.injectProperties(httpClientPolicy, client_config.getMap());
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
//                            httpClientPolicy.setConnectionTimeout(36000);
//                            httpClientPolicy.setAllowChunking(false);
//                            httpClientPolicy.setReceiveTimeout(32000);
//                            
//                            httpClientPolicy.setProxyServer("");
//                            httpClientPolicy.setProxyServerPort(444);
//                            httpClientPolicy.setProxyServerType(ProxyServerType.HTTP);
                            http.setClient(httpClientPolicy);
                        }
                    }
                    clients.put(url, client);
                    /**
                     * 初始化cxf客服端连接参数结束
                     */
                    
                    /**
                     * 初始化cxf客服端ssl参数开始
                     */
//                    TLSClientParameters ssl = new TLSClientParameters();
//                    ssl.setTrustManagers(SSLHelper.getTrustManagers(null, null));
//                    ssl.setKeyManagers(SSLHelper.getKeyManagers(null, null));
//                    
//                    http.setTlsClientParameters((TLSClientParameters)null);
                    /**
                     * 初始化cxf客服端ssl参数结束
                     */
                    /**
                     * 初始化cxf客服端鉴权参数开始
                     */
//                    http.setAuthorization(authorization);
                    /**
                     * 初始化cxf客服端鉴权参数结束
                     */
                }
            }            
        }
        
        Object ret_ = client.sendRPCMessage(Util.getEncoder().encoder(srcmsg));
        RPCMessage ret = (RPCMessage)Util.getDecoder().decoder(ret_);
        return ret;
    }
    
    
    
    
}
