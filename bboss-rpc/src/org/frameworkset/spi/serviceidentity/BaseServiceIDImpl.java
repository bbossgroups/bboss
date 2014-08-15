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
	/**
	 * 
	 */


	protected transient BaseApplicationContext applicationcontext_;
	// SecurityContext security;

	protected String serviceID;

	

	protected String service;

	

	protected String providerID;

	protected int bean_type = PROVIDER_BEAN_SERVICE;

	protected String applicationContext;

	

	protected transient boolean isremote = false;

	
	protected int	containerType = BaseApplicationContext.container_type_application;

	public BaseServiceIDImpl() {

	}

	public BaseServiceIDImpl(String serviceID, String providerID,String applicationcontext,int containerType, int bean_type)
	{
		this.serviceID = serviceID;
		this.providerID = providerID;
		this.bean_type = bean_type;
		this.applicationContext = applicationcontext;
		this.containerType  = containerType;
		this.service = serviceID;
	}
	
	public BaseServiceIDImpl(String serviceID, String providerID,BaseApplicationContext applicationcontext, int bean_type)
	{
		this.serviceID = serviceID;
		this.service = serviceID;
		this.providerID = providerID;
		this.bean_type = bean_type;
		this.applicationContext = applicationcontext.getConfigfile();
		this.applicationcontext_ = applicationcontext;
	}

	

	public int getBean_type() {
		return bean_type;
	}

	

	public String getServiceID() {
		return serviceID;
	}



	

	public String getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(String applicationContext) {
		this.applicationContext = applicationContext;
	}

	public boolean isRemote() {
		return this.isremote;
	}

	
	public String getOrigineServiceID() {
		return this.serviceID;
	}

	public String getService() {
		return this.service;
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

	
	public void setProviderID(String providerID) {
		this.providerID = providerID;
	}
	public String getProviderID() {
		return providerID;
	}
	
}
