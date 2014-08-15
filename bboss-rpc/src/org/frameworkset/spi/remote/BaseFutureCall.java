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

package org.frameworkset.spi.remote;

import java.util.concurrent.Callable;

/**
 * <p>Title: BaseFutureCall.java</p> 
 * <p>Description: 请求发送程序</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-9 下午01:06:51
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseFutureCall implements Callable<RPCMessage>
{
    protected RPCMessage srcmsg;

    protected RPCAddress address;

    protected RPCIOHandler handler;
//    protected ResponseCollector coll;

    public BaseFutureCall(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler)
    {
        this.srcmsg = srcmsg;
        this.address = address;
        this.handler = handler;
//        this.coll = coll;
    }

    public RPCMessage call() throws Exception
    {
//        QName serviceName = new QName("http://webservice.remote.spi.frameworkset.org/", "RPCCallService");
//        QName portName = new QName("http://webservice.remote.spi.frameworkset.org/", Util.getRPCCallServicePort());
//
//        Service service = Service.create(serviceName);
//
//        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, Target.buildWebserviceURL(address, Util
//                .getRPCCallServicePort()));
//        org.frameworkset.spi.remote.webservice.RPCCallService client = service.getPort(portName,
//                org.frameworkset.spi.remote.webservice.RPCCallService.class);
//        RPCMessage ret = client.sendRPCMessage(srcmsg);
        RPCMessage ret = _call();
       
        return ret;
    }
    
    protected abstract RPCMessage _call() throws Exception;
    
}
