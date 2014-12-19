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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.frameworkset.spi.remote.JGroupHelper;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCHelper;
import org.frameworkset.spi.remote.RemoteException;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;

import bboss.org.jgroups.Address;

/**
 * <p>Title: TargetImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 上午10:43:48
 * @author biaoping.yin
 * @version 1.0
 */
public class TargetImpl implements Target{
	
	/**
	 * <ip:port;ip:port......>
	 */
	private String				targets;

	private List<RPCAddress>				targets_;

	
	
	public static final String BROADCAST_TYPE_REST   = "rest";

	private String				broadcastType				= Util.default_protocol;

	private boolean				all							= false;

	private boolean				self						= false;

	public TargetImpl(String targets)
	{
		this.targets = targets;
		this.targets_ = this.buildTargets();
	}
	public TargetImpl()
	{
		
	}

	public String getStringTargets()
	{

		return this.targets;
	}

	private boolean isJGroup()
	{

		return ismuticast() || isunicast() || this.broadcastType.equals(BROADCAST_TYPE_JRGOUP);
	}
	
	private static boolean isJGroup(String protocol)
	{

		return ismuticast(protocol) || isunicast(protocol) || protocol.equals(BROADCAST_TYPE_JRGOUP);
	}

	public static boolean ismuticast(String protocol)
	{

		return protocol.equals(BROADCAST_TYPE_MUTICAST);
	}

	public static boolean isunicast(String protocol)
	{

		return protocol.equals(BROADCAST_TYPE_UNICAST);
	}
	
	public boolean ismuticast()
	{

		return this.broadcastType.equals(BROADCAST_TYPE_MUTICAST);
	}

	public boolean isunicast()
	{

		return this.broadcastType.equals(BROADCAST_TYPE_UNICAST);
	}

	private boolean isMina()
	{

		return this.broadcastType.equals(BROADCAST_TYPE_MINA);
	}
	
	private static boolean isMina(String protocol)
	{

		return protocol.equals(BROADCAST_TYPE_MINA);
	}
	
	private boolean isNetty()
    {

        return this.broadcastType.equals(BROADCAST_TYPE_NETTY);
    }
	
	private static boolean isNetty(String protocol)
    {

        return protocol.equals(BROADCAST_TYPE_NETTY);
    }


	private boolean isWebservice()
	{

		return this.broadcastType.equals(BROADCAST_TYPE_WEBSERVICE);
	}
	
	private static boolean isWebservice(String protocol)
	{

		return protocol.equals(BROADCAST_TYPE_WEBSERVICE);
	}

	private boolean isJms()
	{

		return this.broadcastType.equals(BROADCAST_TYPE_JMS);
	}
	private static boolean isJms(String protocol)
	{

		return protocol.equals(BROADCAST_TYPE_JMS);
	}
	
	private boolean protocol_jgroup = false;
	private boolean protocol_mina = false;
	private boolean protocol_netty = false;
	private boolean protocol_jms = false;
	private boolean protocol_webservice = false;

	private boolean	protocol_rmi;

	private boolean	protocol_ejb;
	
	private boolean	protocol_rest;
	
	
	public boolean protocol_jgroup()
	{
		return protocol_jgroup;
	}
	public boolean protocol_rest()
	{
		return protocol_rest;
	}
	public boolean protocol_mina()
	{
		return protocol_mina;
	}
	
	public boolean protocol_netty()
	{
	    return protocol_netty;
	}
	
	public boolean protocol_webservice()
	{
		return protocol_webservice;
	}
	
	public boolean protocol_jms()
	{
		return protocol_jms;
	}
	
	public boolean protocol_rmi()
	{
		return protocol_rmi;
	}
	
	public boolean protocol_http()
	{
		return protocol_http;
	}
	
