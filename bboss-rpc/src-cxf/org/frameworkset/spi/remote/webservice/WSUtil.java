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

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.Util;


/**
 * <p>Title: WSUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-10 下午11:37:59
 * @author biaoping.yin
 * @version 1.0
 */
public class WSUtil
{

    /**
     * 标记是否启用webservice服务，为false时，启动cxf的serverlet将失效
     */
//        public static boolean webservice_enable = WSLoader.webservice_enable;
//        public static ProList webservices = WSLoader.webservices;
//        static
//        {
//        	try {
//				webservice_enable = ApplicationContext.getApplicationContext().getBooleanProperty("rpc.webservice.enable",true);
//			} catch (Exception e) {
//				
//			}
//        	
//        	 try {
//				webservices = ApplicationContext.getApplicationContext().getListProperty("cxf.webservices.config")   ;
//			} catch (Exception e) {
//				
//			}
//        }
        
        
        
	public static RPCIOHandler getRPCWebserviceIOHandler()
	{

		// TODO Auto-generated method stub
		return (RPCIOHandler)ApplicationContext.getApplicationContext().getBeanObject(Util.rpc_webservice_RPCServerIoHandler);
	}
	
//	 private static RPCCall webserviceRPCCall = (RPCCall)BaseSPIManager.getBeanObject(Util.rpc_webservice_RPCCall);
//	   public static RPCCall getWebServiceRPCCall()
//	   {
//	       return (RPCCall)BaseSPIManager.getBeanObject(Util.rpc_webservice_RPCCall);
//	   }
	
	
	   

	   
}
