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

package org.frameworkset.spi.serviceidentity;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.remote.ServiceID;
import org.frameworkset.spi.remote.Target;

/**
 * <p>
 * Title: BaseServiceIDImpl.java
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
 * @Date 2011-5-11 上午11:42:45
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseServiceIDImpl implements ServiceID {
	protected transient BaseApplicationContext applicationcontext_;
	// SecurityContext security;
	/**
	 * serviceID: (ip:port;ip:port)/serviceid
	 */
	protected String urlParams;
	protected String serviceID;

	protected transient Target target;

	protected String service;

	protected String sourceip;

	/**
	 * 多个远程调用时，返回的结果集类型 result_object = 0; result_first = 1; result_map = 2;
	 * result_rsplist = 3; result_list = 4;
	 */
	protected transient int resultType = result_rsplist;

	protected transient int resultMode = 2;// GET_ALL

	protected int bean_type = PROVIDER_BEAN_SERVICE;

	protected String applicationContext;

	protected String sourceport;

	protected String sourcename;

	protected String providerID;

	protected transient boolean isremote = false;

	protected boolean restStyle = false;
	protected transient ServiceID rest;
	protected String nextRestNode;
	protected String fistRestNode;

	protected transient long timeout = 60 * 1000;
	private int	containerType = BaseApplicationContext.container_type_application;

	public BaseServiceIDImpl() {

	}

	public BaseServiceIDImpl(String serviceID, int resultMode, long timeout,
			int resultType, int bean_type,
			BaseApplicationContext applicationcontext) {
		this(serviceID, null, resultMode, timeout, resultType, bean_type,
				applicationcontext);
	}

	public BaseServiceIDImpl(String serviceID, int resultMode, int waittime,
			int resultType, int bean_type,
			BaseApplicationContext applicationcontext) {
		this(serviceID, null, resultMode, waittime, resultType, bean_type,
				applicationcontext);

	}

	public BaseServiceIDImpl(String serviceID, String providerID,
			int resultMode, long timeout, int resultType, int bean_type,
			BaseApplicationContext applicationcontext) {
		this.serviceID = serviceID;
		this.resultMode = resultMode;
		this.timeout = timeout;
		this.resultType = resultType;
		this.providerID = providerID;
		this.bean_type = bean_type;
		this.applicationContext = applicationcontext.getConfigfile();
		this.applicationcontext_ = applicationcontext;
		// this.buildService();
		// isremote = this.target != null && !this.target.isSelf();
		//       
		// if (this.isremote)
		// {
		// // rpcserviceStarted(target);
		//
		// // IpAddress address =
		// // (IpAddress)JGroupHelper.getJGroupHelper().getLocalAddress();
		// // if(this.compared(address, target))
		// // {
		// // isremote = false;
		// // return;
		// // }
		// if (isLocalAddress(target,false))
		// {
		// // if(serviceID.equals("(jms::yinbiaoping-jms)/rpc.test"))
		// // isremote = true;
		// return;
		// }
		// setLocalAddress();
		// }

	}
	
	
	public BaseServiceIDImpl(String serviceID, String providerID,String applicationcontext,int containerType,int resultMode, long timeout, int resultType, int bean_type
			) {
		this.serviceID = serviceID;
		this.resultMode = resultMode;
		this.timeout = timeout;
		this.resultType = resultType;
		this.providerID = providerID;
		this.bean_type = bean_type;
		this.applicationContext = applicationcontext;
		this.containerType  = containerType;
//		this.applicationcontext_ = applicationcontext;
		// this.buildService();
		// isremote = this.target != null && !this.target.isSelf();
		//       
		// if (this.isremote)
		// {
		// // rpcserviceStarted(target);
		//
		// // IpAddress address =
		// // (IpAddress)JGroupHelper.getJGroupHelper().getLocalAddress();
		// // if(this.compared(address, target))
		// // {
		// // isremote = false;
		// // return;
		// // }
		// if (isLocalAddress(target,false))
		// {
		// // if(serviceID.equals("(jms::yinbiaoping-jms)/rpc.test"))
		// // isremote = true;
		// return;
		// }
		// setLocalAddress();
		// }

	}

	public long getTimeout() {
		return timeout;
	}

	public boolean isRestStyle() {
		return this.restStyle;
	}

	public void setRestStyle(boolean restStyle) {
		this.restStyle = restStyle;
	}

	public String getSourceport() {
		return sourceport;
	}

	public String getSourcename() {
		return sourcename;
	}

	public String getProviderID() {
		return providerID;
	}

	public int getBean_type() {
		return bean_type;
	}

	public int getResultMode() {
		return resultMode;
	}

	public int getResultType() {
		return resultType;
	}

	public String getServiceID() {
		return serviceID;
	}

	public String getSourceip() {
		return sourceip;
	}

	public abstract String getNextRestfulServiceAddress();

	public String getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(String applicationContext) {
		this.applicationContext = applicationContext;
	}

	public boolean isRemote() {
		return this.isremote;
	}

	/**
	 * @fixed biaoping.yin 2010-10-11
	 * @return the rest
	 */
	public ServiceID getRestServiceID() {
		return rest;
	}

	public String getFistRestNode() {
		return fistRestNode;
	}

	public void setFistRestNode(String fistRestNode) {
		this.fistRestNode = fistRestNode;
	}

	public String getNextRestNode() {
		return nextRestNode;
	}

	public void setNextRestNode(String nextRestNode) {
		this.nextRestNode = nextRestNode;
	}

	public ServiceID getRestfulServiceID() {
		return this.rest;
	}

	public Target getRestfulTarget() {
		return this.rest.getTarget();
	}

	public String getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
	}

	public String getOrigineServiceID() {
		return this.serviceID;
	}

	public String getService() {
		return this.service;
	}

	public Target getTarget() {
		return this.target;
	}

	
	public int getContainerType()
	{
	
		return containerType;
	}

	
	public void setContainerType(int containerType)
	{
	
		this.containerType = containerType;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	

	public void setService(String service) {
		this.service = service;
	}

	public void setSourceip(String sourceip) {
		this.sourceip = sourceip;
	}

	public void setSourceport(String sourceport) {
		this.sourceport = sourceport;
	}

	public void setSourcename(String sourcename) {
		this.sourcename = sourcename;
	}

	public void setProviderID(String providerID) {
		this.providerID = providerID;
	}

	
}
