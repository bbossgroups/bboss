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

package org.frameworkset.netty;

import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;

/**
 * <p>Title: NettyFutureCall.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-19 下午11:35:51
 * @author biaoping.yin
 * @version 1.0
 */
public class NettyFutureCall extends BaseFutureCall {
	
	/**
     * @param srcmsg
     * @param address
     * @param handler
     */
    public NettyFutureCall(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler)
    {
        super(srcmsg, address, handler);
        // TODO Auto-generated constructor stub
    }

	@Override
	protected RPCMessage _call() throws Exception {
		NettyIOHandler iohandler = (NettyIOHandler)handler;
        NettyClinentTransport transport = NettyClinentTransport.getClinentTransport(srcmsg.getDest(), iohandler);
        transport.write(srcmsg);
        return null;
	}

}
