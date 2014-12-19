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
package org.frameworkset.event;

import java.util.ArrayList;
import java.util.List;

import org.jgroups.Address;

/**
 * 
 * <p>Title: EventTarget.java</p>
 *
 * <p>Description:  事件消息广播的目的地址，只有eventBroadcastType为远程传播(Event.REMOTE)
 * 本地远程传播 (Event.REMOTELOCAL)两种类型时，才可以指定target属性
 * 如果target为null，那么事件将被广播到所有的远程节点上面的监听器，否则只广播到target
 * 指定的目的地址对应的远程监听器上面，如果对应的target不存在，那么直接丢弃这个事件消息</p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date May 17, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class EventTarget   implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	/**
	 * 事件发送的目的地
	 */
	private String destination;
	/**
	 * 事件目的地监控端口
	 */
	private int port;
	private String userAccount = "admin";
	private String userPassword = "123456";
	
	transient List<Address> broadcastAddresses;	
	
	
	private String targetAddress;
	public EventTarget(Address address)
	{
		targetAddress = address.toString();
		broadcastAddresses = new ArrayList<Address>();
		broadcastAddresses.add(address);
	}
	public EventTarget(List<Address> addresses)
	{
		broadcastAddresses = addresses;
		targetAddress = addresses.toString();
	}

	
	
	
	 
	
	public String toString()
	{
//		StringBuffer ret = new StringBuffer();
//		if(this.protocol != null)
//			ret.append(protocol).append("::");
//		if(destination != null)
//			ret.append(destination).append(":").append(port).toString();
//		else if(this.getStringTargets() != null)
//		{
//			ret.append(this.getStringTargets());
//		}
//		return ret.toString();
		return targetAddress;
	}


	/**
	 * @return the userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}


	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}


	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}


	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}


	public List<Address> getBroadcastAddresses() {
		return broadcastAddresses;
	}
	public boolean hasAddresses() {
		// TODO Auto-generated method stub
		return broadcastAddresses != null && broadcastAddresses.size() > 0;
	}
	

}
