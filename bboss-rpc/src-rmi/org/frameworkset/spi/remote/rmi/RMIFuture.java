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

import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;

/**
 * <p>Title: RMIFuture.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-16 下午07:03:22
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIFuture extends BaseFutureCall
{
	
	public RMIFuture(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler)
	{
		super(srcmsg, address, handler);
		
	}

	@Override
	protected RPCMessage _call() throws Exception {
		RPCMessage msg = RMIRpcServiceClient.send(this.srcmsg,this.address);
		return msg;
	}

}

