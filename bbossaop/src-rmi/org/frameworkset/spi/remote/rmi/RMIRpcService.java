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
import java.rmi.server.UnicastRemoteObject;

import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.Util;


/**
 * <p>Title: RMIRpcService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-16 下午09:36:41
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIRpcService extends UnicastRemoteObject  implements RMIServiceInfo{

	protected RMIRpcService() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object sendRPCMessage(Object message_) throws RemoteException {
	
//		if(Util.asyn_response)
//		{
//	    	FutureTask<RPCMessage> task = new FutureTask<RPCMessage>(future);
//	    	BaseRPCIOHandler.response_threadpool.execute(task);
//	    	return task.get();
//		}
//		else
		{
			
				try {
					RPCMessage message = (RPCMessage)Util.getDecoder().decoder(message_);
					HandleFuture future = new HandleFuture(message);
					return Util.getEncoder().encoder(future.call());
				} catch (Exception e) {
					throw new RemoteException("",e);
				}
			
		}
	}

}
