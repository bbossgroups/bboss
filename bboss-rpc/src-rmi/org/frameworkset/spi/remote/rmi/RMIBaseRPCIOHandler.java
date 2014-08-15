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
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;

/**
 * <p>Title: RMIBaseRPCIOHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-6-16 下午09:18:30
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIBaseRPCIOHandler extends BaseRPCIOHandler {

	public RMIBaseRPCIOHandler(String name, RequestHandler handler) {
		super(name, handler,null);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg,
			RPCAddress address) {
		
		return new RMIFuture(srcmsg,address,this);
	}
	public RPCAddress getLocalAddress()
    {
        return RMIServer.getRMIServer().getLocalAddress();
    }
}
