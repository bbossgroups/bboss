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
package org.frameworkset.spi;

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.assemble.Pro;


/**
 * <p>Title: BeanContainer.java</p> 
 * <p>Description: bean 管理容器</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-17 下午02:44:15
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanContainer
{
	private Map<String,Pro> properties = new HashMap<String,Pro>();
	private Map<String, Object>	servicProviders = new HashMap<String, Object>();
	private BeanContainer parentContainer ;
	private String configfile;
	private boolean root = false;
	
	public BeanContainer(String configfile)
	{
		this.root = true;
		this.configfile = configfile;
	}
	public BeanContainer(BeanContainer parentContainer,String configfile)
	{
		this.parentContainer = parentContainer;
		this.configfile = configfile;
	}
	
	
/*********引入应用模块上下文时注释-2010-03-22*/	
//	public Object getBeanObject(String name)
//    {
//        return getBeanObject(null, name);
//    }
	
//	public Object getBeanObject(CallContext context, String name)
//    {
//	    
//        int idx = name.indexOf("?");
//        
//        String _name = name;
//        if(context == null)
//            context = new CallContext();
//        if(idx > 0)
//        {
//            String params = name.substring(idx + 1);
//            context = BaseSPIManager.buildCallContext(params,context);
//            name = name.substring(0,idx);
//        }
//        ServiceID serviceID = BaseSPIManager.buildBeanServiceID(serviceids, name);
////        if(context != null && context.getSecutiryContext() != null)
////            context.getSecutiryContext().setServiceid(serviceID.getService());
//        if(serviceID.isRemote())
//        	return BaseSPIManager.getBeanObject(context,_name);
//        // new ServiceID(name,GroupRequest.GET_ALL,0,ServiceID.result_rsplist,
//        // ServiceID.PROPERTY_BEAN_SERVICE);
//        Pro providerManagerInfo = properties.get(serviceID.getService());
//        if (providerManagerInfo == null )
//        {
//        	if(this.root || parentContainer == null)
//        		return BaseSPIManager.getBeanObject(context, name);
//        	else
//        		return parentContainer.getBeanObject(context, name);
//            
//        }
//        return getBeanObject(context, providerManagerInfo,  serviceID);
//        
//    }
//	
//	public Object getBeanObject(CallContext context, Pro providerManagerInfo,
//            ServiceID serviceID)
//    {
//        if (providerManagerInfo == null)
//            return null;
//        String key = providerManagerInfo.getName();
//        if (key == null)
//        {
//            key = providerManagerInfo.getRefid();
//        }
//        Object finalsynProvider = null;
//        if (serviceID == null)
//            serviceID = BaseSPIManager.buildBeanServiceID(serviceids, key);
//        // new ServiceID(key, GroupRequest.GET_ALL ,0,ServiceID.result_rsplist,ServiceID.PROPERTY_BEAN_SERVICE);
//        key = serviceID.getOrigineServiceID();
//        finalsynProvider = providerManagerInfo.getBeanObject(context);
//        if (providerManagerInfo.enableTransaction())
//        {
//            if (providerManagerInfo.isSinglable())
//            {
////                String key = serviceID.getServiceID();
//                if(context != null && !context.containHeaders() )//如果包含头信息时，代理类将不能被缓冲，原因是头信息的动态性会导致缓冲实例过多
//                {
//                    Object provider = servicProviders .get(key);
//                    if (provider != null)
//                        return provider;
//                    synchronized (servicProviders)
//                    {
//                        provider = servicProviders.get(key);
//                        if (provider != null)
//                            return provider;
//                        provider = BaseSPIManager.createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                        servicProviders.put(key, provider);
//                    }
//                    return provider;
//                }
//                else
//                {
//                    finalsynProvider = BaseSPIManager.createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                    return finalsynProvider;
//                }
//            }
//            else
//            {
//                finalsynProvider = BaseSPIManager.createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                return finalsynProvider;
//            }
//        }
//        else
//        {
//            return finalsynProvider;
//        }
//    }
	/*********引入应用模块上下文时注释-2010-03-22*/
}