	public boolean protocol_ejb()
	{
		return protocol_ejb;
	}
	
	
	private List<RPCAddress> buildJGroupTargets(String _targets_)
	{
		this.protocol_jgroup = true;
		List<RPCAddress> dests =  new ArrayList<RPCAddress>();
		StringTokenizer tokenizer = new StringTokenizer(_targets_, ";",
				false);
		if (tokenizer.countTokens() == 1)
		{
			String token = tokenizer.nextToken();
			if (token.equalsIgnoreCase("all"))
			{
				all = true;
			}
			else if (token.equalsIgnoreCase("_self"))
			{
				this.self = true;
				// StringTokenizer nexttokenizer = new
				// StringTokenizer(token,":",false);
				// String ip = nexttokenizer.nextToken();
				// String port = nexttokenizer.nextToken();
				//                   
				// try {
				// IpAddress address = new IpAddress(ip,
				// Integer.parseInt(port));
				// dests.add(address);
				// } catch (NumberFormatException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (UnknownHostException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			else
			{
//				StringTokenizer nexttokenizer = new StringTokenizer(token,
//						":", false);
//				String ip = nexttokenizer.nextToken();
//				String port = nexttokenizer.nextToken();
				try
				{
//					IpAddress address = new IpAddress(ip, Integer
//							.parseInt(port));
					Address lad = JGroupHelper.getJGroupHelper().getAddress(token); 
					if(lad == null)
						throw new RuntimeException("地址非法：" + token + " in " + targets + " 不存在.");
					RPCAddress rpcaddr = new RPCAddress(token,lad,Target.BROADCAST_TYPE_JRGOUP);
					dests.add(rpcaddr);
				}
				catch (RuntimeException e)
				{
					throw e;
				}
				catch (Exception e)
				{
					throw new RuntimeException("地址非法：" + token + " in " + targets,e);
				}
			}
			return dests;

		}
		else
		{

			while (tokenizer.hasMoreTokens())
			{
				String next = tokenizer.nextToken();
				

				try
				{
					Address lad = JGroupHelper.getJGroupHelper().getAddress(next); 
//					dests.add(address);
					if(lad == null)
						throw new RuntimeException("地址非法：" + next + " in " + targets + " 不存在.");
					RPCAddress rpcaddr = new RPCAddress(next,lad,Target.BROADCAST_TYPE_JRGOUP);
					dests.add(rpcaddr);
				}
				catch (RuntimeException e)
				{
					throw e;
				}
				catch (Exception e)
				{
					throw new RuntimeException("地址非法：" + next + " in " + targets,e);
				}
			}
		}
		return dests;
	}

	private List<RPCAddress> buildTargets()
	{

		List<RPCAddress> dests = null;

		try
		{
			int protocol = this.targets.indexOf("::");
			String _targets_ = this.targets;
			if (protocol >= 0)
			{
				this.broadcastType = this.targets.substring(0, protocol);
				if (!this.isJGroup() 
						&& !this.isMina()
						&& !this.isJms()
						&& !this.isWebservice()
						&& !this.isRest()
						&& !this.isNetty()
						&& !this.isRMI()
						&& !this.isHTTP())
				    
				{
					throw new RemoteException(this.targets
							+ " 中多播传输协议设置错误，必须为[" + BROADCAST_TYPE_MUTICAST
							+ "]或者[" + BROADCAST_TYPE_UNICAST
							+ "]或者[" + BROADCAST_TYPE_MINA
							+ "]或者[" + BROADCAST_TYPE_WEBSERVICE 
							+ "]或者[" + BROADCAST_TYPE_JMS
							+ "]或者[" + BROADCAST_TYPE_NETTY
							+ "]或者[" + BROADCAST_TYPE_RMI
							+ "]或者[" + BROADCAST_TYPE_REST + "]");
				}
				_targets_ = this.targets.substring(protocol + 2);
			}
			if(this.isJGroup())
			{
				dests = buildJGroupTargets(_targets_);
			}
			else if(this.isMina() || this.isRMI() || this.isNetty() || this.isHTTP() || this.isEJB() )
			{
				dests = buildTargets(_targets_,broadcastType);
			}
//			else if(this.isNetty())
//            {
//                dests = buildTargets(_targets_,BROADCAST_TYPE_NETTY);
//            }
			else if(this.isWebservice() )
			{
				dests = buildWebServiceTargets(_targets_);
			}
			
			else if( this.isJms())
			{
				dests = buildJMSTargets(_targets_);
			}
//			else if( this.isRMI())
//			{
//				dests = buildTargets(_targets_,BROADCAST_TYPE_RMI);
//			}
//			else if( this.isEJB())
//			{
//				dests = buildEJBTargets(_targets_);
//			}
			else if( this.isRest())
			{
				dests = this.buildRestTargets(_targets_);
			}
			else
			{
				throw new IllegalArgumentException(targets);
			}

		}
		catch (IllegalArgumentException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		return dests;
	}
	
	private boolean isRest()
    {
        
        return this.broadcastType.equals(BROADCAST_TYPE_REST);
    }
	
	private static boolean isRest(String protocol)
    {
        
        return protocol.equals(BROADCAST_TYPE_REST);
    }

    private boolean isEJB()
	{


		return this.broadcastType.equals(BROADCAST_TYPE_EJB);
	}
    
    private static boolean isEJB(String protocol)
	{


		return protocol.equals(BROADCAST_TYPE_EJB);
	}

	private boolean isRMI()
	{

		// TODO Auto-generated method stub
		return this.broadcastType.equals(BROADCAST_TYPE_RMI);
	}
	
	private boolean isHTTP()
	{

		// TODO Auto-generated method stub
		return this.broadcastType.equals(BROADCAST_TYPE_HTTP) || broadcastType.equals(BROADCAST_TYPE_HTTPS);
	}
	
	private static boolean isRMI(String potocol)
	{

		// TODO Auto-generated method stub
		return potocol.equals(BROADCAST_TYPE_RMI);
	}
	
	private static boolean isHTTP(String potocol)
	{

		// TODO Auto-generated method stub
		return potocol.equals(BROADCAST_TYPE_HTTP) || potocol.equals(BROADCAST_TYPE_HTTPS);
	}

	private List<RPCAddress> buildWebServiceTargets(String _targets_)
	{
		this.protocol_webservice = true;
		
		List<RPCAddress> dests = new ArrayList<RPCAddress>();
		try
		{
			
			StringTokenizer tokenizer = new StringTokenizer(_targets_, ";",
					false);
			if (tokenizer.countTokens() == 1)
			{
				String token = tokenizer.nextToken();
				if (token.equalsIgnoreCase("all"))
                {
                        all = true;
                }
				else if (token.equalsIgnoreCase("_self"))
				{
					this.self = true;
				}
				else
				{
					boolean security = false;
					if(token.startsWith("http://"))
						token = token.substring("http://".length());
					else if(token.startsWith("https://"))
					{
						token = token.substring("https://".length());
						security = true;
					}
					else
						throw new RuntimeException("地址非法：" + token + " in " + targets);
					int idx = token.indexOf('/');
					String contextpath = null;
					
					if(idx !=-1)
					{
						contextpath = token.substring(idx + 1);
						token = token.substring(0, idx);
					}
					
					StringTokenizer nexttokenizer = new StringTokenizer(token,
							":", false);
					String ip = nexttokenizer.nextToken();
					String port = nexttokenizer.nextToken();
					try
					{
						RPCAddress address = new RPCAddress(ip, Integer
								.parseInt(port),null,Target.BROADCAST_TYPE_WEBSERVICE);
						address.setContextpath(contextpath);
						address.setSecurity(security);
						dests.add(address);
					}
					catch (NumberFormatException e)
					{
						throw new RuntimeException("端口非法：" + port + " in " + targets,e);
					}
					catch (Exception e)
					{
						throw new RuntimeException("地址非法：" + nexttokenizer + " in " + targets,e);
					}
				}
				return dests;

			}
			while (tokenizer.hasMoreTokens())
			{
				String next = tokenizer.nextToken();
				boolean security = false;
				if(next.startsWith("http://"))
					next = next.substring("http://".length());
				else if(next.startsWith("https://"))
				{
					next = next.substring("https://".length());
					security = true;
				}
				else
					throw new RuntimeException("地址非法：" + next + " in " + targets);
					
				
				int idx = next.indexOf('/');
				String contextpath = null;
				
				if(idx !=-1)
				{
					contextpath = next.substring(idx + 1);
					next = next.substring(0, idx );
				}
				StringTokenizer nexttokenizer = new StringTokenizer(next, ":",
						false);
				String ip = nexttokenizer.nextToken();
				String port = nexttokenizer.nextToken();

				try
				{
					RPCAddress address = new RPCAddress(ip, Integer
							.parseInt(port),null,Target.BROADCAST_TYPE_WEBSERVICE);
					address.setContextpath(contextpath);
					address.setSecurity(security);
					dests.add(address);
				}
				catch (NumberFormatException e)
				{
					throw new RuntimeException("端口非法：" + port + " in " + targets,e);
				}
				catch (Exception e)
				{
					throw new RuntimeException("地址非法：" + nexttokenizer + " in " + targets,e);
				}
			}

		}
		catch (Exception e)
		{
			throw new RuntimeException("地址非法：" +  targets,e);
		}

		return dests;
//		throw new java.lang.UnsupportedOperationException(targets); 
	}
	
	
	private static List<RPCAddress> buildAllWebServiceTargets(String _targets_)
        {       
                
                List<RPCAddress> dests = new ArrayList<RPCAddress>();
                try
                {
                        
                        StringTokenizer tokenizer = new StringTokenizer(_targets_, ";",
                                        false);
                        if (tokenizer.countTokens() == 1)
                        {
                                String token = tokenizer.nextToken();
                              
                                {
                                        boolean security = false;
                                        if(token.startsWith("http://"))
                                                token = token.substring("http://".length());
                                        else if(token.startsWith("https://"))
                                        {
                                                token = token.substring("https://".length());
                                                security = true;
                                        }
                                        else
                                                throw new RuntimeException("地址非法：" + token + " in " + _targets_);
                                        int idx = token.indexOf('/');
                                        String contextpath = null;
                                        
                                        if(idx !=-1)
                                        {
                                                contextpath = token.substring(idx + 1);
                                                token = token.substring(0, idx);
                                        }
                                        
                                        StringTokenizer nexttokenizer = new StringTokenizer(token,
                                                        ":", false);
                                        String ip = nexttokenizer.nextToken();
                                        String port = nexttokenizer.nextToken();
                                        try
                                        {
                                                RPCAddress address = new RPCAddress(ip, Integer
                                                                .parseInt(port),null,Target.BROADCAST_TYPE_WEBSERVICE);
                                                address.setContextpath(contextpath);
                                                address.setSecurity(security);
                                                dests.add(address);
                                        }
                                        catch (NumberFormatException e)
                                        {
                                                throw new RuntimeException("端口非法：" + port + " in " + _targets_,e);
                                        }
                                        catch (Exception e)
                                        {
                                                throw new RuntimeException("地址非法：" + nexttokenizer + " in " + _targets_,e);
                                        }
                                }
                                return dests;

                        }
                        while (tokenizer.hasMoreTokens())
                        {
                                String next = tokenizer.nextToken();
                                boolean security = false;
                                if(next.startsWith("http://"))
                                        next = next.substring("http://".length());
                                else if(next.startsWith("https://"))
                                {
                                        next = next.substring("https://".length());
                                        security = true;
                                }
                                else
                                        throw new RuntimeException("地址非法：" + next + " in " + _targets_);
                                        
                                
                                int idx = next.indexOf('/');
                                String contextpath = null;
                                
                                if(idx !=-1)
                                {
                                        contextpath = next.substring(idx + 1);
                                        next = next.substring(0, idx );
                                }
                                StringTokenizer nexttokenizer = new StringTokenizer(next, ":",
                                                false);
                                String ip = nexttokenizer.nextToken();
                                String port = nexttokenizer.nextToken();

                                try
                                {
                                        RPCAddress address = new RPCAddress(ip, Integer
                                                        .parseInt(port),null,Target.BROADCAST_TYPE_WEBSERVICE);
                                        address.setContextpath(contextpath);
                                        address.setSecurity(security);
                                        dests.add(address);
                                }
                                catch (NumberFormatException e)
                                {
                                        throw new RuntimeException("端口非法：" + port + " in " + _targets_,e);
                                }
                                catch (Exception e)
                                {
                                        throw new RuntimeException("地址非法：" + nexttokenizer + " in " + _targets_,e);
                                }
                        }

                }
                catch (Exception e)
                {
                        throw new RuntimeException("地址非法：" +  _targets_,e);
                }

                return dests;
//              throw new java.lang.UnsupportedOperationException(targets); 
        }
	

	public static String buildWebserviceURL(RPCAddress address,String webserviceport)
	{
		
		String wsURI = buildWebserviceURL(address);
		if(webserviceport == null || webserviceport.trim().equals(""))
		{
			return wsURI;
		}
		StringBuffer wsURL = new StringBuffer();
		wsURL.append(wsURI);
		if(wsURI.endsWith("/"))
			wsURL.append(webserviceport);
		else
			wsURL.append("/").append(webserviceport);
		
		
		return wsURL.toString();
		
	}
	
	public static String buildWebserviceURL(RPCAddress address)
	{
//		if(!isWebservice(address.getProtocol()) && !isHTTP(address.getProtocol()))
//				throw new java.lang.IllegalArgumentException("RPCAddress[" + address + "] is not a webservice address.");
		if(isWebservice(address.getProtocol()))
		{
			StringBuffer wsURL = new StringBuffer();
			if(address.isSecurity())
				wsURL.append("https://");
			else
				wsURL.append("http://");
			wsURL.append(address.getIp())
				 .append(":")
				 .append(address.getPort())
				 .append("/");
			if(address.getContextpath() != null)
				wsURL.append(address.getContextpath());
			return wsURL.toString();
		}
		else if(isHTTP(address.getProtocol()))
		{
			StringBuffer wsURL = new StringBuffer();
			
		    wsURL.append(address.getProtocol()).append("://");
		
			wsURL.append(address.getIp())
				 .append(":")
				 .append(address.getPort())
				 ;
			if(address.getContextpath() != null)
			{
				wsURL.append(address.getContextpath());
			}
			return wsURL.toString();
		}
		else
			throw new java.lang.IllegalArgumentException("RPCAddress[" + address + "] is not a webservice/http address.");
		
	}
	
	public static String buildURL(RPCAddress address)
	{
		
		if(TargetImpl.isNetty(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(), address.getIp(), address.getPort() + "");
		}
		
		
		else if(TargetImpl.isRMI(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(), address.getIp(), address.getPort() + "");
		}
		else if(TargetImpl.isJms(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(),address.getServer_uuid());
		}
		
		else if(TargetImpl.isMina(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(), address.getIp(), address.getPort() + "");
		}
		else if(TargetImpl.isHTTP(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(), address.getIp(), address.getPort() + "" ,address.getContextpath());
		}		
		else if(TargetImpl.isRest(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(),address.getServer_uuid());
		}
	
		else if(TargetImpl.isWebservice(address.getProtocol()))
			return RPCHelper.buildContextAddress(address.getProtocol(),TargetImpl.buildWebserviceURL(address));
		else if(TargetImpl.isEJB(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(), address.getIp(), address.getPort() + "");
		}
		else if(TargetImpl.isJGroup(address.getProtocol()))
		{
			return RPCHelper.buildContextAddress(address.getProtocol(),address.getServer_uuid());
		}
		return null;
		
	}
	
	
	
	private List<RPCAddress> buildJMSTargets(String _targets_)
	{
		this.protocol_jms = true;
//		throw new java.lang.UnsupportedOperationException(targets); 
		StringTokenizer tokens = new StringTokenizer(_targets_,";",false);
		List<RPCAddress> addresses = new ArrayList<RPCAddress>();
		if (tokens.countTokens() == 1)
		{
			String token = tokens.nextToken();
			if (token.equalsIgnoreCase("all"))
            {
                    all = true;
            }
			else if (token.equalsIgnoreCase("_self"))
			{
				this.self = true;
			}
			else
			{
				String server_uuid = token;
				RPCAddress address = new RPCAddress(server_uuid);
				addresses.add(address);
			}
			return addresses;

		}
		
		while(tokens.hasMoreTokens())
		{
			String server_uuid = tokens.nextToken();
			RPCAddress address = new RPCAddress(server_uuid);
			addresses.add(address);
		}
		return addresses;
	}
	
	private List<RPCAddress> buildRMITargets(String _targets_)
	{
		this.protocol_rmi = true;
		throw new java.lang.UnsupportedOperationException(targets); 
	}
	
	private static List<RPCAddress> buildAllRMITargets(String _targets_)
	{
//		this.protocol_jms = true;
		throw new java.lang.UnsupportedOperationException(_targets_); 
	}
	
	
	private List<RPCAddress> buildEJBTargets(String _targets_)
	{
		this.protocol_ejb = true;
		throw new java.lang.UnsupportedOperationException(targets); 
	}
	private String firstNode = null;
	public String getFirstNode() {
		return firstNode;
	}

	public String getNextNode() {
		return nextNode;
	}
	
	private String nextNode = REST_LOCAL;

	private boolean protocol_http = false;
	
	public static String[] parserRestFulPath(String server_uuid)
	{
	    String[] ret = new String[2];
	    int index = server_uuid.indexOf('/');
        if(index > 0)
        {
            ret[0] = server_uuid.substring(0,index);
            ret[1] = server_uuid.substring(index+1);
        }
        else if(index == 0)
        {  
            
            ret[0] = server_uuid.substring(1);
            ret[1] = REST_LOCAL;
        }
        else
        {
            ret[0] = server_uuid;
            ret[1] = REST_LOCAL;
        }
        return ret;
	}
	private List<RPCAddress> buildRestTargets(String _targets_)
	{
		this.protocol_rest = true;
		
//		throw new java.lang.UnsupportedOperationException(targets); 
		StringTokenizer tokens = new StringTokenizer(_targets_,";",false);
		List<RPCAddress> addresses = new ArrayList<RPCAddress>();
		if (tokens.countTokens() == 1)
		{
			String token = tokens.nextToken();
			if (token.equalsIgnoreCase("all"))
            {
                    all = true;
            }
			else if (token.equalsIgnoreCase("_self"))
			{
				this.self = true;
			}
			else
			{
				String server_uuid = token;
				RPCAddress address = new RPCAddress(server_uuid,BROADCAST_TYPE_REST);
				
				addresses.add(address);
//				int index = server_uuid.indexOf('/');
//				if(index > 0)
//				{
//					this.firstNode = server_uuid.substring(0,index);
//					this.nextNode = server_uuid.substring(index+1);
//				}
//				else if(index == 0)
//				{  
//				    
//					this.firstNode = server_uuid.substring(1);
//					this.nextNode = REST_LOCAL;
//				}
//				else
//				{
//				    this.firstNode = server_uuid;
//                    this.nextNode = REST_LOCAL;
//				}
				String[] nodes = this.parserRestFulPath(server_uuid);
				this.firstNode = nodes[0];
                this.nextNode = nodes[1];
			}
			return addresses;

		}
		
		while(tokens.hasMoreTokens())//不支持rest风格的多播协议调用
		{
//			String server_uuid = tokens.nextToken();
//			RPCAddress address = new RPCAddress(server_uuid,BROADCAST_TYPE_REST);
//			addresses.add(address);
			throw new IllegalArgumentException();
		}
		return addresses;
	}
	
	
	
	private static List<RPCAddress> buildAllEJBTargets(String _targets_)
	{
//		this.protocol_jms = true;
		throw new java.lang.UnsupportedOperationException(_targets_); 
	}

	private static List<RPCAddress> buildAllTargets_(String _targets_,String protocol)
	{
	    List<RPCAddress> dests = new ArrayList<RPCAddress>();
            try
            {
                    
                    StringTokenizer tokenizer = new StringTokenizer(_targets_, ";",
                                    false);
                    if (tokenizer.countTokens() == 1)
                    {
                            String token = tokenizer.nextToken();
                            
                            {
                                    StringTokenizer nexttokenizer = new StringTokenizer(token,
                                                    ":", false);
                                    String ip = nexttokenizer.nextToken();
                                    String port = nexttokenizer.nextToken();
                                    int split = port.indexOf('/');
                                    
                                    try
                                    {
                                    	if(split < 0)
                                    	{
                                            RPCAddress address = new RPCAddress(ip, Integer
                                                            .parseInt(port),null,protocol);
                                            dests.add(address);
                                    	}
                                    	
                                    	else
                                    	{
                                    		String contextpath = port.substring(split );
                                    		port = port.substring(0,split );
                                    		RPCAddress address = new RPCAddress(protocol,ip, Integer
                                                    .parseInt(port),contextpath);
                                    		dests.add(address);
                                    	}
                                    }
                                    catch (NumberFormatException e)
                                    {
                                            throw new RuntimeException("端口非法：" + port + " in " + _targets_,e);
                                    }
                                    catch (Exception e)
                                    {
                                            throw new RuntimeException("地址非法：" + nexttokenizer + " in " + _targets_,e);
                                    }
                            }
                            return dests;

                    }
                    while (tokenizer.hasMoreTokens())
                    {
                            String next = tokenizer.nextToken();
                            StringTokenizer nexttokenizer = new StringTokenizer(next, ":",
                                            false);
                            String ip = nexttokenizer.nextToken();
                            String port = nexttokenizer.nextToken();

                            int split = port.indexOf('/');
                            
                            try
                            {
                            	if(split < 0)
                            	{
                                    RPCAddress address = new RPCAddress(ip, Integer
                                                    .parseInt(port),null,protocol);
                                    dests.add(address);
                            	}
                            	
                            	else
                            	{
                            		String contextpath = port.substring(split );
                            		port = port.substring(0,split );
                            		RPCAddress address = new RPCAddress(protocol,ip, Integer
                                            .parseInt(port),contextpath);
                            		dests.add(address);
                            	}
                            }
                            catch (NumberFormatException e)
                            {
                                    throw new RuntimeException("端口非法：" + port + " in " + _targets_,e);
                            }
                            catch (Exception e)
                            {
                                    throw new RuntimeException("地址非法：" + nexttokenizer + " in " + _targets_,e);
                            }
                    }

            }
            catch (Exception e)
            {
                    throw new RuntimeException("地址非法：" +  _targets_,e);
            }

            return dests;
	}
	
	private static List<RPCAddress> buildAllJMSORJgroupsTargets(String _targets_,String protocol)
    {
	    StringTokenizer tokens = new StringTokenizer(_targets_,";",false);
        List<RPCAddress> addresses = new ArrayList<RPCAddress>();
        while(tokens.hasMoreTokens())
        {
            String server_uuid = tokens.nextToken();
            RPCAddress address = new RPCAddress(server_uuid,protocol);
            addresses.add(address);
        }
        return addresses;
    }
	private List<RPCAddress> buildTargets(String _targets_,String protocol)
	{
	    if(protocol.equals(BROADCAST_TYPE_MINA))
	        this.protocol_mina = true;
	    else if(protocol.equals(BROADCAST_TYPE_NETTY))
	        this.protocol_netty = true;
	    else if(protocol.equals(BROADCAST_TYPE_RMI))
	        this.protocol_rmi = true;
	    else if(this.isHTTP())
	        this.protocol_http  = true;
	    
		List<RPCAddress> dests = new ArrayList<RPCAddress>();
		try
		{
			
			StringTokenizer tokenizer = new StringTokenizer(_targets_, ";",
					false);
			if (tokenizer.countTokens() == 1)
			{
				String token = tokenizer.nextToken();
				if (token.equalsIgnoreCase("all"))
	                        {
	                                all = true;
//	                                return dests = RPCClient.getInstance().getAllAddress();
	                        }
				else if (token.equalsIgnoreCase("_self"))
				{
					this.self = true;
					
				}
				else
				{
					StringTokenizer nexttokenizer = new StringTokenizer(token,
							":", false);
					String ip = nexttokenizer.nextToken();
					String port = nexttokenizer.nextToken();
					int split = port.indexOf('/');
                    
                    try
                    {
                    	if(split < 0)
                    	{
                            RPCAddress address = new RPCAddress(ip, Integer
                                            .parseInt(port),null,protocol);
                            dests.add(address);
                    	}
                    	
                    	else
                    	{
                    		String contextpath = port.substring(split );
                    		port = port.substring(0,split );
                    		RPCAddress address = new RPCAddress(protocol,ip, Integer
                                    .parseInt(port),contextpath);
                    		dests.add(address);
                    	}
					}
					catch (NumberFormatException e)
					{
						throw new RuntimeException("端口非法：" + port + " in " + targets,e);
					}
					catch (Exception e)
					{
						throw new RuntimeException("地址非法：" + nexttokenizer + " in " + targets,e);
					}
				}
				return dests;

			}
			while (tokenizer.hasMoreTokens())
			{
				String next = tokenizer.nextToken();
				StringTokenizer nexttokenizer = new StringTokenizer(next, ":",
						false);
				String ip = nexttokenizer.nextToken();
				String port = nexttokenizer.nextToken();

				int split = port.indexOf('/');
                
                try
                {
                	if(split < 0)
                	{
                        RPCAddress address = new RPCAddress(ip, Integer
                                        .parseInt(port),null,protocol);
                        dests.add(address);
                	}
                	
                	else
                	{
                		String contextpath = port.substring(split );
                		port = port.substring(0,split );
                		RPCAddress address = new RPCAddress(protocol,ip, Integer
                                .parseInt(port),contextpath);
                		dests.add(address);
                	}
				}
				catch (NumberFormatException e)
				{
					throw new RuntimeException("端口非法：" + port + " in " + targets,e);
				}
				catch (Exception e)
				{
					throw new RuntimeException("地址非法：" + nexttokenizer + " in " + targets,e);
				}
			}

		}
		catch (Exception e)
		{
			throw new RuntimeException("地址非法：" +  targets,e);
		}

		return dests;
	}
	
	public static List<RPCAddress> buildAllTargets(String _targets_,String protocol)
	{
//		this.protocol_mina = true;
//		List<RPCAddress> dests = new ArrayList<RPCAddress>();
		
		if(protocol.equals(Target.BROADCAST_TYPE_MINA) 
		        || protocol.equals(BROADCAST_TYPE_NETTY) || protocol.equals(BROADCAST_TYPE_RMI) || protocol.equals(Target.BROADCAST_TYPE_HTTP		))
		    return buildAllTargets_(_targets_,protocol);
		else if(protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
		{
		    return buildAllWebServiceTargets(_targets_);
		}
		else if(protocol.equals(Target.BROADCAST_TYPE_JMS) || protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
        {
            return buildAllJMSORJgroupsTargets(_targets_,protocol);
        }
		
		else
		{
		    throw new RuntimeException("不支持的协议：" +  _targets_ + ",protocol=" + protocol);
		}

		
	}

	public static void main(String[] args)
	{

		String test = "unicast::192.19:1180;192.19:1182";
		int protocol = test.indexOf("::");
		String targets_ = test;
		if (protocol >= 0)
		{
			String broadcastType = test.substring(0, protocol);
			if (!broadcastType.equals(BROADCAST_TYPE_MUTICAST)
					&& !broadcastType.equals(BROADCAST_TYPE_UNICAST))
			{
				throw new RemoteException(targets_ + " 中多播传输协议错误，必须为["
						+ BROADCAST_TYPE_MUTICAST + "]或者["
						+ BROADCAST_TYPE_UNICAST + "]");
			}
			targets_ = targets_.substring(protocol + 2);
		}
	}

	public List<RPCAddress> getTargets()
	{

		return this.targets_;
	}
	
	

	public boolean isAll()
	{

		return this.all;
	}

	public boolean isSelf()
	{

		return this.self;
	}

	public static RPCAddress buildTarget(String url, String protocol)
	{

		
		if(protocol.equals(Target.BROADCAST_TYPE_MINA)
				|| protocol.equals(Target.BROADCAST_TYPE_RMI) 
				|| protocol.equals(Target.BROADCAST_TYPE_NETTY)
						|| protocol.equals(Target.BROADCAST_TYPE_HTTP		)
				)
		    return buildAllTargets_(url,protocol).get(0);
		else if(protocol.equals(Target.BROADCAST_TYPE_WEBSERVICE))
		{
		    return buildAllWebServiceTargets(url).get(0);
		}
		else if(protocol.equals(Target.BROADCAST_TYPE_JMS) || protocol.equals(Target.BROADCAST_TYPE_JRGOUP))
        {
            return buildAllJMSORJgroupsTargets(url,protocol).get(0);
        }
		else
		{
		    throw new RuntimeException("不支持的协议：" +  url + ",protocol=" + protocol);
		}
	}

}
