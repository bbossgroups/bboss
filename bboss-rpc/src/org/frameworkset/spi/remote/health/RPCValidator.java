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

package org.frameworkset.spi.remote.health;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;

/**
 * <p>Title: RPCValidator.java</p> 
 * <p>Description: 校验地址是否可用</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-5-15 下午11:45:19
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCValidator {
	private static final String fcservice = "rpc.fc";
	public static boolean validator(String contextpath)
	{
		try {
			
			String url = ApplicationContext.getRealPath(contextpath, fcservice);
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean validator(String protocol,String ip,String port)
	{
		String url = RPCHelper.buildAddress(protocol, ip,port, fcservice);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public static boolean validator(String protocol,String url)
	{
		 url = RPCHelper.buildAddress(protocol, url, fcservice);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public static boolean validator(String protocol,String url,String user,String password)
	{
		 url = RPCHelper.buildAuthAddress(protocol, url, fcservice,user,password);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public static boolean validator(String protocol,String ip,String port,String user,String password)
	{
		String url = RPCHelper.buildAuthAddress(protocol, ip,port, fcservice,user,password);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	* 验证远程端口和IP,采用默认等待3秒
	* 
	* @param ipAddress
	* @param port
	* @return
	* @throws UnknownHostException
	*/
	public synchronized static boolean checkIpaddressAndPort(String ipAddress,
	    int port) throws UnknownHostException {
	   return checkIpaddressAndPort(ipAddress, port, 3000);
	}

	/**
	* 验证远程端口和IP
	* 
	* @param ipAddress
	* @param port
	* @param delayTime
	* @return
	* @throws UnknownHostException
	*/
	public synchronized static boolean checkIpaddressAndPort(String ipAddress,
	    int port, int delayTime) throws UnknownHostException {
	   SocketAddress sa;
	   try {
	    sa = new InetSocketAddress(InetAddress.getByName(ipAddress), port);
	    Socket sc = new Socket();
	    sc.connect(sa, delayTime);
	    sc.close();
	   } catch (IOException e) {
		  // e.printStackTrace();
	    return false;
	   }
	   return true;
	}

	/**
	* 验证远程IP，采用默认等待3秒
	* 
	* @param ipAddress
	* @return
	* @throws UnknownHostException
	*/
	public synchronized static boolean checkIpaddress(String ipAddress)
	    throws UnknownHostException {
	   return checkIpaddress(ipAddress, 3000);

	}

	/**
	* 验证远程IP
	* 
	* @param ipAddress
	* @param delayTime
	* @return
	* @throws UnknownHostException
	*/
	public synchronized static boolean checkIpaddress(String ipAddress,
	    int delayTime) throws UnknownHostException {
	   try {
	    InetAddress address = InetAddress.getByName(ipAddress);
	    boolean result = address.isReachable(delayTime);
	    return result;
	   } catch (IOException e) {
	    return false;
	   }

	}

	public static void main(String[] args) {
	   String ip = "172.16.17.216";
	   int port = 3306;

	   try {
	    if (RPCValidator.checkIpaddress(ip)) {
	     System.out.println("this ipAddress_" + ip + " is valid");
	    }
	    if (RPCValidator.checkIpaddressAndPort(ip, port,60000)) {
	     System.out.println("this ipAddress_" + ip + " and port_" + port
	       + " are valid");
	    }
	   } catch (UnknownHostException e) {
	    e.printStackTrace();
	   }
	}

	public static boolean validator(RPCAddress address) {
		String url = RPCHelper.buildAddress(address, fcservice);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public static boolean validator(RPCAddress address,String user,String password) {
		String url = RPCHelper.buildAuthAddress(address, fcservice, user, password);
		try {
			
			
			HealthCheckService fc = (HealthCheckService) ApplicationContext
					.getApplicationContext().getBeanObject(url);
			fc.check();
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
//	,String user,String password



}
