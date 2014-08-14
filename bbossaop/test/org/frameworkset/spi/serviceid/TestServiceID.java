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

package org.frameworkset.spi.serviceid;

import java.util.List;

import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RemoteServiceID;
import org.frameworkset.spi.remote.ServiceID;
import org.frameworkset.spi.serviceidentity.ServiceIDImpl;
import org.frameworkset.spi.serviceidentity.TargetImpl;

import bboss.org.jgroups.blocks.GroupRequest;

import org.junit.Test;


/**
 * <p>Title: TestServiceID.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-11-7 上午11:24:33
 * @author biaoping.yin
 * @version 1.0
 */
public class TestServiceID
{
	public static void main(String[] args)
	{
//		testBuildWebServiceURL();
//	    buildAllTargets();
	}
	
	@Test
	public void testRESTFulServiceID()
	{
	    String serviceID = "(rest::a/b/c)/serviceid";
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
	}
	
	/**
	 * 	<!-- 
			指定远程通讯的默认协议：mina,
							   jgroup,
							   jms,
							   webservice
							   rmi
							   ejb
							   corba
			一个完整的远程组件访问地址的实例如下：
			protocol::ip:port/serviceid
			如果应用程序没有指定protocol头，例如：
			ip:port/serviceid
			将使用rpc.default.protocol指定的协议
		 -->			      
		<property name="rpc.default.protocol" 
					      value="mina"/>
	 */
	@Test
	public  void defaultServiceID()
	{
		//默认的远程服务调用标识,其协议由框架配置中设置的缺省配置协议决定：
		
        String serviceID = "(192.168.0.17:1010;192.168.0.18:1020)/serviceid";        
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
        
	}
	
	@Test
	public   void webserviceServiceID()
	{
		 //webservice远程服务调用标识
		String serviceID = "(webservice::http://192.168.0.17:1010/webroot/;http://192.168.0.17:1010/webroot/)/serviceid";
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	@Test
	public   void testBuildWebServiceURL()
	{
		 //webservice远程服务调用标识
//		String serviceid = "(webservice::http://192.168.0.17:1010/webroot;http://192.168.0.17:1011/webroot/)/serviceid";
		String serviceID = "(webservice::http://192.168.0.17:1010;http://192.168.0.17:1011/)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
        List<RPCAddress> rpcAddresses = ((RemoteServiceID)id).getTarget().getTargets();
        for(RPCAddress address :rpcAddresses)
        {
        	System.out.println(TargetImpl.buildWebserviceURL(address));
        }
                             
	}
	
	@Test
	public   void minaServiceID()
	{
		 //mina远程服务调用标识
		String serviceID = "(mina::192.168.0.17:1010;192.168.0.17:1011)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	
	@Test
	public   void jgroupServiceID()
	{
		 //jgroup远程服务调用标识
		String serviceID = "(jgroup::192.168.0.17:1010;192.168.0.17:1011)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	@Test
	public   void rmiServiceID()
	{
		 //rmi远程服务调用标识
		String serviceID = "(rmi::192.168.0.17:1010;192.168.0.17:1011)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	@Test
	public   void ejbServiceID()
	{
		 //ejb远程服务调用标识
		String serviceID = "(ejb::192.168.0.17:1010;192.168.0.17:1011)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	@Test
	public   void corbaServiceID()
	{
		 //corba远程服务调用标识
		String serviceID = "(corba::192.168.0.17:1010;192.168.0.17:1011)/serviceid";
		 //webservice远程服务调用标识
	    String providerID = null;
	    String applicationcontext = "";
	    int containerType = 0;
	    
	    int resultMode = GroupRequest.GET_ALL;
	    long timeout = -1;
	    int resultType = RemoteServiceID.result_object;
	    int bean_type =  ServiceID.PROVIDER_BEAN_SERVICE;
//	    ServiceID id = new ServiceIDImpl(serviceid, null, GroupRequest.GET_ALL, RemoteServiceID.result_object, 0,
//                ServiceID.PROVIDER_BEAN_SERVICE,null);
	    ServiceID id = new  ServiceIDImpl(serviceID,  providerID, applicationcontext, containerType,  resultMode,  timeout,  resultType,  bean_type);
        System.out.println(id.getService());
        System.out.println(((RemoteServiceID)id).getTarget().getTargets());
                             
	}
	
	@Test
	public   void buildAllTargets()
	{
	    List<RPCAddress> dests = TargetImpl.buildAllTargets("127.0.0.1:12347;127.0.0.1:12346", "mina");
	    System.out.println("dests:" + dests);
	    
	    List<RPCAddress> wsdests = TargetImpl.buildAllTargets("http://127.0.0.1:12347/rpcws;http://127.0.0.1:12346/rpcws", "webservice");
            System.out.println("wsdests:" + wsdests);
	}
}
