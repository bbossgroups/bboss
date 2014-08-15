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

import java.util.List;

/**
 * 
 * <p>
 * Title: Target.java
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
 * @Date 2009-4-20 下午05:30:19
 * @author biaoping.yin
 * @version 1.0
 */
public interface Target extends java.io.Serializable
{

	
	/**
	 * BROADCAST_TYPE_MUTICAST，BROADCAST_TYPE_UNICAST为遗留协议，新版系统不再支持
	 * 分别由以下几种类型替换
	 * BROADCAST_TYPE_JRGOUP
	 * BROADCAST_TYPE_MINA
	 * BROADCAST_TYPE_JMS
	 * BROADCAST_TYPE_WEBSERVICE
	 * 	 * 
	 * 指定相应的协议后，需要配置每种协议的相关配置参数
	 */
	
	public static final String	BROADCAST_TYPE_MUTICAST		= "muticast";

	public static final String	BROADCAST_TYPE_UNICAST		= "unicast";
	
	public static final String	BROADCAST_TYPE_JRGOUP		= "jgroup";

	public static final String	BROADCAST_TYPE_MINA			= "mina";
	public static final String BROADCAST_TYPE_NETTY         = "netty";

	public static final String	BROADCAST_TYPE_JMS			= "jms";
	
	public static final String	BROADCAST_TYPE_RMI			= "rmi";
	public static final String	BROADCAST_TYPE_EJB			= "ejb";
	public static final String	BROADCAST_TYPE_HTTP			= "http";
	public static final String	BROADCAST_TYPE_HTTPS			= "https";
	public static final String	BROADCAST_TYPE_CORBA			= "corba";
	public static final String	BROADCAST_TYPE_WEBSERVICE	= "webservice";
	
	public static final String BROADCAST_TYPE_REST   = "rest";
	public static final String REST_LOCAL = "_local_";

	

	public String getStringTargets();

	
	public boolean ismuticast();

	public boolean isunicast();

	
	
	
	
	

	
	
	
	public boolean protocol_jgroup();
	public boolean protocol_rest();
	public boolean protocol_mina();
	
	public boolean protocol_netty();
	
	public boolean protocol_webservice();
	
	public boolean protocol_jms();
	
	public boolean protocol_rmi();
	
	public boolean protocol_http();
	
	public boolean protocol_ejb();
	
	
	

	public List<RPCAddress> getTargets();
	
	

	public boolean isAll();

	public boolean isSelf();


	public String getFirstNode();


	public String getNextNode();
	

}
