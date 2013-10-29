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

package org.frameworkset.tlq;

import java.util.Map;

import javax.jms.ConnectionFactory;

import org.frameworkset.mq.JMSConnectionFactory;
import org.frameworkset.spi.assemble.ProMap;

import tongtech.jms.client.JmsConnectionFactory;

/**
 * <p>Title: TLQConnectionFactory.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-8 下午05:08:13
 * @author biaoping.yin
 * @version 1.0
 */
public class TLQConnectionFactory  extends JMSConnectionFactory{

	private Map<String,Object> factoryparams;

//  @SuppressWarnings("unchecked")
//  private ProMap prefetchParams;
//  @SuppressWarnings("unchecked")
//  private ProMap redirectParams;

  private String keyStore;

  private String keyStorepassword;

  private String trustStore;

  private String trustPassword;

  private boolean ssl;

  @SuppressWarnings("unchecked")
  public TLQConnectionFactory(String connectURI, String username, String password, boolean usepool, boolean ssl,
          String keyStore, String keyStorepassword, String trustStore, String trustPassword, Map factoryparams
          )
  {

      super(connectURI, username, password, JMSConnectionFactory.JMSProvider_TLQ, usepool, factoryparams);
      if(factoryparams != null)
      this.factoryparams = factoryparams;
//      this.prefetchParams = prefetchParams;
      this.ssl = ssl;
      this.keyStore = keyStore;
      this.keyStorepassword = keyStorepassword;
      this.trustStore = trustStore;
      this.trustPassword = trustPassword;
//      this.redirectParams = redirectParams; 
  }
  
  
  
  

  public TLQConnectionFactory(String connectURI, String username, String password, boolean usepool)
  {

      super(connectURI, username, password, JMSConnectionFactory.JMSProvider_TLQ, usepool, null);

  }


  
  public TLQConnectionFactory(String connectURI, String username, String password, boolean usepool,
          ProMap factoryparams)
  {

      super(connectURI, username, password, JMSConnectionFactory.JMSProvider_TLQ, usepool, null);
      this.factoryparams = factoryparams;
//      this.prefetchParams = prefetchParams;
//      this.redirectParams = redirectParams;
  }
  
  public TLQConnectionFactory(String connectURI, String username, String password,
          ProMap factoryparams)
  {


      this(connectURI,  username,  password,false,
               factoryparams);
//      this.prefetchParams = prefetchParams;
//      this.redirectParams = redirectParams;
  }

  public TLQConnectionFactory(String connectURI, String username, String password)
  {

      super(connectURI, username, password, JMSConnectionFactory.JMSProvider_TLQ, false, null);
  }

  public TLQConnectionFactory(String connectURI)
  {

      super(connectURI, null, null, JMSConnectionFactory.JMSProvider_TLQ, false, null);
  }

  public ConnectionFactory buildConnectionFactory() throws Exception
  {

	  JmsConnectionFactory connectionFactory = new JmsConnectionFactory(
				this.connectURI);
		return connectionFactory;
      
  }
  
  public static String connection_params = "connection.params";
  public static String connection_params_prefetchPolicy = "connection.params.prefetchPolicy";
  public static String connection_params_reconnectPolicy = "connection.params.reconnectPolicy";
  public static String connection_params_redirectPolicy = "connection.params.redirectPolicy";

 


  
	//tongtech.jms.client.JmsConnectionFactory


}
