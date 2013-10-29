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

package org.frameworkset.mq;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.assemble.BeanAccembleHelper;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.SSLHelper;

/**
 * <p>
 * Title: AMQConnectionFactory.java
 * </p>
 * <p>
 * Description: <property name="usepool" value="true" /> <property name="ssl"
 * value="true" /> <property name="keyStore" value="d:/client.ks" /> <property
 * name="keyStorepassword" value="123456" /> <property name="trustStore"
 * value="d:/client.ts" /> <property name="trustPassword" value="123456" />
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-11-22 上午10:56:33
 * @author biaoping.yin
 * @version 1.0
 */
public class AMQConnectionFactory extends JMSConnectionFactory
{

    
    private Map<String,Object> factoryparams;

//    @SuppressWarnings("unchecked")
//    private ProMap prefetchParams;
//    @SuppressWarnings("unchecked")
//    private ProMap redirectParams;

    private String keyStore;

    private String keyStorepassword;

    private String trustStore;

    private String trustPassword;

    private boolean ssl;

    @SuppressWarnings("unchecked")
    public AMQConnectionFactory(String connectURI, String username, String password, boolean usepool, boolean ssl,
            String keyStore, String keyStorepassword, String trustStore, String trustPassword, Map factoryparams
            )
    {

        super(connectURI, username, password, JMSConnectionFactory.JMSProvider_ACTIVEMQ, usepool, factoryparams);
        if(factoryparams != null)
        this.factoryparams = factoryparams;
//        this.prefetchParams = prefetchParams;
        this.ssl = ssl;
        this.keyStore = keyStore;
        this.keyStorepassword = keyStorepassword;
        this.trustStore = trustStore;
        this.trustPassword = trustPassword;
//        this.redirectParams = redirectParams; 
    }
    
    
    
    

    public AMQConnectionFactory(String connectURI, String username, String password, boolean usepool)
    {

        super(connectURI, username, password, JMSConnectionFactory.JMSProvider_ACTIVEMQ, usepool, null);

    }

  
    
    public AMQConnectionFactory(String connectURI, String username, String password, boolean usepool,
            ProMap factoryparams)
    {

        super(connectURI, username, password, JMSConnectionFactory.JMSProvider_ACTIVEMQ, usepool, null);
        this.factoryparams = factoryparams;
//        this.prefetchParams = prefetchParams;
//        this.redirectParams = redirectParams;
    }
    
    public AMQConnectionFactory(String connectURI, String username, String password,
            ProMap factoryparams)
    {


        this(connectURI,  username,  password,false,
                 factoryparams);
//        this.prefetchParams = prefetchParams;
//        this.redirectParams = redirectParams;
    }

    public AMQConnectionFactory(String connectURI, String username, String password)
    {

        super(connectURI, username, password, JMSConnectionFactory.JMSProvider_ACTIVEMQ, false, null);
    }

    public AMQConnectionFactory(String connectURI)
    {

        super(connectURI, null, null, JMSConnectionFactory.JMSProvider_ACTIVEMQ, false, null);
    }

    public ConnectionFactory buildConnectionFactory() throws Exception
    {

        if (!this.ssl)
            return this.createActiveMQConnectionFactory();
        else return this.createSSLConnectionFactory();
    }
    
    public static String connection_params = "connection.params";
    public static String connection_params_prefetchPolicy = "connection.params.prefetchPolicy";
    public static String connection_params_reconnectPolicy = "connection.params.reconnectPolicy";
    public static String connection_params_redirectPolicy = "connection.params.redirectPolicy";

    /**
     * tcp 协议获取链接
     * 
     * @param brokerURL
     *            tcp地址-(tcp://192.168.11.107:61615)
     * @param name
     *            用户名-（name）
     * @param password
     *            密码-（password）
     * @return
     */
    @SuppressWarnings("unchecked")
    public ConnectionFactory createActiveMQConnectionFactory() throws Exception

