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

import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.assemble.ProSet;
import org.frameworkset.spi.assemble.ServiceProviderManager;


/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: 服务管理类，提供aop的最底层的包装，实现简单aop拦截功能，提供日志的管理，事务的管理功能
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * @deprecated use BaseSPIManager2
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseSPIManager
{

    public static ApplicationContext defaultContext; 
    static
    {
        // try {
        // // use reflection and catch the Exception to allow PoolMan to work
        // with 1.2 VM's
        // Class r = Runtime.getRuntime().getClass();
        // java.lang.reflect.Method m =
        // r.getDeclaredMethod("addShutdownHook", new Class[]{Thread.class});
        // m.invoke(Runtime.getRuntime(), new Object[]{new Thread(new
        // BeanDestroyHook())});
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
//        addShutdownHook(new BeanDestroyHook());
        try
        {
            defaultContext = ApplicationContext.getApplicationContext(ServiceProviderManager.defaultConfigFile);
        }
        catch(Exception e)
        {
//            defaultContext = ApplicationContext.getApplicationContext(ServiceProviderManager.defaultConfigFile);
            e.printStackTrace();
        }
        finally
        {
            
        }

    }
    
    public static ApplicationContext getDefaultApplicationContext()
    {
        return defaultContext;
    }
    
    

    /**
     * 添加系统中停止时的回调程序
     * 
     * @param destroyVMHook
     */
    public static void addShutdownHook(Runnable destroyVMHook)
    {
//        try
//        {
//            // use reflection and catch the Exception to allow PoolMan to work
//            // with 1.2 VM's
//            Class r = Runtime.getRuntime().getClass();
//            java.lang.reflect.Method m = r.getDeclaredMethod("addShutdownHook", new Class[] { Thread.class });
//            m.invoke(Runtime.getRuntime(), new Object[] { new Thread(destroyVMHook) });
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        ApplicationContext.addShutdownHook(destroyVMHook);
    }



    /**
     * 通用特定服务提供接口获取类
     * 
     * @param providerManagerType
     *            String
     * @return Object
     * @throws SPIException
     */
    public static Object getProvider(String providerManagerType) throws SPIException
    {
        return getProvider(providerManagerType, null);
    }

    /**
     * 获取全局缺省的provider
     * 
     * @return Object
     * @throws SPIException
     */
    public static Object getProvider() throws SPIException
    {
        return getProvider(null, null);
    }

    // /**
    // * 通用特定服务类型中特定数据源实现的提供接口获取类
    // * @param providerManagerType String
    // * @param sourceType String
    // * @return Provider
    // * @throws SPIException
    // */
    // public static Provider getProvider(String providerManagerType,String
    // sourceType) throws SPIException
    // {
    // ProviderManagerInfo providerManagerInfo = null;
    // if(providerManagerType != null)
    // providerManagerInfo = ConfigManager.getInstance().
    // getProviderManagerInfo(providerManagerType);
    // else
    // {
    // providerManagerInfo =
    // ConfigManager.getInstance().getDefaultProviderManagerInfo();
    // providerManagerType = providerManagerInfo.getId() ;
    // }
    // //各spi管理者的缓冲关键字为providerManagerType ＋ ":" + sourceType;
    // String cacheKey = sourceType != null?providerManagerType+":"+sourceType
    // :providerManagerType+":"+DEFAULT_CACHE_KEY;
    // String syncacheKey = providerManagerType+":"+SYNCHRO_CACHE_KEY;
    //
    // Provider provider = null;
    // //判断是否允许为单实例模式，如果是获取单实例，否则重新创建provider实例
    // if(providerManagerInfo.isSinglable())
    // {
    // provider = (Provider) providers.get(cacheKey);
    // if (provider == null) {
    // try {
    // if (sourceType == null) {
    // provider =
    // (Provider) providerManagerInfo.
    // getDefaulProviderInfo().
    // getProvider();
    // } else {
    // provider =
    // (Provider) providerManagerInfo.
    // getSecurityProviderInfoByType(sourceType).
    // getProvider();
    //
    // }
    // providers.put(cacheKey, provider);
    // } catch (Exception e) {
    // throw new SPIException(
    // "Failed to get UserManagement class instance..."
    // + e.toString());
    // }
    // }
    // }
    // else
    // {
    // if (sourceType == null)
    // provider = providerManagerInfo.
    // getDefaulProviderInfo().getProvider();
    // else
    // provider = providerManagerInfo.
    // getSecurityProviderInfoByType(sourceType)
    // .getProvider();
    // }
    // //如果开启同步机制，获取同步代理接口，否则直接返回缺省的管理接口
    // if (providerManagerInfo.isSynchronizedEnabled()) {
    // Provider synProvider = null;
    // //如果是单实例则获取代理单实例，否则重新生成代理实例
    // if(providerManagerInfo.isSinglable())
    // {
    // synProvider = (Provider) providers.get(
    // syncacheKey);
    //
    // if (synProvider == null) {
    // Provider finalsynProvider = provider;
    // synProvider = (Provider) createInf(providerManagerInfo,
    // finalsynProvider);
    // if (synProvider != null) {
    // providers.put(
    // syncacheKey, synProvider);
    // }
    // }
    // }
    // else
    // {
    // Provider finalsynProvider = provider;
    // synProvider = (Provider) createInf(providerManagerInfo,
    // finalsynProvider);
    //
    // }
    // return synProvider;
    // }
    // else
    // {
    // return provider;
    // }
    // }

    /**
     * 获取管理服务的特定提供者实例对象
     */
    public static Object getProvider(String providerManagerType, String sourceType) throws SPIException
    {
        return getProvider((CallContext) null, providerManagerType, sourceType,false);
    }

//    private static Map<String,Object> servicProviders = new HashMap<String,Object>();
//    
//    private static Map<String,Object> destroyServiceMethods = new HashMap<String,Object>();
//
//    private static final List<DisposableBean> singleDestorys = new ArrayList<DisposableBean>();

    /**
     * 通用特定服务类型中特定数据源实现的提供接口获取类
     * 
     * @param providerManagerType
     *            String
     * @param sourceType
     *            String
     * @return Object
     * @throws SPIException
     */
    // public static Object getProvider(Context parent,
    // String providerManagerType,
    // String sourceType) throws SPIException
    // {
    // ServiceID serviceID = new ServiceID(providerManagerType);
    // ProviderManagerInfo providerManagerInfo = null;
    // if(providerManagerType != null)
    // {
    // providerManagerInfo = defaultContext.
    // getProviderManagerInfo(providerManagerType);
    // }
    // else
    // {
    // providerManagerInfo =
    // defaultContext.getDefaultProviderManagerInfo();
    // providerManagerType = providerManagerInfo.getId() ;
    // }
    // if(providerManagerInfo == null)
    // {
    // throw new SPIException("SPI[" + providerManagerType +"] in "
    // + parent + " does not exist.");
    // }
    // //各spi管理者的缓冲关键字为providerManagerType ＋ ":" + sourceType;
    // if(sourceType == null || sourceType.equals(""))
    // {
    // sourceType = providerManagerInfo.getDefaulProviderInfo().getType();
    // }
    //        
    // String key = providerManagerType + ":" + sourceType;
    // Object finalsynProvider = null;
    // if(providerManagerInfo.isSinglable())
    // {
    // finalsynProvider = servicProviders.get(key);
    // if(finalsynProvider != null)
    // return finalsynProvider;
    // }
    //
    // Object provider = null;
    // //判断是否允许为单实例模式，如果是获取单实例，否则重新创建provider实例
    // if(providerManagerInfo.isSinglable())
    // {
    //            
    // try {
    // provider = providerManagerInfo.
    // getSecurityProviderInfoByType(sourceType).
    // getSingleProvider(parent);
    // } catch (CurrentlyInCreationException e) {
    // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // throw new SPIException(e);
    // }
    //               
    //            
    // }
    // else
    // {
    // try {
    // provider = providerManagerInfo.
    // getSecurityProviderInfoByType(sourceType)
    // .getProvider(parent);
    // } catch (CurrentlyInCreationException e) {
    // // log.error(e);
    // // e.printStackTrace();
    // // return null;
    // throw new SPIException(e);
    // }
    // }
    // if(provider == null)
    // throw new SPIException("管理服务["+key + "]为null,请检查相关配置是否正确。");
    //            
    // finalsynProvider = provider;
    //        
    // //生成管理服务的动态代理实例,如果不是以下的情况则不需要创建代理
    // try
    // {
    // if(providerManagerInfo.enableTransaction() &&
    // providerManagerInfo.isSynchronizedEnabled())
    // {
    // if(providerManagerInfo.getProviderInfoQueue().size() > 1)
    // finalsynProvider =
    // BaseSPIManager.createSynTXInf(parent,providerManagerInfo
    // ,finalsynProvider);
    // else
    // {
    // finalsynProvider =
    // BaseSPIManager.createTXInf(parent,providerManagerInfo,finalsynProvider);
    // }
    //        		
    // }
    // else if(providerManagerInfo.enableTransaction())
    // {
    // finalsynProvider =
    // BaseSPIManager.createTXInf(parent,providerManagerInfo,finalsynProvider);
    // }
    // else if(providerManagerInfo.isSynchronizedEnabled())
    // {
    // if(providerManagerInfo.getProviderInfoQueue().size() > 1)
    // {
    // finalsynProvider =
    // BaseSPIManager.createSynInf(parent,providerManagerInfo,finalsynProvider);
    // }
    // else if(providerManagerInfo.usedCustomInterceptor())
    // {
    // finalsynProvider =
    // BaseSPIManager.createInf(parent,providerManagerInfo,finalsynProvider);
    // }
    //        		
    // }
    // else if(providerManagerInfo.usedCustomInterceptor())
    // {
    // finalsynProvider =
    // BaseSPIManager.createInf(parent,providerManagerInfo,finalsynProvider);
    // }
    //        		
    //
    // }
    // catch(Exception e)
    // {
    // e.printStackTrace();
    // throw new SPIException(e);
    //        	
    // }
    // if(providerManagerInfo.isSinglable())
    // {
    // synchronized(servicProviders)
    // {
    // servicProviders.put(key, finalsynProvider);
    // }
    // }
    // return finalsynProvider;
    //        
    // }
    
    public static Object getProvider(CallContext parent, String providerManagerType, String sourceType) throws SPIException
    {
        return getProvider(parent, providerManagerType, sourceType,false);
    }
    static Object getProvider(CallContext callContext, String providerManagerType, String sourceType,boolean frombeanobject) throws SPIException
    {
        return defaultContext.getProvider(callContext, providerManagerType, sourceType, frombeanobject);
//        int idx = providerManagerType.indexOf("?");
//        
//        String _name = providerManagerType;
//        if(callContext == null)
//            callContext = new CallContext();
//        if(idx > 0)
//        {
//            String params = providerManagerType.substring(idx + 1);
//            callContext = buildCallContext(params,callContext);
//            providerManagerType = providerManagerType.substring(0,idx);
//        }
//        ServiceID serviceID = buildServiceID(RPCHelper.serviceids,providerManagerType, ServiceID.PROVIDER_BEAN_SERVICE, sourceType);
////        if(callContext != null && callContext.getSecutiryContext() != null)
////            callContext.getSecutiryContext().setServiceid(serviceID.getService());
//        // new ServiceID(providerManagerType,sourceType,GroupRequest.GET_ALL,0,
//        // ServiceID.result_rsplist,ServiceID.PROVIDER_BEAN_SERVICE);
//        // ServiceID(String serviceID,int resultMode,int waittime,int
//        // resultType)
//
//        ProviderManagerInfo providerManagerInfo = null;
//        if (providerManagerType != null)
//        {
//            providerManagerInfo = defaultContext.getProviderManagerInfo(serviceID.getService());
//        }
//        else
//        {
//            providerManagerInfo = defaultContext.getDefaultProviderManagerInfo();
//            providerManagerType = providerManagerInfo.getId();
//        }
//        if (providerManagerInfo == null)
//        {
//            if(frombeanobject)
//                throw new SPIException("SPI[" + providerManagerType + "] in " + callContext.getLoopContext() + " does not exist.");
//            else
//            {
//                return BaseSPIManager.getBeanObject(callContext,_name,null,true);
//            }
//        }
//        // 各spi管理者的缓冲关键字为providerManagerType ＋ ":" + sourceType;
//        if (sourceType == null || sourceType.equals(""))
//        {
//            sourceType = providerManagerInfo.getDefaulProviderInfo().getType();
//        }
//
//        String key = providerManagerType + ":" + sourceType;
//        Object finalsynProvider = null;
//        if (providerManagerInfo.isSinglable())
//        {
//            finalsynProvider = servicProviders.get(key);
//            if (finalsynProvider != null)
//                return finalsynProvider;
//        }
//
//        Object provider = null;
//
//        provider = providerManagerInfo.getSecurityProviderInfoByType(sourceType).getProvider(callContext);
//        if (provider == null)
//            throw new SPIException("管理服务[" + key + "]为null,请检查相关配置是否正确。");
//
//        finalsynProvider = provider;
//
//        // 生成管理服务的动态代理实例,如果不是以下的情况则不需要创建代理
//        try
//        {
//            if (providerManagerInfo.enableTransaction() && providerManagerInfo.isSynchronizedEnabled())
//            {
//                if (providerManagerInfo.getProviderInfoQueue().size() > 1)
//                    finalsynProvider = BaseSPIManager.createSynTXInf(callContext, providerManagerInfo, finalsynProvider,
//                            serviceID);
//                else
//                {
//                    finalsynProvider = BaseSPIManager.createTXInf(callContext, providerManagerInfo, finalsynProvider,
//                            serviceID);
//                }
//
//            }
//            else if (providerManagerInfo.enableTransaction())
//            {
//                finalsynProvider = BaseSPIManager.createTXInf(callContext, providerManagerInfo, finalsynProvider, serviceID);
//            }
//            else if (providerManagerInfo.isSynchronizedEnabled())
//            {
//                if (providerManagerInfo.getProviderInfoQueue().size() > 1)
//                {
//                    finalsynProvider = BaseSPIManager.createSynInf(callContext, providerManagerInfo, finalsynProvider,
//                            serviceID);
//                }
//                else if (providerManagerInfo.usedCustomInterceptor())
//                {
//                    finalsynProvider = BaseSPIManager.createInf(callContext, providerManagerInfo, finalsynProvider,
//                            serviceID);
//                }
//
//            }
//            else if (providerManagerInfo.usedCustomInterceptor())
//            {
//                finalsynProvider = BaseSPIManager.createInf(callContext, providerManagerInfo, finalsynProvider, serviceID);
//            }
//            else if (serviceID.isRemote())
//            {
//                finalsynProvider = BaseSPIManager.createInf(callContext, providerManagerInfo, finalsynProvider, serviceID);
//            }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            throw new SPIException(e);
//
//        }
//        if (providerManagerInfo.isSinglable()
//                && (providerManagerInfo.enableTransaction() || providerManagerInfo.isSynchronizedEnabled()
//                        || providerManagerInfo.usedCustomInterceptor() || serviceID.isRemote()))
//        {
//            if(callContext == null || (callContext != null && !callContext.containHeaders() && !serviceID.isRestStyle()))
//            {
//                synchronized (servicProviders)
//                {
//    
//                    Object temp = servicProviders.get(key);
//                    if (temp != null)
//                        return temp;
//                    servicProviders.put(key, finalsynProvider);
//                }
//            }
//            
//        }
//        return finalsynProvider;

    }

    public BaseSPIManager()
    {
    }

    // public static void main(String[] args)
    // {
    // Method[] methods = Test.class.getMethods();
    // for(int i = 0; i < methods.length; i ++)
    // {
    // Method m = methods[i];
    // Class[] cluss = m.getParameterTypes();
    // for(int j = 0; j < cluss.length; j ++)
    // {
    //    			
    // }
    // }
    //    	
    // System.out.println(String.class);
    // }

    public static void main(String[] args)
    {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 600000; i++)
        {
            System.out.println(i);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - s) + "s");
    }

    public static String getProperty(String name)
    {
        return defaultContext.getProperty(name);
    }

    public static int getIntProperty(String name)
    {
        return defaultContext.getIntProperty(name);
    }

    public static boolean getBooleanProperty(String name)
    {
        return defaultContext.getBooleanProperty(name);
    }

    public static String getProperty(String name, String defaultValue)
    {
        return defaultContext.getProperty(name, defaultValue);
    }

    public static Object getObjectProperty(String name)
    {
        return getObjectProperty(name, null);
    }

    public static Object getObjectProperty(String name, String defaultValue)
    {
        return defaultContext.getObjectProperty(name, defaultValue);
    }

    public static int getIntProperty(String name, int defaultValue)
    {
        return defaultContext.getIntProperty(name, defaultValue);
    }

    public static boolean getBooleanProperty(String name, boolean defaultValue)
    {
        return defaultContext.getBooleanProperty(name, defaultValue);
    }

   

    public static Object getBeanObject(String name)
    {
        return getBeanObject(name, null);
        // return ServiceProviderManager.getInstance().getObjectProperty(name);
        // if(value == null)
        // throw new AssembleException("配置文件没有指定属性[" + name + "]！");

    }

    public static Object getBeanObject(String name, Object defaultValue)
    {
        return getBeanObject(null, name, defaultValue,false);
        // return ServiceProviderManager.getInstance().getObjectProperty(name,
        // defaultValue);
        // if(value == null)
        // throw new AssembleException("配置文件没有指定属性[" + name + "]！");

    }

    public static ProSet getSetProperty(String name)
    {
        return defaultContext.getSetProperty(name);
        // if(value == null)
        // throw new AssembleException("配置文件没有指定属性[" + name + "]！");

    }

    public static ProSet getSetProperty(String name, ProSet defaultValue)
    {
        return defaultContext.getSetProperty(name, defaultValue);

    }

    public static ProList getListProperty(String name)
    {
        return defaultContext.getListProperty(name);

    }

    public static ProList getListProperty(String name, ProList defaultValue)
    {
        return defaultContext.getListProperty(name, defaultValue);

    }

    public static ProMap getMapProperty(String name)
    {
        return defaultContext.getMapProperty(name);
        // if(value == null)
        // throw new AssembleException("配置文件没有指定属性[" + name + "]！");

    }

    public static ProMap getMapProperty(String name, ProMap defaultValue)
    {
        return defaultContext.getMapProperty(name, defaultValue);

    }

   
    // /**
    // * 这个方法需要对服务事务进行相应的处理
    // * @param context
    // * @param name
    // * @param defaultValue
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public static Object getBeanObject(Context context, String name,Object
    // defaultValue)
    // {
    // ServiceID serviceID = new
    // ServiceID(name,GroupRequest.GET_ALL,0,ServiceID.
    // result_rsplist,ServiceID.PROPERTY_BEAN_SERVICE);
    // Pro providerManagerInfo =
    // defaultContext.getPropertyBean
    // (serviceID.getService());
    // if(providerManagerInfo == null)
    // {
    // ProviderManagerInfo providerManagerInfo_ = null;
    //            
    // providerManagerInfo_ = ServiceProviderManager.getInstance()
    // .getProviderManagerInfo(serviceID.getService());
    // if(providerManagerInfo_ == null)
    // throw new SPIException("没有定义名称为[" + name + "]的bean对象。");
    // return BaseSPIManager.getProvider(context, name, null);
    // }
    //        
    // String key = name;
    // Object finalsynProvider = null;
    // if (providerManagerInfo.isSinglable()) {
    // finalsynProvider = servicProviders.get(key);
    // if (finalsynProvider != null)
    // return finalsynProvider;
    // }
    // finalsynProvider =
    // defaultContext.getBeanObject(context
    // ,serviceID.getService(),defaultValue);
    // // ServiceID serviceID = new
    // ServiceID(name,GroupRequest.GET_ALL,0,ServiceID
    // .result_rsplist,ServiceID.PROPERTY_BEAN_SERVICE);
    // if(providerManagerInfo.enableTransaction() || serviceID.isRemote())
    // {
    //            
    // finalsynProvider = createInf(context, providerManagerInfo,
    // finalsynProvider,serviceID);
    // if (providerManagerInfo.isSinglable()) {
    // synchronized (servicProviders) {
    // servicProviders.put(key, finalsynProvider);
    // }
    // }
    // }
    // return finalsynProvider;
    // }

    public static Object getBeanObject(CallContext context, String name, Object defaultValue)
    {
        return getBeanObject(context, name, defaultValue,false);
    }
    
//    public static CallContext buildCallContext(String params,CallContext context)
//    {
//        if(context == null)
//            context = new CallContext();
//    	StringTokenizer tokenizer = new StringTokenizer(params,"&",false);
//    	   
//        /**
//         * 协议中包含的属性参数，可以用来做路由条件
//         */
//        Headers headers = null;
//    	SecurityContext securityContext = null;
//    	String user = null;
//    	String password = null;
//    	while(tokenizer.hasMoreTokens())
//    	{
//    	    
//    		String parameter = tokenizer.nextToken();
//    		
//    		int idex = parameter.indexOf("=");
//    		if(idex <= 0)
//    		{
//    			throw new SPIException("非法的服务参数串[" + params +"]");
//    		}
//    		StringTokenizer ptokenizer = new StringTokenizer(parameter,"=",false);
//    		String name = ptokenizer.nextToken();
//    		String value = ptokenizer.nextToken();
//    		Header header = new Header(name,value);
//    		if(name.equals(SecurityManager.USER_ACCOUNT_KEY))
//    		{
//        		user = value;
//        		
//    		}
//    		else if(name.equals(SecurityManager.USER_PASSWORD_KEY))
//            {
//                password = value;
//                
//            }
//    		else
//    		{
//        		if(headers == null)
//        		    headers = new Headers(); 
//        		headers.put(header.getName(),header);
//    		}
//    	}
//    	if(securityContext == null)
//            securityContext = new SecurityContext(user,password);
//    	context.setSecutiryContext(securityContext);
//    	context.setHeaders(headers);   
//    	return context;
//    }
    
    /**
     * bean工厂方法
     * 
     * @param context
     * @param name
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("unchecked")
    static Object getBeanObject(CallContext context, String name, Object defaultValue,boolean fromprovider)
    {
//    	//分析服务参数
//    	int idx = name.indexOf("?");
//    	
//    	
//    	String _name = name;
//    	if(context == null)
//            context = new CallContext();
//    	if(idx > 0)
//    	{
//    		String params = name.substring(idx + 1);
//    		context = buildCallContext(params,context);
////        	name = name.substring(0,idx);
//    	}
//    	 
//    	
//        ServiceID serviceID = buildServiceID(RPCHelper.serviceids,name, ServiceID.PROPERTY_BEAN_SERVICE);
//        // new ServiceID(name,GroupRequest.GET_ALL,0,ServiceID.result_rsplist,
//        // ServiceID.PROPERTY_BEAN_SERVICE);
//        Pro providerManagerInfo = defaultContext.getPropertyBean(serviceID.getService());
////        if(context != null && context.getSecutiryContext() != null)
////            context.getSecutiryContext().setServiceid(serviceID.getService());
//        if (providerManagerInfo == null)
//        {
//            if(!fromprovider)
//            {
//                ProviderManagerInfo providerManagerInfo_ = null;
//    
//                providerManagerInfo_ = defaultContext.getProviderManagerInfo(serviceID.getService());
//                if (providerManagerInfo_ == null)
//                    throw new SPIException("没有定义名称为[" + name + "]的bean对象。");
//                return BaseSPIManager.getProvider(context, _name, null,true);
//            }
//            else
//            {
//                throw new SPIException("没有定义名称为[" + name + "]的bean对象。");
//            }
//        }
//        return getBeanObject(context, providerManagerInfo, defaultValue, serviceID);
        return defaultContext.getBeanObject( context,  name,  defaultValue, fromprovider);
        // String key = name;
        // Object finalsynProvider = null;
        // if (providerManagerInfo.isSinglable()) {
        // finalsynProvider = servicProviders.get(key);
        // if (finalsynProvider != null)
        // return finalsynProvider;
        // }
        // finalsynProvider =
        // defaultContext.getBeanObject(
        // context,serviceID.getService(),defaultValue);
        // // ServiceID serviceID = new
        // ServiceID(name,GroupRequest.GET_ALL,0,ServiceID
        // .result_rsplist,ServiceID.PROPERTY_BEAN_SERVICE);
        // if(providerManagerInfo.enableTransaction() || serviceID.isRemote())
        // {
        //            
        // finalsynProvider = createInf(context, providerManagerInfo,
        // finalsynProvider,serviceID);
        // if (providerManagerInfo.isSinglable()) {
        // synchronized (servicProviders) {
        // servicProviders.put(key, finalsynProvider);
        // }
        // }
        // }
        // return finalsynProvider;
    }

    public static Object getBeanObject(CallContext context, String name)
    {
        return getBeanObject(context, name, null,false);
    }

    public static Pro getProBean(String name)
    {
        // TODO Auto-generated method stub
        return defaultContext.getProBean(name);
    }

    public static Object getBeanObject(CallContext context, Pro providerManagerInfo)
    {
        return getBeanObject(context, providerManagerInfo, null);
    }

    /**
     * bean组件工厂方法， 如果serviceID不为空，则serviceID是根据getBeanObject(Context context,
     * String name,Object defaultValue)方法的name生成的
     * 否则需要根据providerManagerInfo的name或者refid来生成serviceID
     * 
     * @param context
     * @param providerManagerInfo
     * @param defaultValue
     * @param serviceID
     * @return
     */
    public static Object getBeanObject(CallContext context, Pro providerManagerInfo, Object defaultValue)
    {
        return defaultContext.getBeanObject( context,  providerManagerInfo,  defaultValue);
//        if (providerManagerInfo == null)
//            throw new SPIException("bean对象为空。");
//        String key = providerManagerInfo.getName();
//        if(providerManagerInfo.isRefereced())
//        {
//        	Object retvalue = providerManagerInfo.getTrueValue(context,defaultValue);
//        	return retvalue;
//        }
//        
//        Object finalsynProvider = null;
//        if (serviceID == null)
//            serviceID = buildServiceID(RPCHelper.serviceids,key, ServiceID.PROPERTY_BEAN_SERVICE);
//        // new ServiceID(key, GroupRequest.GET_ALL ,0,ServiceID.result_rsplist,ServiceID.PROPERTY_BEAN_SERVICE);
//        key = serviceID.getOrigineServiceID();
//        finalsynProvider = defaultContext.getBeanObject(context, providerManagerInfo,
//                defaultValue);
//        if (providerManagerInfo.enableTransaction() || providerManagerInfo.usedCustomInterceptor() || serviceID.isRemote())
//        {
//            if (providerManagerInfo.isSinglable() )
//            {
////                String key = serviceID.getServiceID();
//                if(context != null && !context.containHeaders() && !serviceID.isRestStyle() )//如果包含头信息时，代理类将不能被缓冲，原因是头信息的动态性会导致缓冲实例过多
//                {
//                    Object provider = servicProviders.get(key);
//                    if (provider != null)
//                        return provider;
//                    synchronized (servicProviders)
//                    {
//                        provider = servicProviders.get(key);
//                        if (provider != null)
//                            return provider;
//                        provider = createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                        servicProviders.put(key, provider);
//                    }
//                    return provider;
//                }
//                else
//                {
//                    finalsynProvider = createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                    return finalsynProvider;
//                }
//                
//            }
//            else
//            {
//                finalsynProvider = createInf(context, providerManagerInfo, finalsynProvider, serviceID);
//                return finalsynProvider;
//            }
//        }
//        else
//        {
//            return finalsynProvider;
//        }
    }

//    private static Map<String, ServiceID> serviceids = new java.util.WeakHashMap<String, ServiceID>();

//    public static ServiceID buildServiceID(Map<String,ServiceID> serviceids,String serviceid, int serviceType, String providertype)
//    {
//        return RPCHelper.buildServiceID( serviceids,serviceid, serviceType, providertype);
//
//    }
//    
//    public static ServiceID buildServiceID(String serviceid, int serviceType)
//    {
//       
//////        SoftReference<ServiceID> reference;
////        
////        
////            long timeout = getRPCRequestTimeout();
////            ServiceID serviceID = new ServiceID(serviceid, null, GroupRequest.GET_ALL, timeout, ServiceID.result_rsplist,
////                    serviceType);
////           
//           
//        return RPCHelper.buildServiceID(serviceid, serviceType);
//
//    }
    
    

//    public static ServiceID buildServiceID(Map<String, ServiceID> serviceids,String serviceid, int serviceType)
//    {
//
//        return buildServiceID(serviceids,serviceid, serviceType, null);
//
//    }
//    
//    public static ServiceID buildBeanServiceID(Map<String, ServiceID> serviceids,String serviceid)
//    {
//
//        return buildServiceID(serviceids,serviceid, ServiceID.PROPERTY_BEAN_SERVICE, null);
//
//    }
    
    
    
    
    
    public static String getStringExtendAttribute(String name ,String extendName)
    {
        return defaultContext.getStringExtendAttribute(name,extendName);
    }

    
    public static Object getExtendAttribute(String name ,String extendName)
    {
//        Pro pro = getProBean(name);
//        if(pro == null)
//            return null;
//        return pro.getExtendAttribute(extendName);
        return defaultContext.getExtendAttribute(name ,extendName);
        
    }
    
    
    public static int getIntExtendAttribute(String name ,String extendName)
    {
//        Pro pro = getProBean(name);
//        if(pro == null)
//            return -1;
//        return pro.getIntExtendAttribute(extendName);
        return defaultContext.getIntExtendAttribute( name , extendName);
        
    }
    
    public static long getLongExtendAttribute(String name ,String extendName)
    {
     // TODO Auto-generated method stub
//        Pro pro = getProBean(name);
//        if(pro == null)
//            return -1;
//        return pro.getLongExtendAttribute(extendName);
        return defaultContext.getLongExtendAttribute( name , extendName);
    }
    
    public static boolean getBooleanExtendAttribute(String name ,String extendName)
    {
//     // TODO Auto-generated method stub
//        Pro pro = getProBean(name);
//        if(pro == null)
//            return false;
//        return pro.getBooleanExtendAttribute(extendName);
        return defaultContext.getBooleanExtendAttribute( name , extendName);
    }

}
