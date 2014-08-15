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

package org.frameworkset.spi.remote.rmi;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.Util;

/**
 * <p>Title: RMIRpcServiceClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-17 下午12:10:58
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIRpcServiceClient {
	public static final String server_uuid_key = "server_uuid" ;
	private static Map<String ,RMIServiceInfo> services = new HashMap<String ,RMIServiceInfo>();
	public static RPCMessage send(RPCMessage msg,RPCAddress rpcaddress) throws Exception
	{		 
		String serveruuid = msg.getParameter(server_uuid_key,"default");
		
//		String address = "//" + rpcaddress.getIp() + ":" + rpcaddress.getPort()
//			+ "/" + serveruuid + "/rpcService";
		String address = "//" + rpcaddress.getIp() + ":" + rpcaddress.getPort()
		+ "/rpcService";
		 RMIServiceInfo rmiInterfactRemote = services.get(address);
		 if(rmiInterfactRemote == null)
		 {
			 synchronized(services)
			 {
				 rmiInterfactRemote = services.get(address);
				 if(rmiInterfactRemote == null)
				 {
//						System.setProperty("sun.rmi.transport.tcp.responseTimeout :"+System.getProperty("sun.rmi.transport.tcp.responseTimeout"));
					 System.setProperty("sun.rmi.transport.connectionTimeout",RMIServer.getRMIServer().getConnectTimeout() + "");
					 System.out.println("查找组件地址：" + address);
					 rmiInterfactRemote = RMIUtil.lookupService(address, RMIServiceInfo.class);
					 System.out.println("查找组件成功：" + address);
					 
					 services.put(address, rmiInterfactRemote);
				 }
			 }
		 }
		 try
		 {
			 Object msg_str = (Object)Util.getEncoder().encoder(msg);
			 
			 Object ret = rmiInterfactRemote.sendRPCMessage(msg_str);
			 return (RPCMessage)Util.getDecoder().decoder(ret);
		 }
		 catch(RemoteException e)
		 {
			 services.remove(address);
			 throw e;
		 }
		 catch(Exception e)
		 {
			 throw e;
		 }
		
	}

}
