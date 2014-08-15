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

import javax.xml.ws.Endpoint;


 /**
  * 
  * <p>Title: RPCCallServiceServer.java</p> 
  * <p>Description: </p>
  * <p>bboss workgroup</p>
  * <p>Copyright (c) 2008</p>
  * @Date 2009-11-4 下午05:05:19
  * @author biaoping.yin
  * @version 1.0
  */
public class RPCCallServiceServer{

    protected RPCCallServiceServer() throws Exception {
        System.out.println("Starting Server");
        Object implementor = new org.frameworkset.spi.remote.webservice.RPCCall();
        String address = "http://localhost:9090/RPCCallServicePort";
        Endpoint.publish(address, implementor);
    }
    
    public static void main(String args[]) throws Exception { 
        new RPCCallServiceServer();
        System.out.println("Server ready..."); 
        
        Thread.sleep(5 * 60 * 1000); 
        System.out.println("Server exiting");
        System.exit(0);
    }
}
 
 