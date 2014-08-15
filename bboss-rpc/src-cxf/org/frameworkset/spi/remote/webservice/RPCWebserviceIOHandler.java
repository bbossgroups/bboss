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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.serviceidentity.TargetImpl;


/**
 * <p>Title: RPCWebserviceIOHandle.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-3 下午11:05:34
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCWebserviceIOHandler extends BaseRPCIOHandler {
//	ThreadPoolExecutor threadPool = new ThreadPoolExecutor();
	
    
    protected static final Log log = LogFactory.getLog(RPCWebserviceIOHandler.class);

    

    protected String webservice_server_type = "cxf";
    
    

    public RPCWebserviceIOHandler(String name, RequestHandler handler,String webservice_server_type)
    {
        super(name,handler,null);
        this.webservice_server_type = webservice_server_type;
        // this.local_addr = local_addr;
    }
    
    public RPCWebserviceIOHandler(String name, RequestHandler handler,String webservice_server_type,String localaddress)
    {
        super(name,handler,localaddress);
        this.webservice_server_type = webservice_server_type;
        if(localaddress != null && !localaddress.equals(""))
        {
            this.src_address = TargetImpl.buildTarget(this.localaddress, Target.BROADCAST_TYPE_WEBSERVICE);
        }
        
        // this.local_addr = local_addr;
    }
   
    
    
    
    /* (non-Javadoc)
     * @see org.frameworkset.spi.remote.BaseRPCIOHandler#buildBaseFutureCall(org.frameworkset.spi.remote.RPCMessage, org.frameworkset.spi.remote.RPCAddress, org.frameworkset.spi.remote.RPCIOHandler)
     */
    @Override
    protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg, RPCAddress address)
    {
        
        return new FutureCall( srcmsg,  address,  this,webservice_server_type);
    }




//    public RPCAddress getLocalAddress()
//    {
//        // TODO Auto-generated method stub
//        return this.src_address;
//    }

}