    {
    	
        ConnectionFactory instance = null;
        ActiveMQConnectionFactory instance_ = new ActiveMQConnectionFactory();

        // tcp://192.168.11.107:61615
        
        instance_.setBrokerURL(formatServerURL(this.connectURI));
        instance_.setUserName(this.username);
        instance_.setPassword(this.password);
        if(factoryparams != null)
        {
            try
            {
                if(factoryparams instanceof ProMap){
                    ProMap temp = (ProMap)factoryparams;
                    ProMap connectionParams = temp.getMap(connection_params);
                    ProMap globalconnectionParams = BaseSPIManager.getMapProperty(connection_params);
                    if(connectionParams != globalconnectionParams)
                    	BeanAccembleHelper.injectProperties(instance_,globalconnectionParams, connectionParams);
                    else
                    	BeanAccembleHelper.injectProperties(instance_, connectionParams);
                    
                    org.apache.activemq.ActiveMQPrefetchPolicy a = new org.apache.activemq.ActiveMQPrefetchPolicy();
                    ProMap prefetchParams = temp.getMap(connection_params_prefetchPolicy);
                    ProMap globalprefetchParams = BaseSPIManager.getMapProperty(connection_params_prefetchPolicy);
                    if(prefetchParams != globalprefetchParams)
	                    BeanAccembleHelper.injectProperties(a, 
	                    		globalprefetchParams,prefetchParams);
                    else
                    	BeanAccembleHelper.injectProperties(a,prefetchParams);
                    
                    instance_.setPrefetchPolicy(a);            
                    RedeliveryPolicy r = new RedeliveryPolicy();
                    ProMap redirectParams = temp.getMap(connection_params_redirectPolicy);
                    ProMap globalredirectParams = BaseSPIManager.getMapProperty(connection_params_redirectPolicy);
                    if(redirectParams != globalredirectParams)
                    	BeanAccembleHelper.injectProperties(r,globalredirectParams, redirectParams);
                    else
                    	BeanAccembleHelper.injectProperties(r, redirectParams);            
                    instance_.setRedeliveryPolicy(r);
                }else{
                	Map temp = (Map)factoryparams;
                    ProMap connectionParams = (ProMap)temp.get(connection_params);
                    ProMap globalconnectionParams = BaseSPIManager.getMapProperty(connection_params);
                    if(connectionParams != globalconnectionParams)
                    	BeanAccembleHelper.injectProperties(instance_,globalconnectionParams, connectionParams);
                    else
                    	BeanAccembleHelper.injectProperties(instance_, connectionParams);
                    
                    org.apache.activemq.ActiveMQPrefetchPolicy a = new org.apache.activemq.ActiveMQPrefetchPolicy();
                    ProMap prefetchParams = (ProMap)temp.get(connection_params_prefetchPolicy);
                    ProMap globalprefetchParams = BaseSPIManager.getMapProperty(connection_params_prefetchPolicy);
                    if(prefetchParams != globalprefetchParams)
	                    BeanAccembleHelper.injectProperties(a, 
	                    		globalprefetchParams,prefetchParams);
                    else
                    	BeanAccembleHelper.injectProperties(a,prefetchParams);
                    
                    instance_.setPrefetchPolicy(a);            
                    RedeliveryPolicy r = new RedeliveryPolicy();
                    ProMap redirectParams = (ProMap)temp.get(connection_params_redirectPolicy);
                    ProMap globalredirectParams = BaseSPIManager.getMapProperty(connection_params_redirectPolicy);
                    if(redirectParams != globalredirectParams)
                    	BeanAccembleHelper.injectProperties(r,globalredirectParams, redirectParams);
                    else
                    	BeanAccembleHelper.injectProperties(r, redirectParams);            
                    instance_.setRedeliveryPolicy(r);
                    
                    
                }
            }
            catch (IntrospectionException e)
            {
                throw e;
            }
        }
        // instance = new PooledConnectionFactory(instance_);
        // mapActiveMQConnectionFactory.put(key, instance);
        if (this.usepool)
        {
            instance = new PooledConnectionFactory(instance_);

        }
        else
        {
            instance = instance_;
        }

        return instance;

    }
//    /**
//     * 默认存在的参数 backOffMultiplier change reconnectDelayExponent 文档提供的参数不正确
//     */
//    public static final String[] param = { "initialReconnectDelay",
//            "maxReconnectDelay", "useExponentialBackOff",
//            "reconnectDelayExponent", "maxReconnectAttempts", "randomize" };
    
