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
package org.frameworkset.spi.remote.webservice;

import javax.jws.WebService;

import org.frameworkset.spi.remote.RPCMessage;

/**
 * 
 * <p>Title: RPCCall.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-4 ÏÂÎç05:04:28
 * @author biaoping.yin
 * @version 1.0
 */

@WebService(name="RPCCall", endpointInterface="org.frameworkset.spi.remote.webservice.RPCCallService", targetNamespace="http://webservice.remote.spi.frameworkset.org/", portName="RPCCallServicePort", serviceName="RPCCallService" )
public class RPCCall implements RPCCallService {
	public RPCMessage sendRPCMessage(RPCMessage message) throws Exception
	{	    
		HandleFuture future = new HandleFuture(message);
//		if(Util.asyn_response)
//		{
//	    	FutureTask<RPCMessage> task = new FutureTask<RPCMessage>(future);
//	    	BaseRPCIOHandler.response_threadpool.execute(task);
//	    	return task.get();
//		}
//		else
		{
			return future.call();
		}

		
	}

}
