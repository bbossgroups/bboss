/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.soa;


/**
 * <p>Title: MethodCall.java</p> 
 * <p>Description: 用来描述soa调用的方法信息</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-10 上午11:16:20
 * @author biaoping.yin
 * @version 1.0
 */
public class SOAMethodCall {	
	public SOAMethodCall(String requestor, long requestid, String password,
			String encypt, String encyptalgorithem, String serviceid,
			boolean issynchronized, SOAMethodInfo soamethodinfo,
			String address, String ip, int port) {
		super();
		this.requestor = requestor;
		this.requestid = requestid;
		this.password = password;
		this.encypt = encypt;
		this.encyptalgorithem = encyptalgorithem;
		this.serviceid = serviceid;
		this.issynchronized = issynchronized;
		this.soamethodinfo = soamethodinfo;
		this.address = address;
		this.ip = ip;
		this.port = port;
	}
	private String requestor;
	private long requestid;
	private String password;
	private String encypt;
	private String encyptalgorithem;

	private String serviceid;
	private boolean issynchronized;
	private SOAMethodInfo soamethodinfo;
	
	private String address;
	private String ip;
	private int port;
	
	
	public SOAMethodCall() {
		super();
		
	}
	/**
	 * @return the requestor
	 */
	public String getRequestor() {
		return requestor;
	}
	/**
	 * @param requestor the requestor to set
	 */
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}
	/**
	 * @return the requestid
	 */
	public long getRequestid() {
		return requestid;
	}
	/**
	 * @param requestid the requestid to set
	 */
	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the encypt
	 */
	public String getEncypt() {
		return encypt;
	}
	/**
	 * @param encypt the encypt to set
	 */
	public void setEncypt(String encypt) {
		this.encypt = encypt;
	}
	/**
	 * @return the encyptalgorithem
	 */
	public String getEncyptalgorithem() {
		return encyptalgorithem;
	}
	/**
	 * @param encyptalgorithem the encyptalgorithem to set
	 */
	public void setEncyptalgorithem(String encyptalgorithem) {
		this.encyptalgorithem = encyptalgorithem;
	}
	/**
	 * @return the serviceid
	 */
	public String getServiceid() {
		return serviceid;
	}
	/**
	 * @param serviceid the serviceid to set
	 */
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}
	/**
	 * @return the issynchronized
	 */
	public boolean isIssynchronized() {
		return issynchronized;
	}
	/**
	 * @param issynchronized the issynchronized to set
	 */
	public void setIssynchronized(boolean issynchronized) {
		this.issynchronized = issynchronized;
	}
	
	/**
	 * @return the soamethodinfo
	 */
	public SOAMethodInfo getSoamethodinfo() {
		return soamethodinfo;
	}
	/**
	 * @param soamethodinfo the soamethodinfo to set
	 */
	public void setSoamethodinfo(SOAMethodInfo soamethodinfo) {
		this.soamethodinfo = soamethodinfo;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	

}