    public List<String> getParams(ProMap<String,Pro> reconnectPolicy) 
    {
        
        Set<String> keys = reconnectPolicy.keySet();
        List<String> list = new ArrayList<String>();
        if(keys.size() > 0)
        {
            Iterator<String> it = keys.iterator();
            while(it.hasNext())
            {
                String key  = it.next();
                if(key.equals("USE_FAILOVER"))
                    continue;
                if(key.startsWith("reconnectPolicy."))
                    list.add(key.substring("reconnectPolicy.".length()));
                else
                    list.add(key);
            }
        }
        
        return list;
        
        
    }
    /**
     * 是否启用故障恢复机制，如果使用了将url增加参数
     * 
     * @param mq
     * @return
     */
    private String formatServerURL(String brokerurl) {
        if(this.factoryparams != null)
        {
            ProMap reconnectPolicy = null;
            ProMap globalreconnectPolicy = null;
            if(factoryparams instanceof ProMap){
                reconnectPolicy = ((ProMap)factoryparams).getMap(connection_params_reconnectPolicy);
                globalreconnectPolicy = BaseSPIManager.getMapProperty(connection_params_reconnectPolicy);
            }else{
            	globalreconnectPolicy = BaseSPIManager.getMapProperty(connection_params_reconnectPolicy);
            	
                reconnectPolicy = (ProMap)(factoryparams).get(connection_params_reconnectPolicy);
            }
            if(globalreconnectPolicy == null)
        		return brokerurl;
            if(reconnectPolicy == null || reconnectPolicy.size() == 0)
            {
            	reconnectPolicy = globalreconnectPolicy;
            }
            StringBuffer url = new StringBuffer();
            String SERVER_URL = brokerurl;
            
            boolean USE_FAILOVER = reconnectPolicy.getBoolean("USE_FAILOVER", globalreconnectPolicy.getBoolean("USE_FAILOVER",true));
            // 是否启用故障重连机制
            
            if (USE_FAILOVER) {
                String[] exitsParam = null;
                // 判断所填写的地址是否带参数
                int isParam = SERVER_URL.indexOf("?");
                if (isParam != -1) {
                    exitsParam = (SERVER_URL.substring(isParam + 1, SERVER_URL
                            .length())).split("&");
                    SERVER_URL = SERVER_URL.substring(0, isParam);
                }
                // 填写的服务地址中是否使用了failover
                if (SERVER_URL.startsWith("failover")) {
                    url.append(SERVER_URL).append("?");
                } else {
                    url.append("failover://(").append(SERVER_URL).append(")")
                            .append("?");
                }
                List<String> keys = this.getParams(globalreconnectPolicy);
                if (exitsParam != null && exitsParam.length > 0) {
//                    List list = java.util.Arrays.asList(param);
                    for (int i = 0; i < exitsParam.length; i++) {
                        String existparam = exitsParam[i].split("=")[0];                        
                        if(!keys.contains(existparam)) {
                            url.append(exitsParam[i]).append("&");
                        }
                    }
                }
                
                
                boolean flag = false;
                // backOffMultiplier change reconnectDelayExponent
                Iterator<String> keys_ = reconnectPolicy.keySet().iterator();
                while(keys_.hasNext())
                {
                    String key_ = keys_.next();
                    if(!key_.startsWith("reconnectPolicy."))
                        continue;
                    String key = key_.substring("reconnectPolicy.".length());
                    if(flag)
                        url.append("&").append(key).append("=").append(reconnectPolicy.getString(key_,
                        		globalreconnectPolicy.getString(key_)));
                    else
                    {
                        url.append(key).append("=").append(reconnectPolicy.getString(key_,
                        		globalreconnectPolicy.getString(key_)));
                        flag = true;
                    }
                    
                }
//                url.append(param[0]).append("=").append(initialReconnectDelay)
//                        .append("&").append(param[1]).append("=").append(
//                                mq.getMaxreconnectdelay()).append("&").append(
//                                param[2]).append("=").append(
//                                mq.getUseexponentialbackoff() == 1).append("&")
//                        .append(param[3]).append("=").append(
//                                mq.getBackoffmutipler()).append("&").append(
//                                param[4]).append("=").append(
//                                mq.getMaxreconnecttimes()).append("&").append(
//                                param[5]).append("=")
//                        .append(mq.getRandomize() == 1);
                return url.toString();
            }
            return SERVER_URL;
        }
        else
        {
            return brokerurl;
        }
        
    }
    public ConnectionFactory createSSLConnectionFactory() throws Exception
    {

        ConnectionFactory instance = null;
        try
        {
            ActiveMQSslConnectionFactory instance_ = new ActiveMQSslConnectionFactory();
            // tcp://192.168.11.107:61615
            instance_.setBrokerURL(formatServerURL(this.connectURI));
            instance_.setUserName(this.username);
            instance_.setPassword(this.password);
            instance_.setKeyAndTrustManagers(SSLHelper.getKeyManagers(keyStore, this.keyStorepassword), SSLHelper
                    .getTrustManagers(trustStore, trustPassword), new java.security.SecureRandom());
            try
            {
                if(factoryparams instanceof ProMap){
                    ProMap temp = (ProMap)factoryparams;
                    ProMap connectionParams = temp.getMap(connection_params);
                    ProMap globalconnectionParams = BaseSPIManager.getMapProperty(connection_params);
                    if(connectionParams != globalconnectionParams)
                    	BeanAccembleHelper.injectProperties(instance_,globalconnectionParams, connectionParams);
                    else
                    	BeanAccembleHelper.injectProperties(instance_, connectionParams);
                    
                    org.apache.activemq.ActiveMQPrefetchPolicy a = new org.apache.activemq.ActiveMQPrefetchPolicy();
                    ProMap prefetchParams = temp.getMap(connection_params_prefetchPolicy);
                    ProMap globalprefetchParams = BaseSPIManager.getMapProperty(connection_params_prefetchPolicy);
                    if(prefetchParams != globalprefetchParams)
	                    BeanAccembleHelper.injectProperties(a, 
	                    		globalprefetchParams,prefetchParams);
                    else
                    	BeanAccembleHelper.injectProperties(a,prefetchParams);
                    
                    instance_.setPrefetchPolicy(a);            
                    RedeliveryPolicy r = new RedeliveryPolicy();
                    ProMap redirectParams = temp.getMap(connection_params_redirectPolicy);
                    ProMap globalredirectParams = BaseSPIManager.getMapProperty(connection_params_redirectPolicy);
                    if(redirectParams != globalredirectParams)
                    	BeanAccembleHelper.injectProperties(r,globalredirectParams, redirectParams);
                    else
                    	BeanAccembleHelper.injectProperties(r, redirectParams);            
                    instance_.setRedeliveryPolicy(r);
                }else{
                	Map temp = (Map)factoryparams;
                    ProMap connectionParams = (ProMap)temp.get(connection_params);
                    ProMap globalconnectionParams = BaseSPIManager.getMapProperty(connection_params);
                    if(connectionParams != globalconnectionParams)
                    	BeanAccembleHelper.injectProperties(instance_,globalconnectionParams, connectionParams);
                    else
                    	BeanAccembleHelper.injectProperties(instance_, connectionParams);
                    
                    org.apache.activemq.ActiveMQPrefetchPolicy a = new org.apache.activemq.ActiveMQPrefetchPolicy();
                    ProMap prefetchParams = (ProMap)temp.get(connection_params_prefetchPolicy);
                    ProMap globalprefetchParams = BaseSPIManager.getMapProperty(connection_params_prefetchPolicy);
                    if(prefetchParams != globalprefetchParams)
	                    BeanAccembleHelper.injectProperties(a, 
	                    		globalprefetchParams,prefetchParams);
                    else
                    	BeanAccembleHelper.injectProperties(a,prefetchParams);
                    
                    instance_.setPrefetchPolicy(a);            
                    RedeliveryPolicy r = new RedeliveryPolicy();
                    ProMap redirectParams = (ProMap)temp.get(connection_params_redirectPolicy);
                    ProMap globalredirectParams = BaseSPIManager.getMapProperty(connection_params_redirectPolicy);
                    if(redirectParams != globalredirectParams)
                    	BeanAccembleHelper.injectProperties(r,globalredirectParams, redirectParams);
                    else
                    	BeanAccembleHelper.injectProperties(r, redirectParams);            
                    instance_.setRedeliveryPolicy(r);
                }
            }
            catch (IntrospectionException e)
            {

                throw e;
            }
            // instance.getPrefetchPolicy().setAll(100);
            if (this.usepool)
            {
                instance = new PooledConnectionFactory(instance_);

            }
            else
            {
                instance = instance_;

            }
        }
        catch (NoSuchAlgorithmException e)
        {
            throw e;
        }
        catch (KeyStoreException e)
        {
            throw e;
        }
        catch (CertificateException e)
        {
            throw e;
        }
        catch (UnrecoverableKeyException e)
        {
            throw e;
        }
        catch (GeneralSecurityException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw e;
        }
        return instance;

    }

