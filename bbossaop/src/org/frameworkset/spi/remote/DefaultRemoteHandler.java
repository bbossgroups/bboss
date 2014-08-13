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



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.SPIException;
/**
 * 
 * <p>Title: DefaultRemoteHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Apr 24, 2009 10:50:10 PM
 * @author biaoping.yin
 * @version 1.0
 */

public class DefaultRemoteHandler implements RemoteHandler{

    
    /**
     * 向所有的远程组件发送远程方法调用请求
     * 服务id，
     * mehthod 方法名称
     * Object[] parameters 方法参数
     * Class[]  types 方法参数类型
     * @throws SPIException 
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public Object callMethod(RemoteServiceID serviceID,
                             String methodName,
                             Object[] parameters,
                             Class[] types) throws SPIException, 
                                                   SecurityException, 
                                                   NoSuchMethodException, 
                                                   IllegalArgumentException, 
                                                   IllegalAccessException, 
                                                   InvocationTargetException
    {
        
        Object instance = null;
        if(!serviceID.isRestStyle() )
        {
	        if(serviceID.getBean_type() != ServiceID.PROVIDER_BEAN_SERVICE)
	        {
	        	
	        	BaseApplicationContext context = BaseApplicationContext.getBaseApplicationContext(serviceID.getApplicationContext(),serviceID.getContainerType());
	            instance = context.getBeanObject(serviceID.getService());
	        }
	        else
	        {

	        	BaseApplicationContext context = BaseApplicationContext.getBaseApplicationContext(serviceID.getApplicationContext(),serviceID.getContainerType());
	            instance = context.getProvider(serviceID.getService(),serviceID.getProviderID());
	        }
        }
        else //需要考虑转换成ClientProxyContext的调用模式，无需中介代理包含服务配置文件和实现组件类，可作为服务总线的实现技术
        {
//        	BaseApplicationContext context = BaseApplicationContext.getBaseApplicationContext(serviceID.getApplicationContext(),serviceID.getContainerType());        	
//        	instance = context.getBeanObject(serviceID.getNextRestfulServiceAddress());
//        	instance = ClientProxyContext.getApplicationClientBean(serviceID.getApplicationContext(),serviceID.getNextRestfulServiceAddress(),serviceID.getInfType(),serviceID.getContainerType());
        	instance = ClientProxyContext.getRestClientBean(serviceID);
        }
        
        Method method = instance.getClass().getMethod(methodName, types);
        
        return method.invoke(instance, parameters);
    }
    
//    /**
//     * 向所有的远程组件发送远程方法调用请求
//     * 服务id，
//     * mehthod 方法名称
//     * Object[] parameters 方法参数
//     * Class[]  types 方法参数类型
//     * @throws SPIException 
//     * @throws NoSuchMethodException 
//     * @throws SecurityException 
//     * @throws InvocationTargetException 
//     * @throws IllegalAccessException 
//     * @throws IllegalArgumentException 
//     */
//    public Object callMethod(ServiceID serviceID,
//                             String methodName,
//                             Object[] parameters,
//                             Class[] types,
//                             SecurityContext securityContext) throws SPIException, 
//                                                   SecurityException, 
//                                                   NoSuchMethodException, 
//                                                   IllegalArgumentException, 
//                                                   IllegalAccessException, 
//                                                   InvocationTargetException
//    {
//        Object instance = null;
//        if(serviceID.getBean_type() == ServiceID.PROVIDER_BEAN_SERVICE)
//            instance = BaseSPIManager.getProvider(serviceID.getService(),serviceID.getProviderID());
//        else
//            instance = BaseSPIManager.getBeanObject(serviceID.getService());
//        Method method = instance.getClass().getMethod(methodName, types);        
//        return method.invoke(instance, parameters);
//    }
    
    
	
}
