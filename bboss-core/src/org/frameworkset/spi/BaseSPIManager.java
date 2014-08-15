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
 * 
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

//    public static void destroySingleBeans()
//    {
//        if (singleDestorys != null && singleDestorys.size() > 0)
//        {
//            //			
//            Iterator<DisposableBean> ite = singleDestorys.iterator();
//            while (ite.hasNext())
//            {
//                DisposableBean et = ite.next();
//                try
//                {
//                    et.destroy();
//                }
//                catch (Exception e)
//                {
//                    log.error(e);
//                }
//            }
//        }
//        
//        if(destroyServiceMethods != null && destroyServiceMethods.size() > 0)
//        {
//        	Iterator<Map.Entry<String, Object>> entrys = destroyServiceMethods.entrySet().iterator();
//        	for(;entrys.hasNext();)
//        	{
//        		Map.Entry<String, Object> entry = entrys.next();
//        		Object value = entry.getValue();
//        		String method = entry.getKey();
//        		try
//				{
//					Method m = value.getClass().getDeclaredMethod(method, new Class[0]);
//					m.invoke(value, new Object[0]);
//				}
//				catch (SecurityException e)
//				{
//					log.error(e);
//				}
//				catch (NoSuchMethodException e)
//				{
//					log.error(e);
//				}
//				catch (Exception e)
//				{
//					log.error(e);
//				}
//        	}
//        }
//    }

//    public static void registDisableBean(DisposableBean disposableBean)
//    {
//        singleDestorys.add(disposableBean);
//    }
//    
//    public static void registDestroyMethod(String destroyMethod,Object instance)
//    {
//    	destroyServiceMethods.put(destroyMethod, instance);
//    }

//    private static Logger log = Logger.getLogger(BaseSPIManager.class);

//    /**
//     * 缺省接口key
//     */
//    public static final String DEFAULT_CACHE_KEY = "DEFAULT_CACHE_KEY";
//
//    /**
//     * 同步缓冲key
//     */
//
//    public static final String SYNCHRO_CACHE_KEY = "SYNCHRO_CACHE_KEY";

//    /**
//     * 创建不具有同步控制的provider代理接口实例 该代理接口可能有事务控制的功能也可能没有有事务控制，根据具体的配置来决定 createInf
//     * 
//     * @return Object
//     */
//    public static Object createInf(final CallContext callcontext, final BaseTXManager providerManagerInfo,
//            final Object delegate, final ServiceID serviceID)
//    {
//        return ProxyFactory.createProxy(new InvocationHandler(delegate)
//        {
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
//            {
//                if (!serviceID.isRemote())
//                {
//                    Interceptor interceptor = providerManagerInfo.getTransactionInterceptor();
//                    try
//                    {
//
//                        Object obj = null;// 只返回delegate中方法返回的值
//                        // 如果不是同步方法，则无需同步调用其他接口方法
//                        // log.debug("method.getName():" + method.getName());
//                        //log.debug("providerManagerInfo.getSynchronizedMethod("
//                        // +
//                        // method.getName() + "):" +
//                        // providerManagerInfo.getSynchronizedMethod
//                        // (method.getName()));
//                        if (interceptor != null)
//                            interceptor.before(method, args);
//                        obj = method.invoke(delegate, args);
//                        if (interceptor != null)
//                            interceptor.after(method, args);
//                        return obj;
//
//                    }
//                    catch (InvocationTargetException e)
//                    {
//                        log.error(e);
//                        if (interceptor != null)
//                        {
//                            try
//                            {
//                                interceptor.afterThrowing(method, args, e.getTargetException());
//                            }
//                            catch (Exception ei)
//                            {
//                                ei.printStackTrace();
//                            }
//                        }
//
//                        // 将方法抛出的异常直接抛出方法异常
//                        throw e.getTargetException();
//                    }
//                    catch (Throwable t)
//                    {
//                        log.error(t);
//                        try
//                        {
//                            // t.printStackTrace();
//                            if (interceptor != null)
//                                interceptor.afterThrowing(method, args, t);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        throw t;
//                    }
//                    finally
//                    {
//                        try
//                        {
//                            if (interceptor != null)
//                                interceptor.afterFinally(method, args);
//
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        interceptor = null;
//                    }
//                }
//                else
//                {
//
//                    return RPCHelper.getRPCHelper().rpcService(serviceID, method, args,callcontext);
//
//                }
//            }
//        });
//
//    }