    public Destination createDestination(Session session, String name, byte defaultType) throws JMSException
    {
        if (name == null)
        {
              
            throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name="
                    + name);
            

        }
        if (name.startsWith(MQUtil.QUEUE_QUALIFIED_PREFIX))
        {
            return new ActiveMQQueue(name.substring(MQUtil.QUEUE_QUALIFIED_PREFIX.length()));
        }
        else if (name.startsWith(MQUtil.TOPIC_QUALIFIED_PREFIX))
        {
            return new ActiveMQTopic(name.substring(MQUtil.TOPIC_QUALIFIED_PREFIX.length()));
        }
        else if (name.startsWith(MQUtil.TEMP_QUEUE_QUALIFED_PREFIX))
        {
            return new ActiveMQTempQueue(name.substring(MQUtil.TEMP_QUEUE_QUALIFED_PREFIX.length()));
        }
        else if (name.startsWith(MQUtil.TEMP_TOPIC_QUALIFED_PREFIX))
        {
            return new ActiveMQTempTopic(name.substring(MQUtil.TEMP_TOPIC_QUALIFED_PREFIX.length()));
        }

        switch (defaultType)
        {
            case MQUtil.TYPE_QUEUE:
                return new ActiveMQQueue(name);
            case MQUtil.TYPE_TOPIC:
                return new ActiveMQTopic(name);
            case MQUtil.TYPE_TEMP_QUEUE:
                return new ActiveMQTempQueue(name);
            case MQUtil.TYPE_TEMP_TOPIC:
                return new ActiveMQTempTopic(name);
            default:
                throw new IllegalArgumentException("Invalid default destination type: " + defaultType + ",name=" + name);
        }
    }

}
