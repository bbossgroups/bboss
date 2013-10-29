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

package org.frameworkset.spi.remote.mina.server;

import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.mina.client.ClinentTransport;

/**
 * <p>Title: MinaFutureCall.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-9 下午02:06:04
 * @author biaoping.yin
 * @version 1.0
 */
public class MinaFutureCall extends BaseFutureCall
{

    /**
     * @param srcmsg
     * @param address
     * @param handler
     */
    public MinaFutureCall(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler)
    {
        super(srcmsg, address, handler);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.frameworkset.spi.remote.BaseFutureCall#_call()
     */
    @Override
    protected RPCMessage _call() throws Exception
    {
        RPCServerIoHandler iohandler = (RPCServerIoHandler)handler;
        ClinentTransport transport = ClinentTransport.getClinentTransport(srcmsg.getDest(), iohandler);
//        if (!MinaRPCServer.defaultlongconnection && coll != null)
//            coll.registConnection(transport);
        transport.write(srcmsg);
        return null;
    }

}