//    /**
//     * 创建没有同步但有事务控制的provider代理接口实例 该方法的实现逻辑目前和createInf方法一致
//     * 
//     * @return Object
//     */
//    protected static Object createTXInf(final CallContext callcontext, final BaseTXManager providerManagerInfo,
//            final Object delegate, ServiceID serviceID)
//    {
//        return createInf(callcontext, providerManagerInfo, delegate, serviceID);
//
//    }

//    /**
//     * 创建即具有同步控制功能但不具备事务管理功能的provider代理接口实例 createInf
//     * 
//     * @return Object
//     */
//    protected static Object createSynInf(final CallContext callcontext, final ProviderManagerInfo providerManagerInfo,
//            final Object delegate, final ServiceID serviceID)
//    {
//        return ProxyFactory.createProxy(new InvocationHandler(delegate)
//        {
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
//            {
//                if (!serviceID.isRemote())
//                {
//                    Interceptor interceptor = providerManagerInfo.getTransactionInterceptor();
//                    try
//                    {
//
//                        Object obj = null;// 只返回delegate中方法返回的值
//                        // 如果不是同步方法，则无需同步调用其他接口方法
//                        // log.debug("method.getName():" + method.getName());
//                        //log.debug("providerManagerInfo.getSynchronizedMethod("
//                        // +
//                        // method.getName() + "):" +
//                        // providerManagerInfo.getSynchronizedMethod
//                        // (method.getName()));
//                        if (!providerManagerInfo.isSynchronizedEnabled()
//                                || !providerManagerInfo.isSynchronizedMethod(method))
//                        {// 如果没有配置以方法及相应的方法参数的为标识的同步方法
//                            // ，
//                            // 如果有则执行所有provider的同步方法
//
//                            if (interceptor != null)
//                                interceptor.before(method, args);
//                            obj = method.invoke(delegate, args);
//                            if (interceptor != null)
//                                interceptor.after(method, args);
//                            return obj;
//
//                        }
//                        else
//                        {
//                            // 获取服务提供者队列，该队列已经按照指定的优先级顺序进行排序
//                            ProviderInfoQueue providerInfoQueue = providerManagerInfo.getProviderInfoQueue();
//
//                            //同步调用所有的接口方法，如果同步方法是是事务性方法，则需要进行事务处理，及要么全部成功，要么全部失败
//                            // 否则在处理的过程中,一些服务提供者方法被执行时抛出异常，将继续执行后续的同步方法
//
//                            Throwable throwable = null;
//                            if (interceptor != null)
//                                interceptor.before(method, args);
//                            for (int i = 0; i < providerInfoQueue.size(); i++)
//                            {
//                                SecurityProviderInfo securityProviderInfo = providerInfoQueue
//                                        .getSecurityProviderInfo(i);
//                                if (!securityProviderInfo.isUsed() && !securityProviderInfo.isIsdefault())
//                                {
//                                    continue;
//                                }
//                                // Object provider =
//                                // !providerManagerInfo.isSinglable() ?
//                                // securityProviderInfo
//                                // .getProvider(parent) :
//                                // securityProviderInfo.getSingleProvider
//                                // (parent);
//                                Object provider = securityProviderInfo.getProvider(callcontext);
//                                boolean isdelegate = provider.getClass() == delegate.getClass();
//                                // 缺省的provider和被标识为使用的provider都需要进行同步调用
//                                if (isdelegate || securityProviderInfo.isUsed() || securityProviderInfo.isIsdefault())
//                                {
//                                    if (isdelegate && !securityProviderInfo.isUsed())
//                                    {
//                                        log.warn("调用的Provider[" + delegate.getClass() + "]，[" + securityProviderInfo
//                                                + "]被设置为未使用的服务提供者。");
//                                    }
//
//                                    /**
//                                     * 缺省的提供者，也就是默认的提供者，不需要进行事务处理时，只保留服务提供者的返回值
//                                     */
//                                    if (isdelegate)
//                                    {
//                                        try
//                                        {
//                                            obj = method.invoke(provider, args);
//                                        }
//                                        catch (InvocationTargetException e)
//                                        {
//                                            throwable = e.getTargetException();
//                                            log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
//                                                    + e.getMessage(), e);
//                                        }
//                                        catch (Exception e)
//                                        {
//                                            throwable = e;
//                                            log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
//                                                    + e.getMessage(), e);
//                                        }
//
//                                    }
//                                    else
//                                    {
//                                        try
//                                        {
//                                            method.invoke(provider, args);
//                                        }
//                                        catch (Exception e)
//                                        {
//                                            // 不是事务方法调用时，忽略其他服务提供者方法执行时的异常
//                                            log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
//                                                    + e.getMessage(), e);
//                                        }
//                                    }
//
//                                }
//                                // log.debug(
//                                // "providerInfoQueue.getSecurityProviderInfo("+
//                                // i
//                                // +").getProvider()" +
//                                // providerInfoQueue.getSecurityProviderInfo(i)
//                                // .getProvider());
//                            }
//                            // 抛出缺省服务提供者方法异常
//                            if (throwable != null)
//                                throw throwable;
//                            if (interceptor != null)
//                                interceptor.after(method, args);
//                            return obj;
//                        }
//                    }
//                    catch (InvocationTargetException e)
//                    {
//                        if (interceptor != null)
//                        {
//                            try
//                            {
//                                interceptor.afterThrowing(method, args, e.getTargetException());
//                            }
//                            catch (Exception ei)
//                            {
//                                ei.printStackTrace();
//                            }
//                        }
//                        throw e.getTargetException();
//                    }
//                    catch (Throwable t)
//                    {
//                        try
//                        {
//                            t.printStackTrace();
//                            if (interceptor != null)
//                                interceptor.afterThrowing(method, args, t);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        throw t;
//                    }
//                    finally
//                    {
//                        try
//                        {
//                            if (interceptor != null)
//                                interceptor.afterFinally(method, args);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        interceptor = null;
//                    }
//
//                }
//                else
//                {
//                    return RPCHelper.getRPCHelper().rpcService(serviceID, method, args,callcontext);
//                }
//            }
//        });
//    }

