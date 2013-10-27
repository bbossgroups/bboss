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
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

/**
 * 
 * <p>Title: RPCCallServiceClient.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-11-4 下午05:05:08
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCCallServiceClient {

    public static void main(String[] args) throws Exception {
        QName serviceName = new QName("http://webservice.remote.spi.frameworkset.org/", "RPCCallService");
        QName portName = new QName("http://webservice.remote.spi.frameworkset.org/", "RPCCallServicePort");
        
        Service service = Service.create(serviceName);
        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING,
                        "http://localhost:8080/rpcws/services/RPCCallServicePort"); 
        org.frameworkset.spi.remote.webservice.RPCCallService client = service.getPort(portName,  org.frameworkset.spi.remote.webservice.RPCCallService.class);
        client.sendRPCMessage(null);
//        Dispatch
        // Insert code to invoke methods on the client here
    }

}