//    /**
//     * 创建具有同步和事务控制的provider代理接口实例 createInf
//     * 
//     * @return Object
//     */
//    protected static Object createSynTXInf(final CallContext callcontext, final ProviderManagerInfo providerManagerInfo,
//            final Object delegate, final ServiceID serviceID)
//    {
//        return ProxyFactory.createProxy(new InvocationHandler(delegate)
//        {
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
//            {
//                if (!serviceID.isRemote())
//                {
//                    Interceptor interceptor = providerManagerInfo.getTransactionInterceptor();
//                    try
//                    {
//
//                        Object obj = null;// 只返回delegate中方法返回的值
//                        // 如果不是同步方法，则无需同步调用其他接口方法
//                        // log.debug("method.getName():" + method.getName());
//                        //log.debug("providerManagerInfo.getSynchronizedMethod("
//                        // +
//                        // method.getName() + "):" +
//                        // providerManagerInfo.getSynchronizedMethod
//                        // (method.getName()));
//                        if (!providerManagerInfo.isSynchronizedEnabled()
//                                || !providerManagerInfo.isSynchronizedMethod(method))
//                        {// 如果没有配置以方法及相应的方法参数的为标识的同步方法
//                            // ，
//                            // 如果有则执行所有provider的同步方法
//
//                            if (interceptor != null)
//                                interceptor.before(method, args);
//                            obj = method.invoke(delegate, args);
//                            if (interceptor != null)
//                                interceptor.after(method, args);
//                            return obj;
//
//                        }
//                        else
//                        {
//                            // 获取服务提供者队列，该队列已经按照指定的优先级顺序进行排序
//                            ProviderInfoQueue providerInfoQueue = providerManagerInfo.getProviderInfoQueue();
//
//                            //同步调用所有的接口方法，如果同步方法是是事务性方法，则需要进行事务处理，及要么全部成功，要么全部失败
//                            // 否则在处理的过程中,一些服务提供者方法被执行时抛出异常，将继续执行后续的同步方法
//
//                            SynchronizedMethod synm = providerManagerInfo.isTransactionMethod(method);
//                            boolean isTXMethod = synm != null;
//                            Throwable throwable = null;
//                            if (interceptor != null)
//                                interceptor.before(method, args);
//                            for (int i = 0; i < providerInfoQueue.size(); i++)
//                            {
//                                SecurityProviderInfo securityProviderInfo = providerInfoQueue
//                                        .getSecurityProviderInfo(i);
//                                if (!securityProviderInfo.isUsed() && !securityProviderInfo.isIsdefault())
//                                {
//                                    continue;
//                                }
//                                Object provider = securityProviderInfo.getProvider(callcontext);
//                                // Object provider =
//                                // !providerManagerInfo.isSinglable() ?
//                                // securityProviderInfo
//                                // .getProvider(parent) :
//                                // securityProviderInfo.getSingleProvider
//                                // (parent);
//                                boolean isdelegate = provider.getClass() == delegate.getClass();
//                                // 缺省的provider和被标识为使用的provider都需要进行同步调用
//                                if (isdelegate || securityProviderInfo.isUsed() || securityProviderInfo.isIsdefault())
//                                {
//                                    if (isdelegate && !securityProviderInfo.isUsed())
//                                    {
//                                        log.warn("调用的Provider[" + delegate.getClass() + "]，[" + securityProviderInfo
//                                                + "]被设置为未使用的服务提供者。");
//                                    }
//
//                                    if (!isTXMethod) // 不是事务方法时，
//                                                     // 执行所有的provider的同一个方法，
//                                    // 忽略失败的方法调用，
//                                    {
//                                        /**
//                                         * 缺省的提供者，也就是默认的提供者，不需要进行事务处理时，
//                                         * 只保留服务提供者的返回值
//                                         */
//                                        if (isdelegate)
//                                        {
//                                            try
//                                            {
//                                                obj = method.invoke(provider, args);
//                                            }
//                                            catch (InvocationTargetException e)
//                                            {
//                                                throwable = e.getTargetException();
//                                                log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
//                                                        + e.getMessage(), e);
//                                            }
//                                            catch (Exception e)
//                                            {
//                                                throwable = e;
//                                                log.error("调用服务提供者[" + securityProviderInfo + "]中方法[" + method + "]异常："
//                                                        + e.getMessage(), e);
//                                            }
//                                        }
//                                        else
//                                        {
//                                            try
//                                            {
//                                                method.invoke(provider, args);
//                                            }
//                                            catch (Exception e)// 抛出异常时不记录该异常，
//                                            // 继续执行后续的provider的方法
//                                            {
//                                                // 不是事务方法调用时，忽略其他服务提供者方法执行时的异常
//                                                log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
//                                                        + "]异常：" + e.getMessage(), e);
//                                            }
//                                        }
//                                        // 抛出缺省服务提供者方法异常，同时终止其他provider同步执行逻辑
//
//                                    }
//                                    else
//                                    // 事务性方法执行逻辑，不处理事务性异常，但是需要处理非事务性异常
//                                    {
//                                        if (isdelegate)
//                                        {
//                                            try
//                                            {
//                                                obj = method.invoke(provider, args);
//                                            }
//                                            catch (InvocationTargetException e)
//                                            {
//                                                if (synm.isRollbackException(e.getTargetException())) // 事务性异常
//                                                // ，
//                                                // 继续抛出异常
//                                                // ，
//                                                // 中断其他服务提供者方法执行
//                                                {
//                                                    throw e.getTargetException();
//                                                }
//                                                else
//                                                {
//                                                    throwable = e.getTargetException();// 抛出异常时记录该异常
//                                                    // ，
//                                                    // 继续执行后续的provider的同步方法
//                                                }
//                                            }
//
//                                            catch (Exception e)
//                                            {
//                                                if (synm.isRollbackException(e)) // 事务性异常
//                                                // ，
//                                                // 继续抛出异常
//                                                // ，
//                                                // 中断其他服务提供者方法执行
//                                                {
//                                                    throw e;
//                                                }
//                                                else
//                                                {
//                                                    throwable = e;// 抛出异常时记录该异常，
//                                                    // 继续执行后续的provider的同步方法
//                                                }
//                                            }
//                                        }
//                                        else
//                                        {
//                                            try
//                                            {
//                                                method.invoke(provider, args);
//                                            }
//                                            catch (InvocationTargetException e)
//                                            {
//                                                if (synm.isRollbackException(e.getTargetException())) // 事务性异常
//                                                // ，
//                                                // 继续抛出异常
//                                                // ，
//                                                // 中断其他服务提供者方法执行
//                                                {
//                                                    throw e;
//                                                }
//                                                else
//                                                {
//                                                    log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
//                                                            + "]异常：" + e.getMessage(), e);
//                                                }
//                                            }
//                                            catch (Exception e)
//                                            {
//                                                if (synm.isRollbackException(e)) // 事务性异常
//                                                // ，
//                                                // 继续抛出异常
//                                                // ，
//                                                // 中断其他服务提供者方法执行
//                                                {
//                                                    throw e;
//                                                }
//                                                else
//                                                {
//                                                    log.error("同步调用服务提供者[" + securityProviderInfo + "]中方法[" + method
//                                                            + "]异常：" + e.getMessage(), e);
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                                // log.debug(
//                                // "providerInfoQueue.getSecurityProviderInfo("+
//                                // i
//                                // +").getProvider()" +
//                                // providerInfoQueue.getSecurityProviderInfo(i)
//                                // .getProvider());
//                            }
//                            if (throwable != null)// 如果在方法的执行过程中发生异常，抛出该异常
//                                throw throwable;
//                            if (interceptor != null)
//                                interceptor.after(method, args);
//                            return obj;
//                        }
//                    }
//                    catch (InvocationTargetException e)
//                    {
//                        if (interceptor != null)
//                        {
//                            try
//                            {
//                                interceptor.afterThrowing(method, args, e.getTargetException());
//                            }
//                            catch (Exception ei)
//                            {
//                                ei.printStackTrace();
//                            }
//                        }
//                        throw e.getTargetException();
//                    }
//                    catch (Throwable t)
//                    {
//                        try
//                        {
//                            t.printStackTrace();
//                            if (interceptor != null)
//                                interceptor.afterThrowing(method, args, t);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        throw t;
//                    }
//                    finally
//                    {
//                        try
//                        {
//                            if (interceptor != null)
//                                interceptor.afterFinally(method, args);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        interceptor = null;
//                    }
//                }
//                else
//                {
//                    return RPCHelper.getRPCHelper().rpcService(serviceID, method, args,callcontext);
//                }
//            }
//        });
//
//    }

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
